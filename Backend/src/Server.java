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
            if (file.equals("/clima")) {
                outputLine = home();
//                out.println(outputLine);

            } else if(file.contains("/consulta")) {

                System.out.println(Arrays.toString(file.split("=")));
                String[] newFile = file.split("=");
                System.out.println(newFile[1]);
                String city = newFile[1];
                outputLine = searchCity(city);
            }
            else{
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>URL malformed</title>\n"
                        + "</head>"
                        + "<body>"
                        + "URL malformed"
                        + "</body>"
                        + "</html>";
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
                + "Buscar clima "
                + "<form>"
                + "<h2>"
                +"Nombre Ciudad"
                +"</h2>"
                +"<label for='ciudadLabel'>Ciudad</label><br />"
                +"<input id='ciudadInput'  /> <br />"
                +"<p>"
                +"respuesta"
                +"</p>"
                +"<p id='ciudadRespuesta'></p>"

                +"<button id='buscarButton' type='button' onclick='buscarListener()' >Buscar</button>"
                +"</form>"
                +"<script>"
                +"const URL = 'http://localhost:35000/consulta';\n" +
                "const ciudadInput = document.getElementById('ciudadInput');\n" +
                "console.log('TEST', ciudadInput.value);\n" +
                "const buscarButton = document.getElementById('buscarButton');\n" +
                "const ciudadRespuesta = document.getElementById('ciudadRespuesta');\n" +
                "\n" +
                "\n" +
                "async function buscarListener(){\n" +
                "    const ans = await fetchData(ciudadInput.value); \n" +
                "    const res = JSON.stringify(ans);"+
                " ciudadRespuesta.innerHTML = `El resultado es: ${res}`;"+
                "}\n" +
                "\n" +
                "async function fetchData(value){\n" +
                "    const url = `${URL}?ciudad=${value}`;\n" +
                "    const res = await fetch(url, {\n" +
                "    method:'GET',\n" +
                "        headers:{\n" +
                "        'Content-Type': 'application/json'\n" +
                "        }\n" +
                "    });\n" +
                "const data = await res.json();\n" +
                "return data;\n" +
                "}"+

                "</script>"

                + "</body>"
                + "</html>";

    }

    public String searchCity(String city) throws IOException {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "\r\n"
                + WeatherService.service(city);


    }

    public String charge(String city) throws IOException {
        WeatherService weatherService = new WeatherService();
        return weatherService.service(city);
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
