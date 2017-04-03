package com.developer.gavine.ebooks;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {

    public  static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public  static Context mContext;

    public QueryUtils(Context context) {

        mContext = context;
    }

    public static List<Ebooks> fetchEbooksData (String requesturl)
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(requesturl);

        String jsonResponse= null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e)
        {
            Log.e(LOG_TAG,"Error in HTTP Request...",e);
        }

        List<Ebooks> eb = extractFeatureFromJson(jsonResponse);
        return eb;
    }

    public static URL createUrl(String stringUrl)
    {
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG,"Error in creating URL",e);
            return null;
        }
        return url;
    }
    public static String makeHttpRequest(URL url) throws IOException{

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String jsonResponse = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readStream(inputStream);
            }
            else{
                Log.e(LOG_TAG,"Error Response code..."+ urlConnection.getResponseCode());
            }
        }catch(IOException e)
        {
            Log.e(LOG_TAG,"Couldn't retrieve ebooks data..");
        }
        finally {
            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if(inputStream != null)
            {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static String readStream(InputStream ip) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if(ip != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(ip , Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null)
            {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Ebooks> extractFeatureFromJson(String json)
    {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        List<Ebooks> elist = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(json);
            String quote= jsonObject.getString("quote");
            String charac = jsonObject.getString("character");

            /*for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject currentEarthquake = jsonArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("");
                //String bib = jsonObject.getString("bib_key");
                String bib = properties.getString("character");
                String preview = properties.getString("quote");*/
                String preview_url = "pu";
                String info_url = "i";
                String thumbnail = "t";

                Ebooks ebb = new Ebooks(quote, preview_url, info_url, charac, thumbnail);
                elist.add(ebb);




        }catch(JSONException e){
            Log.e(LOG_TAG,"Coudn't retrieve json data..",e);
        }
        return elist;
    }
}
