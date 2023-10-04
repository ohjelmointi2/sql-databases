package databases.part01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * A simple application that prints all artists in the Chinook database.
 * This version uses JDBC directly, and the result is verbose, hard to test and
 * not reusable. We'll improve this in the next part.
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

            // Iterate over the result set and print the results. The result set contains
            // the rows returned by the query. Each time next() is called, the result set
            // moves to the next row. next() returns false when there are no more rows in
            // the result set, ending the while loop.
            while (resultSet.next()) {

                // getLong() and getString() are used to retrieve the values from the current
                // row in the result set. The argument passed to these methods is the name of
                // the column in the result set.
                String name = resultSet.getString("Name");

                System.out.println(name);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // Close the result set, prepared statement, and connection in the finally block
            // to ensure they are closed even if an exception is thrown.
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
                e.printStackTrace();
            }
        }
    }
}
