// Search Engine

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

class SearchEngine implements ISearchEngine {
    Scanner keyboard = new Scanner(System.in);
    SimpleMarkdownReader m = new SimpleMarkdownReader();
    private LinkedList<Webpage> pages;
    private Cache cache;
    private Sponsors sponsors;

    // constructor
    SearchEngine(LinkedList<String> initPages) throws FileNotFoundException, UnsupportedFileExn {
        pages = new LinkedList<Webpage>();
        for (String pageLoc : initPages) {
            pages.add(addSite(pageLoc));
        }
        //Cache cache = new Cache();
        cache = new Cache();
        sponsors = new Sponsors();
        generatePageRankings(this.pages);
    }

    public Scanner getKeyboard() {
        return keyboard;
    }

    // given query string, produce webpages that meet query
    public LinkedList<Webpage> runQuery(String query) {
        //strip and clean query
        query = stripFillers(query);
        if (alreadySawQuery(query)) {
            System.out.println("This query has already been cached...");
            //return list of previously cached results
            return sortByRank(cache.getContent().get(query));
        }

        // get all related pages and attach to query in hashmap
        LinkedList<Webpage> temp = new LinkedList<Webpage>();
        for (Webpage page : this.pages) {
            //replaced with helper
            if (isPageRelatedtoQuery(query, page)) {
                temp.add(page);
            }
        }
        // if no related page found, return empty list as-is.
        if (temp.isEmpty()) {
            return (temp);
        }
        // reset ranks of all pages in temp to 0
        resetPageRanks(this.pages);
        // method that iterates over the pages to update their rank
        // based on sponsorship
        generatePageRankings(this.pages);
        // give credits for number of times linked
        distributeRankingCredits(this.pages);
        //Sort the pages based on ranking,
        // and return the sorted list as the result of runQuery.
        sortByRank(temp);
        cache.addToCache(query, temp);
        return cache.getContent().get((query));
    }

    // helper method determines if a page is related to a query
    public boolean isPageRelatedtoQuery(String query, Webpage page) {
        return (page.getTitle().contains(query) || page.getBody().contains(query));
    }


    // given a page location, return the corresponding page after updating queries
    public Webpage visitPage(String location) throws UnsupportedFileExn, FileNotFoundException {
        for (Webpage page : pages) {
            if (page.getUrl().equals((location))) {
                System.out.println(new StringBuilder().append("Visiting ").append(page.getUrl()));

                // open file with default desktop app
                try {
                    File file = new File((new StringBuilder().append("PageFiles/").append(page.getUrl())).toString());
                    Desktop desktop = Desktop.getDesktop();
                    if((Desktop.isDesktopSupported()) & (file.exists())) {
                        desktop.open(file);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return page;
            }
        }

        //if not visited before , make a webpage for the location
        // and find a query to attach it to.
        Webpage webpage = addSite(location);
        LinkedList<Webpage> temp = new LinkedList<Webpage>();
        for (String query : cache.getContent().keySet()) {
            if (isPageRelatedtoQuery(query, webpage)) {
                // if the webpage is related to a query retrieve and add to cache
                temp = cache.getContent().get(query);
                temp.add(webpage);
                System.out.println(webpage.getUrl());
                cache.addToCache(query, temp);
            }
        }
        pages.add(webpage);
        generatePageRankings(this.pages);
        return webpage;
    }

    // produce the number of web pages known in the system
    public int knownPageCount() {
        return pages.size();
    }

    // determine whether given query has been seen in the search engine
    public boolean alreadySawQuery(String query) {
        // return this.cache.containsQuery(stripFillers(query));
        return this.cache.getContent().containsKey(stripFillers(query));
    }


    // ****** ADDING SITES TO THE ENGINE ***************
    // parses given file into a webpage
    Webpage addSite(String locationName) throws UnsupportedFileExn, FileNotFoundException {
        Webpage newWP;
        if (locationName.endsWith(".md")) {
            return (m.readPage(locationName));
        } else {
            throw new UnsupportedFileExn(locationName);
        }
    }

    // ****** REMOVING FILLER WORDS FROM QUERIES *****************
    private LinkedList<String> fillerWords =
            new LinkedList<String>(Arrays.asList("a", "an", "the", "of", "on", "in",
                    "to", "by", "about", "how", "is",
                    "what", "when"
            ));

    // remove the filler words from a string
    // assume string has no punctuation
    private String stripFillers(String query) {
        String[] wordArray = query.toLowerCase().split(" ");
        String resultStr = "";
        for (int i = 0; i < wordArray.length; i++) {
            if (!(fillerWords.contains(wordArray[i])))
                resultStr = resultStr + wordArray[i];
        }
        return resultStr;
    }

    // method that returns a linkedlist of webpages sorted by rank
    private LinkedList<Webpage> sortByRank(LinkedList<Webpage> myPages) {
        Collections.sort(myPages);
        return myPages;
    }

    // method updateSponsor that takes a sponsor name and a new rate (a double)
    // and sets the sponsor to have the given rate.
    @Override
    public void updateSponsor(String sponsor, double rate) throws LowerRateException, InvalidRateException {
        this.sponsors.editDatabase(sponsor, rate);
    }

    // find sponsor rate for any given url
    public double getSponsoredRate(String url) {
        for (String sponsor : sponsors.viewData().keySet()) {
            if (url.contains(sponsor)) {
                System.out.println("URL:" + url + ",sponsor name: " + sponsor + ",sponsors rank: " + sponsors.viewData().get(sponsor));
                return sponsors.viewData().get(sponsor);
            }
        }
        // If none of the known sponsor names are a substring of the given URL
        return 0;
    }

    // performed initially when the search engine is constructed and
    // whenever a new webpage is added in visitPages;
    public void generatePageRankings(LinkedList<Webpage> pages) {
        for (Webpage page : pages) {
            page.setRank(getSponsoredRate(page.getUrl()));
        }
    }

    // resets all page ranks
    public void resetPageRanks(LinkedList<Webpage> pages) {
        for (Webpage page : pages) {
            page.rank = 0;
        }
    }

    // method that iterates over all pages and distributes the link credits
    public void distributeRankingCredits(LinkedList<Webpage> pages) {
        for (Webpage page : pages) {
            int links = page.getReferencedURLs().size();
            if (page.getReferencedURLs().contains(page.getUrl())) {
                links--;
            }
            for (String url : page.getReferencedURLs()) {
                if (!url.equals(page.getUrl())) {
                    //get pages attached to url and update the rank
                    for (Webpage web : this.pages) {
                        String tmpUrl = "PageFiles/" + url;
                        if (tmpUrl.contains(web.getUrl())) {
                            web.rank += 1.0 / (double) links;
                        }
                    }
                }
            }
        }
    }
}
