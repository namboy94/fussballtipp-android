package net.namibsun.johannesbucher.api;



import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * A class that establishes the connection to the website and authenticates
 * itself using a supplied username and password
 */
public class TippSpielConnection {

    private String tippSpielUrl = "https://tippspiel.johannes-bucher.de/";
    private Map<String, String> cookies;
    private String username;
    private String password;

    /**
     * The onstructor that sets the username and password for the user to connect to the
     * website with
     * @param username the user's username
     * @param password the user's password
     */
    TippSpielConnection(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Connects to the website using the provided credentials the session is stored using a
     * cookie.
     * @throws IOException in case the connection was unsuccessful or the credentials were not accepted
     */
    public void connect() throws IOException {

        //Log In
        Connection.Response loginResponse = Jsoup
                .connect(this.tippSpielUrl + "login.php")
                .data("user_name", username, "user_pswd", password, "submit", "Log in")
                .method(Connection.Method.POST).execute();

        //Store the authentication token
        this.cookies = loginResponse.cookies();

        //Check if authentication successful
        if (!this.isConnected()) {
            throw new IOException("Incorrect credentials");
        }
    }

    /**
     * Method that checks if the connection to the website is still intact
     * @return true if the connection is fine, false if either the authentiaction expired or the connection
     *          could not be established
     */
    public boolean isConnected() {
        try {
            String loginCheck = Jsoup.connect(this.tippSpielUrl + "rank.php").cookies(cookies).get().toString();
            return (!loginCheck.contains("Passwort vergessen?")); //This String only appears if you are not logged in.
        } catch (IOException e) {
            return false;
        }
    }

}
