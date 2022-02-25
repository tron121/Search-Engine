/*
 * If you are looking at this file before Thurs/Fri, the try/catch
 * statements won't make sense (unless you already know exceptions).
 * By Friday, we will cover those.  In the meantime, you can write
 * tests by creating your search engine classes in the Examples
 * constructor, as indicated by the comments.
 */

import java.util.LinkedList;
import java.util.Arrays;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.*;

public class Examples {
    //  declare variables for all of your search engines here
    SearchEngine s;

    //  local method to add a page to a search engine.  Created a
    //  helper so that we can isolate the exception handling here,
    //  rather than clutter up each test case with the exceptions
    private void addPage(SearchEngine s, String p) {
        try {
            s.visitPage(p);
        } catch (FileNotFoundException e) {
            fail("Aborting Example setup -- file not found: " + e);
        } catch (UnsupportedFileExn e) {
            fail("Aborting Examples setup -- unsupported file extension: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Examples() {
        try {
            s = new SearchEngine(new LinkedList<String>());
            addPage(s, "PageFiles/goats.md");
            addPage(s, "PageFiles/aboutWPI.md");
            addPage(s, "PageFiles/Random.md");
            s.updateSponsor("WPI", 0.00);
        } catch (FileNotFoundException e) {
            System.out.println("Aborting Example setup -- file not found: " + e);
        } catch (UnsupportedFileExn e) {
            System.out.println("Aborting Examples setup -- unsupported file extension: " + e);
        } catch (LowerRateException e) {
            System.out.println(e.newRate + " is lower than the older rate " + e.oldRate + ": " + e);
        } catch (InvalidRateException e) {
            System.out.println(e.rate + " is lower than 0 or greater than 0.1: " + e);
        }
    }

    @Test
    public void testGoatsQuery() {
        assertEquals(3, s.runQuery("goat").size());
    }

    @Test
    public void testWPIQuery() {
        assertEquals(2, s.runQuery("WPI").size());
        addPage(s, "PageFiles/worcester.md");
        assertEquals(3, s.runQuery("WPI").size());
    }

    @Test
    public void testVisitPages() {
        addPage(s, "PageFiles/worcester.md");
        try {
            assertEquals("PageFiles/worcester.md", s.visitPage("PageFiles/worcester.md").url);

        } catch (FileNotFoundException e) {
            System.out.println("Aborting Example setup -- file not found: " + e);
        } catch (UnsupportedFileExn e) {
            System.out.println("Aborting Examples setup -- unsupported file extension: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void alreadySawQuery2() {
        s.runQuery("goat");
        assertTrue(s.alreadySawQuery("goat"));
    }


    /* order of queries tests
     */

    // for this first test aboutWPI.md will be first in list
    // because it has the most
    @Test
    public void queryOrder1() {
        // compare url of expected  to stuff returned from runquery
        assertTrue(s.runQuery("WPI").getFirst().getUrl().equals("aboutWPI.md"));
    }

    // finding the exact pageRank of aboutWPI based on the number of times
    // it is linked and it's sponsorship
    @Test
    public void queryOrder2() {
        // compare url of expected  to stuff returned from runquery
        assertEquals(0.5, s.runQuery("WPI").getFirst().rank, 0.0001);
    }


    //testing sponsorship plus rank
    @Test
    public void queryOrder3() {
        // compare url of expected  to stuff returned from runquery
//        for (Webpage page : s.runQuery("goats")) {
//            System.out.print(page.url + " " + page.rank + "\t");
//        }
        try {
//            System.out.println("Rate prrint1: " + s.runQuery("goat").getFirst().rank);
            s.updateSponsor("goat", 0.05);
        } catch (LowerRateException e) {
            System.out.println(e.newRate + " is lower than the older rate " + e.oldRate + ": " + e);
        } catch (InvalidRateException e) {
            System.out.println(e.rate + " is lower than 0 or greater than 0.1: " + e);
        }
        assertEquals(0.5, s.runQuery("goat").getFirst().rank, 0.0001);
    }


    /*   Exceptions tests
         invalid rate above
         invalid rate below
         lower rate
         valid results test
         valid rate at 0
         valid rate at 0.1
     */

    // Testing an update that is too high and should throw an exception
    @Test(expected = InvalidRateException.class)
    public void invalidRateTest1() throws LowerRateException,
            InvalidRateException {
        s.updateSponsor("WPI", 2.0);
    }

    // Testing an update that is too low and should throw an exception
    @Test(expected = InvalidRateException.class)
    public void invalidRateTest2() throws LowerRateException,
            InvalidRateException {
        s.updateSponsor("WPI", -0.1);
    }

    // Testing an update that is lower than old rate and should throw an exception
    @Test(expected = LowerRateException.class)
    public void LowerRateTest1() throws LowerRateException,
            InvalidRateException {
        s.updateSponsor("WPI", 0.09);
        s.updateSponsor("WPI", 0.02);
    }

    // test update sponsor with no exception expected
    @Test
    public void validRateTest1() throws LowerRateException, InvalidRateException {
        s.updateSponsor("WPI", 0.05);
    }


    // test update sponsor with no exception expected
    @Test
    public void validRateTest2() throws LowerRateException, InvalidRateException {
        s.updateSponsor("WPI", 0.1);
    }

    // test update sponsor with no exception expected
    @Test
    public void validRateTest3() throws LowerRateException, InvalidRateException {
        s.updateSponsor("goats", 0.06);
        //System.out.println(s.runQuery("goats").getFirst().rank);
    }
}