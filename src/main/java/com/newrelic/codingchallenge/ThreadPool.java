package com.newrelic.codingchallenge;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.newrelic.codingchallenge.clienthandler.ClientHandler;
import com.newrelic.codingchallenge.clienthandler.ClientSocketHandler;
import com.newrelic.codingchallenge.clienthandler.RefuseConnectionHandler;
import com.newrelic.codingchallenge.clienthandler.ServerListener;
import com.newrelic.codingchallenge.file.FileSaverRunner;
import com.newrelic.codingchallenge.metrics.MetricsReporter;

/**
 * @author uchousu
 * this main, system thread pool.
 * 
 * handles execution life cycle of server monitoring thread, server 
 * port listener thread all the active client threads, connection 
 * refused to a client thread and file saver thread. It is also  
 * responsible for destruction of the threads during application 
 * termination.
 */
public class ThreadPool {

  private static ThreadPool instance = null;
  private static ExecutorService executor;

  private Runnable metricsReporterWorker;
  private Runnable serverListenerWorker;
  private Runnable fileSaverWorker;

  public static ThreadPool getInstance(int size) {
    if (instance == null) {
      instance = new ThreadPool(size);
    }
    return instance;
  }

  private ThreadPool() {}

  private ThreadPool(int size) {
    executor = Executors.newFixedThreadPool(size);
  }

  public void createClientHandler(Socket clientSocket) {
    Runnable worker = new ClientHandler(clientSocket);
    ClientSocketHandler.addSocket(clientSocket);
    executor.execute(worker);
  }

  public void createServerListener(ServerSocket server) {
    serverListenerWorker = new ServerListener(server, this);
    executor.execute(serverListenerWorker);
  }

  public void createMetricsReporter() {
    metricsReporterWorker = new MetricsReporter();
    executor.execute(metricsReporterWorker);
  }

  public void createFileSaver() {
    fileSaverWorker = new FileSaverRunner();
    executor.execute(fileSaverWorker);
  }
  
  public void createRefuseConnectionHandler(Socket clientSocket) {
    Runnable refuseConnectionWorker = new RefuseConnectionHandler(clientSocket);
    executor.execute(refuseConnectionWorker);
  }

  public void terminateThreads() {

    ClientSocketHandler.terminateAllClientConnections();
    // terminate metrics thread
    ((MetricsReporter) metricsReporterWorker).terminate();
    // terminate listener
    ((ServerListener) serverListenerWorker).terminate();
    // terminate file saver
    ((FileSaverRunner) fileSaverWorker).terminate();
    try {
      // wait till the File saver is safely terminated
      // to prevent data loss
      Thread.sleep(2000);
    } catch (InterruptedException e) {
    }
    executor.shutdownNow();
    System.out.println("Executer terminated : " + executor.isTerminated());
  }
}
