package pt.ipg.seminarioandroidbt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button bu_conn, bu_led1, bu_led2;
    private BluetoothAdapter bluetoothAdapter = null;
    private static final int REQUEST_CODE_ENABLE_BT = 1001;
    private boolean bt_connected = false;
    private static final int REQUEST_CODE_CONNECTION_BT = 1002;
    private String deviceMACAddress = null;
    private BluetoothDevice bluetoothDevice = null;
    private BluetoothSocket bluetoothSocket = null;
    private UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private MyBluetoothService.ConnectedThread connectedThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bu_conn = (Button) findViewById(R.id.button_conn);
        bu_led1 = (Button) findViewById(R.id.button_led1);
        bu_led2 = (Button) findViewById(R.id.button_led2);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "O dispositivo não possui Bluetooth.", Toast.LENGTH_LONG).show();
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_CODE_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "O Bluetooth foi ativado.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "O Bluetooth não foi ativado.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case REQUEST_CODE_CONNECTION_BT:
                if (resultCode == Activity.RESULT_OK) {
                    deviceMACAddress = data.getExtras().getString(DeviceList.deviceMACAddress);
                    Toast.makeText(getApplicationContext(), "Device MAC: " + deviceMACAddress, Toast.LENGTH_LONG).show();
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceMACAddress);
                    try {
                        bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(mUUID);
                        bluetoothSocket.connect();
                        bt_connected = true;
                        bu_conn.setText("Terminar ligação");

                        connectedThread = new MyBluetoothService.ConnectedThread(bluetoothSocket);
                        connectedThread.start();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro " + e, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Falha ao obter o MAC address.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void button_connOnClick(View v) {
        if (bt_connected) {
            try {
                bluetoothSocket.close();
                bt_connected = false;
                bu_conn.setText("Estabelecer ligação");
                Toast.makeText(getApplicationContext(), "Ligação terminada", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro " + e, Toast.LENGTH_LONG).show();
            }
        } else {
            Intent mIntent = new Intent(MainActivity.this, DeviceList.class);
            startActivityForResult(mIntent, REQUEST_CODE_CONNECTION_BT);
        }
    }

    //MANAGING A CONNECTION
    public class MyBluetoothService {
        private static final String TAG = "MY_APP_DEBUG_TAG";
        private Handler mHandler; // handler that gets info from Bluetooth service

        // Defines several constants used when transmitting messages between the
        // service and the UI.
        private interface MessageConstants {
            public static final int MESSAGE_READ = 0;
            public static final int MESSAGE_WRITE = 1;
            public static final int MESSAGE_TOAST = 2;

            // ... (Add other message types here as needed.)
        }

        private class ConnectedThread extends Thread {
            private final BluetoothSocket mmSocket;
            private final InputStream mmInStream;
            private final OutputStream mmOutStream;
            private byte[] mmBuffer; // mmBuffer store for the stream

            public ConnectedThread(BluetoothSocket socket) {
                mmSocket = socket;
                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                // Get the input and output streams; using temp objects because
                // member streams are final.
                try {
                    tmpIn = socket.getInputStream();
                } catch (IOException e) {
                    Log.e(TAG, "Error occurred when creating input stream", e);
                }
                try {
                    tmpOut = socket.getOutputStream();
                } catch (IOException e) {
                    Log.e(TAG, "Error occurred when creating output stream", e);
                }

                mmInStream = tmpIn;
                mmOutStream = tmpOut;
            }

            public void run() {
                mmBuffer = new byte[1024];
                int numBytes; // bytes returned from read()

                // Keep listening to the InputStream until an exception occurs.
                /*while (true) {
                    try {
                        // Read from the InputStream.
                        numBytes = mmInStream.read(mmBuffer);
                        // Send the obtained bytes to the UI activity.
                        Message readMsg = mHandler.obtainMessage(
                                MessageConstants.MESSAGE_READ, numBytes, -1,
                                mmBuffer);
                        readMsg.sendToTarget();
                    } catch (IOException e) {
                        Log.d(TAG, "Input stream was disconnected", e);
                        break;
                    }
                }
            }*/

            // Call this from the main activity to send data to the remote device.
            public void send(String dataToSend) {
                byte[] buffer = dataToSend.getBytes();
                try {
                    mmOutStream.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            /*public void send(byte[] bytes) {
                try {
                    mmOutStream.write(bytes);

                    // Share the sent message with the UI activity.
                    NotificationCompat.MessagingStyle.Message writtenMsg = mHandler.obtainMessage(
                            MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                    writtenMsg.sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "Error occurred when sending data", e);

                    // Send a failure message back to the activity.
                    NotificationCompat.MessagingStyle.Message writeErrorMsg =
                            mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                    Bundle bundle = new Bundle();
                    bundle.putString("toast",
                            "Couldn't send data to the other device");
                    writeErrorMsg.setData(bundle);
                    mHandler.sendMessage(writeErrorMsg);
                }
            }*/

            // Call this method from the main activity to shut down the connection.
            public void cancel() {
                try {
                    mmSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close the connect socket", e);
                }
            }
        }
    }
}