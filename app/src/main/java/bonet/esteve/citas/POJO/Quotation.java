package bonet.esteve.citas.POJO;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "quotation_table", indices = {@Index(value = {"quote"}, unique = true)} )
public class Quotation {
    @ColumnInfo(name = "quote") @NonNull
    @SerializedName("quoteText")
    private String cita;
    @ColumnInfo(name = "author") @NonNull
    @SerializedName("quoteAuthor")
    private String autor;
    @PrimaryKey(autoGenerate = true)
    private int id;

    public Quotation(String cita, String autor) {
        this.cita = cita;
        this.autor = autor;
    }
    public Quotation(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCita() {
        return cita;
    }

    public void setCita(String cita) {
        this.cita = cita;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
