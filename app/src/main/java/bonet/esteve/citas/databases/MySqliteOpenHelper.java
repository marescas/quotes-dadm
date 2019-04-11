package bonet.esteve.citas.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import bonet.esteve.citas.POJO.Quotation;

public class MySqliteOpenHelper extends SQLiteOpenHelper {
    private MySqliteOpenHelper(Context context) {
        super(context, "quotation_database",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE quotation_table (id INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, quote TEXT NOT NULL, author TEXT, UNIQUE(quote));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public ArrayList<Quotation> getQuotations(){
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Quotation> quotations = new ArrayList<>();
        Cursor c = database.query("quotation_table",new String[]{"quote", "author"},null,null,null,null,null);
        //c.moveToFirst();
        if(c.getCount() <= 0) return quotations;
        while (c.moveToNext()){
            quotations.add(new Quotation(c.getString(0),c.getString(1)));
            Log.d("SQL_LIST",c.getString(0)+" "+c.getString(1));

        }
        c.close();
        database.close();
        return quotations;
    }
    public synchronized static MySqliteOpenHelper getInstance(Context context){
        return new MySqliteOpenHelper(context);

    }
    public boolean isQuotationInTable(String quote){
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.query("quotation_table",null,"quote=?",new String[]{quote},null,null,null);
        Boolean result = c.getCount() >0;
        c.close();
        database.close();
        Log.d("SQL_Quote_In_Table",String.valueOf(result)+ " "+ quote);
        return result;
    }
    public void addquote(String quote, String autor){
        ContentValues contentValues =  new ContentValues();
        contentValues.put("quote",quote);
        contentValues.put("author",autor);
        SQLiteDatabase database = getWritableDatabase();
        database.insert("quotation_table",null,contentValues);
        database.close();
    }
    public void deleteAll(){
        SQLiteDatabase database = getWritableDatabase();
        database.delete("quotation_table",null,null);
        database.close();
    }
    public void delete(String quote){
        SQLiteDatabase database = getWritableDatabase();
        database.delete("quotation_table","quote=?",new String[]{quote});
        database.close();
    }
}
