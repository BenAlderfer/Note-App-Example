package com.alderferstudios.noteappexample

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    //the list of items
    private var listItems: MutableList<String>? = null
    private var adapter: ArrayAdapter<String>? = null

    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    /**
     * Executes on app start
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)

        prefs = getPreferences(Context.MODE_PRIVATE)
        editor = prefs!!.edit()

        //read in saved list
        val savedList = prefs!!.getString("list", "")
        if (savedList == "") { //nothing saved
            listItems = ArrayList<String>()
        } else {    //restore saved
            val type = object : TypeToken<ArrayList<String>>() {

            }.type
            listItems = Gson().fromJson<List<String>>(savedList, type) as MutableList<String>
        }

        val list = findViewById(R.id.list) as ListView

        // adapter to tie listItems to list
        adapter = ArrayAdapter(this,
                R.layout.list_row, R.id.listText, listItems!!)

        // assign the list adapter
        list.adapter = adapter
    }

    /**
     * Save list on app close
     */
    public override fun onStop() {
        super.onStop()

        //turn list into json
        val items = Gson().toJson(listItems)

        //save list to cache
        editor!!.putString("list", items)
        editor!!.apply() //use apply, its faster than commit
    }

    /**
     * Adds an item to the ListView
     * @param v the FAB
     */
    fun addItem(v: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Please enter the note to add")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, whichButton ->
            //add the input to the list
            listItems!!.add(input.text.toString())
            adapter!!.notifyDataSetChanged()
        }
        builder.setNegativeButton("CANCEL", null)

        val dialog = builder.create()
        dialog.show()

        //show the keyboard when the dialog appears
        try {
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        } catch (e: NullPointerException) {
            Log.e("failed to show keyboard", e.message)
        }

    }
}
