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

package net.namibsun.johannesbucher.api.utility;

import net.namibsun.johannesbucher.api.utility.objects.UserTable;
import net.namibsun.johannesbucher.api.utility.objects.UserTableEntry;
import org.javatuples.Triplet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Class that scrapes the user table displayed by rank.php
 */
public class UserTableScraper {

    /**
     * Gets the current table information as a UserTable object
     * @param cookies the authenticated cookie to be used to log in
     * @return the current table
     */
    public static UserTable getCurrentTable(Map<String, String> cookies) {

        Triplet<Elements, Elements, String[]> tableData = UserTableScraper.fetchTableData(cookies);
        UserTable table = new UserTable();

        assert tableData != null;
        Elements profileElements = tableData.getValue0();

        for (int i = 0; i < tableData.getValue0().size(); i++) {
            Element profile = tableData.getValue0().get(i);
            Element additionalStats = tableData.getValue1().get(i);
            String previousStandings = tableData.getValue2()[i];

            int[] previousPositions = UserTableScraper.parsePreviousStandings(previousStandings);
            table.addUser(UserTableScraper.parseTableEntry(profile, previousPositions));

        }

        return table;
    }

    /**
     * Fetches the table data and returns it as a triplet
     * @param cookies the cookie that holds the authentication data
     * @return The normal table elements, additional statistics, information about previous placements
     */
    private static Triplet<Elements, Elements, String[]> fetchTableData(Map<String, String> cookies) {

        Document ranks;
        try {
            ranks = Jsoup.connect("https://tippspiel.johannes-bucher.de/rank.php").cookies(cookies).get();
        } catch (IOException e) {
            return null;
        }

        Elements elements = ranks.select("tr");
        Elements profileElements = new Elements();
        Elements otherStatistics = new Elements();

        for (Element element : elements) {
            if (element.toString().contains("profile.php")) {
                profileElements.add(element);
            }
            else if (element.toString().contains("<td style=\"text-align: left\">")) {
                otherStatistics.add(element);
            }
        }
        assert profileElements.size() == otherStatistics.size();

        String[] previousGames = UserTableScraper.preparePreviousStandings(ranks, profileElements.size());


        return new Triplet<>(profileElements, otherStatistics, previousGames);

    }

    /**
     * Parses the main user table to determine the most important stats of the participating users
     * @param profile the html element containing the information on a particular user
     * @param previousPositions the previous positions of that user as determined by parsePreviousStandings()
     * @return A new UserTableEntry object with the parsed information
     */
    private static UserTableEntry parseTableEntry(Element profile, int[] previousPositions) {
        String[] userTableData = profile.toString().split("<td");

        int position = Integer.parseInt(userTableData[2].split("<b>")[1].split("</b>")[0]);

        String username = userTableData[4]
                .split("<a href=\"profile\\.php\\?")[1]
                .split("\">")[1]
                .split("</a>")[0];

        int points = Integer.parseInt(userTableData[5].split(">", 2)[1].split("</td>")[0]);
        float averagePoints = Float.parseFloat(userTableData[6].split(">", 2)[1].split("</td>")[0]);
        int amountOfBets = Integer.parseInt(userTableData[7].split(">", 2)[1].split("</td>")[0]);

        return new UserTableEntry(
                username,
                position,
                previousPositions,
                points,
                averagePoints,
                amountOfBets);
    }

    /**
     * Extracts the relevant Javascript snippet from the HTML code to be able to determine the
     * previous standings of a user
     * @param rankDocument the HTML document of rank.php
     * @param userCount the amount of users in that document
     * @return an array of Strings that define the previous standings of the users. This needs to be further parsed
     *         using parsePreviousStandings
     */
    private static String[] preparePreviousStandings(Document rankDocument, int userCount) {
        ArrayList<String> rawPreviousGames = new ArrayList<>(Arrays.asList(rankDocument.toString()
                .split("series: \\[\\{")[1]
                .split("}\\);\n}\\);")[0]
                .split("name: '")));
        rawPreviousGames.remove(0);
        String[] previousGames = new String[userCount];

        int counter = -1;
        for (String gameElement : rawPreviousGames) {
            if (gameElement.contains("color") && gameElement.contains("data")) {
                counter++;
                previousGames[counter] = ""; //gameElement;  If we want to check if the username is correct,
                                             //              but breaks parsePreviousStandings()
            }
            else {
                previousGames[counter] += gameElement;
            }
        }
        return previousGames;
    }

    /**
     * Parses the previous standings of a user
     * The result is an array of integers that are ordered from newest->oldest
     * @param previousStandings the previous standings string generated by preparePreviousStandings
     * @return the integer array that models the previous positions of the user in the table
     */
    private static int[] parsePreviousStandings(String previousStandings) {

        if (previousStandings == null) {
            return new int[]{};
        }

        ArrayList<Integer> standings = new ArrayList<>();

        for (String standing : previousStandings.split("y: ")) {
            try {
                String standingString = standing.split(",")[0];
                standings.add(new Integer(standingString));
            } catch (NumberFormatException ignored) {
            }
        }

        //Convert to int array
        int[] standingsArray = new int[standings.size()];

        for (int i = 0; i < standings.size(); i++) {
            standingsArray[standings.size() - i - 1] = standings.get(i);
        }

        return standingsArray;
    }
}
