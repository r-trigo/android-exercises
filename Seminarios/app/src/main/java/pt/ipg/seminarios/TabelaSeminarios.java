package pt.ipg.seminarios;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

/**
 * Created by RT on 11/11/2016.
 */
public class TabelaSeminarios extends pt.ipg.Tabela<Seminario> {
    public static final String TABELA = "seminarios";
    public static final String CHAVE = "_id";
    public static final String TITULO = "titulo";
    public static final String ID_ORADOR = "id_orador";
    public static final String SUMARIO = "sumario";
    public static final String CHAVE_COMPLETA = TABELA + "." + CHAVE;

    public TabelaSeminarios(SQLiteDatabase db) {
       super(db);
    }

    @Override
    public void Cria() {
        String sql = "create table " + TABELA + "(" +
                CHAVE + " integer primary key autoincrement, " +
                TITULO + " nvarchar(50) not null, " +
                ID_ORADOR + " integer not null, " +
                SUMARIO + " nvarchar(255), " +
                "foreign key (" + ID_ORADOR + ") references " + TabelaOradores.TABELA + "(" + TabelaOradores.CHAVE + ")" +
                ");";

        db.execSQL(sql);
    }

    @NonNull
    @Override
    protected ContentValues getContentValues(Seminario objeto) {
        ContentValues campos = new ContentValues();
        campos.put(TITULO, objeto.getTitulo());
        campos.put(ID_ORADOR, objeto.getIdOrador());
        campos.put(SUMARIO, objeto.getSumario());
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
    protected String getStringId(Seminario objeto) {
        return objeto.getStringId();
    }

    @Override
    public Cursor getDados() {
        String sql = "select " + CHAVE_COMPLETA + ", " + TITULO +", " + ID_ORADOR + ", " + SUMARIO + ", " + TabelaOradores.NOME +
                " from " + TABELA + " inner join " + TabelaOradores.TABELA + " on " +
                ID_ORADOR + " = " + TabelaOradores.CHAVE_COMPLETA + ";";

        return db.rawQuery(sql, null);
    }

    public Seminario getSeminario(int id) {
        String[] campos = { CHAVE, TITULO, ID_ORADOR, SUMARIO };
        Cursor cursor = db.query(TABELA, campos, CHAVE + "=?", new String[]{ new Integer(id).toString() }, null, null, null, null);

        if (cursor.moveToNext()) {
            return new Seminario(id, cursor.getString(1), cursor.getInt(2), cursor.getString(3));
        } else {
            return null;
        }
    }

}