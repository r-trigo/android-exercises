package pt.edp.trainingday.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import java.util.ArrayList;

import pt.edp.trainingday.R;

/**
 * Created by e348900 on 14-02-2017.
 */

public class CheckMateDialog extends DialogFragment {

    private boolean[] checkedItems;
    private ArrayList mSelectedItems;
    private SharedPreferences sharedpreferences;
    private TextView tv_escolhas;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        tv_escolhas = (TextView) getActivity().findViewById(R.id.textView_escolhas);
        mSelectedItems = new ArrayList();
        BuscarEscolhas();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Escolha as peças a assassinar")
                .setMultiChoiceItems(R.array.pecas, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems.add(which);
                        } else if (mSelectedItems.contains(which)) {
                            mSelectedItems.remove(Integer.valueOf(which));
                        }
                    }
                })
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_escolhas.setText("");
                        GuardarEscreverEscolhas();
                        if (tv_escolhas.getText().toString().isEmpty()) tv_escolhas.setText(getResources().getString(R.string.promptCheckMate));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    private void BuscarEscolhas() {
        sharedpreferences = getContext().getSharedPreferences("checkMateCheckedItems", 0);
        checkedItems = new boolean[getResources().getStringArray(R.array.pecas).length];

        for (int i = 0; i < checkedItems.length; i++) {
            checkedItems[i] = sharedpreferences.getBoolean("i=" + i, false);
        }
    }

    private void GuardarEscreverEscolhas() {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        for(int i=0;i<checkedItems.length;i++) {
            editor.putBoolean("i=" + i, checkedItems[i]);
            if (checkedItems[i]) {
                if (tv_escolhas.getText().toString().isEmpty() || tv_escolhas.getText().equals(getResources().getString(R.string.promptCheckMate))) {
                    tv_escolhas.setText(getResources().getStringArray(R.array.pecas)[i]);
                } else {
                    tv_escolhas.append(", " + getResources().getStringArray(R.array.pecas)[i]);
                }
            }
        }
        editor.commit();
    }
}