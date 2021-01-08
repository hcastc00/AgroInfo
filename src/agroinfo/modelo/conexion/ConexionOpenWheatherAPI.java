package agroinfo.modelo.conexion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
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
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            System.out.println(jsonText);
            return new JSONObject(jsonText);
        } finally {
            is.close();
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

        lista[0] = json.getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png";

        return lista;
    }


}
