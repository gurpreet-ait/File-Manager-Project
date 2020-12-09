package ServerSide;
public class Main
{
    public static void main(String[] args) 
    {
        StoreManager smnr = new StoreManager();
        try {
            int pno=8090;
            Server svr = new Server(pno);
        } 
        catch (Exception e) {
            System.out.println("Err : " + e);
        }
    }
}