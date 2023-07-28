import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        WestTerrier ws = new WestTerrier();
        String url = "http://westieinfo.com/DB/";
        ws.parseTable(url,1);
        ws.readDogFromFile();
        ws.printDogsFromFile();

    }
}
