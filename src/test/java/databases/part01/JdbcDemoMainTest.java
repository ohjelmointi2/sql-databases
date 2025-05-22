package databases.part01;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This test class tests the JdbcDemoMain class. The class being tested only
 * contains a main method, which returns nothing. This makes it difficult to
 * test with unit tests, and is not a very good design.
 *
 * To test the main method, we capture the output of the main method and check
 * that the output is correct. Typically, you want to avoid testing the side
 * effects of methods, such as printing to the console.
 *
 * In the next parts, we will improve the design and make it more reusable and
 * testable.
 */
public class JdbcDemoMainTest {

    // To test the output of the main method, we need to capture the output stream
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    /*
     * Before each test, we set the output stream to our own stream captor.
     */
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    /*
     * After each test, we set the output stream back to the original output.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void artistsAreOrderedByName() {
        // Call the main method of the JdbcDemoMain class and get the output
        JdbcDemoMain.main(new String[] {});
        String output = outputStreamCaptor.toString();

        // Check that the artitsts are ordered by their names
        checkOrderOfArtists(output, "AC/DC", "Metallica");
        checkOrderOfArtists(output, "Aaron Goldberg", "Alanis Morissette");
        checkOrderOfArtists(output, "Aaron Copland & London Symphony Orchestra", "Metallica");
    }

    @Test
    void artistIdsAreIncludedInOutput() {
        // Call the main method of the JdbcDemoMain class and get the output
        JdbcDemoMain.main(new String[] {});
        String output = outputStreamCaptor.toString();

        String metallicaLine = findLineContaining(output, "Metallica");
        assertTrue(metallicaLine.contains("50"),
                "Expected the id 50 to be included for Metallica, but the line was: '" + metallicaLine + "'");

        String acdcLine = findLineContaining(output, "AC/DC");
        assertTrue(acdcLine.contains("1"),
                "Expected the id 1 to be included for AC/DC, but the line was: '" + acdcLine + "'");
    }

    /**
     * Returns the first line of text that contains the given substring. Otherwise,
     * throws an assertion error.
     */
    private String findLineContaining(String text, String substring) {
        return text
                .lines()
                .filter(line -> line.contains(substring))
                .findFirst()
                .orElseThrow(() -> new AssertionError("The text '" + substring + "' was not found."));
    }

    /**
     * Checks that the artists in the output are in the correct order. If either is
     * not found or artist2 is before artist1, an assertion error is thrown.
     */
    private void checkOrderOfArtists(String output, String artist1, String artist2) {
        assertStringContains(output, artist1);
        assertStringContains(output, artist2);

        int index1 = output.indexOf(artist1);
        int index2 = output.indexOf(artist2);

        assertTrue(index1 < index2, "Expected " + artist1 + " to be before " + artist2);
    }

    /**
     * Asserts that the given text contains the given substring. If it does not, an
     * assertion error is thrown with a message.
     */
    private void assertStringContains(String text, String substring) {
        assertTrue(text.contains(substring), "Expected '" + substring + "' to be in the output, but it was not.");
    }
}
