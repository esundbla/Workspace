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

    import java.security.*;

    public class StatisticalUUID
        implements UUID {
      transient static SecureRandom random = null;
      private long randomValue;
      private long timeValue;
      public StatisticalUUID() {
        if (random == null) {
          try {
            random = SecureRandom.getInstance("SHA1PRNG");
          }
          catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
          }
        }
        randomValue = random.nextLong();
        timeValue = System.currentTimeMillis();
      }

      public int compareTo(Object object) {
        StatisticalUUID other = (StatisticalUUID) object;
        if (timeValue < other.timeValue) {
          return -1;
        }
        else if (timeValue > other.timeValue) {
          return 1;
        }
        else {
          if (randomValue < other.randomValue) {
            return -1;
          }
          else if (randomValue > other.randomValue) {
            return 1;
          }
        }
        return 0;
      }

      public String toString() {
        return ("UUID(" + timeValue + ":" + randomValue + ")");
      }

      public int hashCode() {
        return ( (int) ( (timeValue >> 32) & 0xFFFFFFFF)) ^
            ( (int) (timeValue & 0xFFFFFFFF)) ^
            ( (int) ( (randomValue) >> 32) & 0xFFFFFFFF) ^
            ( (int) (randomValue & 0xFFFFFFFF));
      }

      public boolean equals(Object obj) {
        StatisticalUUID other = (StatisticalUUID) obj;
        return ( (timeValue == other.timeValue) &&
                (randomValue == other.randomValue));
      }
    }
