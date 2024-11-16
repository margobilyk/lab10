import org.junit.jupiter.api.*;

import lab10.decorators.DBConnection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DBConnectionTest {

    private DBConnection dbConnection;

    @BeforeEach
void setup() throws Exception {
    dbConnection = DBConnection.getInstance();
    Field connectionField = DBConnection.class.getDeclaredField("connection");
    connectionField.setAccessible(true);
    Connection connection = (Connection) connectionField.get(dbConnection);

    connection.createStatement().execute("CREATE TABLE IF NOT EXISTS document (path TEXT PRIMARY KEY, parsed TEXT NOT NULL)");
    connection.createStatement().execute("DELETE FROM document");
}

    @Test
    void testSingletonInstance() {
        DBConnection anotherInstance = DBConnection.getInstance();
        assertSame(dbConnection, anotherInstance, "DBConnection should return the same instance");
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
}
