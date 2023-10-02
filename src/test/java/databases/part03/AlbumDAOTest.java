package databases.part03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import databases.part02.Artist;

public class AlbumDAOTest {
    private AlbumDAO albumDAO = new AlbumDAO();

    @Test
    void testGettingAddingUpdatingAndDeletingAlbums() {
        Artist artist = new Artist(1, "AC/DC");

        // Get all albums by AC/DC and make sure there are more than 0:
        List<Album> albums = albumDAO.getAlbumsByArtist(artist);
        assertTrue(albums.size() > 0);

        // Add an album and make sure the number of albums increases by 1:
        Album album = new Album("Test Album", 1);
        boolean added = albumDAO.addAlbum(album);
        assertTrue(added);

        List<Album> albumsAfterAdd = albumDAO.getAlbumsByArtist(artist);
        assertEquals(albums.size() + 1, albumsAfterAdd.size());

        // Update the last album's title:
        Album latestAlbum = albums.get(albums.size() - 1);
        latestAlbum.setTitle("Updated Title");
        boolean updated = albumDAO.updateAlbum(latestAlbum);
        assertTrue(updated);

        // Make sure the last album's title was updated:
        List<Album> albumsAfterUpdate = albumDAO.getAlbumsByArtist(artist);
        Album lastAfterUpdate = albumsAfterUpdate.get(albumsAfterUpdate.size() - 1);
        assertEquals("Updated Title", lastAfterUpdate.getTitle());

        // Delete the last album and make sure the number of albums decreases by 1:
        boolean deleted = albumDAO.deleteAlbum(latestAlbum);
        assertTrue(deleted);

        List<Album> albumsAfterDelete = albumDAO.getAlbumsByArtist(artist);
        assertEquals(albums.size(), albumsAfterDelete.size());
    }
}
