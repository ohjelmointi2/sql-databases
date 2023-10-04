package databases.part02;

import java.util.List;

/**
 * A simple application that prints all artists in the Chinook database.
 *
 * This version uses the DAO approach, and the result is cleaner, easier to test
 * and reusable.
 */
public class ArtistAppMain {

    /**
     * The connection string used to connect to the database. If you are using a
     * MySQL database, you will need to change this string to include your username
     * and password. For example:
     *
     * jdbc:mysql://localhost:3306/Chinook?user=CHANGE&password=CHANGE
     *
     * If you change the connection string here, you don't need to change it back
     * when you submit your code. The tests will use a different connection string
     * specified in the test class.
     */
    private static final String JDBC_URL = "jdbc:sqlite:data/Chinook_Sqlite.sqlite";

    public static void main(String[] args) {
        ArtistDAO artistDAO = new ArtistDAO(JDBC_URL);
        List<Artist> artists = artistDAO.getArtists();

        for (Artist artist : artists) {
            System.out.println(artist.getName() + " (" + artist.getId() + ")");
        }
    }
}
