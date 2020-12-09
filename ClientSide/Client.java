package ClientSide;

import java.net.*;
import java.io.*;

public class Client 
{
  Socket skt;
  Client(String ip, int port) throws Exception
  {
    skt = new Socket(ip, port);
  }
  
  void query()throws Exception
  {
    DataInputStream din = new DataInputStream(skt.getInputStream());
    DataOutputStream dout = new DataOutputStream(skt.getOutputStream());
    
    String query = "Gurpreet";//"bubble", "fish", "static"
    dout.writeUTF(query);
    int qty = din.readInt();
    if(qty > 0)
    {
      int i;
      for(i =1 ; i <= qty; i++)
      {
        System.out.println(i + ") " + din.readUTF());  
      }
      System.out.println("options");
    }
    else
      System.out.println(query+ " not found");
    
  } 
}
