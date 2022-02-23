import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.*;


public class WeatherService {


    public static String service(String city) throws IOException {
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city +"&appid=02e00071281cb2afed10f21f6f80e12e");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        String cont = "";
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
            cont += inputLine;
        }
        in.close();

        System.out.println(cont);

        return cont;
    }


//    public static void main(String [] args) throws IOException {
//        service();
//    }


}
