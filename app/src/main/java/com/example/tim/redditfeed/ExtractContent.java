package com.example.tim.redditfeed;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ExtractContent {
    public static final String TAG = "extract content";

    private String tag;
    private String content;
    private String endtag;


    public ExtractContent(String content,String tag) {
        this.tag = tag;
        this.content = content;
        this.endtag = "NONE";
    }
    public ExtractContent(String content,String tag, String endtag) {
        this.tag = tag;
        this.content = content;
        this.endtag = endtag;
    }
    public List<String> start(){  // method to parse data
        List<String> result = new ArrayList<>();
        String[] splitContent = null;


        String marker = null;
        if(endtag.equals("NONE")){
            marker = "\"";
            splitContent = content.split(tag + marker);
        }else{
            marker = endtag;
            splitContent = content.split(tag);
        }
        int count = splitContent.length; // count item in array

        for(int i = 1; i < count; i++){ // interated through array to get information
            String getIndex = splitContent[i];
            int index = getIndex.indexOf(marker);// get position of "
            Log.d(TAG, "start: index: " + index);
            Log.d(TAG, "start: get index: " + getIndex);
            getIndex = getIndex.substring(0,index);
            Log.d(TAG, "start: extracted " + getIndex);
            result.add(getIndex); //append to result array
        }

        return result;

    }
}

