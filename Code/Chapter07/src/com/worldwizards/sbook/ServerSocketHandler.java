/*****************************************************************************
 * Copyright (c) 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistribution of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materails provided with the distribution.
 *
 * Neither the name Sun Microsystems, Inc. or the names of the contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANT OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMEN, ARE HEREBY EXCLUDED.  SUN MICROSYSTEMS, INC. ("SUN") AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS
 * A RESULT OF USING, MODIFYING OR DESTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.  IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.  HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OUR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for us in
 * the design, construction, operation or maintenance of any nuclear facility
 *
 *****************************************************************************/

package com.worldwizards.sbook;

import java.net.*;
import java.util.*;

public class ServerSocketHandler
    implements StreamSocketListener, Runnable {
  ServerSocket myServerSocket;
  List listeners = new ArrayList();
  private List streams = new ArrayList();
  public ServerSocketHandler(int port) {
    // Create the Server Socket.  This makes an end-point for
    // joining games to connect to.
    try {
      myServerSocket = new ServerSocket(port);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    new Thread(this).start();
  }

  /**
   * run
   */
  public void run() {
    while (true) { // loop until program is interrupted
      try {
        // accept() says "I'm ready to handle a connector"
        Socket newConnSocket = myServerSocket.accept();
        StreamSocketHandler ssockrdr =
            new StreamSocketHandler(newConnSocket);
        ssockrdr.addListener(this);
        streams.add(ssockrdr);
        Thread socketThread = new Thread(
            ssockrdr);
        socketThread.start();
        doPlayerJoined();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public void addListsener(ServerSocketListener l) {
    listeners.add(l);
  }

  public void doPlayerJoined() {
    for (Iterator i = listeners.iterator(); i.hasNext(); ) {
      ( (ServerSocketListener) i.next()).playerJoined();
    }
  }

  public void doPlayerLeft() {
    for (Iterator i = listeners.iterator(); i.hasNext(); ) {
      ( (ServerSocketListener) i.next()).playerLeft();
    }
  }

  /**
   * dataArrived
   *
   * @param rdr StreamSocketHandler
   * @param data byte[]
   */
  public void dataArrived(StreamSocketHandler rdr, byte[] data,
                          int length) {
    for (Iterator i = listeners.iterator(); i.hasNext(); ) {
      ( (ServerSocketListener) i.next()).dataArrived(rdr, data,
          length);
    }
  }

  /**
   * socketClosed
   *
   * @param StreamSocketHandler StreamSocketHandler
   */
  public void socketClosed(StreamSocketHandler StreamSocketHandler) {
    streams.remove(StreamSocketHandler);
    doPlayerLeft();
  }

  /**
   * send
   *
   * @param bs byte[]
   */
  public void send(byte[] bs, int sz) {
    for (Iterator i = streams.iterator(); i.hasNext(); ) {
      ( (StreamSocketHandler) i.next()).send(bs,sz);
    }
  }

}
