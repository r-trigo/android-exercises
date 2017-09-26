package pt.ipg.seminarios;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static android.R.attr.id;

public class EditarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            FragmentManager gestorFragmentos = getSupportFragmentManager();
            FragmentTransaction transaction = gestorFragmentos.beginTransaction();

            transaction.add(R.id.content_editar, new SeminariosFragment());

            transaction.commit();
        }


    }

    public void onGuardar (View v) {
        try {
            Spinner spinnerOrador = (Spinner) findViewById(R.id.spinnerOrador);
            EditText editTextTitulo = (EditText) findViewById(R.id.editTextTitulo);
            EditText editTextResumo = (EditText) findViewById(R.id.editTextResumo);

            AppSeminarios.seminario.setTitulo(editTextTitulo.getText().toString());
            AppSeminarios.seminario.setSumario(editTextResumo.getText().toString());
            AppSeminarios.seminario.setIdOrador((int) spinnerOrador.getSelectedItemId());

            SQLiteDatabase bd = AppSeminarios.getWritableDatabaseSeminarios(this);

            if (new TabelaSeminarios(bd).Altera(AppSeminarios.seminario)) {
                Toast.makeText(this, R.string.seminario_alterado_sucesso, Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        } catch (Resources.NotFoundException e) {
        }
        Toast.makeText(this, R.string.seminario_alterar_erro, Toast.LENGTH_LONG).show();
    }

    public void onCancelar (View v) {
        finish();
    }

}
