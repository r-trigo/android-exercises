package pt.ipg.seminarioandroidbt;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by RT on 29/04/2017.
 */

public class DeviceList extends ListActivity {

    private BluetoothAdapter bluetoothAdapter = null;
    public static String deviceMACAddress = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> bt_device_data = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> paired_devices = bluetoothAdapter.getBondedDevices();

        if (paired_devices.size() > 0) {
            for (BluetoothDevice device : paired_devices) {
                String deviceName = device.getName();
                String deviceMAC = device.getAddress();
                bt_device_data.add(deviceName + "\n" + deviceMAC);
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String deviceInfo = ((TextView) v).getText().toString();
        //Toast.makeText(getApplicationContext(), "Device info: " + deviceInfo, Toast.LENGTH_LONG).show();

        String macAddress = deviceInfo.substring(deviceInfo.length()-17);
        Toast.makeText(getApplicationContext(), "MAC: " + macAddress, Toast.LENGTH_LONG).show();

        Intent getMACIntent = new Intent();
        getMACIntent.putExtra(deviceMACAddress, macAddress);

        setResult(RESULT_OK, getMACIntent);
    }
}
