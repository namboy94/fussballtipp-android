package net.namibsun.johannesbucher.api;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            TippSpielConnection connection = new TippSpielConnection("namboy94", "");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
