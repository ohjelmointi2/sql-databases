package databases.part03;

import java.util.List;

import databases.part02.Artist;

/**
 * Data Access Object for the Album table in the Chinook database.
 */
public class AlbumDAO {

    /**
     * Returns a list of all albums that have the specified artist as the artist.
     * If there are no albums in the database
     *
     * @param artist the artist whose albums to retrieve.
     * @return a list of all albums that have the specified artist as the artist.
     */
    public List<Album> getAlbumsByArtist(Artist artist) {
        // hint: use the artist.getId() method to get the artist's id and add it
        // to the SQL query using PreparedStatement's setLong() method.
        return null;
    }

    /**
     * Adds the specified album to the database. Returns true if the album was
     * added successfully, false otherwise.
     *
     * @param album the album to add to the database.
     * @return true if the album was added successfully, false otherwise.
     */
    public boolean addAlbum(Album album) {
        // hint 1: use PreparedStatement's setString() and setLong() methods to
        // add the album's title and artist id to the SQL query. Leave the AlbumId
        // blank, as it will be automatically generated by the database.

        // hint 2: executeUpdate() returns the number of rows affected by the query.
        // If the number of rows affected is greater than 0, the album was added.
        return false;
    }

    /**
     * Updates the specified album in the database. Returns true if the album was
     * updated successfully, false otherwise.
     *
     * @param album the album to update in the database.
     * @return true if the album was updated successfully, false otherwise.
     */
    public boolean updateAlbum(Album album) {
        // hint 1: use PreparedStatement's setString() and setLong() methods to
        // add the album's title and artist id to the SQL query. Do not change the
        // AlbumId, as it is used to identify the album to update.
        return false;
    }

    /**
     * Deletes the specified album from the database. Returns true if the album was
     * deleted successfully, false otherwise.
     *
     * @param album the album to delete from the database.
     * @return true if the album was deleted successfully, false otherwise.
     */
    public boolean deleteAlbum(Album album) {
        // see hints for the methods above
        return false;
    }
}