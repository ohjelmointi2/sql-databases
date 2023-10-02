package databases.part02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ArtistDAOTest {

    private ArtistDAO artistDAO = new ArtistDAO();

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
