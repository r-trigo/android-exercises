package com.example.recyclercards;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Seksu on 01/07/2016.
 */
public class HelperHero {

    public Hero[] FillMyArray() {
        Hero[] heroes = new Hero[8];

        heroes[0] = new Hero(R.drawable.genji_portrait, "Genji", "Offense", "3");
        heroes[1] = new Hero(R.drawable.bastion_portrait, "Bastion", "Defense", "1");
        heroes[2] = new Hero(R.drawable.dva_portrait, "Dva", "Tank", "2");
        heroes[3] = new Hero(R.drawable.lucio_portrait, "Lúcio", "Support", "2");
        //
        heroes[4] = new Hero(R.drawable.mccree_portrait, "McCree", "Offense", "2");
        heroes[5] = new Hero(R.drawable.hanzo_portrait, "Hanzo", "Defense", "3");
        heroes[6] = new Hero(R.drawable.reinhardt_portrait, "Reinhardt", "Tank", "1");
        heroes[7] = new Hero(R.drawable.mercy_portrait, "Mercy", "Support", "1");
        return heroes;
    }

    public Hero[] GetJsonFromString(String imjson) {

//        String imjson = "[\n" +
//                "\t{\n" +
//                "\t\t\"name\":\"Genji\",\n" +
//                "\t\t\"role\":\"Offense\",\n" +
//                "\t\t\"difficulty_level\":\"3\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"name\":\"Bastion\",\n" +
//                "\t\t\"role\":\"Defense\",\n" +
//                "\t\t\"difficulty_level\":\"1\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"name\":\"Dva\",\n" +
//                "\t\t\"role\":\"Tank\",\n" +
//                "\t\t\"difficulty_level\":\"2\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"name\":\"Lúcio\",\n" +
//                "\t\t\"role\":\"Support\",\n" +
//                "\t\t\"difficulty_level\":\"2\"\n" +
//                "\t}\n" +
//                "]";
        Hero[] heroes = new Hero[25];

        try {
            JSONArray jsonArray = new JSONArray(imjson);
            for (int i = 0; i < jsonArray.length(); i++) {
                Hero h = new Hero();

                JSONObject heroObject = jsonArray.getJSONObject(i);
                h.setName(heroObject.getString("name"));
                h.setRole(heroObject.getString("role"));
                h.setDifficulty_level(heroObject.getString("difficulty_level"));

                heroes[i] = h;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return heroes;
    }

    public Hero[] GetJsonFromURL(URL[] myURL) {
        //array due to async task requirements
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();

        try {
            conn = (HttpURLConnection) myURL[0].openConnection();
            conn.connect();

            InputStream stream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String jsonString = buffer.toString();
        return GetJsonFromString(jsonString);
    }

    public void AddHeatMap(Context context, GoogleMap map) {
        // Get the data: latitude/longitude positions of police stations.
        ArrayList<LatLng> list = new ArrayList<>();
        try {
            InputStreamReader is = new InputStreamReader(context.getResources().openRawResource(R.raw.mobile_heatmap_points));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                LatLng spot = new LatLng(Float.parseFloat(row[0]), Float.parseFloat(row[1]));
                list.add(spot);
            }

            // Create a heat map tile provider, passing it the latlngs of the police stations.
            HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder().data(list).build();
            // Add a tile overlay to the map, using the heat map tile provider.
            TileOverlay mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int CountMatches(String text, String what_to_find) {
        int count = 0;
            for (int i = 0; i < text.length(); i++) {
                for (int j = 0; j < what_to_find.length(); j++) {
                    if (text.charAt(i) == what_to_find.charAt(j))
                        count++;
                }
            }
        return count;
    }

    public String CharsCounter(String written) {

        String patternUCS2 = "^@£$¥èéùìòÇ\rØø\nÅåΔ_ΦΓΛΩΠΨΣΘΞÆæßÉ !\"#¤%&'()*+,-./0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà~€\f\\^\\{\\}\\]\\[\\|\\\\";
        int countUCS2 = CountMatches(written, patternUCS2);
        String patternTableExtended = "~€\f\\^\\{\\}\\]\\[\\|\\\\";
        int countTE = CountMatches(written, patternTableExtended);
        int characters;
        int charactersTotal;
        int nrSMS;
        boolean UCS2 = false;

        int textLength = written.length();
        characters = textLength;

        if (countUCS2 < textLength) {
            charactersTotal = textLength > 70 ? 67 : 70;
        } else {
            if (countTE > 0) {
                characters = textLength + countTE;
            }
            UCS2 = true;
            charactersTotal = characters > 160 ? 153 : 160;
        }

        if (characters > charactersTotal) {
            int remainder = characters % charactersTotal;
            int divisionRes = ((characters - remainder) / charactersTotal);

            nrSMS = (remainder > 0 ? divisionRes + 1 : divisionRes);

            charactersTotal = (nrSMS * charactersTotal);
        } else {
            nrSMS = 1;
        }

        String output = "NrCharacters: " + characters + ", NrCharactersTotal: " + charactersTotal +
                ", NrSegments: " + nrSMS + ", UCS2: " + UCS2;

        return output;
    }

    public String[] SimpleSringToStringArray (String simpleString) {
        String[] stringArray = simpleString.split(", ");
        return stringArray;
    }
}
