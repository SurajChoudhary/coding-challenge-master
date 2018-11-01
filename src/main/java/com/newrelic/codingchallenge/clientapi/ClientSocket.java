package com.newrelic.codingchallenge.clientapi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author uchousu
 * main client api thread.
 * this class manages threads for handling server response, 
 * client listener on the port thread and an automated client thread.
 */
public class ClientSocket {
  private Socket socket;
  private Scanner scanner;

  public static final int maxStringLength = 9;
  public static final String serverNewlineChar = "\\n";
  public static final int defaultPort = 4000;

  private static boolean automateClient;

  private ClientSocket(InetAddress serverAddress, int serverPort) throws Exception {
    automateClient = false;
    this.socket = new Socket(serverAddress, serverPort);
    this.scanner = new Scanner(System.in);
  }

  public static void main(String[] args) throws Exception {

    if (args.length < 1) {
      System.out
          .println("Error: enter a valid host server address to continue..\nUsage: "
              + "java -cp pathtoshadowjar clientfileclasspath serveripaddress [flagtoautomateclient]"
              + "\nExample usage: java -cp pathtoshadowjar com.newrelic.codingchallenge.clientapi.ClientSocket localhost [true | false]");
      System.exit(0);
    }

    ClientSocket client =
        new ClientSocket(InetAddress.getByName(args[0]), defaultPort);

    if (args.length == 2) {
      automateClient = Boolean.parseBoolean(args[1]);
    }
    
    client.createThreads();
  }

  private void createThreads() {

    if (automateClient) {
      GenerateData generateRunnable = new GenerateData(this);
      Thread generator = new Thread(generateRunnable);
      generator.start();
    } else {
      ClientListener listnerRunnable = new ClientListener(this);
      Thread listner = new Thread(listnerRunnable);
      listner.start();
    }

    ServerResponseHandler handlerRunnable = new ServerResponseHandler(this);
    Thread responseHandler = new Thread(handlerRunnable);
    responseHandler.start();
  }

  public void terminateClient() {
    try {
      socket.close();
    } catch (IOException e) {
    }
    // System.out.println("closing client.");
    System.exit(0);
  }

  /**
   * @return the socket
   */
  public Socket getSocket() {
    return socket;
  }

  /**
   * @return the scanner
   */
  public Scanner getScanner() {
    return scanner;
  }

}
