package net.namibsun.fussballtipp.android.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import net.namibsun.fussballtipp.android.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.login)
    }
}