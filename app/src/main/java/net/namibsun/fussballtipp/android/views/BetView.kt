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

package net.namibsun.fussballtipp.android.views

import net.namibsun.fussballtipp.android.R
import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.CardView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import net.namibsun.fussballtipp.android.global.Flags
import net.namibsun.fussballtipp.android.global.SessionSingleton
import net.namibsun.fussballtipp.lib.objects.Match
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread

@SuppressLint("ViewConstructor")
/**
 * A custom view that displays a single matchup and editable fields for bets.
 * @param match: The match to display
 */
class BetView(
    context: Context,
    private val match: Match
) : CardView(context, null) {

    /**
     * Initializes the Bet View. Initializes all the text data and downloads/displays the
     * Flags of the teams
     */
    init {

        View.inflate(context, R.layout.bet, this)

        // Display Team names
        val homeTeamTitle = this.findViewById<TextView>(R.id.bet_home_team_title)
        val awayTeamTitle = this.findViewById<TextView>(R.id.bet_away_team_title)
        homeTeamTitle.text = this.match.homeTeam.name
        awayTeamTitle.text = this.match.awayTeam.name

        if (this.match.homeTeamBet != null && this.match.awayTeamBet != null) {
            val homeTeamEdit = this.findViewById<EditText>(R.id.bet_home_team_edit)
            val awayTeamEdit = this.findViewById<EditText>(R.id.bet_away_team_edit)
            homeTeamEdit.setText("${this.match.homeTeamBet}")
            awayTeamEdit.setText("${this.match.awayTeamBet}")
        }

        // Download/Display the Logos
        val homeImage = this.findViewById<ImageView>(R.id.bet_home_team_flag)
        val awayImage = this.findViewById<ImageView>(R.id.bet_away_team_flag)

        context.doAsync {
            val homeTeamLogoBitmap = Flags.getFlag(this@BetView.match.homeTeam)
            val awayTeamLogoBitmap = Flags.getFlag(this@BetView.match.awayTeam)

            this@BetView.context.runOnUiThread {
                homeImage.setImageBitmap(homeTeamLogoBitmap)
                awayImage.setImageBitmap(awayTeamLogoBitmap)
            }
        }
    }

    /**
     * Places the currently set bet values
     */
    fun placeBet() {
        try {
            val homeBet =
                    this.findViewById<EditText>(R.id.bet_home_team_edit).text.toString().toInt()
            val awayBet =
                    this.findViewById<EditText>(R.id.bet_away_team_edit).text.toString().toInt()
            if (this.match.homeTeamBet != homeBet || this.match.awayTeamBet != awayBet) {
                this.match.placeBet(SessionSingleton.session!!, homeBet, awayBet)
            }
        } catch (e: NumberFormatException) {
            // Skip when input can't be parsed as integers
        }
    }
}
