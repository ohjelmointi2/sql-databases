package databases.part02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        try (
                Connection conn = DriverManager.getConnection(JDBC_CONNECTION_STRING);
                PreparedStatement statement = conn
                        .prepareStatement("SELECT ArtistId, Name FROM Artist ORDER BY Name ASC");
                ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                Artist artist = getArtist(resultSet);
                artists.add(artist);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        return getArtists()
                .stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates an Artist object from the current row in the result set.
     */
    private Artist getArtist(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("ArtistId");
        String name = resultSet.getString("Name");

        return new Artist(id, name);
    }
}
