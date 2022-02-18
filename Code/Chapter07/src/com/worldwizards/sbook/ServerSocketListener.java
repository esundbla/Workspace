    package com.worldwizards.sbook;

    /**
     * <p>Title: </p>
     * <p>Description: </p>
     * <p>Copyright: Copyright (c) 2004</p>
     * <p>Company: </p>
     * @author not attributable
     * @version 1.0
     */

    public interface ServerSocketListener {
      public void playerJoined();

      public void playerLeft();

      public void dataArrived(StreamSocketHandler rdr, byte[] data,
                              int length);

      public void mainSocketClosed();

    }
