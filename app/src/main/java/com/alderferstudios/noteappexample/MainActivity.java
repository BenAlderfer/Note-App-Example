package com.alderferstudios.noteappexample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //the list of items
    private List<String> listItems;
    private ArrayAdapter<String> adapter;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    /**
     * Executes on app start
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        prefs = getPreferences(Context.MODE_PRIVATE);
        editor = prefs.edit();

        //read in saved list
        String savedList = prefs.getString("list", "");
        if (savedList.equals("")) { //nothing saved
            listItems = new ArrayList<>();
        } else {    //restore saved
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            listItems = new Gson().fromJson(savedList, type);
        }

        ListView list = (ListView) findViewById(R.id.list);

        // adapter to tie listItems to list
        adapter = new ArrayAdapter<>(this,
                R.layout.list_row, R.id.listText, listItems);

        // assign the list adapter
        list.setAdapter(adapter);
    }

    /**
     * Save list on app close
     */
    @Override
    public void onStop() {
        super.onStop();

        //turn list into json
        String items = new Gson().toJson(listItems);

        //save list to cache
        editor.putString("list", items);
        editor.apply(); //use apply, its faster than commit
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
