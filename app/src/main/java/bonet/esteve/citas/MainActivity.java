package bonet.esteve.citas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import bonet.esteve.citas.databases.favoriteDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);

         if(preferences.getBoolean("first_run",true)){
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     favoriteDatabase.getInstance(getApplicationContext()).quotation_dao().getQuotations();
                     //TODO descomentar
                 }
             }).start();

             Log.d("test_database","llega aquí");
             preferences.edit().putBoolean("first_run",false);
             preferences.edit().apply();

         }
    }
    public void botonPulsado(View v){
        Intent i = null;
        switch (v.getId()){
            case R.id.btngetQuotation:
                i = new Intent(this,QuotationActivity.class);
                break;
            case R.id.btnFavoriteQuotation:
                i = new Intent(this,FavouriteActivity.class);
                break;
            case R.id.btnSettings:
                i = new Intent(this,SettingsActivity.class);
                break;
            case R.id.btnAbout:
                i = new Intent(this,AboutActivity.class);

                break;
        }
        startActivity(i);

    }

}
