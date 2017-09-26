package pt.ipg.seminarios;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Snackbar snackbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabelaSeminarios tabelaSeminarios = getTabelaSeminarios();
        Cursor cursorSeminarios = tabelaSeminarios.getDados();
        startManagingCursor(cursorSeminarios);

        SimpleCursorAdapter adaptadorSeminarios = new SimpleCursorAdapter(this, R.layout.linha_seminario, cursorSeminarios,
                new String[]{TabelaSeminarios.TITULO, TabelaOradores.NOME},
                new int[]{R.id.textViewTitulo, R.id.textViewOrador}
        );

        ListView listViewSeminarios = getListView();
        listViewSeminarios.setAdapter(adaptadorSeminarios);

        listViewSeminarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MostraSeminario(id);
            }
        });
    }

    @NonNull
    private TabelaSeminarios getTabelaSeminarios() {
        SQLiteDatabase bd = AppSeminarios.getReadableDatabaseSeminarios(this);

        return new TabelaSeminarios(bd);
    }

    private void MostraSeminario(long id) {
        TabelaSeminarios tabelaSeminarios = getTabelaSeminarios();
        AppSeminarios.seminario = tabelaSeminarios.getSeminario((int) id);
        snackbar = Snackbar.make(getListView(), AppSeminarios.seminario.getSumario(), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        invalidateOptionsMenu();
    }

    private ListView getListView() {
        return (ListView) findViewById(R.id.listViewSeminarios);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem itemAlterar = menu.findItem(R.id.action_edit);
        MenuItem itemEliminar = menu.findItem(R.id.action_delete);
        if (AppSeminarios.seminario == null) {
            itemAlterar.setVisible(false);
            itemEliminar.setVisible(false);
        } else {
            itemAlterar.setVisible(true);
            itemEliminar.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_add) {
            AppSeminarios.seminario = null;
            getListView().setSelection(-1);
            if (snackbar != null) snackbar.dismiss();
            Intent intent = new Intent(this, AdicionarActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(this, EditarActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_delete) {
            ApagarSeminario(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void ApagarSeminario(final Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Tem a certeza que pretende eliminar este seminario?");

        alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    SQLiteDatabase bd = AppSeminarios.getWritableDatabaseSeminarios(context);

                    if (new TabelaSeminarios(bd).Elimina(AppSeminarios.seminario)) {
                        Toast.makeText(context, "Seminário eliminado com sucesso.", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
            } catch (Resources.NotFoundException e) {
            }
            Toast.makeText(context, "Não foi possível eliminar o seminário.", Toast.LENGTH_LONG).show();
            }
        });

        alertDialogBuilder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialogBuilder.show();
    }
}
