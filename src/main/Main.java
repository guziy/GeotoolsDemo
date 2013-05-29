package main;

import org.geotools.geometry.jts.JTS;

/**
 * User: san
 * Date: 2-Aug-2010
 * Time: 9:51:55 AM
 */
public class Main {


    public static void main(String[] args) {
        JTS.smooth(null, 0.5);
        System.out.println("Hello world");
    }

}
