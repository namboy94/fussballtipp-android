/*
Copyright 2018 Hermann Krumrey <hermann@krumreyh.com>

This file is part of fussballtipp-android.

fussballtipp-android is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

fussballtipp-android is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with fussballtipp-android.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.namibsun.fussballtipp.lib.auth

import net.namibsun.fussballtipp.lib.exceptions.AuthenticationException
import net.namibsun.fussballtipp.lib.objects.Match
import net.namibsun.fussballtipp.lib.parsing.HtmlParser
import okhttp3.OkHttpClient
import okhttp3.FormBody
import okhttp3.Request

/**
 * Class that handles all website access. The internals of this class should be
 * replaced with API calls once the API is implemented
 * @param username: The username to use
 * @param password: The password to use
 */
class Session(private val username: String, private val password: String) {

    /**
     * The internal HTTP client
     */
    private val client = OkHttpClient.Builder().cookieJar(CookieHandler()).build()

    /**
     * Tries to log in using the username and password provided in the constructor
     * Login Failures may lead to AuthenticationExceptions being thrown
     */
    init {
        this.login()
        if (!this.isLoggedIn()) {
            throw AuthenticationException(this.username)
        }
    }

    /**
     * Logs in the user
     */
    private fun login() {
        this.post("https://fussball-tipp.eu/login", hashMapOf(
            "login-btn" to "",
                "username" to this.username,
                "password" to this.password
        ))
    }

    /**
     * Checks if the user is logged in in this session
     * @return true if the session is logged in, false otherwise
     */
    fun isLoggedIn(): Boolean {
        val body = this.get("https://fussball-tipp.eu/matchlist")
        return !body.contains("Du bist nicht angemeldet")
    }

    /**
     * Executes a GET request
     * @param url: The URL to which to send the request
     * @return The response body
     */
    fun get(url: String): String {
        val request = Request.Builder().url(url).build()
        val response = this.client.newCall(request).execute()
        return response.body()!!.string()
    }

    /**
     * Executes a POST request
     * @param url: The URL to send the request to
     * @param params: The parameters to send in the POST request
     */
    fun post(url: String, params: HashMap<String, String>) {

        var formBuilder = FormBody.Builder()
        for (key in params.keys) {
            formBuilder = formBuilder.add(key, params[key]!!)
        }
        val request = Request.Builder().url(url).post(formBuilder.build()).build()
        this.client.newCall(request).execute()
    }

    /**
     * Retrieves a list of matches on which the user can currently bet on
     * @return The list of matches
     */
    fun getMatches(): List<Match> {
        val parser = HtmlParser(this)
        return parser.getMatches()
    }
}
