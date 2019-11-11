package com.example.smartlamp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    final UUID btUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket btSocket = null;
    boolean bulb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final ToggleButton tBtn = findViewById(R.id.tbtn);
        final CheckBox checkBox = findViewById(R.id.checkBox);
        final ImageView imgBulb = findViewById(R.id.imageView);

        new Thread(new Runnable() {
            @Override
            public void run() { //connetti
                try {
                    System.out.print("Collegamento 00:02:6B:00:AC:0D");
                    BluetoothDevice btDevice = btAdapter.getRemoteDevice("98:D3:31:F5:9E:E1");
                    btSocket = btDevice.createRfcommSocketToServiceRecord(btUUID);
                    btSocket.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        /*new Thread(new Runnable() {
            @Override
            public void run() { //ricevi byte
                while (!isDestroyed()) {
                    try {
                        byte[] buffer = new byte[1024];
                        int bytes = btSocket.getInputStream().read(buffer, 0, buffer.length);
                        String readMessage = new String(buffer, 0, bytes);
                        System.out.println(readMessage);

                        if(readMessage.equals("A") || readMessage.equals("ccendi")){
                            System.out.println("Accesa");
                            bulb = true;
                            imgBulb.setImageResource(R.mipmap.bulbon);
                        }else if(readMessage.equals("S") || readMessage.equals("spegni")){
                            bulb = false;
                            System.out.println("else");
                            imgBulb.setImageResource(R.mipmap.bulb);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
        tBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    boolean cbState = checkBox.isChecked();
                    if(cbState){
                        vib.vibrate(500);
                    }
                    imgBulb.setImageResource(R.mipmap.bulbon);
                    try {
                        String msg = ("O");
                        btSocket.getOutputStream().write(msg.getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    imgBulb.setImageResource(R.mipmap.bulb);
                    try {
                        String msg = ("F");
                        btSocket.getOutputStream().write(msg.getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
