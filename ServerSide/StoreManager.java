package ServerSide;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

public class StoreManager extends Thread
{
    static HashMap<String, Long> storeMap;
  boolean flag;
  
  static
  {
    //an empty store map
    storeMap = new HashMap<String, Long>();
  }
        
  StoreManager()
  {
    start();
  }
  
  public void run()
  {//executes concurrently
    flag = true;
    polling();
  }//run
  
  public void stopParsing()
  {
    flag = false;
  }
  
  private void polling()
  {//synchronization
    while(flag)
    {
      parseStore();
      
      try
      { //10 second delay
        Thread.sleep(10000);
      }
      catch(InterruptedException ex)
      {
        System.out.println("err : " + ex);
      }
    }//while
  }//polling  
  
  
  private void parseStore()
  {
    File folderReference = new File(Presets.STORE_LOCATION);
    File subStore;
    LinkedList<String> directories = new LinkedList<String>();
    
    String storeContents[] = folderReference.list();
    
    for(String curr: storeContents)
    {
      subStore = new File(folderReference, curr);
      if(subStore.isDirectory())
      {
        directories.add(curr);
        if(!storeMap.containsKey(curr))
        {
          //make an entry
          storeMap.put(curr, subStore.lastModified());
          //create a cluster of indices
          new IndexManager(curr, Presets.MAKE_INDEX);
          //Pages of subStore live here
          FileManager.getObject().addToMemoryMap(curr);
        }
        else 
        {  
          if(storeMap.get(curr) < subStore.lastModified())
          {
            //update the storeMap
            storeMap.put(curr, subStore.lastModified());
            //refresh the index cluster
            new IndexManager(curr, Presets.MAKE_INDEX);
          }
        }
      }//if
    }//for
    
    for(String mapKey : storeMap.keySet())
    {
      if(!directories.contains(mapKey))
      {
        //remove the index cluster
        new IndexManager(mapKey, Presets.DELETE_INDEX);
        //Pages of subStore removed
        FileManager.getObject().deleteFromMemoryMap(mapKey);
      }
    }
  }
  
  public static HashMap <String, Long>  getStoreMap()
  {
    return storeMap;
  }
}
