package ClientSide;

public class ClientNWFileSharing 
{

  public static void main(String[] args) 
  {
    try
    {
      System.out.println("----------");
      Client clnt = new Client("127.0.0.1", 8090);
      System.out.println("**********");
      clnt.query();
      System.out.println("^^^^^^^^^");
    }
    catch(Exception ex)
    {
      System.out.println("Err "+ ex);
    }
  }
  
}
