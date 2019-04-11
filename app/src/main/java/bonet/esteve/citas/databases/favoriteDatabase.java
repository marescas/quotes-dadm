package bonet.esteve.citas.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import bonet.esteve.citas.POJO.Quotation;

@Database(entities = {Quotation.class}, version = 1)
public abstract class favoriteDatabase extends RoomDatabase {
    private static favoriteDatabase favoriteDatabase;
    public synchronized static favoriteDatabase getInstance(Context context) {
        if (favoriteDatabase == null) {
            favoriteDatabase = Room
                    .databaseBuilder(context, favoriteDatabase.class, "quotation_database")
                    .build();
        }
        return favoriteDatabase;
    }
    public abstract quotation_dao quotation_dao();

}
