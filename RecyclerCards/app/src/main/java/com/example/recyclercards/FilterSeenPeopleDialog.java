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

public class FilterSeenPeopleDialog extends DialogFragment {

    private TextView textView_seenPeople;
    private SharedPreferences sharedPref;

    private boolean[] previousChecked;
    private String[] seenPeople;
    private ArrayList<Integer>  checkedItems;
    private ArrayList<String> filter_seenPeople;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Display
        textView_seenPeople = (TextView) getActivity().findViewById(R.id.textView_seenPeople);
        //All choices array
        seenPeople = getResources().getStringArray(R.array.seenPeople);
        //Checkboxes previously checked
        previousChecked = new boolean[seenPeople.length];
        //Checked items when hitting "CONFIRMAR"
        checkedItems = new ArrayList<>();
        //Choices chosen by user
        filter_seenPeople = new ArrayList<>();

        //Get previous choices
        final Context context = getActivity();
        sharedPref = context.getSharedPreferences("fsp", Context.MODE_PRIVATE);
        for (int i = 0; i < previousChecked.length; i++) {
            previousChecked[i] = sharedPref.getBoolean(""+i,false);
            if (previousChecked[i] == true) {
                checkedItems.add(i);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pessoas ultimamente vistas").setMultiChoiceItems(R.array.seenPeople, previousChecked,
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
                            for (int i = 0; i < seenPeople.length; i++) {
                                checkedItems.add(i);
                            }
                            Toast.makeText(context, "Não é permitido escolher 0 opções.", Toast.LENGTH_SHORT).show();
                        }

                        SharedPreferences.Editor editor = sharedPref.edit();
                        //Fill arrays needed
                        for (int i = 0; i < previousChecked.length; i++) {
                            if (checkedItems.contains(i)) {
                                previousChecked[i] = true;
                                filter_seenPeople.add(seenPeople[i]);
                            } else {
                                previousChecked[i] = false;
                            }
                            //Save choices
                            editor.putBoolean(""+i, previousChecked[i]);
                        }
                        editor.commit();

                        //Write in textview
                        if (filter_seenPeople.size() == seenPeople.length) {
                            textView_seenPeople.setText("Tudo");
                        } else {
                            textView_seenPeople.setText("");
                            for (int i = 0; i < filter_seenPeople.size(); i++) {
                                if (i != 0) {
                                    textView_seenPeople.append(", ");
                                }
                                textView_seenPeople.append(filter_seenPeople.get(i));
                            }
                        }
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FilterSeenPeopleDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}