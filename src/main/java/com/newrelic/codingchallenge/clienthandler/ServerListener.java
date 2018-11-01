package com.newrelic.codingchallenge.clienthandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.newrelic.codingchallenge.ThreadPool;

/**
 * @author uchousu
 * this is the main server socket listener thread.
 * 
 * It accepts connection request from the clients and invokes client handler threads.
 * 
 * It also invokes RefuseConnectionHandler thread if the application has 
 * five active clients connected to it. 
 * 
 * Terminates the server socket when application is shutting down.
 */
public class ServerListener implements Runnable {

  private ServerSocket server;
  private ThreadPool clientConnectionPool;

  public ServerListener(ServerSocket server, ThreadPool clientConnectionPool) {

    this.clientConnectionPool = clientConnectionPool;
    this.server = server;
  }

  @Override
  public void run() {
    Socket clientSocket;
    try {
      while (true) {
        clientSocket = this.server.accept();
        if (ClientSocketHandler.acceptMoreclients.get()) {
//          String clientAddress = clientSocket.getInetAddress().getHostAddress();
//          System.out.println("\nNew connection from " + clientAddress);
          clientConnectionPool.createClientHandler(clientSocket);
        } else {
          clientConnectionPool.createRefuseConnectionHandler(clientSocket);
        }
      }
    } catch (IOException e) {
    }
  }

  public void terminate() {
    try {
      // stop listening, close the socket
      this.server.close();
    } catch (IOException e) {
    }
  }
}
