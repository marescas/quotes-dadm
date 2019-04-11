package bonet.esteve.citas.tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bonet.esteve.citas.FavouriteActivity;
import bonet.esteve.citas.POJO.Quotation;
import bonet.esteve.citas.databases.MySqliteOpenHelper;
import bonet.esteve.citas.databases.favoriteDatabase;

public class gQuotation extends AsyncTask<Boolean,Void, List<Quotation>> {
    private final WeakReference<FavouriteActivity> activity;
    public gQuotation(FavouriteActivity activity){
        this.activity = new WeakReference<>(activity) ;
    }
    @Override
    protected List<Quotation> doInBackground(Boolean... booleans) {
        List<Quotation> quotes;
        if (booleans[0]){
           quotes = MySqliteOpenHelper.getInstance(activity.get().getApplicationContext()).getQuotations();
        }else {
            quotes = favoriteDatabase.getInstance(activity.get().getApplicationContext()).quotation_dao().getQuotations();
        }
        return quotes;

    }

    @Override
    protected void onPostExecute(List<Quotation> quotations) {
        activity.get().putCitas(quotations);
    }
}
