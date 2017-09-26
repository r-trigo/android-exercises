package pt.ipg.seminarios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import pt.ipg.seminarios.Seminario;

/**
 * Created by RT on 10/12/2016.
 */

public final class AppSeminarios {
    public static Seminario seminario = null;

    @NonNull
    private static BaseDadosSeminariosOpenHelper getOpenHelper(Context context) {
        return new BaseDadosSeminariosOpenHelper(context);
    }

    public static SQLiteDatabase getReadableDatabaseSeminarios(Context context) {
        return getOpenHelper(context).getReadableDatabase();
    }

    public static SQLiteDatabase getWritableDatabaseSeminarios(Context context) {
        return getOpenHelper(context).getWritableDatabase();
    }
}
