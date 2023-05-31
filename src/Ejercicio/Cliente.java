package Ejercicio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);

           
            System.out.println(lector.readLine());

           
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String username = consoleReader.readLine();

          
            escritor.println(username);

           
            Thread svhl = new Thread(() -> {
                String servimsg;
                try {
                    while ((servimsg = lector.readLine()) != null) {
                        System.out.println(servimsg);
                        // primer cierre
                        if (servimsg.equalsIgnoreCase("chao")) {
                           
                            break;
                        }
                    }
                } catch (IOException e) {
                	//mensaje cierre
                	System.out.println("Adiosin, vuelva pronto");
                   // e.printStackTrace();
                }
            });
            svhl.start();

         
            String clienmsg;
            while ((clienmsg = consoleReader.readLine()) != null) {
            	//cierre v2
                if (clienmsg.equalsIgnoreCase("chao")) {
               
                	escritor.println(clienmsg);
                	break;
                }
                escritor.println(clienmsg);
            }

           
            escritor.close();
            lector.close();
            socket.close();
        } catch (IOException e) {
        	System.out.println();
        	System.out.println("Adiosin, vuelva pronto");
            //e.printStackTrace();
        }
    }
}