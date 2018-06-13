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

package net.namibsun.fussballtipp.lib

import net.namibsun.fussballtipp.lib.auth.Session
import net.namibsun.fussballtipp.lib.exceptions.AuthenticationException
import net.namibsun.fussballtipp.lib.objects.Team
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * A class that tests fetching matches
 */
class TestMatchFetching {

    /**
     * Tests fetching matches.
     * May stop working once the tournament advances
     */
    @Test
    fun testFetchMatches() {
        val session = Session(
                System.getenv("FUSSBALLTIPP_USERNAME"),
                System.getenv("FUSSBALLTIPP_PASSWORD")
        )
        val matches = session.getMatches()

        assertEquals(
                matches[0].homeTeam,
                Team("Russland", "https://fussball-tipp.eu/resources/flags/rus.svg")
        )
    }
}
