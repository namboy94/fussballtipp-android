package net.namibsun.johannesbucher.api;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;

public class Main {

    public static void main(String[] args) {

        String url = "https://tippspiel.johannes-bucher.de/";
        System.out.println(url);

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);



    }
}
