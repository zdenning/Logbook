package com.murach.logbook;

import android.database.Cursor;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;


import java.util.ArrayList;
import java.util.HashMap;



public class LogActivity extends AppCompatActivity implements OnItemLongClickListener {
    private LogbookDB db;
    private ListView itemsListView;
    private Entry entry;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        db = new LogbookDB(getApplicationContext());

        itemsListView = (ListView) findViewById(R.id.itemsListView);
        itemsListView.setOnItemLongClickListener(this);
        refreshLog();
    }

    public void refreshLog() {
        Context context = this;

        ArrayList<Entry> entries = db.getEntries();

        ArrayList<HashMap<String, String>> data =
                new ArrayList<>();
        for (Entry entry : entries) {

            HashMap<String, String> map = new HashMap<>();
            map.put("glucose", entry.getGlucose());
            map.put("carbs", entry.getCarbs());
            map.put("time", entry.getTime());
            data.add(map);
        }

        EntryAdapter adapter = new EntryAdapter(context, entries);
        itemsListView.setAdapter(adapter);
    }

    public void onResume() {
        super.onResume();
        refreshLog();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_log, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAdd:
                Intent intent = new Intent(this, EntryScreenActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //cursor.moveToPosition(position);
        final int entryId = cursor.getInt(position);
        new AlertDialog.Builder(this)
                        .setMessage("Delete entry?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // delete entry
                                entry = LogbookDB.getEntryFromCursor(cursor);
                                db.deleteEntry(entry);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();

        return false;
    }
}
