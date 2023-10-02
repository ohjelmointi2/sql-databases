package databases.part02;

import java.util.List;

/**
 * A simple application that prints all artists in the Chinook database.
 *
 * This version uses the DAO approach, and the result is cleaner, easier to test
 * and reusable.
 */
public class ArtistAppMain {

    public static void main(String[] args) {
        ArtistDAO artistDAO = new ArtistDAO();
        List<Artist> artists = artistDAO.getArtists();

        for (Artist artist : artists) {
            System.out.println(artist.getName() + " (" + artist.getId() + ")");
        }
    }
}
