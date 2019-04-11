package bonet.esteve.citas.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import bonet.esteve.citas.POJO.Quotation;

@Dao
public interface quotation_dao {
    @Insert
    void addquote(Quotation quote);
    @Delete
    void delete (Quotation quote);
    @Query("SELECT * FROM quotation_table")
    List<Quotation> getQuotations();
    @Query("SELECT * FROM quotation_table WHERE quote = :quote")
    Quotation getQuote(String quote);
    @Query("DELETE  FROM quotation_table")
    void deleteAll();
}
