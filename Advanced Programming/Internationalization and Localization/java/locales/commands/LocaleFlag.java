package locales.commands;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocaleFlag implements Command {
    @Override
    public void execute(Locale locale) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> request = new HashMap<>();
        request.put("sCountryISOCode", locale.getCountry());
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            URL url = new URL("http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso/CountryFlag");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Content-Length", "length");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                byte[] input = requestJson.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }
            try (BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = bufferedReader.readLine();
                while (responseLine != null) {
                    response.append(responseLine.trim());
                    responseLine = bufferedReader.readLine();
                }
                try {
                    Desktop desktop = java.awt.Desktop.getDesktop();
                    URI uri = new URI(objectMapper.readValue(response.toString(), String.class));
                    desktop.browse(uri);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
