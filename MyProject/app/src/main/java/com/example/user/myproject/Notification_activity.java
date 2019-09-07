package com.example.user.myproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.parse.ParseUser;

import Classes.Information;
import Utilities.parseutilities.DBManager;


public class Notification_activity extends ActionBarActivity {

    DBManager manager ;
    ParseUser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_activity);
        init();
    }

    private void init(){
        getSupportActionBar().hide();
        manager = new DBManager( this );
        manager.initializeConnection();
        user = ParseUser.getCurrentUser();

    }

    public void GoHome(View view) {

        startActivity(new Intent(Notification_activity.this, TimeLine_Activity.class));
    }

    public void GoPost(View view) {

        startActivity( new Intent( Notification_activity.this , Post_Activity.class ) );

    }

    public void GoProfile(View view) {

        startActivity( new Intent( Notification_activity.this , Profile_Activity.class ).putExtra( "user" , user.getUsername() ) );

    }


    public void settings3(View view) {
        startActivity( new Intent( Notification_activity.this , SettingsActivity.class )   );
    }
}
