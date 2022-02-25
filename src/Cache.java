import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Ron on 12/4/2016.
 */


public class Cache {
    private Map<String, LinkedList<Webpage>> content;
    //private LinkedList<Webpage> relatedPages;

    public Cache() {
        this.content = new HashMap<String, LinkedList<Webpage>>();
       // this.relatedPages = new LinkedList<Webpage>();
    }

    public Map<String, LinkedList<Webpage>> getContent() {
        return this.content;
    }

    public void storeQuery(String query, LinkedList<Webpage> related) {
        //this.relatedPages = related;
        this.content.put(query, related);
    }

    public void addToCache(String key, LinkedList<Webpage> pages) {
        this.content.put(key, pages);
    }

    public LinkedList<Webpage> retrieveFromCache(String query) {return this.content.get(query);}
}
