package ServerSide;

import java.util.HashMap;
import java.util.LinkedList;

public class FileManager {
    private static FileManager ref;
    private HashMap<String,LinkedList<Page>> memoryMap;
    private final int Threshold = 3;
    
    static
    {
        ref=null;
    }

    public static FileManager getObject()
    {
        if(ref==null)
            ref = new FileManager();
        return ref;
    }

    private FileManager()
    {
        memoryMap = new HashMap<String,LinkedList<Page>>();

        for(String astore : StoreManager.getStoreMap().keySet())
            memoryMap.put(astore, new LinkedList<Page>());
    }

    public void addToMemoryMap(String aStore)
    {
        memoryMap.put(aStore, new LinkedList<Page>());
    }

    public void deleteFromMemoryMap(String aStore)
    {
        memoryMap.remove(aStore);
    }

    public LinkedList<String> search(String subStore, String fileName)
    {
        fileName = fileName.toUpperCase();
        char temp = fileName.charAt(0);
        String keyword;
        if(temp>='A' && temp<='Z')
            keyword = String.valueOf(temp);
        else
            keyword = "Others";

        LinkedList<Page> memoryQ = memoryMap.get(subStore);
        if(memoryQ == null)
            return null;

        Page p=null;
        for(Page current : memoryQ)
        {
            if(current.keyword.equals(keyword))
            {
                p = current;
                break;
            }
        }

        LinkedList<String> matches;
        if(p != null)
        {
            matches = p.find(fileName);
            if(matches.size() > 0)
                p.frequency++;
        }
        else
        {
            p = new Page(subStore, keyword);
            matches = p.find(fileName);
            if(matches.size() > 0)
            {
                p.frequency++;
                pageReplace(memoryQ, p);
            }
            else
            {
                return null;
            }
        }
        return matches;
    }

    void pageReplace(LinkedList<Page> memoryQ, Page p)
    {
        int i,total;
        total = memoryQ.size();
        if(total < Threshold)
            memoryQ.add(p);
        else
        {
            int min;
            min = 0;
            for( i = 1; i < Threshold; i++)
            {
                if(memoryQ.get(i).frequency < memoryQ.get(min).frequency)
                    min = i;
            }
            memoryQ.remove(min);
            memoryQ.add(p);
        }
    }

    class Page
    {
        String keyword;
        String subStore;
        int frequency;
        LinkedList<String> index;

        Page(String subStore, String k)
        {
            this.subStore = subStore;
            keyword = k;
            frequency = 0;
            index = IndexManager.readIndex(subStore,keyword);
        }

        LinkedList<String> find(String fileName)
        {
            LinkedList<String> matches = new LinkedList<String>();
            for(String aFile : index)
            {
                if(fileName.contains(aFile) || aFile.contains(fileName))
                    matches.add(aFile);
            }
            return matches;
        } 
    }
}