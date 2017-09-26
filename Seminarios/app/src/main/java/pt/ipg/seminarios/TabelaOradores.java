package pt.ipg.seminarios;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import pt.ipg.Tabela;

/**
 * Created by RT on 11/11/2016.
 */
public class TabelaOradores extends Tabela<Orador>{

    public static final String TABELA = "oradores";
    public static final String CHAVE = "_id";
    public static final String NOME = "nome";
    public static final String CHAVE_COMPLETA = TABELA + "." + CHAVE;

    public TabelaOradores(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public void Cria() {
        db.execSQL("create table " + TABELA + " (" +
                CHAVE + " integer primary key autoincrement, " +
                NOME + " nvarchar(50) not null" +
                ");"
        );
    }

    @NonNull
    @Override
    protected ContentValues getContentValues(Orador orador) {
        ContentValues campos = new ContentValues();
        campos.put(NOME, orador.getNome());
        return campos;
    }

    @Override
    protected String getNomeTabela() {
        return TABELA;
    }

    @Override
    protected String getChave() {
        return CHAVE;
    }

    @Override
    protected String getStringId(Orador objeto) {
        return objeto.getStringId();
    }

    @Override
    public Cursor getDados() {
        String[] campos = { CHAVE, NOME };
        return db.query(TABELA, campos, null, null, null, null, null);
    }
}
