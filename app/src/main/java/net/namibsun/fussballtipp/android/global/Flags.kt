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

package net.namibsun.fussballtipp.android.global

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import net.namibsun.fussballtipp.lib.objects.Team
import java.net.URL

/**
 * Singleton that keeps track of flags
 */
object Flags {

    /**
     * Map that keeps track of the downloaded flag bitaps
     */
    private val flags = mutableMapOf<String, Bitmap>()

    /**
     * Retrieves a flag bitmap either from memory or by downloading it
     * @param team: The team for which to download the flag
     * @return The flag bitmap
     */
    fun getFlag(team: Team): Bitmap {

        while (team.name !in flags) {
            this.downloadFlag(team)
        }
        return this.flags[team.name]!!
    }

    /**
     * Downloads a flag bitmap from an URL and stores it in the map
     * @param team: The team for which to download the flag for
     */
    private fun downloadFlag(team: Team) {
        val url = URL(team.flagUrl)
        val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        this.flags[team.name] = bitmap
    }
}