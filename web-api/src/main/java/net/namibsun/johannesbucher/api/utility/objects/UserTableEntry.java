package net.namibsun.johannesbucher.api.utility.objects;

/**
 * Class that models a user entry on the UserTable
 */
public class UserTableEntry {

    private String username;
    private int position;
    private int[] previousPositions;
    private int points;
    private float averagePoints;
    private int amountOfBets;

    /**
     * Constructor that assigns the user information to their respective local variables
     * @param username the user's name
     * @param position the user's current position
     * @param previousPositions the user's placement history
     * @param points the current amount of points the user has
     * @param averagePoints the average amount of points the user earned per match
     * @param amountOfBets the total amount of bets the user made
     */
    public UserTableEntry(String username,
                          int position, int[] previousPositions, int points, float averagePoints, int amountOfBets) {
        this.username = username;
        this.position = position;
        this.previousPositions = previousPositions;
        this.points = points;
        this.averagePoints = averagePoints;
        this.amountOfBets = amountOfBets;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the current position in the table
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return the position history of the user
     */
    public int[] getPreviousPositions() {
        return previousPositions;
    }

    /**
     * @return the current amount of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return the average amount of points per match
     */
    public float getAveragePoints() {
        return averagePoints;
    }

    /**
     * @return the amount of bets the user has made so far
     */
    public int getAmountOfBets() {
        return amountOfBets;
    }
}
