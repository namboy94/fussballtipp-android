package net.namibsun.johannesbucher.api;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        TippSpielConnection connection = new TippSpielConnection("namboy94", Password.pass);
        connection.connect();
        System.out.print(connection.getCurrentTable().toString());
    }
}
