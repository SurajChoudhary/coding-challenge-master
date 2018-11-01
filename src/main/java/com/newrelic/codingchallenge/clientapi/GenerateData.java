package com.newrelic.codingchallenge.clientapi;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;

/**
 * @author uchousu
 * this thread is an automated client response generator
 * specially made for the purpose of testing application 
 * performance.
 */
public class GenerateData implements Runnable {

  private Socket socket;
  private ClientSocket clientSocket;
  private int maxRecordsNum;
  private int maxDigitsInNumGenerated; 

  public GenerateData(ClientSocket clientSocket) {
    maxRecordsNum = 500000;
    maxDigitsInNumGenerated = 1000000000;
    this.clientSocket = clientSocket;
    this.socket = clientSocket.getSocket();
  }

  @Override
  public void run() {
    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      String input;
      System.out.println("Generating " + maxRecordsNum + " messages.");
      Random r = new Random();
      for (int i = 0; i < maxRecordsNum; i++) {
        int num = r.nextInt(maxDigitsInNumGenerated);
        input = String.valueOf(num).concat(ClientSocket.serverNewlineChar);
        input = StringUtils.leftPad(input, ClientSocket.maxStringLength, "0");
        out.println(input);
      }
      clientSocket.terminateClient();
    } catch (IOException e) {
    }
  }

}
