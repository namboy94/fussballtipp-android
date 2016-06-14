package net.namibsun.johannesbucher.api;



import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.CookieStore;
import java.util.Map;

/**
 * A class that establishes the connection to the website and authenticates
 * itself using a supplied username and password
 */
public class TippSpielConnection {

    private String tippSpielUrl = "https://tippspiel.johannes-bucher.de/";

    TippSpielConnection(String username, String password) throws IOException {

        //Log In
        Connection.Response response = Jsoup.connect(this.tippSpielUrl + "login.php")
                .data("user_name", username, "user_pswd", password, "submit", "Log in")
                .method(Connection.Method.POST).execute();

        Map<String, String> cookies = response.cookies();
        System.out.print(cookies);

        Document doc = Jsoup.connect(this.tippSpielUrl + "rank.php").cookies(cookies).get();
        System.out.print(doc.toString());
    }

}
