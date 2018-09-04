package com.example.tim.redditfeed.Account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.tim.redditfeed.R;

public class LogIn extends AppCompatActivity{

    private static final String TAG = "LogIn";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private EditText mName, mPassword;
    private Button btnLogin;
    private CheckBox mCheckbox;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        mName = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnSave);
        mCheckbox = (CheckBox) findViewById(R.id.check);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        checkPref();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckbox.isChecked()){
                    mEditor.putString(getString(R.string.checkbox), "True");
                    mEditor.commit();

                    String name = mName.getText().toString();
                    mEditor.putString(getString(R.string.password), name);
                    mEditor.commit();

                    String password = mName.getText().toString();
                    mEditor.putString(getString(R.string.password), password);
                    mEditor.commit();
                }else{
                    mEditor.putString(getString(R.string.checkbox), "Flase");
                    mEditor.commit();


                    mEditor.putString(getString(R.string.password), "");
                    mEditor.commit();

                    mEditor.putString(getString(R.string.password), "");
                    mEditor.commit();
                }
            }
        });


    }
    private  void checkPref(){
        String checkbox = mPreferences.getString(getString(R.string.checkbox),  "False");
        String name = mPreferences.getString(getString(R.string.name),  "");
        String password = mPreferences.getString(getString(R.string.password),  "");

        mName.setText(name);
        mPassword.setText(password);


        if(checkbox.equals("True")){
            mCheckbox.setChecked(true);
        }else{
            mCheckbox.setChecked(false);
        }
    }
}
