package pt.ipg.android.camapp2016;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//public class MainActivity extends AppCompatActivity
public class MainActivity extends ListActivity {

    // Strings to became the items of the ListActivity.
    String classes[] = {"GetBarCode", "TakePhoto", "C3", "C4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);

        // Configures the ListAdapter that contains the list items of the ListActivity.
        setListAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, classes));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try {

            // Create a Class object to a java class of the project.
            Class myClass = Class.forName("pt.ipg.android.camapp2016." + classes[position]);

            // Intent to start the activity defined by a Class object.
            Intent myIntent = new Intent(MainActivity.this, myClass);
            startActivity(myIntent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
