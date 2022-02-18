
    package com.worldwizards.sbook;
    import java.net.ServerSocket;
    import java.io.*;
    import java.net.Socket;

    /** SimpleSocketListener
    * This class is a simple application that opens a TCP/IP socket
    * on port 1138 and prints any data sent there.
    *
    * To try it, run the program and then use telnet to connect to port 1138
    * on the system on which the app is running.
    */


    public class SimpleSocketListener {
    ServerSocket myServerSocket;
    public SimpleSocketListener() {
    // Create the Server Socket.  This makes an end-point for
    // the telnet sessions to connect to.
    try {
      myServerSocket = new ServerSocket(1138);
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    }


    /**
    * listen
    */
    private void listen() {
    while(true) { // loop until program is interrupted
      try {
        // accept() says "I'm ready to handle a connector"
        Socket newConnSocket = myServerSocket.accept();
        Thread socketThread = new Thread(
                new SocketToStdout(newConnSocket));
        socketThread.start();
      }
      catch (IOException ex) {
      }
    }
    }

    static public void main(String[] args) {
      new SimpleSocketListener().listen();
    }
  }
