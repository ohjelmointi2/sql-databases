package databases.part02;

/**
 * Represents an artist in the Chinook database.
 */
public class Artist {

    private long id;
    private String name;

    public Artist(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
