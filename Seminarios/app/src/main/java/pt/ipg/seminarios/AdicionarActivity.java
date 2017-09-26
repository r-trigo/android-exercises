package pt.ipg.seminarios;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AdicionarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onGuardar (View v) {
        try {
            Spinner spinnerOrador = (Spinner) findViewById(R.id.spinnerOrador);
            EditText editTextTitulo = (EditText) findViewById(R.id.editTextTitulo);
            EditText editTextResumo = (EditText) findViewById(R.id.editTextResumo);

            String titulo = editTextTitulo.getText().toString();
            String resumo = editTextResumo.getText().toString();
            int idOrador = (int) spinnerOrador.getSelectedItemId();

            Seminario seminario = new Seminario(titulo, idOrador, resumo);
            SQLiteDatabase bd = AppSeminarios.getWritableDatabaseSeminarios(this);
            long id = new TabelaSeminarios(bd).Insere(seminario);

            if (id > 0) {
                Toast.makeText(this, R.string.seminario_inserido_sucesso, Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        } catch (Resources.NotFoundException e) {
        }
        Toast.makeText(this, R.string.erro_inserir_seminario, Toast.LENGTH_LONG).show();
    }

    public void onCancelar (View v) {
        finish();
    }
}
