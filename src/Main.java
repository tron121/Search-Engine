import java.io.FileNotFoundException;
import java.util.LinkedList;

import static org.junit.Assert.fail;

/**
 * Created by Ron on 12/4/2016.
 */


public class Main {

    public static void main(String args[]) {
        startUp();
    }

    public static void startUp() {
        SearchEngine g;
        try {           
            g = new SearchEngine(new LinkedList<String>());
            addPage(g, "PageFiles/goats.md");
            addPage(g, "PageFiles/aboutWPI.md");
            // additional pages
            addPage(g, "PageFiles/worcester.md");
            addPage(g, "PageFiles/Random.md");

            BrowserWindow window = new BrowserWindow(g);
            window.screen();
        } catch (FileNotFoundException e) {
            System.out.println("Aborting Example setup -- file not found: " + e);
        } catch (UnsupportedFileExn e) {
            System.out.println("Aborting Examples setup -- unsupported file extension: " + e);
        }
    }

    private static void addPage(SearchEngine g, String p) {
        try {
            g.visitPage(p);
        } catch (FileNotFoundException e) {
            fail("Aborting Example setup -- file not found: " + e);
        } catch (UnsupportedFileExn e) {
            fail("Aborting Examples setup -- unsupported file extension: " + e);
        }
    }
}
