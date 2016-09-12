package com.murach.logbook;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextClock;



/**
 * Created by zacdenning on 4/14/16.
 */
public class EntryScreenActivity extends FragmentActivity implements OnClickListener {

    private EditText glucoseEditText, carbsEditText;
    private Button addEntryButton, cancelButton;
    private LogbookDB db;
    private boolean editMode;
    private Entry entry;
    private TextClock textClock;


    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_entry);


        //widget references

        textClock = (TextClock) findViewById(R.id.textClock);
        glucoseEditText = (EditText) findViewById(R.id.glucoseEditText);
        carbsEditText = (EditText) findViewById(R.id.carbsEditText);
        addEntryButton = (Button) findViewById(R.id.addEntryButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        addEntryButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        db = new LogbookDB(this);

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode", false);

        // if editing
        if (editMode) {
            // get entry
            long entryId = intent.getLongExtra("entryId", -1);
            entry = db.getEntry(entryId);

            // update UI with entry
            glucoseEditText.setText(entry.getGlucose());
            carbsEditText.setText(entry.getCarbs());

        }
    }

    private void saveToDB()  {
        //get data
        String glucose = glucoseEditText.getText().toString();
        String carbs = carbsEditText.getText().toString();
        String time = textClock.getText().toString();

        // if no data entered, exit method
        if  (glucose == null || glucose.equals("")
                && carbs == null || carbs.equals("")
                && time == null || time.equals("")
                ) {
            finish();
        }

        if (!editMode) {
            entry = new Entry();
        }

        entry.setGlucose(glucose);
        entry.setCarbs(carbs);
        entry.setTime(time);


        if (editMode) {
            db.updateEntry(entry);
        }
        else {
            db.insertEntry(entry);
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addEntryButton:
                saveToDB();
                this.finish();
                break;
            case R.id.cancelButton:
                this.finish();
                break;
        }
    }
}
