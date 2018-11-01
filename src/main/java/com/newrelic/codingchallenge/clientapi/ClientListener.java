package com.newrelic.codingchallenge.clientapi;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

/**
 * @author uchousu
 * client thread listening on the socket for user input.
 * also validates the user input.
 */
public class ClientListener implements Runnable {

  private Socket socket;
  private Scanner scanner;
  private ClientSocket clientSocket;

  public ClientListener(ClientSocket clientSocket) {
    this.clientSocket = clientSocket;
    this.socket = clientSocket.getSocket();
    this.scanner = clientSocket.getScanner();
  }

  @Override
  public void run() {
    String input;
    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      while (true) {
        input = scanner.nextLine();
        input = validateInput(input);
        out.println(input);
      }
    } catch (IOException e) {
    }
  }

  /**
   * Validate the input. A valid input is either a 9 digit number or a 'terminate' string. On all
   * other input entries the client should terminate the connection.
   * 
   * @param input
   * @return
   */
  private String validateInput(String input) {
    // as per requirement number 2, the input data should be validated at
    // client side so that only valid input is presented to the application.

    // trim input of all white spaces
    String trimInput = input.replaceAll("\\s+", "");

    int inputLength = String.valueOf(trimInput).length();

    // Assuming that only valid data is presented to the application, the client should terminate
    // the itself, if invalid data is entered
    if (inputLength > ClientSocket.maxStringLength) {
      clientSocket.terminateClient();
    }

    String validInput = "";
    if (StringUtils.isNumeric(trimInput)) {
      // left pad the String with zeros to make it a
      // 9 digit input to the application server
      validInput = StringUtils.leftPad(trimInput, ClientSocket.maxStringLength, "0")
          .concat(ClientSocket.serverNewlineChar);
    } else if (trimInput.equals("terminate")) {
      validInput = trimInput.concat(ClientSocket.serverNewlineChar);
    } else {
      clientSocket.terminateClient();
    }
//    System.out.println("Valid input: " + validInput);
    return validInput;
  }
}
