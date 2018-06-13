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

package net.namibsun.fussballtipp.lib.objects

import net.namibsun.fussballtipp.lib.auth.Session

/**
 * Class that models a match
 * @param id: The ID of the match in the fussball-tipp.eu website
 * @param homeTeam: The team classified as the home team
 * @param awayTeam: The team classified as the away team
 */
data class Match(
    private val id: Int,
    val homeTeam: Team,
    val awayTeam: Team,
    val homeTeamBet: Int?,
    val awayTeamBet: Int?
) {

    fun placeBet(session: Session, homeScore: Int, awayScore: Int) {
        session.post("https://fussball-tipp.eu/bet.php", hashMapOf(
                "id" to this.id.toString(),
                "home-bet" to homeScore.toString(),
                "away-bet" to awayScore.toString()
        ))
    }
}
