package net.namibsun.johannesbucher.api;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        TippSpielConnection connection = new TippSpielConnection("namboy94", "");
        connection.connect();
        connection.getCurrentTable();
    }
}
