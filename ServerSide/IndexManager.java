package ServerSide;

import java.io.*;
import java.util.LinkedList;

public class IndexManager extends Thread
{
  String key;
  int opFlag;
  IndexManager(String key, int opFlag) 
  { 
    this.key = key;
    this.opFlag = opFlag;
    start();
  }
  
  public void run()
  {
    if(opFlag == Presets.MAKE_INDEX)
      makeIndex();
    else if(opFlag == Presets.DELETE_INDEX)
      deleteIndex();
  }

  void deleteIndex()
  {
    File subStoreIndexReference = new File(Presets.INDEX_LOCATION + "/" + key);
    String all[] = subStoreIndexReference.list();
    for(String temp : all)
      new File(subStoreIndexReference, temp).delete();
    
    if(subStoreIndexReference.delete())
    {
      StoreManager.getStoreMap().remove(key);

      //update the memoryMap
      FileManager fmgr = FileManager.getObject();
      fmgr.deleteFromMemoryMap(key);
    }  
  }
  
  void makeIndex()
  {
    try
    {
      //update index files
      File fileStore = new File(Presets.STORE_LOCATION +"/" + key );
      
      File fileIndex = new File(Presets.INDEX_LOCATION +"/" + key );
      fileIndex.mkdirs();
      
      String all[] = fileStore.list();
      String nm;
      int i, firstChar;

      FileWriter allFiles[] = new FileWriter[27];
      for(i =0; i < 26; i++)
        allFiles[i] = new FileWriter(Presets.INDEX_LOCATION + "/" + key + "/"+ (char)(65+i)+ ".txt");

      allFiles[i]= new FileWriter(Presets.INDEX_LOCATION + "/" + key +"/Others.txt");

      for(String s : all)
      {
        nm = s.toUpperCase();
        firstChar = nm.charAt(0);
        if(firstChar >=65 && firstChar <=90)
          i = firstChar - 65;
        else
          i = 26;


        allFiles[i].write(s.toUpperCase() + "\n");

      }//for each

      for(i = 0; i< 27; i++)
      {
        allFiles[i].flush();
        allFiles[i].close();
      }

      //update the memoryMap
      FileManager fmgr = FileManager.getObject();
      fmgr.addToMemoryMap(key);
    }
    catch(Exception ex)
    {
      System.out.println("Err: "+ ex);
      StoreManager.getStoreMap().put(key, 0L);
    }
  }//makeIndex
  
  public static  LinkedList<String> readIndex(String subStore, String keyword)
  {
    LinkedList<String> index = new LinkedList<String>();
    try
    {
      String fileName = Presets.INDEX_LOCATION + "/" + subStore + "/"+ keyword+ ".txt";
      //read 
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);  

      String thisLine;
      while((thisLine = br.readLine()) != null)
      {
        index.add(thisLine.toUpperCase());
      }
      br.close();
    }
    catch(Exception ex)
    {}
    return index;
  }
  
}
