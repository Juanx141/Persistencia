package Ejercicio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static List<ClientHandler> clientes = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Servidor iniciado, esperando clientes");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Usuario conectado: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientes.add(clientHandler);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();

               
                clientesconectados();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clientesconectados() {
        System.out.println("Usuarios conectados:");
        for (ClientHandler cliente : clientes) {
            System.out.println(cliente.nombredeu());
        }
        System.out.println("-----------------");
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

   
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

               
                writer.println("Ingresa tu nombre de usuario:");
                username = reader.readLine();

                String mensaje;

                while ((mensaje = reader.readLine()) != null) {
                    System.out.println(username + ": " + mensaje);
                    if (mensaje.equalsIgnoreCase("chao")){
                    	System.out.println("El usuario " + username + " se ha desconectado");
                    }
                    		
                    enviarmsg(username + ": " + mensaje);
                }
            } catch (IOException e) {
            	System.out.println("El usuario " + username + " se ha desconectado");
              //  e.printStackTrace();
            } finally {
                try {
                    reader.close();
                    writer.close();
                    clientSocket.close();
                    clientes.remove(this);

                  
                    clientesconectados();
                } catch (IOException e) {
                //	System.out.println("Aqui falle");
                   e.printStackTrace();
                }
            }
        }

        public String nombredeu() {
            return username;
        }

        private void enviarmsg(String message) {
            for (ClientHandler cliente : clientes) {
                cliente.envio(message);
            }
        }

        private void envio(String message) {
            writer.println(message);
            writer.flush();
        }
    }
}