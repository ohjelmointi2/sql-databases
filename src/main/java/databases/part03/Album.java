package databases.part03;

/**
 * Represents an album in the Chinook database. You don't need to modify this
 * class in this exercise.
 */
public class Album {

    private long id;
    private String title;
    private long artistId;

    /**
     * Creates a new album with the specified title and artist id. This constructor
     * is used when adding a new album to the database, as the database will
     * automatically generate an id for the album.
     */
    public Album(String title, long artistId) {
        // Call the other constructor with -1 as the id. -1 is used to indicate that
        // the id is not yet known.
        this(-1, title, artistId);
    }

    /**
     * Creates a new album with the specified id, title, and artist id. This
     * constructor is used when retrieving an album from the database, as the
     * database will provide the id.
     */
    public Album(long id, String title, long artistId) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getArtistId() {
        return artistId;
    }

    @Override
    public String toString() {
        return "Album [id=" + id + ", title=" + title + ", artistId=" + artistId + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Album other) {
            return id == other.id && title.equals(other.title) && artistId == other.artistId;
        }
        return false;
    }
}
