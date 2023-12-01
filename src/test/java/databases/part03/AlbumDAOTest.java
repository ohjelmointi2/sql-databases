package databases.part03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import databases.part02.Artist;
import databases.utils.TestUtils;

public class AlbumDAOTest {
    /**
     * The connection string used to connect to the database. Note that this string
     * is different from the one used in the main() method. This is because the
     * tests use a different database than the main() method.
     */
    private static final String TEST_JDBC_URL = "jdbc:sqlite:data/Chinook_Sqlite_TEST.sqlite";

    /**
     * The DAO we are testing. We use a different DAO object with a different
     * JDBC_URL for testing because the other database contains data that we don't
     * want to modify or delete.
     */
    private AlbumDAO albumDAO = new AlbumDAO(TEST_JDBC_URL);

    // RHCP has 2 albums in test database (see TestUtils.initialize())
    private final Artist redHotChiliPeppers = new Artist(3000, "Red Hot Chili Peppers");
    private final Album rhcpAlbum1 = new Album(9001, "Californication", 3000);
    private final Album rhcpAlbum2 = new Album(9003, "By the Way", 3000);

    // ABBA has no albums in test database (see TestUtils.initialize())
    private final Artist abba = new Artist(5000, "ABBA");

    /**
     * Before each test, we initialize the database to a known state. This ensures
     * that each test is independent of the others.
     */
    @BeforeEach
    void setUp() throws SQLException {
        TestUtils.initialize(TEST_JDBC_URL);
    }

    /**
     * There are two albums for Red Hot Chili Peppers in the test database. This
     * test verifies that we can retrieve both of them in the correct order and
     * with the correct data.
     */
    @Test
    void getAlbumsByArtistReturnsTwoAlbumsForRedHotChiliPeppers() {
        List<Album> albums = albumDAO.getAlbumsByArtist(redHotChiliPeppers);

        assertNotNull(albums, "The list of albums for RHCP should not be null");

        // There should be 2 albums:
        assertEquals(2, albums.size());

        // The albums returned should equal the ones in the test database:
        assertEquals(List.of(rhcpAlbum1, rhcpAlbum2), albums);
    }

    /**
     * There are no albums for ABBA in the test database.
     */
    @Test
    void getAlbumsByArtistReturnsEmptyListOfAlbumsForAbba() {
        List<Album> albums = albumDAO.getAlbumsByArtist(abba);

        assertNotNull(albums, "The list of albums should not be null");
        assertTrue(albums.isEmpty(), "The list of albums should be empty");
    }

    /**
     * This test adds a new album for ABBA. The test verifies that the album was
     * added successfully, and that it is the last album for ABBA.
     */
    @Test
    void addAlbumStoresTheNewAlbumInTheDatabase() {
        Album superTrouper = new Album("Super Trouper", abba.getId());

        boolean success = albumDAO.addAlbum(superTrouper);
        assertTrue(success, "The album should have been added and the method should return true");

        // There should now be 1 album for ABBA:
        List<Album> albums = albumDAO.getAlbumsByArtist(abba);

        assertEquals(1, albums.size());

        Album added = albums.get(0);

        assertEquals(superTrouper.getTitle(), added.getTitle(), "The title of the album should be correct");
        assertEquals(superTrouper.getArtistId(), added.getArtistId(), "The artist id of the album should be correct");
    }

    /**
     * This test adds a new album for ABBA. The album's title contains characters
     * that need to be escaped in SQL queries. The test verifies that the album was
     * added successfully, and that the title is correct.
     */
    @Test
    void albumNamesWithSpecialCharactersAreProperlyEscaped() {
        // This title contains characters that need to be escaped in SQL queries:
        String unsafeTitle = "greatest hits'); DROP TABLE \"Album\"; --";

        Album album = new Album(unsafeTitle, abba.getId());

        // The album should be added successfully:
        boolean added = albumDAO.addAlbum(album);
        assertTrue(added, "The album should have been added and the method should return true");

        // There should now be 1 album for ABBA:
        List<Album> albums = albumDAO.getAlbumsByArtist(abba);
        assertEquals(1, albums.size());

        Album addedAlbum = albums.get(albums.size() - 1);

        // If the title matches, the escaping worked correctly:
        assertEquals(unsafeTitle, addedAlbum.getTitle());
    }

    @Test
    void updatingAnAlbumChangesTheTitleInDatabase() {
        // Same id and artist as in the test database, but a different title:
        Album remastered = new Album(9001, "Californication remastered", 3000);

        // Make sure the album was updated successfully:
        boolean updated = albumDAO.updateAlbum(remastered);
        assertTrue(updated, "The album should have been updated and the method should return true");

        // Verify that the name of the last album was updated by retrieving the albums:
        List<Album> albumsAfterUpdate = albumDAO.getAlbumsByArtist(redHotChiliPeppers);

        // There should still be 2 albums as before:
        assertEquals(2, albumsAfterUpdate.size());

        // The first album should now match the details we provided:
        assertEquals(remastered, albumsAfterUpdate.get(0));
    }

    @Test
    void deleteAlbumRemovesTheAlbumFromTheDatabase() {
        // Delete the last album:
        boolean deleted = albumDAO.deleteAlbum(rhcpAlbum1);
        assertTrue(deleted, "The album should have been deleted and the method should return true");

        // Verify that the album was deleted by retrieving the albums:
        List<Album> albumsAfterDelete = albumDAO.getAlbumsByArtist(redHotChiliPeppers);
        assertEquals(1, albumsAfterDelete.size());

        // The second album should be the only album left for RHCP:
        assertEquals(rhcpAlbum2.getTitle(), albumsAfterDelete.get(0).getTitle());
    }

    @Test
    void deleteAlbumReturnsFalseIfAnAlbumWasNotDeleted() {
        // Delete an album that does not exist:
        boolean deleted = albumDAO.deleteAlbum(new Album(-1000, "Does not exist", -1000));

        assertFalse(deleted, "The album should not have been deleted and the method should return false");
    }
}
