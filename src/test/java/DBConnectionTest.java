import org.junit.jupiter.api.*;

import lab10.decorators.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionTest {

    private DBConnection dbConnection;

    @BeforeEach
    public void setUp() {
        dbConnection = DBConnection.getInstance();
        initializeDatabase();
        clearDatabase();
    }

    @AfterAll
    public static void tearDownClass() {
        try {
            DBConnection.getInstance().close();
        } catch (Exception e) {
            System.err.println("Failed to close DBConnection: " + e.getMessage());
        }
    }

    @Test
    public void testCreateAndGetDocument() {
        String path = "test-path";
        String parsedContent = "This is a test document content.";
        dbConnection.createDocument(path, parsedContent);
        String retrievedContent = dbConnection.getDocument(path);
        assertNotNull(retrievedContent);
        assertEquals(parsedContent, retrievedContent);
    }

    @Test
    public void testGetNonexistentDocument() {
        String nonExistentPath = "non-existent-path";
        String result = dbConnection.getDocument(nonExistentPath);
        assertNull(result);
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:cache.db");
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS document (path TEXT PRIMARY KEY, parsed TEXT);");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private void clearDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:cache.db");
            Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM document;");
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear database", e);
        }
    }
}
