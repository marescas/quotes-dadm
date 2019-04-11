package bonet.esteve.citas.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bonet.esteve.citas.POJO.Quotation;
import bonet.esteve.citas.QuotationActivity;
import bonet.esteve.citas.R;

public class webQuotation extends AsyncTask<Void,Void, Quotation> {
    WeakReference<QuotationActivity> ref;
    @Override
    protected Quotation doInBackground(Void... voids) {
        Quotation quote = null;
        String idioma = "en";
        String idiomaPreference =PreferenceManager.getDefaultSharedPreferences(ref.get().getApplicationContext()).getString("list_idiomas","en");
        if(idiomaPreference.equals(ref.get().getString(R.string.ruso))){
            idioma = "ru";
        }
        Log.d("language",idioma);
        String typeConsulta =PreferenceManager.getDefaultSharedPreferences(ref.get().getApplicationContext()).getString("list_http","GET");

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            builder.authority("api.forismatic.com");
            builder.appendPath("api");
            builder.appendPath("1.0");
            builder.appendPath("");
        if(typeConsulta.equals("GET")) {
            builder.appendQueryParameter("method", "getQuote");
            builder.appendQueryParameter("format", "json");
            builder.appendQueryParameter("lang", idioma);
            try{
                URL url = new URL(builder.build().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                if(connection.getResponseCode() ==HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Gson gsonfile =  new Gson();
                    Log.d("HTTP_CORRECT","GET correct");
                    quote = gsonfile.fromJson(reader,Quotation.class);
                    reader.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            String body = "?language="+idioma+"&format=json&method=getQuote";
            try {
                URL url = new URL(builder.build().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();
                if(connection.getResponseCode() ==HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Gson gsonfile = new Gson();
                    Log.d("HTTP_CORRECT_POST", "POST correct");
                    quote = gsonfile.fromJson(reader, Quotation.class);
                    reader.close();
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return quote;
    }

    public webQuotation(QuotationActivity ref) {
        this.ref = new WeakReference<>( ref);
    }

    @Override
    protected void onPreExecute() {
        ref.get().progressOn();
    }

    @Override
    protected void onPostExecute(Quotation quotation) {
        ref.get().newQuote(quotation);
    }
}
