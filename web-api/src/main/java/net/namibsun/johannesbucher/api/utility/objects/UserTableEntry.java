package net.namibsun.johannesbucher.api.utility.objects;

public class UserTableEntry {

    private String username;
    private int position;
    private int[] previousPositions;
    private int points;
    private float averagePoints;
    private int amountOfBets;

    public UserTableEntry(String username,
                          int position, int[] previousPositions, int points, float averagePoints, int amountOfBets) {
        this.username = username;
        this.position = position;
        this.previousPositions = previousPositions;
        this.points = points;
        this.averagePoints = averagePoints;
        this.amountOfBets = amountOfBets;
    }

    public String getUsername() {
        return username;
    }

    public int getPosition() {
        return position;
    }

    public int[] getPreviousPositions() {
        return previousPositions;
    }

    public int getPoints() {
        return points;
    }

    public float getAveragePoints() {
        return averagePoints;
    }

    public int getAmountOfBets() {
        return amountOfBets;
    }
}
