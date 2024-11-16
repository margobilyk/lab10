import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDBConnection {

    private Connection connection;
    private String dbFilePath;

    @BeforeEach
    void setup() throws IOException, SQLException {
        Path tempFile = Files.createTempFile("test_cache", ".db");
        dbFilePath = tempFile.toAbsolutePath().toString();
        System.out.println("Database path: " + dbFilePath);

        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        System.out.println("Connection established.");
    }

    @AfterEach
    void tearDown() throws SQLException, IOException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connection closed.");
        }

        Files.deleteIfExists(Paths.get(dbFilePath));
        System.out.println("Database file deleted.");
    }

    @Test
    void testConnection() throws SQLException {
        assertNotNull(connection, "Connection should not be null.");
        System.out.println("Test passed: Connection is valid.");
    }
}
