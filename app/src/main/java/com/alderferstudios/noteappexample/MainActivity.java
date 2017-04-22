package com.alderferstudios.noteappexample;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //the list of items
    private List<String> listItems;
    private ListView list;
    private ArrayAdapter<String> adapter;

    /**
     * Executes on app start
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        listItems = new ArrayList<>();
        list = (ListView) findViewById(R.id.list);

        // initiate the listadapter
        adapter = new ArrayAdapter<>(this,
                R.layout.list_row, R.id.listText, listItems);

        // assign the list adapter
        list.setAdapter(adapter);
    }

    /**
     * Adds an item to the ListView
     * @param v the FAB
     */
    public void addItem(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter the note to add");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //add the input to the list
                listItems.add(input.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("CANCEL", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        //show the keyboard when the dialog appears
        try {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } catch (NullPointerException e) {
            Log.e("failed to show keyboard", e.getMessage());
        }
    }
}
