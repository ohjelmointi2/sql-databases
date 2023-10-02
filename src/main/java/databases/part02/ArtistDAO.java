package databases.part02;

import java.util.ArrayList;
import java.util.List;

public class ArtistDAO {

    private static final String JDBC_CONNECTION_STRING = "jdbc:sqlite:data/Chinook_Sqlite.sqlite";

    /**
     * Returns a list of all artists in the database. The list is ordered by artist
     * name. If there are no artists in the database, an empty list is returned.
     *
     * @return a list of all artists in the database.
     */
    public List<Artist> getArtists() {
        List<Artist> artists = new ArrayList<>();

        /*
         * hint: see the class from part 1 for an example of how to connect to the
         * database and retrieve data from it. Create new Artist objects instead of
         * printing the results to the console.
         */

        return artists;
    }

    /**
     * Returns the artist with the specified id, or null if no artist exists with
     * that id.
     *
     * @param id the id of the artist to retrieve.
     * @return the artist with the specified id, or null if no artist exists with
     *         that id.
     */
    public Artist getArtistById(long id) {
        /*
         * hint: use similar code to the getArtists() method above, but add a WHERE
         * clause to the SQL query to only retrieve the artist with the specified id.
         * You can also just call the getArtists() method above and iterate through the
         * results until you find the artist with the specified id. This is less
         * efficient, but it gets the job done and is easy to implement.
         */
        return null;
    }
}
