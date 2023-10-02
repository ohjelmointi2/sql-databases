package databases.part03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import databases.part02.Artist;

public class AlbumDAOTest {
    private AlbumDAO albumDAO = new AlbumDAO();

    /**
     * Ideally, each test should be independent of the others. This is usually
     * accomplished by clearing the database before each test and by using a test
     * fixture, so each test runs with the same initial data.
     *
     * Since we are using the same database for testing and development, we won't
     * clear the database before each test. As a result, we add, change and delete
     * albums in a single test. This also means that if one assertion fails, the
     * rest of the test will fail, even if the other assertions would be correct.
     *
     * This is not ideal, but it is acceptable for this assignment.
     */
    @Test
    void testGettingAddingUpdatingAndDeletingAlbums() {
        Artist artist = new Artist(1, "AC/DC");

        // Get all albums by AC/DC and make sure there are more than 0:
        List<Album> albums = albumDAO.getAlbumsByArtist(artist);
        assertTrue(albums.size() > 0);

        // The album's title contains quotes to verify the SQL query is properly
        // escaped:
        Album album = new Album("~~~ The album's \"name\"", 1);
        boolean added = albumDAO.addAlbum(album);
        assertTrue(added);

        // Make sure the number of albums increases by 1 and the last album is the one
        // we just added:
        List<Album> albumsAfterAdd = albumDAO.getAlbumsByArtist(artist);
        Album latestAlbum = albums.get(albums.size() - 1);
        assertEquals(albums.size() + 1, albumsAfterAdd.size());
        assertEquals("~~~ The album's \"name\"", latestAlbum.getTitle(),
                "The added album should be last in the list and the title should be properly escaped.");
        assertEquals(1, latestAlbum.getArtistId());

        // Update the last album's title. Once again, the album's title contains quotes
        // to test that the SQL query is properly escaped:
        latestAlbum.setTitle("~~~ Updated \"Title\"");
        boolean updated = albumDAO.updateAlbum(latestAlbum);
        assertTrue(updated);

        // Fetch the last album again and make sure the title was updated:
        List<Album> albumsAfterUpdate = albumDAO.getAlbumsByArtist(artist);
        Album lastAfterUpdate = albumsAfterUpdate.get(albumsAfterUpdate.size() - 1);
        assertEquals("~~~ Updated \"Title\"", lastAfterUpdate.getTitle());

        // Finally, delete the last album and make sure the number of albums decreases:
        boolean deleted = albumDAO.deleteAlbum(latestAlbum);
        assertTrue(deleted);

        List<Album> albumsAfterDelete = albumDAO.getAlbumsByArtist(artist);
        assertEquals(albums.size(), albumsAfterDelete.size());
    }
}
