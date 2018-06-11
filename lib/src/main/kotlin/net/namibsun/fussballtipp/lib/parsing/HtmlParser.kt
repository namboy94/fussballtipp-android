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

package net.namibsun.fussballtipp.lib.parsing

import org.jsoup.Jsoup
import net.namibsun.fussballtipp.lib.auth.Session
import net.namibsun.fussballtipp.lib.objects.Match
import net.namibsun.fussballtipp.lib.objects.Team

/**
 * Class that handles HTML parsing
 */
class HtmlParser(private val session: Session) {

    /**
     * Retrieves the currently available matches
     * @return The available matches
     */
    fun getMatches(): List<Match> {
        val parser = Jsoup.parse(session.get("https://fussball-tipp.eu/matchlist"))
        val matches = mutableListOf<Match>()

        for (matchData in parser.getElementsByClass("match-list-link")) {
            val matchId = matchData.attr("href").split("id=")[1].toInt()
            val homeTeam = matchData.getElementsByClass("text-right")[0].text()
            val awayTeam = matchData.getElementsByClass("text-left")[0].text()
            val flags = matchData.getElementsByClass("img-fluid")
            val homeFlag = "https://fussball-tipp.eu/" + flags[0].attr("src")
            val awayFlag = "https://fussball-tipp.eu/" + flags[1].attr("src")

            matches.add(Match(matchId, Team(homeTeam, homeFlag), Team(awayTeam, awayFlag)))
        }
        return matches
    }
}