package pt.ipg;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

/**
 * Created by RT on 11/11/2016.
 */

public abstract class Tabela <T> {

    protected SQLiteDatabase db;

    public Tabela(SQLiteDatabase db) {
        this.db = db;
    }

    public abstract void Cria();

    @NonNull
    protected abstract ContentValues getContentValues(T objeto);

    protected abstract String getNomeTabela();
    protected abstract String getChave();
    protected abstract String getStringId(T objeto);

    public long Insere(T objeto) {
        return db.insert(getNomeTabela(), null, getContentValues(objeto));
    }

    public boolean Altera(T objeto) {
        return db.update(getNomeTabela(), getContentValues(objeto), getChave() + "= ?", new String[] { getStringId(objeto) }) == 1;
    }

    public boolean Elimina(T objeto) {
        return db.delete(getNomeTabela(), getChave() + "=?", new String[] { getStringId(objeto) }) == 1;
    }

    public abstract Cursor getDados();
}