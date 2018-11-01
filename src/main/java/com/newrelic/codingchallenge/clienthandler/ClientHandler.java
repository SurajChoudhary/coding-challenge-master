package com.newrelic.codingchallenge.clienthandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import com.newrelic.codingchallenge.process.NumberProcessor;

/**
 * @author uchousu
 * this is a client handler thread, responsible for handling
 * inputs to the server socket from the client.
 * it sets a atomic boolean flag to safely close all 
 * application threads and terminate the application when 
 * client passes a keyword - 'terminate'
 */
public class ClientHandler implements Runnable {
  // TODO changed this to private
  private Socket socket;
  public static final String terminate = "terminate\\n";

  public static AtomicBoolean initiateShutdown = new AtomicBoolean(false);

  public ClientHandler(Socket clientSocket) {
    this.socket = clientSocket;
  }

  public void run() {
//    String clientAddress = socket.getInetAddress().getHostAddress();
    String data;
    try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      out.println("Connected");
      while (true) {
        data = br.readLine();
        // System.out.println("data: " + data);
        if (data == null) {
          ClientSocketHandler.removeSocket(socket);
          socket.close();
//          System.out.println("Connection closed for client address: " + clientAddress);
          break;
        } else {
          processData(data);
        }
      }
    } catch (IOException e) {
    }
  }

  private void processData(String data) {
    if (data.equals(terminate)) {
      initiateShutdown.set(true);
    } else {
      NumberProcessor.getInstance().process(data);
    }
  }

}
