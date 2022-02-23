import java.net.*;
import java.io.*;
import java.util.Arrays;

public class Server {

    public void start() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }



        boolean running = true;



        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }



            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean primeraLinea = true;
            String file = "";
            while ((inputLine = in.readLine()) != null) {
                if (primeraLinea) {

                    file = inputLine.split(" ")[1];
                    System.out.println("File: " + file);
                    primeraLinea = false;
                }
                if (!in.ready()) {
                    break;
                }
            }


            System.out.println();
            System.out.println(file.equals("/clima"));
            if (file.equals("/clima")) {
                outputLine = home();
                out.println(outputLine);

            } else {
                outputLine = searchCity();
            }
            out.println(outputLine);



            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }


    public String home() throws IOException {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Clima 2</title>\n"
                + "</head>"
                + "<body>"
                + "Clima 2222"
                + "<form>"
                + "<h2>"
                +"Nombre Ciudad"
                +"</h2>"
                +"<label for='celsius'>Ciudad</label><br />"
                +"<input id='ciudad' type='text' /> <br />"
                +"<p>"
                +"respuesta"
                +"</p>"
                +"<p id='fahrenheitResult'></p>"

                +"<button id='buscarButton' type='button' value='Submit' >Buscar</button>"
                +"</form>"
                + "</body>"
                + "</html>";
    }

    public String searchCity(String city) throws IOException {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Clima</title>\n"
                + "</head>"
                + "<body>"
                + "Clima"
                + charge()
                + "</body>"
                + "</html>";
    }

    public String charge() throws IOException {
        WeatherService weatherService = new WeatherService();
        return weatherService.service("London");
    }


    public static void main(String [] args){
        Server server = new Server();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
