/**
 *
 *  @author Wojas Kamil S23878
 *
 */

package zad2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.json.JSONObject;

public class Service {
    private final String country;

    public Service(String country) {
        this.country = country;
    }

    public String getWeather(String city) throws IOException {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + URLEncoder.encode(city, "UTF-8") + ","
                + URLEncoder.encode(country, "UTF-8")
                + "&units=metric&appid=<API_KEY>";

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        }
    }

    public Double getRateFor(String currencyCode) throws IOException {
        String url = "https://api.exchangerate.host/latest?base=" + URLEncoder.encode(country, "UTF-8")
                + "&symbols=" + URLEncoder.encode(currencyCode, "UTF-8");

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            JSONObject response = new JSONObject(reader.lines().collect(Collectors.joining()));
            return response.getJSONObject("rates").getDouble(currencyCode);
        }
    }

    public Double getNBPRate() throws IOException {
        String url = "http://www.nbp.pl/kursy/kursya.html";

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(country)) {
                    String[] values = line.split("\\s+");
                    return Double.parseDouble(values[2].replace(",", "."));
                }
            }
        }

        url = "http://www.nbp.pl/kursy/kursyb.html";

        connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(country)) {
                    String[] values = line.split("\\s+");
                    return Double.parseDouble(values[2].replace(",", ".")) / Double.parseDouble(values[1].replace(",", "."));
                }
            }
        }

        throw new IllegalArgumentException("Cannot find exchange rate for " + country);
    }
}