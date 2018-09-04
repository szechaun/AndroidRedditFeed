package com.example.tim.redditfeed.Account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginCheck {

    @SerializedName("json")
    @Expose
    private Json json;

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "LoginCheck{" +
                "json=" + json +
                '}';
    }
}
