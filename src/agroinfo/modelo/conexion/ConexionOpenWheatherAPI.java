package agroinfo.modelo.conexion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ConexionOpenWheatherAPI {

    private String APIKEY = "26ff56f4aaa3010b43e26cee6c2b8cb1";

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    public String[] getTiempo(double lat, double lon) throws IOException, JSONException {

        String[] lista = new String[2];

        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" +
                lat +
                "&lon=" +
                lon +
                "&appid=" + this.APIKEY + "&units=metric&andlang=es";
        JSONObject json = readJsonFromUrl(url);

        lista[0] = json.getJSONArray("weather").getJSONObject(0).getString("icon") + "@2x.png";

        return lista;
    }


}
