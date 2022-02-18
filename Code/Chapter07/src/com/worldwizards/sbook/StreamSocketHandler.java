    package com.worldwizards.sbook;

    import java.io.*;
    import java.net.*;
    import java.util.*;

    /**
     * <p>Title: </p>
     * <p>Description: </p>
     * <p>Copyright: Copyright (c) 2004</p>
     * <p>Company: </p>
     * @author not attributable
     * @version 1.0
     */

    public class StreamSocketHandler
        implements Runnable {
      int MAX_PKT_SIZE = 255;
      byte[] buff = new byte[MAX_PKT_SIZE];
      List listeners = new ArrayList();
      Socket mySocket;
      DataInputStream ins;
      DataOutputStream os;

      public StreamSocketHandler(Socket socket) {
        mySocket = socket;
        try {
          ins = new DataInputStream(mySocket.getInputStream());
          os = new DataOutputStream(mySocket.getOutputStream());
        }
        catch (Exception e) {
          e.printStackTrace();
        }

      }

      public void addListener(StreamSocketListener l) {
        listeners.add(l);
      }

      /**
       * run
       */
      public void run() {
        boolean done = false;
        while (!done) {
          try {
            int count = ins.readInt();
            byte[] buff = new byte[count];
            ins.readFully(buff,0,count);
            doDataIn(count, buff);
          }
          catch (Exception e) {
            // socket closed
            doSocketClosed();
            done = true;
          }
        }
      }

      /**
       * doSocketClosed
       */
      private void doSocketClosed() {
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
          ( (StreamSocketListener) i.next()).socketClosed(this);
        }
      }

      /**
       * doDataIn
       *
       * @param count int
       * @param buff byte[]
       */
      private void doDataIn(int count, byte[] buff) {
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
          ( (StreamSocketListener) i.next()).dataArrived(this, buff,
              count);
        }
      }

      /**
       * send
       *
       * @param bs byte[]
       */
      public void send(byte[] bs,int sz) {
        try {
          os.writeInt(sz);
          os.write(bs,0,sz);
        }
        catch (Exception e) {
          // socket closed
          doSocketClosed();
        }
      }
    }
