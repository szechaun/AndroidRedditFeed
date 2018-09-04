package com.example.tim.redditfeed;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.tim.redditfeed.Account.LoginActivity;
import com.example.tim.redditfeed.Comment.InPostActivity;
import com.example.tim.redditfeed.Database.DatabaseHelper;
import com.example.tim.redditfeed.Database.History;
import com.example.tim.redditfeed.tag.Entry;
import com.example.tim.redditfeed.tag.Feed;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String DEFAULT_URL= "https://www.reddit.com/r/";
    private ImageButton btnSearch;
    private EditText mSearchBar;
    private String feedInput;
    private Button mLogButton;
    private Button mLocation;
    //create instance name of database
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //init button, editext and String
        btnSearch = (ImageButton) findViewById(R.id.buttonSearch);
        mSearchBar = (EditText) findViewById(R.id.searchBar);

        //run adddata
//        AddData();
        //run function
        startRetro();


        //create instance of database
        myDb = new DatabaseHelper(this);
        SQLiteDatabase myDatabase = myDb.getWritableDatabase();

        //take parameter in edtitext and submit to retrofit
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchName = mSearchBar.getText().toString();
                String newEntry = mSearchBar.getText().toString();


                if(!searchName.equals("")){
                    feedInput = searchName;
                    startRetro();
                }else{
                    startRetro();
                }
                if(mSearchBar.length() != 0){
                    AddData(newEntry);

                }else{
                    toastMsg("put somethign in teh text field!");
                }
            }
        });

        setToolbar();


    }



    public void AddData(String newEntry){
        boolean insertData = myDb.addData(newEntry);
//        if(insertData){
//            toastMsg("data insert suscess");
//        }else{
//            toastMsg("data insert NOT success");
//        }
    }
    private void toastMsg(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick:  clicked menu item:" + item);


                switch (item.getItemId()){
                    case R.id.navLogin:
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.naHistory:
                        Intent historyIntent = new Intent(MainActivity.this, History.class);
                        startActivity(historyIntent);


                }
                if(item.getItemId()== R.id.navLocation){
                    Intent locationIntent = new Intent(MainActivity.this, Location.class);
                    startActivity(locationIntent);

                }
                return false;
            }
        });
    }

    private void startRetro(){
        //declare and initialise Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DEFAULT_URL)
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())  //helper clas to transform to XML
                .build();

        API api = retrofit.create(API.class);



        Call<Feed> call = api.getFeed(feedInput);//call feed object




        //schedule the call to happen asynchronously and provide the callback to be executed upon completion
        call.enqueue(new Callback<com.example.tim.redditfeed.tag.Feed>() {
            @Override
            public void onResponse(Call<com.example.tim.redditfeed.tag.Feed> call, Response<com.example.tim.redditfeed.tag.Feed> response) {
                //Log.d(TAG, "onResponse: feed" + response.body().toString()); //show entry
                Log.d(TAG, "onResponse: server response" +response.toString());//show http server response
                List<Entry> entrys = response.body().getEntrys();


//                Log.d(TAG, "onResponse: entrys " + response.body().getEntrys());

                final ArrayList<ViewPost> viewPost = new ArrayList<ViewPost>();// arraylist to hold viewpost object

                for(int i =0; i< entrys.size(); i++) {  // iterate through entrys
                    ExtractContent extractContent1 = new ExtractContent(entrys.get(i).getContent(), "<a href=");//extract past <a href"
                    List<String> postContent = extractContent1.start();//create string list to save result

                    ExtractContent extractContent2 = new ExtractContent(entrys.get(i).getContent(), "<img src=");// extract past <img src"
                    //there is only one img src tag so we only need to worry about the 0 index
                    try{
                        postContent.add(extractContent2.start().get(0));
                    }catch(NullPointerException e){//if object doesn't exist
                        postContent.add(null);
                        Log.e(TAG, "onResponse: Nullpointer: " + e.getMessage());
                    }
                    catch(IndexOutOfBoundsException e){// error if tag doesn't exist at all
                        postContent.add(null);
                        Log.e(TAG, "onResponse: IndexOuttaBounds: " + e.getMessage());
                    }
                    int lastIndex = postContent.size()-1;
                    viewPost.add(new ViewPost(entrys.get(i).getTitle(),entrys.get(i).getAuthor().getName(),
                            entrys.get(i).getUpdated(), postContent.get(0),postContent.get(lastIndex)
                    ));

                }
//                for(int i = 0; i < viewPost.size(); i ++){
//                    Log.d(TAG, "onResponse: \n" +
//                            "posturl: " + viewPost.get(i).getPostURL() + "\n" +
//                            "Author: " + viewPost.get(i).getAuthor() + "\n" +
//                            "Title: " + viewPost.get(i).getTitle() + "\n" +
//                            "posturl: " + viewPost.get(i).getPostURL() + "\n" );
//                }
                ListView listView = (ListView) findViewById(R.id.listView);
                MyAdapter myAdapter = new MyAdapter(MainActivity. this, R.layout.cardview_layout, viewPost);
                listView.setAdapter(myAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, InPostActivity.class);
                        intent.putExtra("post_url", viewPost.get(position).getPostURL());
                        intent.putExtra("post_author", viewPost.get(position).getAuthor());
                        intent.putExtra("post_title", viewPost.get(position).getTitle());
                        intent.putExtra("post_thumbnail", viewPost.get(position).getThumbnail_URL());
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onFailure(Call<com.example.tim.redditfeed.tag.Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: cant retrieve RSS" + t.getMessage());// show error msg
                Toast.makeText(MainActivity.this, "unable to retrieve RSS", Toast.LENGTH_SHORT).show();// show error toast

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        return true;
    }

//    @Override
//    public void onClick(View v) {
//        if(v.getId() == R.id.logInBtn){
//            startActivity(new Intent(MainActivity.this, LogIn.class));
//        }
//    }

}
