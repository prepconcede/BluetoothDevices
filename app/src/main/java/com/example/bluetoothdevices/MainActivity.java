package com.example.bluetoothdevices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = findViewById(R.id.bt_btn);
        final ListView list = findViewById(R.id.bt_devices);
        final ArrayList<String> devicesInformation = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devicesInformation);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clickedItem = (String) list.getItemAtPosition(i);
                Toast.makeText(MainActivity.this, clickedItem, Toast.LENGTH_LONG).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devicesInformation.clear();
                for (BluetoothDevice device : getBluetoothDevices()) {
                    devicesInformation.add(device.getName() + " | " + device.getAddress() + " | " + getDeviceType(device));
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }
    protected String getDeviceType(BluetoothDevice device) {
        int majorDeviceClass = device.getBluetoothClass().getMajorDeviceClass();
        switch (majorDeviceClass) {
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                return "audio/video";
            case BluetoothClass.Device.Major.COMPUTER:
                return "computer";
            case BluetoothClass.Device.Major.HEALTH:
                return "health";
            case BluetoothClass.Device.Major.PHONE:
                return "phone";
            case BluetoothClass.Device.Major.PERIPHERAL:
                return "peripheral";
            case BluetoothClass.Device.Major.WEARABLE:
                return "wearable";
            case BluetoothClass.Device.Major.TOY:
                return "toy";
            case BluetoothClass.Device.Major.IMAGING:
                return "imaging";
            case BluetoothClass.Device.Major.NETWORKING:
                return "networking";
            case BluetoothClass.Device.Major.UNCATEGORIZED:
                return "uncategorized";
            case BluetoothClass.Device.Major.MISC:
                return "miscellaneous";
            default:
                return "???";
        }
    }
    protected List<BluetoothDevice> getBluetoothDevices() {
        try {
            Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            List<BluetoothDevice> connectedDevices = new ArrayList<>();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (isConnected(device)) {
                        connectedDevices.add(device);
                    }
                }
            }
            return connectedDevices;
        } catch (Exception e) {
            Log.e("exception", "exception: ", e);
            throw new IllegalStateException("something is wrong with bluetooth adapter, try turning bluetooth on");
        }
    }

    public boolean isConnected(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("isConnected", (Class[]) null);
            return (boolean) m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}