package databases.part02;

import java.util.List;

public class ArtistAppMain {

    public static void main(String[] args) {
        ArtistDAO artistDAO = new ArtistDAO();
        List<Artist> artists = artistDAO.getArtists();

        for (Artist artist : artists) {
            System.out.println(artist.getName() + " (" + artist.getId() + ")");
        }
    }
}
