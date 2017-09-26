package pt.ipg.seminariocamara;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    String classes[] = {"TakePhoto", "GetBarCode", "C3", "C4"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setListAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, classes));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Class myClass = null;
        try {
            myClass = Class.forName("pt.ipg.seminariocamara." + classes[position]);
            Intent i = new Intent(MainActivity.this, myClass);
            startActivity(i);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(MainActivity.this, myClass);
    }
}