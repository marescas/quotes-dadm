package bonet.esteve.citas;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import bonet.esteve.citas.POJO.Quotation;
import bonet.esteve.citas.databases.MySqliteOpenHelper;
import bonet.esteve.citas.databases.favoriteDatabase;
import bonet.esteve.citas.tasks.gQuotation;
import bonet.esteve.citas.tasks.webQuotation;

public class QuotationActivity extends AppCompatActivity {
    TextView textQuotation, textAuthor = null;
    Menu menup;
    ProgressBar progressBar = null;
    String database_type = "";
    webQuotation task;
    Boolean isVisible = false;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        handler = new Handler();
       SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        database_type = preferences.getString("database_type","");
        Log.d("data_type",database_type);
        textQuotation = findViewById(R.id.textQuote);
        textAuthor = findViewById(R.id.authorName);
        String username = preferences.getString("username","");
        String test ="";
        if (username.equals("")) test = getString(R.string.refreshToQuotation,getString(R.string.notuser));
        else test = getString(R.string.refreshToQuotation,username);
        textQuotation.setText(test);
        progressBar = findViewById(R.id.progressBar);
        if(savedInstanceState != null){
            textQuotation.setText(savedInstanceState.getString("cita"));
            textAuthor.setText(savedInstanceState.getString("autor"));
            isVisible = savedInstanceState.getBoolean("añadir");
        }


    }
    public void newQuote(){
        /*citasrecibidas++;

        textQuotation.setText(getString(R.string.sampleQuotation,String.valueOf(citasrecibidas)));
        textAuthor.setText(getString(R.string.sampleAuthor,String.valueOf(citasrecibidas)));
        */

        final Boolean[] quotationinTable = new Boolean[1];
        Thread t =  new  Thread(new Runnable() {
            @Override
            public void run() {

                if(database_type.equals("SQLiteOpenHelper")){
                    quotationinTable[0] = MySqliteOpenHelper.getInstance(getApplicationContext()).isQuotationInTable(textQuotation.getText().toString());
                }else{
                    quotationinTable[0] = favoriteDatabase.getInstance(getApplicationContext()).quotation_dao().getQuote(textQuotation.getText().toString()) != null;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(quotationinTable[0]){
                            menup.findItem(R.id.menu_add).setVisible(false);
                        }else{
                            menup.findItem(R.id.menu_add).setVisible(true);
                        }
                    }
                });
            }
        });
        t.start();
        /*try {
            t.join();
            if(quotationinTable[0]){
                menup.findItem(R.id.menu_add).setVisible(false);
            }else{
                menup.findItem(R.id.menu_add).setVisible(true);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mymenu= getMenuInflater();
        mymenu.inflate(R.menu.menuquotation,menu);
        menup = menu;
        menup.findItem(R.id.menu_add).setVisible(isVisible);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                addDatabase();
                break;
            case R.id.menu_newquote:
                task =  new webQuotation(this);
                if(isNetworkConnected()) {
                    task.execute();
                }
                break;

                default:
                    return super.onOptionsItemSelected(item);

        }
        return true;
    }
    public void addDatabase(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean quotationinTable;
                if(database_type.equals("SQLiteOpenHelper")){
                    quotationinTable= MySqliteOpenHelper.getInstance(getApplicationContext()).isQuotationInTable(textQuotation.getText().toString());
                    if(!quotationinTable) MySqliteOpenHelper.getInstance(getApplicationContext()).addquote(textQuotation.getText().toString(), textAuthor.getText().toString());
                }else{
                    quotationinTable = favoriteDatabase.getInstance(getApplicationContext()).quotation_dao().getQuote(textQuotation.getText().toString()) != null;
                    if(!quotationinTable) favoriteDatabase.getInstance(getApplicationContext()).quotation_dao().addquote(new Quotation(textQuotation.getText().toString(), textAuthor.getText().toString()));
                }
                Log.d("database_irsert","ok");



            }
        }).start();
        menup.findItem(R.id.menu_add).setVisible(false);

    }
    public void progressOn(){
        progressBar.setVisibility(View.VISIBLE);
        menup.findItem(R.id.menu_add).setVisible(false);
        menup.findItem(R.id.menu_newquote).setVisible(false);
    }
    public void newQuote(Quotation quote){
        textQuotation.setText(quote.getCita());
        textAuthor.setText(quote.getAutor());
        progressBar.setVisibility(View.INVISIBLE);
        menup.findItem(R.id.menu_add).setVisible(true);
        menup.findItem(R.id.menu_newquote).setVisible(true);
        newQuote();

    }
    public boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return ((info != null) && (info.isConnected()));
    }

    @Override
    protected void onDestroy() {

        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cita",textQuotation.getText().toString());
        outState.putString("autor",textAuthor.getText().toString());
        outState.putBoolean("añadir",menup.findItem(R.id.menu_add).isVisible());

    }
}
