package com.example.tim.redditfeed.Comment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tim.redditfeed.API;
import com.example.tim.redditfeed.Account.LogIn;
import com.example.tim.redditfeed.ExtractContent;
import com.example.tim.redditfeed.MainActivity;
import com.example.tim.redditfeed.R;
import com.example.tim.redditfeed.tag.Entry;
import com.example.tim.redditfeed.tag.Feed;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class InPostActivity extends AppCompatActivity{
    private static final String TAG = "InPostActivity";
    private int defaultImage;
    private static String postURL;
    private static String postAuthor;
    private static String postTitle;
    private static String postThumbnail;
    private static final String DEFAULT_URL= "https://www.reddit.com/r/";

    private String backURL;

    private ArrayList<Comment> postComments;

    private ListView mListview;
    private ProgressBar  loadCommments;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_post);
        Log.d(TAG, "onCreate: started");

        setupImageLoader();
//
//        setToolbar();

        Intent incomingIntent = getIntent();
        postURL = incomingIntent.getStringExtra("post_url");
        postAuthor = incomingIntent.getStringExtra("post_author");
        postTitle = incomingIntent.getStringExtra("post_title");
        postThumbnail = incomingIntent.getStringExtra("post_thumbnail");

        TextView author = (TextView)  findViewById(R.id.postAuthor);
        TextView title = (TextView)  findViewById(R.id.postTitle);
        ImageView thumbnail = (ImageView) findViewById(R.id.postImage);

        author.setText(postAuthor);
        title.setText(postTitle);

//        loadCommments = (ProgressBar)  findViewById(R.id.commentProgressBar);
//        loadCommments.setVisibility(View.VISIBLE);


        image(postThumbnail, thumbnail);

        //some post may cause error
        try{
            String[] splitURL = postURL.split(DEFAULT_URL);
            backURL = splitURL[1];
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "onCreate: arrayindex outabounds" + e.getMessage());

        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DEFAULT_URL).addConverterFactory(SimpleXmlConverterFactory.createNonStrict()).build();
        API feedAPI = retrofit.create(API.class);
        Call<Feed> call = feedAPI.getFeed(backURL);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.d(TAG, "onResponse: server response" + response.toString());

                postComments = new ArrayList<Comment>();
                List<Entry> entrys = response.body().getEntrys();
                for (int i = 0; i < entrys.size(); i++){
                    ExtractContent extract = new ExtractContent(entrys.get(i).getContent(), "<div class=\"md\"><p>", "</p>");
                    List<String> commentDetail = extract.start();

                    try {
                        postComments.add(new Comment(
                                commentDetail.get(0),
                                entrys.get(i).getAuthor().getName(),
//                                entrys.get(i).getUpdated(),
                                entrys.get(i).getId()

                        ));

                    }catch (IndexOutOfBoundsException e){
                        Log.e(TAG, "onResponse: index outta bounds" + e.getMessage() );

                    }catch (NullPointerException e){
                        Log.e(TAG, "onResponse: nullpointerexceptio9n" + e.getMessage() );
                    }




                }
                mListview = (ListView) findViewById(R.id.commentListView);
                CommentAdapter adapter = new CommentAdapter(InPostActivity.this, R.layout.comments_layout, postComments);
                mListview.setAdapter(adapter);
//                loadCommments.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

            }
        });



    }
//    private void setToolbar(){
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Log.d(TAG, "onMenuItemClick:  clicked menu item:" + item);
//
//
//                switch (item.getItemId()){
//                    case R.id.navLogin:
//                        Intent intent = new Intent(InPostActivity.this, LogIn.class);
//                        startActivity(intent);
//                }
//                return false;
//            }
//        });
//    }

    private void image(String imageURl, ImageView imageView){

        //create the imageloader object
        ImageLoader imageLoader = ImageLoader.getInstance();

        int defaultImage = InPostActivity.this.getResources().getIdentifier("@drawable/image_failed",null,InPostActivity.this.getPackageName());

        //create display options
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        //download and display image from url
        imageLoader.displayImage(imageURl, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {


            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {


            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {


            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
//                holder.progressBar.setVisibility(View.GONE);

            }

        });

    }

    //Required for setting up the Universal Image loader Library
    private void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                InPostActivity.this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
        defaultImage = InPostActivity.this.getResources().getIdentifier("@drawable/image_failed",null,InPostActivity.this.getPackageName());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        return true;
    }
}
