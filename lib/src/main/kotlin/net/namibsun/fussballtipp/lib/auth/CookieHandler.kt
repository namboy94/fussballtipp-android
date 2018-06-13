package net.namibsun.fussballtipp.lib.auth

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * A CookieJar that stores all cookies
 */
class CookieHandler : CookieJar {

    /**
     * Stores the cookies
     */
    private val cookieStore = mutableMapOf<String, MutableList<Cookie>?>()

    /**
     * Stores cookies from a response
     * @param url: The URL for which to save the cookies
     * @param cookies: The received cookies
     */
    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
        if (url != null) {
            this.cookieStore[url.host()] = cookies
        }
    }

    /**
     * Loads the cookies from the storage for a host
     * @param url: The URL for which to load the cookies
     * @return The cookies
     */
    override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
        if (url != null) {
            return cookieStore[url.host()] ?: mutableListOf()
        }
        return mutableListOf()
    }
}