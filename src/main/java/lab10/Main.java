package lab10;

import lab10.decorator.CachedDocument;
import lab10.decorator.MockedDocument;
import lab10.decorator.TimedDocument;

public class Main {
    public static void main(String[] args) {
        MockedDocument mockedDocument = new MockedDocument();
        TimedDocument timedDocument = new TimedDocument(mockedDocument);
        System.out.println(timedDocument.parse());

        CachedDocument cachedDocument = new CachedDocument(mockedDocument);
        System.out.println(cachedDocument.parse());
    }
}