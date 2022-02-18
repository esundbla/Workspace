    package com.worldwizards.sbook;

    public interface StreamSocketListener {
      public void dataArrived(StreamSocketHandler rdr, byte[] data,
                              int length);

      /**
       * socketClosed
       *
       * @param StreamSocketHandler StreamSocketHandler
       */
      public void socketClosed(StreamSocketHandler StreamSocketHandler);
    }
