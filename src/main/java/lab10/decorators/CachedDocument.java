package lab10.decorators;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CachedDocument implements Document {
    private Document document;

    @Override
    public String parse() {
        String cached = DBConnection.getInstance().getDocument(document.getGcsPath());
        if (cached != null) {
            return cached;
        } else {
            String parced = document.parse();
            DBConnection.getInstance().createDocument(document.getGcsPath(), parced);
        }
        return document.parse();
    }

    @Override
    public String getGcsPath() {
        return document.getGcsPath();
    }
}
