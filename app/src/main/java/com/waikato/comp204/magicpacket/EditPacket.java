package com.waikato.comp204.magicpacket;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditPacket extends AppCompatActivity {

    private Boolean save = false;
    private int position = -1;
    EditText NameEdit;
    EditText MACEdit;
    //Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_packet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if (intent.hasExtra("position")) {
            position = intent.getIntExtra("position", 1);
            Log.d("HelloGetPosition", String.valueOf(position));
        }
        NameEdit = (EditText) findViewById(R.id.editTextName);
        NameEdit.setText(intent.getStringExtra("Name"));
        final String ComputerName = NameEdit.getText().toString();
        NameEdit.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ComputerName.equals(NameEdit.getText().toString())) {
                    save = false;
                } else {
                    save = true;
                }
                return false;
            }
        });
        MACEdit = (EditText) findViewById(R.id.editTextMAC);
        MACEdit.setText(intent.getStringExtra("MAC"));
        final String MACAddress = MACEdit.getText().toString();
        MACEdit.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (MACAddress.equals(MACEdit.getText().toString())) {
                    save = false;
                } else {
                    save = true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            Log.d("Hello_Save", "I'm now Saved");
            returnIntent.putExtra("Name", NameEdit.getText().toString());
            returnIntent.putExtra("MAC", MACEdit.getText().toString());
            if (position != -1) {
                returnIntent.putExtra("position", position);
            }
            setResult(Activity.RESULT_OK, returnIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && save) {
            showTextDialog();
        } else if (keyCode == 4) {
            finish();
        }
        return true;
    }

    final Intent returnIntent = new Intent();

    private void showTextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to save your changes?");
        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Hello", String.valueOf("Saved"));
                returnIntent.putExtra("Name", NameEdit.getText().toString());
                returnIntent.putExtra("MAC", MACEdit.getText().toString());
                if (position != -1) {
                    returnIntent.putExtra("position", position);
                }
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Hello", String.valueOf("Unchanged"));
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        builder.show();
    }

}
