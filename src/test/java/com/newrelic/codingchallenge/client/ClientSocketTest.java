package com.newrelic.codingchallenge.client;

import com.newrelic.codingchallenge.clientapi.ClientSocket;

public class ClientSocketTest {
  
//  @Test
  public void test() throws Exception {
    
    String[] args = {"localhost", "4000"};
    ClientSocket.main(args);
    
  }
  
}
