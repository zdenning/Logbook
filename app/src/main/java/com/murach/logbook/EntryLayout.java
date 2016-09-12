package com.murach.logbook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;



/**
 * Created by zacdenning on 4/21/16.
 */
public class EntryLayout extends RelativeLayout implements OnClickListener {

    private TextView glucose, carbs, time, carbsLabel, mgdL;
    private LogbookDB db;
    private Context context;
    private Entry entry;

    public EntryLayout(Context context) { super(context); }

    public EntryLayout(Context context, Entry e) {
        super(context);

        this.context = context;
        db = new LogbookDB(context);

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.single_entry_layout, this, true);

        glucose = (TextView) findViewById(R.id.glucose);
        carbs = (TextView) findViewById(R.id.carbs);
        time = (TextView) findViewById(R.id.time);
        carbsLabel = (TextView) findViewById(R.id.carbsLabel);
        mgdL = (TextView) findViewById(R.id.mgdL);

        this.setOnClickListener(this);

        setEntry(e);
    }

    public void setEntry(Entry e) {
        entry = e;

            if (entry.getGlucose() == null || entry.getGlucose() == "") {
                glucose.setVisibility(GONE);
                mgdL.setVisibility(GONE);

            } else {
                glucose.setText(entry.getGlucose());
                if (Integer.valueOf(entry.getGlucose()) < 70 || Integer.valueOf(entry.getGlucose()) > 189) {
                    glucose.setTextColor(Color.rgb(190, 30, 30));
                } else if (Integer.valueOf(entry.getGlucose()) < 80 || Integer.valueOf(entry.getGlucose()) > 169) {
                    glucose.setTextColor(Color.rgb(235, 125, 0));
                } else {
                    glucose.setTextColor(Color.rgb(0, 139, 0));
                }
            }

            if (entry.getCarbs() == "") {
                carbs.setText("0");
                carbsLabel.setText("");
            } else {
                carbs.setText(entry.getCarbs());
            }

            if (entry.getTime() == null) {
                time.setVisibility(GONE);
            } else {
                time.setText(entry.getTime());
            }

            if (entry.getGlucose() == null &&
                    entry.getCarbs() == null &&
                    entry.getTime() == null) {
                this.setVisibility(GONE);
            }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_entry_layout:
                db.updateEntry(entry);
                break;
            default:
                Intent intent = new Intent(context, EntryScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("entryId", entry.getEntryID());
                intent.putExtra("editMode", true);
                context.startActivity(intent);
                break;
        }
    }
}
