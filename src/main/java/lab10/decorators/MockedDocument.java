package lab10.decorators;

public class MockedDocument implements Document {
    public String gcsPath;
    
    @Override
    public String parse() {
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return "Mocked Document Parse";
    }

    @Override
    public String getGcsPath() {
        return gcsPath;
    }
}
