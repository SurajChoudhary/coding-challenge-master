package com.newrelic.codingchallenge.clientapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author uchousu
 * this thread handles response sent from the server.
 * terminates the client application on receiving 
 * a refused connection message from the server. 
 */
public class ServerResponseHandler implements Runnable {

  private Socket socket;
  private ClientSocket clientSocket;

  public ServerResponseHandler(ClientSocket clientSocket) {
    this.clientSocket = clientSocket;
    this.socket = clientSocket.getSocket();
  }

  @Override
  public void run() {
    String response;
    try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
      while (true) {
        response = br.readLine();
        if (response == null) {
          System.out.println("Server shutting down!");
          clientSocket.terminateClient();
          break;
        } else if (response.equals("Connected")) {
          System.out.println("Connected to Server..");
        } else if (response.equals("Connection Refused")) {
          System.out.println("Connection refused by Server!");
          clientSocket.terminateClient();
        }
      }
    } catch (IOException e) {
    }
  }
}
