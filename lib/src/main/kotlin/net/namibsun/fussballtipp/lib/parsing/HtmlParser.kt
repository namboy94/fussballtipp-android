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
            val homeFlag = this.mapSvgFlagUrlToPngFlagUrl(flags[0].attr("src"))
            val awayFlag = this.mapSvgFlagUrlToPngFlagUrl(flags[1].attr("src"))

            val bet = matchData.getElementsByClass("bet")[0].text()

            val bets = if (bet.contains("Tipp abgeben!")) { // No bet yet!
                Pair(null, null)
            } else {
                val components = bet.split("Dein Tipp: ")[1].split("-")
                Pair(components[0].toInt(), components[1].toInt())
            }

            matches.add(Match(
                    matchId,
                    Team(homeTeam, homeFlag),
                    Team(awayTeam, awayFlag),
                    bets.first,
                    bets.second
            ))
        }
        return matches
    }

    /**
     * Maps an SVG Url for a countries' flag to a PNG file
     * @param flagUrl: The SVG Url
     * @return The PNG URL
     */
    private fun mapSvgFlagUrlToPngFlagUrl(flagUrl: String): String {

        val countryCode = flagUrl.split("/flags/")[1].split(".svg")[0]
        val newCode = hashMapOf(
                "rus" to "ru",
                "ksa" to "sa",
                "egy" to "eg",
                "uru" to "uy",
                "mar" to "ma",
                "irn" to "ir",
                "por" to "pt",
                "esp" to "es",
                "fra" to "fr",
                "aus" to "au",
                "arg" to "ar",
                "isl" to "is",
                "per" to "pe",
                "den" to "dk",
                "cro" to "hr",
                "nga" to "ng",
                "crc" to "cr",
                "srb" to "rs",
                "ger" to "de",
                "mex" to "mx",
                "bra" to "br",
                "sui" to "ch",
                "swe" to "se",
                "kor" to "kr",
                "bel" to "be",
                "pan" to "pa",
                "tun" to "tn",
                "eng" to "ENGLAND",
                "col" to "co",
                "jpn" to "jp",
                "pol" to "pl",
                "sen" to "sn"
        )[countryCode]

        return if (newCode == "ENGLAND") {
            "https://upload.wikimedia.org/wikipedia/en/thumb/b/be/" +
                    "Flag_of_England.svg/320px-Flag_of_England.svg.png"
        } else {
            "http://flags.fmcdn.net/data/flags/h80/$newCode.png"
        }
    }
}