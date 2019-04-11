package bonet.esteve.citas;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bonet.esteve.citas.Adapter.favouriteAdapter;
import bonet.esteve.citas.POJO.Quotation;
import bonet.esteve.citas.databases.MySqliteOpenHelper;
import bonet.esteve.citas.databases.favoriteDatabase;
import bonet.esteve.citas.tasks.gQuotation;

public class FavouriteActivity extends AppCompatActivity {
    Button btnAutor = null;
    ListView listaFavoritos = null;
    String autor ="";
    ArrayList<Quotation> misCitas;
    favouriteAdapter adapter;
    Menu  menup;
    String database_type = "";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        autor = "Albert Einstein";
        listaFavoritos = findViewById(R.id.listFavourite);
        //misCitas = getMockQuotations();
        //misCitas = new ArrayList<>();
        handler = new Handler();
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        database_type = preferences.getString("database_type","");
        misCitas = new ArrayList<>();
        adapter = new favouriteAdapter(this,R.layout.quotation_list_row,misCitas);
        listaFavoritos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                autor =  misCitas.get(i).getAutor();
                if(autor.equals(" ")){
                    Toast.makeText(FavouriteActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://en.wikipedia.org/wiki/Special:Search?search=" + autor));
                    //existe una app que puede resolverlo?
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    //Toast.makeText(FavouriteActivity.this, misCitas.get(i).getAutor(), Toast.LENGTH_SHORT).show();
                }

            }

        });
        listaFavoritos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(R.string.delete);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                       Thread t =  new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(database_type.equals("SQLiteOpenHelper")) {
                                    MySqliteOpenHelper.getInstance(getApplicationContext()).delete(misCitas.get(i).getCita());
                                }else{
                                    favoriteDatabase.getInstance(getApplicationContext()).quotation_dao().delete(misCitas.get(i));
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.remove(misCitas.get(i));
                                        if(misCitas.size() == 0){
                                            menup.findItem(R.id.menu_delete).setVisible(false);
                                        }
                                    }
                                });

                            }
                        });
                       t.start();
                      /* try {
                           t.join();
                           misCitas.remove(i);
                           adapter.notifyDataSetChanged();
                           if(misCitas.size() == 0){
                               menup.findItem(R.id.menu_delete).setVisible(false);
                           }
                       } catch (InterruptedException e) {
                           e.printStackTrace();

                    }
                    */
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();

                return true;
            }
        });
        listaFavoritos.setAdapter(adapter);

       gQuotation task =  new gQuotation(this);
       task.execute(database_type.equals("SQLiteOpenHelper"));

    }
    public void putCitas(List<Quotation> quotations){
        misCitas.clear();
        for(Quotation quote:quotations){
            misCitas.add(quote);
        }
        /*if(misCitas.size()<= 0) menup.findItem(R.id.menu_delete).setVisible(false);
        else menup.findItem(R.id.menu_delete).setVisible(true);
        */

        adapter.notifyDataSetChanged();
    }
    public ArrayList<Quotation> getMockQuotations(){
        ArrayList<Quotation> listaCitas = new ArrayList<>();
        String[] cita= {"test1","pepe","test3","test4", "test5","test6","test7","test8","test9","test10","test1","test2","test3","test4", "test5","test6","test7","test8","test9","test10"};
        String[] autor= {"test1","peperoni","test3","test4", "test5","test6","test7","test8","test9","test10","test1","test2","test3","test4", "test5","test6","test7","test8","test9","test10"};
        for (int i = 0; i<cita.length; i++){
            listaCitas.add(new Quotation(cita[i],autor[i]));
        }
        return listaCitas;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mymenu = getMenuInflater();
        mymenu.inflate(R.menu.menufavorite,menu);
        menup = menu;
        if(misCitas.size() == 0) {
            menu.findItem(R.id.menu_delete).setVisible(false);
        }else{
            menu.findItem(R.id.menu_delete).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteAll);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                if(database_type.equals("SQLiteOpenHelper")) {
                                    MySqliteOpenHelper.getInstance(getApplicationContext()).deleteAll();
                                }else{
                                    favoriteDatabase.getInstance(getApplicationContext()).quotation_dao().deleteAll();
                                }

                            }
                        }).start();
                        misCitas.clear();
                        adapter.notifyDataSetChanged();
                        item.setVisible(false);


                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();

                return true;

                default:
                    return super.onOptionsItemSelected(item);



        }

    }
}
