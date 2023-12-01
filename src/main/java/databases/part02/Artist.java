package databases.part02;

/**
 * Represents an artist in the Chinook database. You don't need to modify this
 * class in this exercise.
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

    @Override
    public String toString() {
        return "Artist [id=" + id + ", name=" + name + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Artist other) {
            return id == other.id && name.equals(other.name);
        }
        return false;
    }
}
