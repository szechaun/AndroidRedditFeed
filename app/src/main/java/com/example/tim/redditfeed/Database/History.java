package com.example.tim.redditfeed.Database;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tim.redditfeed.R;

import java.util.ArrayList;

public class History extends AppCompatActivity{
    private static final String TAG = "History";

    DatabaseHelper myDb;
//
//    private Button btnViewData;

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
//        btnViewData = (Button) findViewById(R.id.btnViewHist);

        mListView = (ListView) findViewById(R.id.historyListView);
        myDb = new DatabaseHelper(this);
//        btnViewData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(History.this, History.class);
//                startActivity(intent);
//            }
//        });


        
        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displayign data in teh ListView.");

        //get th data and append to a list
        Cursor data = myDb.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from teh database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);
        //set onitemclicklisternr to listgiew
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: you clicked on" + name);

                Cursor data = myDb.getItemID(name);
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: the ID is" + itemID);

                }else{
                    Toast.makeText(History.this, "no ID associated", Toast.LENGTH_SHORT).show();
                    Intent editIntent= new Intent(History.this, EditDataActivity.class);
                    editIntent.putExtra("id", itemID);
                    editIntent.putExtra("name", name);
                    startActivity(editIntent);
                }


            }
        });

    }
}
