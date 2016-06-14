/*
Copyright 2016 Hermann Krumrey

This file is part of johannes-bucher-tippspiel.

    johannes-bucher-tippspiel is a library/program to display and interact with
    tippspiel.johannes-bucher.de

    johannes-bucher-tippspiel is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    johannes-bucher-tippspiel is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with johannes-bucher-tippspiel. If not, see <http://www.gnu.org/licenses/>.
*/

package net.namibsun.johannesbucher.api;

import net.namibsun.johannesbucher.api.utility.UserTableScraper;
import net.namibsun.johannesbucher.api.utility.objects.UserTable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * A class that establishes the connection to the website and authenticates
 * itself using a supplied username and password
 */
public class TippSpielConnection {

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
                .connect("https://tippspiel.johannes-bucher.de/login.php")
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
            String loginCheck = Jsoup
                    .connect("https://tippspiel.johannes-bucher.de/rank.php")
                    .cookies(cookies).get().toString();

            return (!loginCheck.contains("Passwort vergessen?")); //This String only appears if you are not logged in.
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Wrapper method around the utils.Scraper.getCurrentTable method that supplies the method with the
     * authenticated cookie
     * @return the current table of the tippspiel
     */
    public UserTable getCurrentTable() {
        return UserTableScraper.getCurrentTable(this.cookies);
    }
}
