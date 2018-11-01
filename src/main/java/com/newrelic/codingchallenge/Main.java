package com.newrelic.codingchallenge;

import java.net.InetAddress;
import java.net.ServerSocket;
import com.newrelic.codingchallenge.cache.Cache;
import com.newrelic.codingchallenge.clienthandler.ClientHandler;
import com.newrelic.codingchallenge.process.NumberProcessor;

/**
 * @author uchousu
 * the main application thread.
 * this class has a main application monitoring thread
 * which continuously monitors the value of a atomic terminate flag
 * and safely terminates the application if the flag is found true.
 * 
 * this class is responsible for application termination, requesting 
 * for creation of application level threads.. 
 */
public class Main {

  private static Main app;
  private ServerSocket server;

  public static int maxClientConnectionAllowed;
  private int threadPoolSize;
  private ThreadPool threadPool;

  public Main(String ipAddress) throws Exception {

    // the connection port is set to 4000 by default
    this.server = new ServerSocket(4000, 0, InetAddress.getByName(ipAddress));
    // TODO removed, this.server.setReceiveBufferSize(1);

    maxClientConnectionAllowed = 5;
    threadPoolSize = 10;
    threadPool = ThreadPool.getInstance(threadPoolSize);
    Cache.getInstance();
    NumberProcessor.getInstance();
  }

  public static void main(String[] args) throws Exception {

    if (args.length == 0) {
      System.out.println(
          "Error: enter a valid host server address to continue..\nUsage: java -jar jarfilepath"
              + " serveraddress\nFor ex: use 'localhost' as serveraddress");
      System.exit(0);
    }

    app = new Main(args[0]);
    System.out.println("Starting up server....\nRunning Server: " + "Host="
        + app.getSocketAddress().getHostAddress() + " Port=" + app.getPort());

    app.systemThreads();

    // main monitoring loop
    while (true) {
      if (ClientHandler.initiateShutdown.get()) {
        app.terminateApp();
      }
      Thread.sleep(1);
    }
  }

  // TODO removed main as argument
  private void systemThreads() {
    threadPool.createServerListener(server);
    threadPool.createFileSaver();
    threadPool.createMetricsReporter();
  }

  public void terminateApp() {
    threadPool.terminateThreads();
    System.out.println("Server shutting down!");
    System.exit(0);
  }

  public InetAddress getSocketAddress() {
    return this.server.getInetAddress();
  }

  public int getPort() {
    return this.server.getLocalPort();
  }
}
