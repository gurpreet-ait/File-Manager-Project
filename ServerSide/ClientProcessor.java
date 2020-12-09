package ServerSide;

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientProcessor extends Thread{
    Socket clnt;
  ClientProcessor(Socket s)
  {
    clnt = s;
    //active the thread
    start();
    
  }
  
  public void run()
  {
    try
    {
      DataInputStream din = new DataInputStream(clnt.getInputStream());
      DataOutputStream dout = new DataOutputStream(clnt.getOutputStream());

      
      String input = din.readUTF();
      //fetch client input (search)
      //fish / bubble / graph
      
      //to extend this system 
      //apply nlp on "input" and detect the topic
      //of search and content to search
      //example, topic : networking  and content : datagrams
      
      String topic = "default"; //substore
      String content = input;//file to search in the substore
      
      FileManager fmgr = FileManager.getObject();
      LinkedList<String> matches = fmgr.search(topic, content);
      
      if(matches == null)
      {
        dout.writeInt(0);
      }
      else
      {
        dout.writeInt(matches.size());
        
        for(String s : matches)
        {
          dout.writeUTF(s);
        }
      }
      
    }
    catch(Exception ex)
    {
      System.out.println("Err : "+ ex);
    }
  
  }//run
}
