package net.namibsun.johannesbucher.api;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Main {

    public static void main(String[] args) {

        String url = "https://tippspiel.johannes-bucher.de/";
        System.out.println(url);

        try {
            System.out.println(Unirest.get(url).asString().getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }


    }
}
