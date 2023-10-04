package databases.part02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ArtistDAOTest {

    /**
     * The connection string used to connect to the database. Note that this string
     * is different from the one used in the main() method. This is because the
     * tests use a different database than the main() method.
     */
    private static final String JDBC_URL = "jdbc:sqlite:data/Chinook_Sqlite_TEST.sqlite";

    /**
     * The DAO we are testing. We use a different DAO with a different JDBC_URL for
     * testing because the other database contains data that we don't want to modify
     * or delete.
     */
    private ArtistDAO artistDAO = new ArtistDAO(JDBC_URL);

    @Test
    void testGetArtistById() {
        Artist artist = artistDAO.getArtistById(50L);

        assertEquals(50L, artist.getId());
        assertEquals("Metallica", artist.getName());
    }

    @Test
    void testArtistByNonExistentIdReturnsNull() {
        Artist artist = artistDAO.getArtistById(-1000L);
        assertEquals(null, artist);
    }

    @Test
    void testGetArtists() {
        List<Artist> artists = artistDAO.getArtists();

        assertTrue(artists.size() > 0);
        assertEquals("A Cor Do Som", artists.get(0).getName(),
                "The artists should be sorted by name in ascending order.");
    }
}
