package com.worldwizards.sbook;

import java.net.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface GameSessionListener {
  /**
   * sessionAdded
   *
   * @param address InetAddress
   * @param sessionUUID UUID
   */
  public void sessionAdded(InetAddress address, UUID sessionUUID);

  /**
   * sessionRemoved
   *
   * @param sessionUUID UUID
   */
  public void sessionRemoved(UUID sessionUUID);

}
