package com.waikato.comp204.magicpacket;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> myPacketList = new ArrayList<String>();
    ArrayList<String> myPacketMACList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getPreferences();
        try {
            Process p = Runtime.getRuntime().exec("su");
        } catch (Exception ex) {

        }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditPacket.class);
                startActivityForResult(intent, 2);
            }
        });
        ListView listview = (ListView) findViewById(R.id.magicPacketList);
        listview.setLongClickable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendPacket(position);
                Log.d("Hello_Send", "Sent");
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View arg1, int position, long id) {
                Log.i("HelloListView", " at position:" + position);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), EditPacket.class);
                intent.putExtra("Name", myPacketList.get(position));
                intent.putExtra("MAC", myPacketMACList.get(position));
                intent.putExtra("position", position);
                startActivityForResult(intent, 1);
                return true;
            }
        });
    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences("Packet.List", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putInt("Size", myPacketList.size());
        String index;
        for (int i = 0; i < myPacketList.size(); i++) {
            index = String.valueOf(i);
            prefEditor.putString("Name" + index, myPacketList.get(i));
            prefEditor.putString("MAC" + index, myPacketMACList.get(i));
        }
        prefEditor.apply();
    }

    private void getPreferences() {
        SharedPreferences settings = getSharedPreferences("Packet.List", MODE_PRIVATE);
        int size = settings.getInt("Size", 0);
        String index;
        for (int i = 0; i < size; i++) {
            index = String.valueOf(i);
            myPacketList.add(i, settings.getString("Name" + index, ""));
            myPacketMACList.add(i, settings.getString("MAC" + index, ""));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra("Name");
                String mac = data.getStringExtra("MAC");
                int position = data.getIntExtra("position", myPacketMACList.size());
                Log.d("Hello_Result", name);
                Log.d("Hello_Result", mac);
                Log.d("Hello_Result", String.valueOf(position));
                myPacketList.set(position, name);
                myPacketMACList.set(position, mac);
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra("Name");
                String mac = data.getStringExtra("MAC");
                Log.d("Hello_Result", name);
                Log.d("Hello_Result", mac);
                myPacketList.add(name);
                myPacketMACList.add(mac);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    protected void onDestroy() {
        Log.d("Hello", "I'm now Destroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d("Hello", "I'm now Paused");
        super.onPause();
        savePreferences();
    }

    private void updateList() {
        ListView lv = (ListView) findViewById(R.id.magicPacketList);
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myPacketList);
        lv.setAdapter(myArrayAdapter);
        lv.setTextFilterEnabled(false);
    }

    DatagramSocket socket;
    DatagramPacket packet;

    private void sendPacket(int position) {
        try {
            socket = new DatagramSocket();
            String message = "FFFFFFFFFFFFFF";
            String MAC = myPacketMACList.get(position);
            MAC = MAC.replace(":", "");
            for (int i = 0; i < 16; i++) {
                message += MAC;
            }
            byte[] buf = message.getBytes();
            packet = new DatagramPacket(buf, buf.length ,InetAddress.getByName("255.255.255.255"), 9);
            //packet = new DatagramPacket(buf, buf.length ,InetAddress.getByName("210.54.39.180"), 5660);
            socket.send(packet);
        } catch (Exception ex) {
                Log.d("Hello_Send", ex.getMessage() + "");
                Log.d("Hello_Send", ex.toString());
        }
    }

    private void createLists() {
        String[] temp = new String[]{
                "Alpha - 100",
                "Beta - 90",
                "Gamma - 80",
                "Delta - 70",
                "Epsilon - 60",
                "Zeta - 50",
                "Eta - 40",
                "Theta - 30",
                "Iota - 20",
                "Kappa - 10"
        };
        myPacketList = new ArrayList<String>(Arrays.asList(temp));
        myPacketMACList = new ArrayList<String>();
        Log.d("Hello", String.valueOf(myPacketMACList.size()));
    }
}
