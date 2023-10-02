package databases.part03;

/**
 * Represents an album in the Chinook database.
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
        this.id = -1; // the database will generate an id for this album
        this.title = title;
        this.artistId = artistId;
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

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Album [id=" + id + ", title=" + title + ", artistId=" + artistId + "]";
    }
}
