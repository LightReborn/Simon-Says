import java.util.Scanner;

public class simpleMessenger {
    public static void main (String args[]){
        ProtocolManager manager;
        Scanner scanner = new Scanner(System.in);
        String ip;

        System.out.print("Partner's IP?: ");
        ip = scanner.next();

        manager = new ProtocolManager(ip, 555, 555);

        while (true) {
          System.out.print("\nYou: ");
          ip = scanner.next();
          manager.send(ip); //send your message

          System.out.print("Partner: " + manager.receive()); //get their message.
        }
    }
}
