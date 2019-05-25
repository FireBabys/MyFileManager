package net.zhengtianyi.util;

import org.apache.tika.Tika;

public class SingLetonTika {

    private static Tika tika = new Tika();

    private SingLetonTika(){};

    public static Tika getInstance() {
        return tika;
    }



}
