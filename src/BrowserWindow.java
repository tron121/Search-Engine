import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Ron on 12/4/2016.
 */


public class BrowserWindow {
    private Scanner keyboard = new Scanner(System.in);
    private SimpleMarkdownReader m = new SimpleMarkdownReader();
    private SearchEngine searchEngine;
    public BrowserWindow(SearchEngine s) {
        this.searchEngine = s;
    }

    // ***** THE SCREEN/USER INTERFACE *******************
    void screen() {
        System.out.println("-------------------------------------");
        System.out.println("Enter a number to select an option");
        System.out.println("1. Search for a term in pages");
        System.out.println("2. Visit a page");
        System.out.println("3. Exit the system (lose all index data, and reset cache)");
        String choice = keyboard.next();
        keyboard.nextLine();
        try {
            if (choice.equals("1")) {
                System.out.println("Enter your query:");
                String query = keyboard.nextLine();
                System.out.println("Search results, prioritized by page rank: ");
                for (Webpage page : searchEngine.runQuery(query)){
                    System.out.println(page.getUrl());
                }
                screen();
            } else if (choice.equals("2")) {
                System.out.println("Which page (filename) do you want to visit?:");
                String filename = keyboard.next();
                searchEngine.visitPage(filename);
                screen();
            } else if (choice.equals("3"))
                System.out.println("Thanks for searching with us");
            else
                System.out.println("ERROR *** Unrecognized option " + choice + ". Try again");
        } catch (UnsupportedFileExn e) {
            System.out.println("ERROR *** Filetype not supported: " + e.filename);
            screen();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR *** No page found at address " + e);
            screen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
