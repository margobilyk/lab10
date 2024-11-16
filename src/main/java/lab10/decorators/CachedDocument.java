package lab10.decorators;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CachedDocument implements Document {
    private Document document;

    @Override
public String parse() {
    String cached = DBConnection.getInstance().getDocument(document.getGcsPath());
    if (cached != null) {
        System.out.println("Cache hit for path: " + document.getGcsPath());
        return cached;
    } else {
        System.out.println("Cache miss for path: " + document.getGcsPath());
        String parsed = document.parse();
        DBConnection.getInstance().createDocument(document.getGcsPath(), parsed);
        return parsed;
    }
}


    @Override
    public String getGcsPath() {
        return document.getGcsPath();
    }
}
