import org.junit.jupiter.api.*;

import lab10.decorators.DBConnection;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DBConnectionTest {

    private static final String TEST_DB_PATH = "/Users/margo/Documents/ucu/oop/lab10/cache.db";
    private DBConnection dbConnection;

    @BeforeEach
    void setup() throws Exception {
        Path directory = Path.of("/Users/margo/Documents/ucu/oop/lab10");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        File dbFile = new File(TEST_DB_PATH);
        if (!dbFile.exists()) {
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + TEST_DB_PATH)) {
                connection.createStatement().execute("CREATE TABLE IF NOT EXISTS document (path TEXT PRIMARY KEY, parsed TEXT NOT NULL)");
            }
        }

        dbConnection = DBConnection.getInstance();
    }

    @Test
    void testCreateAndGetDocument() {
        String path = "test/path/document1";
        String parsedData = "Parsed content of document 1";

        dbConnection.createDocument(path, parsedData);

        String retrievedData = dbConnection.getDocument(path);
        assertEquals(parsedData, retrievedData, "The retrieved parsed data should match the inserted data");
    }

    @Test
    void testGetNonExistentDocument() {
        String path = "nonexistent/path";

        String result = dbConnection.getDocument(path);
        assertNull(result, "Retrieving a non-existent document should return null");
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(Path.of(TEST_DB_PATH));
    }
}
