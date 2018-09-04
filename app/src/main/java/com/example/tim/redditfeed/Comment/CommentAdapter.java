package com.example.tim.redditfeed.Comment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tim.redditfeed.Comment.Comment;
import com.example.tim.redditfeed.R;

import java.util.ArrayList;


public class CommentAdapter extends ArrayAdapter<Comment> {

    private static final String TAG = "CustomListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

//hodls variabel in view
    private static class ViewHolder {
        TextView comment;
        TextView author;
        TextView date_updated;
        ProgressBar progressBar;

    }

//default constructor
    public CommentAdapter(Context context, int resource, ArrayList<Comment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        //get the persons information
        String title = getItem(position).getComment();

        String author = getItem(position).getAuthor();
        String date_updated = getItem(position).getDate();


        try{


            //create the view result for showing the animation
            final View result;

            //ViewHolder object
            final ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);
                holder= new ViewHolder();
                holder.comment = (TextView) convertView.findViewById(R.id.comment);
                holder.author = (TextView) convertView.findViewById(R.id.commentAuthor);
                holder.date_updated = (TextView) convertView.findViewById(R.id.commentDate);
                holder.progressBar = (ProgressBar) convertView.findViewById(R.id.commentProgressBar);


                result = convertView;

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
                result = convertView;
                holder.progressBar.setVisibility(View.VISIBLE);
            }


            lastPosition = position;

            holder.comment.setText(title);
            holder.author.setText(author);
            holder.date_updated.setText(date_updated);
            holder.progressBar.setVisibility(View.GONE);


            return convertView;
        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }

    }

}