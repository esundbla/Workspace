package com.worldwizards.sbook;

import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class GameSession
    implements StreamSocketListener {
  StreamSocketHandler hndlr;
  List listeners = new ArrayList();
  public GameSession(StreamSocketHandler streamSocketHandler) {
    hndlr = streamSocketHandler;
    hndlr.addListener(this);
  }

  public void addListener(GameCommListener l) {
    listeners.add(l);
  }

  /**
   * dataArrived
   *
   * @param rdr StreamSocketHandler
   * @param data byte[]
   * @param length int
   */
  public void dataArrived(StreamSocketHandler rdr, byte[] data,
                          int length) {
    for (Iterator i = listeners.iterator(); i.hasNext(); ) {
      ( (GameCommListener) i.next()).dataArrived(data, length);
    }
  }

  /**
   * socketClosed
   *
   * @param StreamSocketHandler StreamSocketHandler
   */
  public void socketClosed(StreamSocketHandler StreamSocketHandler) {
    for (Iterator i = listeners.iterator(); i.hasNext(); ) {
      ( (GameCommListener) i.next()).socketClosed();
    }
  }

  /**
   * sendData
   *
   * @param data byte[]
   * @param i int
   */
  public void sendData(byte[] data, int i) {
    hndlr.send(data, i);
  }

}
