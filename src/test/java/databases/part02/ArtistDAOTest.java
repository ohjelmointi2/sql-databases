package databases.part02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import databases.utils.TestUtils;

public class ArtistDAOTest {

    /**
     * The connection string used to connect to the database. Note that this string
     * is different from the one used in the main() method. This is because the
     * tests use a different database than the main() method.
     */
    private static final String TEST_JDBC_URL = "jdbc:sqlite:data/Chinook_Sqlite_TEST.sqlite";

    private final Artist rhcp = new Artist(3000, "Red Hot Chili Peppers");

    /**
     * Before each test, we initialize the database to a known state. This ensures
     * that each test is independent of the others.
     */
    @BeforeEach
    void setUp() throws SQLException {
        TestUtils.initialize(TEST_JDBC_URL);
    }

    /**
     * The DAO we are testing. We use a different DAO with a different JDBC_URL for
     * testing because the other database contains data that we don't want to modify
     * or delete.
     */
    private ArtistDAO artistDAO = new ArtistDAO(TEST_JDBC_URL);

    @Test
    void getArtistByIdReturnsCorrectArtist() {
        // Red Hot Chili Peppers has id 3000 in the test database (see
        // TestUtils.initialize())
        Artist artist = artistDAO.getArtistById(rhcp.getId());

        assertEquals(rhcp, artist, "getArtistById() should return the correct artist.");
    }

    @Test
    void getArtistByIdReturnsNullWhenNoArtistIsFound() {
        Artist artist = artistDAO.getArtistById(-1000L);
        assertEquals(null, artist, "getArtistById() should return null if the artist does not exist.");
    }

    @Test
    void getArtistsReturnsAListOfArtists() {
        List<Artist> artists = artistDAO.getArtists();
        assertNotNull(artists, "getArtists() should not return null.");

        // There should be 5 artists in the test database (see TestUtils.initialize())
        assertEquals(5, artists.size(), "getArtists() should return a list of all artists in the database.");
    }

    @Test
    void getArtistsReturnsArtistsOrderedByTheirNames() {
        List<Artist> artists = artistDAO.getArtists();

        // There are 5 artists in the test database
        assertNotNull(artists, "getArtists() should not return null.");
        assertEquals(5, artists.size());

        // Each artist should have the correct name and id. See TestUtils.initialize().
        Artist abba = new Artist(5000L, "ABBA");
        Artist ledZeppelin = new Artist(1000L, "Led Zeppelin");
        Artist pinkFloyd = new Artist(4000L, "Pink Floyd");
        Artist radiohead = new Artist(2000L, "Radiohead");

        List<Artist> expected = List.of(abba, ledZeppelin, pinkFloyd, radiohead, rhcp);
        assertEquals(expected, artists);
    }
}
