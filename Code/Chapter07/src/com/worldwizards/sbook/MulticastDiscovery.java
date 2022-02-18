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

    import java.io.*;
    import java.net.*;
    import java.util.*;
    import java.util.Map.*;

    public class MulticastDiscovery
        implements Discoverer, DatagramListener,
        Runnable {
      InetAddress discoveryGroup;
      MulticastSocket discoverySocket;
      List listeners = new ArrayList();
      Map timeTracker = new HashMap();
      UUID myUUID;
      private boolean done = false;
      private boolean announcer = false;
      private static final long HEARTBEAT_TIME = 500; // half a second btw them

      private DatagramPacket hbDatagram;
      private static final int DISCOVERY_PORT = 8976;
      public MulticastDiscovery() {
        try {
          discoveryGroup = InetAddress.getByName("228.9.8.7");
          discoverySocket = new MulticastSocket(DISCOVERY_PORT);
          discoverySocket.joinGroup(discoveryGroup);
          discoverySocket.setTimeToLive(1); // just run on our local sub-net
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        myUUID = new StatisticalUUID();
        // create heartbeat datagram
        byte[] hbdata = null;
        try {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(baos);
          oos.writeObject(myUUID);
          oos.flush();
          hbdata = baos.toByteArray();
          oos.close();
          baos.close();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        hbDatagram = new DatagramPacket(hbdata, hbdata.length,
                                        discoveryGroup, DISCOVERY_PORT);
        // create and start listen thread  on discovery socket
        DatagramSocketHandler rdr = new DatagramSocketHandler(
            discoverySocket, 1000);
        rdr.addListener(this);
        new Thread(rdr).start();
        // start heartbeating
        new Thread(this).start();
      }

      /**
       * This method starts this discoverer announcing itself to the world.
       */

      public void startAnnouncing() {
        announcer = true;
      }

      /**
       * addListener
       *
       * @param listener DiscoveryListener
       */
      public void addListener(DiscoveryListener listener) {
        listeners.add(listener);
      }

      /**
       * listSessions
       *
       * @return UUID[]
       */
      public UUID[] listSessions() {
        Set ids = timeTracker.keySet();
        UUID[] ar = new UUID[ids.size()];
        return (UUID[]) ids.toArray(ar);
      }

      /**
       * run
       * This method is a loop that runs on its own thread generating
       * heartbeats.
       */
      public void run() {
        while (!done) {
          try {
            if (announcer) {
              discoverySocket.send(hbDatagram);
            }
            checkTimeStamps();
            Thread.sleep(HEARTBEAT_TIME);
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }

      /**
       * checkTimeStamps
       */
      private void checkTimeStamps() {
        List removedIDs = new ArrayList();
        // find all outdated peers
        synchronized (timeTracker) {
          for (Iterator i = timeTracker.entrySet().iterator();
               i.hasNext(); ) {
            Entry entry = (Entry) i.next();
            long tstamp = ( (Long) entry.getValue()).longValue();
            if ( (System.currentTimeMillis() - tstamp) >
                (HEARTBEAT_TIME * 3)) {
              // missed the alst 3 heartbeats. we think its gone
              removedIDs.add(entry.getKey());
            }
          }
        }
        // now remove them
        for (Iterator i = removedIDs.iterator(); i.hasNext(); ) {
          UUID id = (UUID) i.next();
          synchronized (timeTracker) {
            timeTracker.remove(id);
          }
          for (Iterator i2 = listeners.iterator(); i2.hasNext(); ) {
            ( (DiscoveryListener) i2.next()).sessionRemoved(id);
          }
        }
      }

      /**
       * datagramReceived
       *
       * @param dgram DatagramPacket
       */
      public void datagramReceived(DatagramPacket dgram) {
        try {
          ByteArrayInputStream bais = new ByteArrayInputStream(dgram.
              getData());
          ObjectInputStream ois = new ObjectInputStream(bais);
          UUID uuidIn = (UUID) ois.readObject();
          ois.close();
          bais.close();
          boolean isNew = (timeTracker.get(uuidIn) == null);
          synchronized (timeTracker) {
            timeTracker.put(uuidIn, new Long(System.currentTimeMillis()));
          }
          if (isNew) {
            for (Iterator i = listeners.iterator(); i.hasNext(); ) {
              ( (DiscoveryListener) i.next()).sessionAdded(dgram.
                  getAddress(),
                  uuidIn);
            }
          }
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }

      // test main
      public static void main(String[] args) {
        MulticastDiscovery mcd = new MulticastDiscovery();
        mcd.addListener(new DiscoveryListener() {
          public void sessionAdded(InetAddress address,
                                   UUID sessionUUID) {
            System.out.println("Session joined with ID = " +
                               sessionUUID);
          }

          public void sessionRemoved(UUID sessionUUID) {
            System.out.println("Session left with ID = " + sessionUUID);
          }
        });
        mcd.startAnnouncing();

      }
    }
