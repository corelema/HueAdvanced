package com.cocorporation.hueadvanced.shared;

/**
 * Created by Corentin on 12/15/2015.
 */
public class Util {
    public static boolean isIPAddressValid(String ipAddress) {
        return (ipAddress != null) && !ipAddress.equals("");
    }

}
