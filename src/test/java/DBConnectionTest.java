import org.junit.jupiter.api.*;
import lab10.decorators.DBConnection;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DBConnectionTest {

    private DBConnection dbConnection;
    private Path tempDbPath;

    @BeforeEach
    void setup() throws SQLException, IOException, NoSuchFieldException, IllegalAccessException {
        Path tempDirectory = Files.createTempDirectory("test-db");
        tempDbPath = tempDirectory.resolve("cache.db");
        Files.createFile(tempDbPath);

        dbConnection = DBConnection.getInstance();

        Field connectionField = DBConnection.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + tempDbPath.toAbsolutePath());
        connectionField.set(dbConnection, connection);

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE document (path TEXT PRIMARY KEY, parsed TEXT)");
        }
    }

    @Test
    void testCreateAndGetDocument() {
        String path = "test/path/document1";
        String parsedData = "Parsed content of document 1";
        dbConnection.createDocument(path, parsedData);
        String retrievedData = dbConnection.getDocument(path);
        assertEquals(parsedData, retrievedData);
    }

    @Test
    void testGetNonExistentDocument() {
        String path = "nonexistent/path";
        String result = dbConnection.getDocument(path);
        assertNull(result);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (tempDbPath != null) {
            Files.deleteIfExists(tempDbPath);
            Files.deleteIfExists(tempDbPath.getParent());
        }
    }
}
