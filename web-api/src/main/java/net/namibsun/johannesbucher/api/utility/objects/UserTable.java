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
