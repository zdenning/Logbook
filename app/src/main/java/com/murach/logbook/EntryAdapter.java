package com.murach.logbook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by zacdenning on 4/21/16.
 */
public class EntryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Entry> entries;

    public EntryAdapter(Context context, ArrayList<Entry> entries) {
        this.context = context;
        this.entries = entries;
    }

    public int getCount() { return entries.size(); }

    public Object getItem(int position) { return entries.get(position); }

    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, ViewGroup parent) {
        EntryLayout entryLayout = null;
        Entry entry = entries.get(position);

        if (convertView == null) {
            entryLayout = new EntryLayout(context, entry);
        }
        else {
            entryLayout = (EntryLayout) convertView;
            entryLayout.setEntry(entry);
        }
        return entryLayout;
    }

}
