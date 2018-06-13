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
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import net.namibsun.fussballtipp.android.R
import net.namibsun.fussballtipp.android.global.SessionSingleton
import net.namibsun.fussballtipp.android.views.BetView
import org.jetbrains.anko.doAsync

/**
 * This activity allows a user to place bets, as well as view already placed bets
 */
class BetActivity : Activity() {

    /**
     * List of the individual bet views
     */
    private val betViews: MutableList<BetView> = mutableListOf()

    /**
     * View that contains the individual bet views
     */
    private val betList = this.findViewById<LinearLayout>(R.id.bets_list)

    /**
     * A progress spinner
     */
    private val progress = findViewById<View>(R.id.bets_progress)

    /**
     * Initializes the Activity. Sets the OnClickListeners for the bet button and starts
     * fetching bet and match data asynchronously.
     * @param savedInstanceState: The Instance Information of the app.
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        this.setContentView(R.layout.bets)
        super.onCreate(savedInstanceState)

        this.findViewById<View>(R.id.bets_submit_button).setOnClickListener { this.placeBets() }

        this@BetActivity.doAsync {
            this@BetActivity.populateBetList()
        }
    }

    /**
     * Populates the bet list with data from fussball-tipp.eu
     */
    private fun populateBetList() {
        this.startNetwork()
        this@BetActivity.doAsync {
            for (match in SessionSingleton.session!!.getMatches()) {
                val betView = BetView(this@BetActivity, match)
                this@BetActivity.betViews.add(betView)
            }
            this@BetActivity.runOnUiThread { this@BetActivity.renderBets() }
        }
    }

    /**
     * Method that should be called before network activity. Clears all bet views
     * and sets the progress spinner's visibility to VISIBLE
     */
    private fun startNetwork() {
        this.betList.removeAllViews()
        this.progress.visibility = View.VISIBLE
    }

    /**
     * Renders the bets and sets the visibility of the progress spinner to INVISIBLE
     */
    private fun renderBets() {
        for (betView in this.betViews) {
            this.betList.addView(betView)
        }
        this.progress.visibility = View.INVISIBLE
    }

    /**
     * Places all currently set bets
     */
    private fun placeBets() {
        this.startNetwork()
        this@BetActivity.doAsync {
            for (betView in this@BetActivity.betViews) {
                betView.placeBet()
            }
        }
    }
}
