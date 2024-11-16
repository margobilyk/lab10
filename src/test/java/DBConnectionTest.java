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

    private void clearDatabase() {
        int retryCount = 5;
        while (retryCount > 0) {
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/margo/Documents/ucu/oop/lab10/cache.db");
                 Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA journal_mode=WAL;");
                statement.execute("DELETE FROM document;");
                return;
            } catch (Exception e) {
                retryCount--;
                if (retryCount == 0) {
                    throw new RuntimeException("Failed to clear database after multiple attempts", e);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
