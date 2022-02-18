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

import java.util.*;

public class GameCreator implements ServerSocketListener{
  Discoverer discoverer;
  ServerSocketHandler serverSocketHandler;
  List listeners = new ArrayList();

  public GameCreator(int gameTCPPort) {
    serverSocketHandler = new ServerSocketHandler(gameTCPPort);
    serverSocketHandler.addListsener(this);
    discoverer = new MulticastDiscovery();
    discoverer.startAnnouncing();
  }

  public void addListener(GameCommListener l){
    listeners.add(l);
  }

  // callbacks from serverSocketHandler

  public void dataArrived(StreamSocketHandler rdr, byte[] data,
                          int length) {
    for(Iterator i= listeners.iterator();i.hasNext();){
      ((GameCommListener)i.next()).dataArrived(data,length);
    }
  }

  /**
   * mainSocketClosed
   */
  public void mainSocketClosed() {
    String txt = "Server Msg: Error, Server socket as closed!";
    byte[] txtbuff = txt.getBytes();
    for(Iterator i= listeners.iterator();i.hasNext();){
      ((GameCommListener)i.next()).dataArrived(txtbuff,txtbuff.length);
    }
    System.exit(1);
  }

  /**
   * playerJoined
   */
  public void playerJoined() {
    String txt = "Server Msg: Player Connected!";
    byte[] txtbuff = txt.getBytes();
    for(Iterator i= listeners.iterator();i.hasNext();){
      ((GameCommListener)i.next()).dataArrived(txtbuff,txtbuff.length);
    }

  }

  /**
   * playerLeft
   */
  public void playerLeft() {
    String txt = "Server Msg: Player Disconnected!";
   byte[] txtbuff = txt.getBytes();
   for(Iterator i= listeners.iterator();i.hasNext();){
     ((GameCommListener)i.next()).dataArrived(txtbuff,txtbuff.length);
   }
  }

  /**
   * sendData
   *
   * @param data byte[]
   * @param i int
   */
  public void sendData(byte[] data, int length) {
    serverSocketHandler.send(data,length);
  }
}
