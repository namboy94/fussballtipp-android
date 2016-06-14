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

        // Check for table rows and additional statistics statistics
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

        int[] standingsArray = new int[standings.size()];

        for (int i = 0; i < standings.size(); i++) {
            standingsArray[standings.size() - i - 1] = standings.get(i);
        }

        return standingsArray;
    }
}
