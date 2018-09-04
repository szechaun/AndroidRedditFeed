package com.example.tim.redditfeed.Account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tim.redditfeed.API;
import com.example.tim.redditfeed.R;
import com.example.tim.redditfeed.tag.Feed;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private ProgressBar mProgressBar;
    private EditText mUsername;
    private EditText mPassword;
    private static final String LOGIN_URL= "https://www.reddit.com/api/login/";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longin);
        mPassword = (EditText) findViewById(R.id.input_password);
        mUsername = (EditText)findViewById(R.id.input_username);
        mProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
        mProgressBar.setVisibility(View.GONE);
        Button buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attemp login");
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if(!username.equals("") && !password.equals("")){
                    mProgressBar.setVisibility(View.VISIBLE);
                    //signing in method
                    login(username, password);
                }
            }
        });
    }

    private void login(final String username, String password){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(LOGIN_URL).addConverterFactory(GsonConverterFactory.create()).build();
        API api = retrofit.create(API.class);


        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Call<LoginCheck> call = api.signIn(headerMap, username, username,password,"json");//call feed object

        call.enqueue(new Callback<LoginCheck>() {
            @Override
            public void onResponse(Call<LoginCheck> call, Response<LoginCheck> response) {

                Log.d(TAG, "onResponse:  server response:" + response.toString());
                String modhash = response.body().getJson().getData().getModhash();
                String cookie= response.body().getJson().getData().getCookie();
                Log.d(TAG, "onResponse: modhash: " + modhash);
                Log.d(TAG, "onResponse: cookie: " + cookie);

                try {
                    if(!modhash.equals("")){
                        saveModnCookie(username,modhash,cookie);
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "login Success", Toast.LENGTH_SHORT).show();
                        //redirect to previous activity
                        finish();
                    }
                }catch (NullPointerException e){
                    Log.e(TAG, "onResponse: Nullpointerexception" + e.getMessage() );
                }

            }

            @Override
            public void onFailure(Call<LoginCheck> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: feed:" + t.getMessage() );
                Toast.makeText(LoginActivity.this, "login error occured", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }
    private void saveModnCookie(String username, String modhash, String cookie){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        Log.d(TAG, "saveModnCookie: Storing mondhas n cookie "+ "\n" + username +
                "\n" + modhash +
                "\n" + cookie);

        editor.putString("@string/SessionUsername", username);
        editor.commit();
        editor.putString("@string/SessionModhash", modhash);
        editor.commit();
        editor.putString("@string/SessionCookie", cookie);
        editor.commit();


    }
}
