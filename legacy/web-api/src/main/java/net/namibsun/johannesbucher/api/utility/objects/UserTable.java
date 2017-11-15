/*
Copyright 2016 Hermann Krumrey

This file is part of johannes-bucher-tippspiel.

    johannes-bucher-tippspiel is a library/program to display and interact with
    tippspiel.johannes-bucher.de

    johannes-bucher-tippspiel is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    johannes-bucher-tippspiel is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with johannes-bucher-tippspiel. If not, see <http://www.gnu.org/licenses/>.
*/

package net.namibsun.johannesbucher.api.utility.objects;

import java.util.ArrayList;

/**
 * Class that holds the user table of the rank.php page
 */
public class UserTable {

    private ArrayList<UserTableEntry> users = new ArrayList<>();

    /**
     * Adds a user to the users array list
     * @param user the user to be added
     */
    public void addUser(UserTableEntry user) {
        this.users.add(user);
    }

    /**
     * Turns the UserTable into a string
     * @return the generated string
     */
    public String toString() {

        String userTable = "";

        for (UserTableEntry user: this.users) {

            String previousPositions = "";
            for (int prevPos : user.getPreviousPositions()) {
                previousPositions += prevPos + "-";
            }

            userTable += user.getPosition() + ": " + user.getUsername() + " " + user.getPoints();
        }

        return userTable;
    }

}
