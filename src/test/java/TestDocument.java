import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lab10.decorators.Document;

import static org.junit.jupiter.api.Assertions.*;

public class TestDocument {

    private Document document;

    private static class MockDocument implements Document {
        private final String gcsPath;
        private final String content;

        public MockDocument(String gcsPath, String content) {
            this.gcsPath = gcsPath;
            this.content = content;
        }

        @Override
        public String parse() {
            return content;
        }

        @Override
        public String getGcsPath() {
            return gcsPath;
        }
    }

    @BeforeEach
    void setUp() {
        document = new MockDocument("test/path", "Test content");
    }

    @Test
    void testParse() {
        String parsedContent = document.parse();
        assertEquals("Test content", parsedContent);
    }

    @Test
    void testGetGcsPath() {
        String gcsPath = document.getGcsPath();
        assertEquals("test/path", gcsPath);
    }

    @Test
    void testNullContent() {
        document = new MockDocument("test/path", null);
        String parsedContent = document.parse();
        assertNull(parsedContent);
    }

    @Test
    void testEmptyContent() {
        document = new MockDocument("test/path", "");
        String parsedContent = document.parse();
        assertEquals("", parsedContent);
    }

    @Test
    void testNullPath() {
        document = new MockDocument(null, "Test content");
        String gcsPath = document.getGcsPath();
        assertNull(gcsPath);
    }

    @Test
    void testEmptyPath() {
        document = new MockDocument("", "Test content");
        String gcsPath = document.getGcsPath();
        assertEquals("", gcsPath);
    }
}
