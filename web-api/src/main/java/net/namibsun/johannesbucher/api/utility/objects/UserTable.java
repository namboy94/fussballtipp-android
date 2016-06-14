package net.namibsun.johannesbucher.api.utility.objects;

import java.util.ArrayList;

public class UserTable {

    private ArrayList<UserTableEntry> users;

    public void addUser(UserTableEntry user) {
        this.users.add(user);
    }

}
