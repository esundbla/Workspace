package com.worldwizards.sbook;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface GameCommListener {
  /**
   * dataArrived
   *
   * @param data byte[]
   * @param length int
   */
  public void dataArrived(byte[] data, int length);

  /**
   * socketClosed
   */
  public void socketClosed();
}
