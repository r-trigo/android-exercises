package pt.ipg.seminarios;

/**
 * Created by RT on 10/12/2016.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * A placeholder fragment containing a simple view.
 */
public class SeminariosFragment extends Fragment {

    public SeminariosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seminarios, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        SQLiteDatabase bd = AppSeminarios.getReadableDatabaseSeminarios(getActivity());
        Cursor cursorOradores = new TabelaOradores(bd).getDados();
        getActivity().startManagingCursor(cursorOradores);

        SimpleCursorAdapter adaptadorOradores = new SimpleCursorAdapter(getActivity(), R.layout.linha_seminario,
                cursorOradores, new String[] {TabelaOradores.NOME}, new int[] {R.id.textViewOrador}
        );

        Spinner spinnerOrador = (Spinner) getActivity().findViewById(R.id.spinnerOrador);
        spinnerOrador.setAdapter(adaptadorOradores);

        if (AppSeminarios.seminario != null) {
            EditText editTextTitulo = (EditText) getActivity().findViewById(R.id.editTextTitulo);
            EditText editTextResumo = (EditText) getActivity().findViewById(R.id.editTextResumo);

            editTextTitulo.setText(AppSeminarios.seminario.getTitulo());
            editTextResumo.setText(AppSeminarios.seminario.getSumario());

            int idOrador = AppSeminarios.seminario.getIdOrador();
            int numeroOradores = spinnerOrador.getCount();
            for (int i = 0; i < numeroOradores; i++) {
                int id = (int) spinnerOrador.getItemIdAtPosition(i);

                if (id == idOrador) {
                    spinnerOrador.setSelection(i);
                    break;
                }
            }
        }

        super.onActivityCreated(savedInstanceState);
    }
}