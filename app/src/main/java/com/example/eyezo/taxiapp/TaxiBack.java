package com.example.eyezo.taxiapp;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class TaxiBack extends Application
{
    public static final String APPLICATION_ID = "D721FEFD-B5BC-BBC9-FF78-4EA0543C8500";
    public static final String API_KEY = "553D3E19-D8D0-34E8-FF38-37CD287F8000";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static List<Person> persons;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl(SERVER_URL);
        Backendless.initApp(getApplicationContext(), APPLICATION_ID, API_KEY);
    }
}