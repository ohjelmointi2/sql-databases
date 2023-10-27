package databases.part01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A simple application that prints all artists in the Chinook database.
 * This version uses JDBC directly, and the result is verbose, hard to test and
 * not reusable. It is still useful to understand the basics of JDBC.
 *
 * We'll improve this in the next part.
 */
public class JdbcDemoMain {

    /**
     * The connection string used to connect to the database. If you are using a
     * MySQL database, you will need to change this string. For example:
     *
     * jdbc:mysql://localhost:3306/Chinook?user=CHANGE&password=CHANGE
     *
     * If you change the connection string, *please change it back* before
     * submitting your code, as the first test depends on the SQLite database.
     */
    private static final String JDBC_CONNECTION_STRING = "jdbc:sqlite:data/Chinook_Sqlite.sqlite";

    public static void main(String[] args) {

        /*
         * These variables are declared outside of the try block so they can be closed
         * in the finally block. This ensures that the resources are closed even if an
         * exception is thrown and the try block is exited early.
         */
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Create a connection to the database
            connection = DriverManager.getConnection(JDBC_CONNECTION_STRING);

            // Create a prepared statement to execute a query
            preparedStatement = connection.prepareStatement("SELECT ArtistId, Name FROM Artist ORDER BY ArtistId ASC");

            // Execute the query and get the result set
            resultSet = preparedStatement.executeQuery();

            /*
             * Iterate over the result set and print the results. The result set contains
             * the rows returned by the query. Each time next() is called, the result set
             * moves to the next row.
             *
             * If next() returns false, then there are no more rows in the result set, and
             * the loop terminates.
             */
            while (resultSet.next()) {

                /*
                 * getLong() and getString() are used to retrieve the values from the current
                 * row in the result set. The argument passed to these methods is the name of
                 * the column in the result set. There are also methods for other types of
                 * data, such as getInt() and getDouble().
                 */
                String name = resultSet.getString("Name");

                System.out.println(name);
            }
        } catch (SQLException e) {
            /*
             * Operations that access the database can throw SQLExceptions. SQLException is
             * a checked exception, so it must be caught or thrown. Here we don't really
             * handle the exception, we just print the stack trace and exit.
             */
            e.printStackTrace();

        } finally {
            /*
             * Close the result set, prepared statement, and connection in the finally block
             * to ensure they are closed even if an exception is thrown.
             *
             * This is a bit verbose, and you could either implement a utility method to
             * close the resources, or use a try-with-resources block.
             */
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                /*
                 * Even operations that close the resources can throw exceptions, so we still
                 * need to catch them.
                 */
                e.printStackTrace();
            }
        }
    }
}
