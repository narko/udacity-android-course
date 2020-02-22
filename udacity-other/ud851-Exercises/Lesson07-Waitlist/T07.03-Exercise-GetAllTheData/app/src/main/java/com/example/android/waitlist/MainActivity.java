package com.example.android.waitlist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.waitlist.data.TestUtil;
import com.example.android.waitlist.data.WaitlistContract;
import com.example.android.waitlist.data.WaitlistDbHelper;


public class MainActivity extends AppCompatActivity {

    private GuestListAdapter mAdapter;

    // (1) Create a local field member of type SQLiteDatabase called mDb
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView waitlistRecyclerView;

        // Set local attributes to corresponding views
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // (2) Create a WaitlistDbHelper instance, pass "this" to the constructor as context
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);

        // (3) Get a writable database reference using getWritableDatabase and store it in mDb
        mDb = dbHelper.getWritableDatabase();

        // (4) call insertFakeData from TestUtil and pass the database reference mDb
        TestUtil.insertFakeData(mDb);

        // (7) Run the getAllGuests function and store the result in a Cursor variable
        Cursor cursor = getAllGuests();

        // Create an adapter for that cursor to display the data
        // (12) Pass the resulting cursor count to the adapter
        mAdapter = new GuestListAdapter(this, cursor.getCount());

        // Link the adapter to the RecyclerView
        waitlistRecyclerView.setAdapter(mAdapter);
    }

    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    public void addToWaitlist(View view) {

    }

    // (5) Create a private method called getAllGuests that returns a cursor
    // (6) Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
    private Cursor getAllGuests() {
        return mDb.query(WaitlistContract.WaitlistEntry.TABLE_NAME,
                null, null, null, null, null, WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP);
    }
}