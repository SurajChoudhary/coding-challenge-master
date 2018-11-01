package com.newrelic.codingchallenge.clienthandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author uchousu
 * This thread is responsible for sending a connection 
 * refused response back to the client.
 */
public class RefuseConnectionHandler implements Runnable {

  private Socket socket;
  
  public RefuseConnectionHandler(Socket clientSocket) {
    this.socket = clientSocket;
  }
  
  @Override
  public void run() {
    String clientAddress = socket.getInetAddress().getHostAddress();
    
    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

      out.println("Connection Refused");
      System.out.println("Connection refused to client adrdesss: " + clientAddress);      
    } catch (IOException e) {
    }
//    System.out.println(Thread.currentThread().getName() + " exiting.");
  }

}
