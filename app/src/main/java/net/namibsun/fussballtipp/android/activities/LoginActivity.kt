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

package net.namibsun.fussballtipp.android.activities

import android.app.Activity
import net.namibsun.fussballtipp.lib.auth.Session
import net.namibsun.fussballtipp.android.R
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.EditText
import org.jetbrains.anko.doAsync
import java.io.IOException
import net.namibsun.fussballtipp.android.global.SessionSingleton

/**
 * The Login Screen that enables the user to log in to the fussball-tipp.eu
 * website using a username and password, which will be prompted during the login process.
 * Credentials can be stored locally on the device.
 */
class LoginActivity : Activity() {

    /**
     * Initializes the Login Activity. Sets the OnClickListener of the
     * login button and sets the input fields with stored data if available
     * @param savedInstanceState: The Instance Information of the app.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.login)

        this.findViewById<View>(R.id.login_screen_button).setOnClickListener { this.login() }
        this.findViewById<View>(R.id.login_screen_logo).setOnClickListener { this.login() }

        val prefs = this.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)!!
        val username = prefs.getString("username", "")
        val password = prefs.getString("password", "")
        this.findViewById<EditText>(R.id.login_screen_username).setText(username)
        this.findViewById<EditText>(R.id.login_screen_password).setText(password)

        this.findViewById<View>(R.id.login_screen_button).setOnClickListener {
            val uri = Uri.parse("https://fussball-tipp.eu/loginpage?register")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            this.startActivity(intent)
        }
    }

    /**
     * Attempts to log in the user
     */
    private fun login() {
        val username = this.findViewById<EditText>(R.id.login_screen_username).text.toString()
        val password = this.findViewById<EditText>(R.id.login_screen_password).text.toString()
        Log.i("LoginActivity", "$username trying to log in.")

        this.setUiElementEnabledState(false)

        this@LoginActivity.doAsync {
            try {
                SessionSingleton.session = Session(username, password)
                Log.i("LoginActivity", "Login Successful")
                this@LoginActivity.storeCredentials(username, password)
                this@LoginActivity.runOnUiThread { this@LoginActivity.showBetsActivity() }
            } catch (e: IOException) {
                Log.i("LoginActivity", "Login Failed")
                SessionSingleton.session = null
                this@LoginActivity.runOnUiThread { this@LoginActivity.showLoginErrorDialog() }
            }
            this@LoginActivity.runOnUiThread {
                this@LoginActivity.setUiElementEnabledState(true)
            }
        }
    }

    /**
     * Enables or disables all user-editable UI elements
     * @param state: Sets the enabled state of the elements
     */
    private fun setUiElementEnabledState(state: Boolean) {
        this.findViewById<View>(R.id.login_screen_logo).isEnabled = state
        this.findViewById<View>(R.id.login_screen_button).isEnabled = state
        this.findViewById<View>(R.id.login_screen_username).isEnabled = state
        this.findViewById<View>(R.id.login_screen_password).isEnabled = state
        this.findViewById<View>(R.id.login_screen_remember).isEnabled = state

        if (state) { // Activate UI elements
            findViewById<View>(R.id.login_screen_logo).clearAnimation()
        } else {
            val animation = AnimationUtils.loadAnimation(this, R.anim.rotate)
            this.findViewById<View>(R.id.login_screen_logo).startAnimation(animation)
        }
    }

    /**
     * Stores the credentials after a successful login if the "Remember Me"
     * checkbox is checked
     * @param username: The username to store
     * @param password: The password to store
     */
    private fun storeCredentials(username: String, password: String) {
        if (this.findViewById<CheckBox>(R.id.login_screen_remember).isChecked) {
            val editor = this.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)!!.edit()
            editor.putString("username", username)
            editor.putString("password", password)
            editor.apply()
        }
    }

    /**
     * Shows the bets activity
     */
    private fun showBetsActivity() {
        this.startActivity(Intent(this, LoginActivity::class.java))
    }

    /**
     * Shows a dialog indicating that the login failed
     */
    private fun showLoginErrorDialog() {
        val errorDialogBuilder = AlertDialog.Builder(this)
        errorDialogBuilder.setTitle(this.getString(R.string.login_error_title))
        errorDialogBuilder.setMessage(this.getString(R.string.login_error_body))
        errorDialogBuilder.setCancelable(true)
        errorDialogBuilder.setPositiveButton("Ok") { dialog, _ -> dialog!!.dismiss() }
        errorDialogBuilder.create()
        errorDialogBuilder.show()
    }
}
