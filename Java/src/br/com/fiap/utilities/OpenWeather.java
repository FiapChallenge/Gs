package br.com.fiap.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class OpenWeather {


    // Método para obter informações do clima de uma cidade através da API do OpenWeather
    public static List<String> getInfo(String city) {
        String apiKey = "3d6dbc169a239f24eada6faa74fc9dc4";
        String encodedCity = city;
        try {
            encodedCity = URLEncoder.encode(city, "UTF-8");
        } catch (Exception e) {
            System.out.println("Erro ao codificar a String cidade");
        }
        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + apiKey
                + "&units=metric" + "&lang=pt_br";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            JSONObject jsonResponse = new JSONObject(response.toString());

            double temperatura = jsonResponse.getJSONObject("main").getDouble("temp");
            double tempMaxima = jsonResponse.getJSONObject("main").getDouble("temp_max");
            double tempMinima = jsonResponse.getJSONObject("main").getDouble("temp_min");
            int umidade = jsonResponse.getJSONObject("main").getInt("humidity");
            String clima = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            double velocidadeVento = jsonResponse.getJSONObject("wind").getDouble("speed");

            long sunriseTimestamp = jsonResponse.getJSONObject("sys").getLong("sunrise");
            LocalDateTime sunriseDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(sunriseTimestamp),
                    ZoneId.systemDefault());
            String sunriseTime = sunriseDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            long sunsetTimestamp = jsonResponse.getJSONObject("sys").getLong("sunset");
            LocalDateTime sunsetDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(sunsetTimestamp),
                    ZoneId.systemDefault());
            String sunsetTime = sunsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            List<String> weatherInfoList = new ArrayList<>();
            weatherInfoList.add(String.valueOf(temperatura));
            weatherInfoList.add(String.valueOf(tempMaxima));
            weatherInfoList.add(String.valueOf(tempMinima));
            weatherInfoList.add(String.valueOf(umidade));
            weatherInfoList.add(clima.substring(0, 1).toUpperCase() + clima.substring(1));
            weatherInfoList.add(String.valueOf(velocidadeVento));
            weatherInfoList.add(String.valueOf(sunriseTime));
            weatherInfoList.add(String.valueOf(sunsetTime));

            return weatherInfoList;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}