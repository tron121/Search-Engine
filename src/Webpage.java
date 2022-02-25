import java.util.LinkedList;

// class for webpage objects
public class Webpage implements Comparable {
    public String url;
    private String title;
    private String body;
    private LinkedList<String> referencedURLs;
    public double rank;

    // The constructor converts title and body to lowercase, to ease
    //  other computations later
    Webpage(String locator, String title, String body,
            LinkedList<String> referencedURLs) {
        this.url = locator;
        this.title = title.toLowerCase();
        this.body = body.toLowerCase();
        this.referencedURLs = referencedURLs;
        this.rank = 0;
    }

    // edit URL
    public void setUrl(String url) {
        this.url = url;
    }

    // retrieves title
    public String getTitle() {
        return title;
    }

    // edits title
    public void setTitle(String title) {
        this.title = title;
    }

    // retrieves body
    public String getBody() {
        return body;
    }

    // edits body
    public void setBody(String body) {
        this.body = body;
    }

    // returns referencedURLs
    public LinkedList<String> getReferencedURLs() {
        return referencedURLs;
    }

    // edits referencedURLs
    public void setReferencedURLs(LinkedList<String> referencedURLs) {
        this.referencedURLs = referencedURLs;
    }

    // retrieves rank
    public double getRank() {
        return rank;
    }

    // edits rank
    public void setRank(double rank) {
        this.rank = rank;
    }

    public String getUrl() {
        String[] strArray = this.url.split("/");
        return strArray[strArray.length - 1];
    }


    @Override
    public int compareTo(Object o) {
        Webpage other = (Webpage) o;
        if ((this.rank - other.rank) < 0.0001) {
            return 0;
        }
        if (this.rank < other.rank) {
            return 1;
        }
        if (this.rank > other.rank) {
            return -1;
        }
        return 0;
    }
}