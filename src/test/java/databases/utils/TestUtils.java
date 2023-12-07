package databases.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Stream;

/**
 * Utility class for initializing the test database. Each test should call the
 * initialize() method before making any database calls. This ensures that each
 * test is independent of the others.
 *
 * Initializing the database can be automated by adding a @BeforeEach annotation
 * to a setup method in JUnit.
 */
public class TestUtils {

    private static final String dropArtist = "DROP TABLE IF EXISTS Artist;";
    private static final String dropAlbum = "DROP TABLE IF EXISTS Album;";

    private static final String createArtist = """
            CREATE TABLE Artist (
                ArtistId INTEGER  NOT NULL,
                Name NVARCHAR(120),
                CONSTRAINT PK_Artist PRIMARY KEY  (ArtistId)
            );
            """;

    private static final String createAlbum = """
            CREATE TABLE Album (
                AlbumId INTEGER  NOT NULL,
                Title NVARCHAR(160)  NOT NULL,
                ArtistId INTEGER  NOT NULL,
                CONSTRAINT PK_Album PRIMARY KEY  (AlbumId),
                FOREIGN KEY (ArtistId) REFERENCES Artist (ArtistId) ON DELETE NO ACTION ON UPDATE NO ACTION
            );
            """;

    private static final String insertArtists = """
            INSERT INTO Artist (ArtistId, Name) VALUES
                (1000, 'Led Zeppelin'),
                (2000, 'Radiohead'),
                (3000, 'Red Hot Chili Peppers'),
                (4000, 'Pink Floyd'),
                (5000, 'ABBA');
            """;

    private static final String insertAlbums = """
            INSERT INTO Album (AlbumId, Title, ArtistId) VALUES
                (9001, 'Californication', 3000),
                (9002, 'The Wall', 4000),
                (9003, 'By the Way', 3000);
            """;

    /**
     * Initializes the database with the Chinook database schema and some test data.
     *
     * The artist table contains the following artists and albums:
     *
     * # Led Zeppelin
     *
     * # Radiohead
     *
     * # Red Hot Chili Peppers
     * -- Californication
     * -- By the Way
     *
     * # Pink Floyd
     * -- The Wall
     *
     * # ABBA
     *
     *
     * @param databaseUrl The JDBC URL to the test database.
     * @throws SQLException
     */
    public static void initialize(String databaseUrl) throws SQLException {
        Connection connection = DriverManager.getConnection(databaseUrl);

        Stream.of(dropArtist, dropAlbum, createArtist, createAlbum, insertArtists, insertAlbums).forEach(
                sql -> {
                    try {
                        connection.prepareStatement(sql).executeUpdate();
                    } catch (SQLException e) {
                        // No way to recover from this exception. Throw a RuntimeException to make the
                        // test fail.
                        throw new RuntimeException(e);
                    }
                });
    }
}
