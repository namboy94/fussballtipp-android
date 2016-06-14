package net.namibsun.johannesbucher.api.utility.objects;

import java.util.ArrayList;

public class UserTable {

    private ArrayList<UserTableEntry> users = new ArrayList<>();

    public void addUser(UserTableEntry user) {
        this.users.add(user);
    }

    public String toString() {
        String userTable = "";

        for (UserTableEntry user: this.users) {

            String previousPositions = "";
            for (int prevPos : user.getPreviousPositions()) {
                previousPositions += prevPos + "-";
            }

            userTable += user.getPosition() + ": " + user.getUsername() + " " + user.getPoints() + " " + previousPositions + "\n";
        }

        return userTable;
    }

}
