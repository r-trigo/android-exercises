package com.example.recyclercards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FilterAgeDialog extends DialogFragment {

    private TextView textView_age;
    private SharedPreferences sharedPref;

    private boolean[] previousChecked;
    private String[] age;
    private ArrayList<Integer>  checkedItems;
    private ArrayList<String> filter_age;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Display
        textView_age = (TextView) getActivity().findViewById(R.id.textView_age);
        //All choices array
        age = getResources().getStringArray(R.array.age);
        //Checkboxes previously checked
        previousChecked = new boolean[age.length];
        //Checked items when hitting "CONFIRMAR"
        checkedItems = new ArrayList<>();
        //Choices chosen by user
        filter_age = new ArrayList<>();

        //Get previous choices
        final Context context = getActivity();
        sharedPref = context.getSharedPreferences("fa", Context.MODE_PRIVATE);
        for (int i = 0; i < previousChecked.length; i++) {
            previousChecked[i] = sharedPref.getBoolean(""+i,false);
            if (previousChecked[i] == true) {
                checkedItems.add(i);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pessoas ultimamente vistas").setMultiChoiceItems(R.array.age, previousChecked,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            checkedItems.add(which);
                        } else if (checkedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            checkedItems.remove(Integer.valueOf(which));
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        //If the user checks none of the boxes, check all boxes again
                        if (checkedItems.size() == 0) {
                            for (int i = 0; i < age.length; i++) {
                                checkedItems.add(i);
                            }
                            Toast.makeText(context, "Não é permitido escolher 0 opções.", Toast.LENGTH_SHORT).show();
                        }

                        SharedPreferences.Editor editor = sharedPref.edit();
                        //Fill arrays needed
                        for (int i = 0; i < previousChecked.length; i++) {
                            if (checkedItems.contains(i)) {
                                previousChecked[i] = true;
                                filter_age.add(age[i]);
                            } else {
                                previousChecked[i] = false;
                            }
                            //Save choices
                            editor.putBoolean(""+i, previousChecked[i]);
                        }
                        editor.commit();

                        //Write in textview
                        if (filter_age.size() == age.length) {
                            textView_age.setText("Tudo");
                        } else {
                            textView_age.setText("");
                            for (int i = 0; i < filter_age.size(); i++) {
                                if (i != 0) {
                                    textView_age.append(", ");
                                }
                                textView_age.append(filter_age.get(i));
                            }
                        }
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FilterAgeDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}