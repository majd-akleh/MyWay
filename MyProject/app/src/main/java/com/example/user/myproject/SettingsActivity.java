package com.example.user.myproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;


public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();


        final ParseUser user = ParseUser.getCurrentUser();

        ( findViewById( R.id.popup_menu_button1 ) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, Edit_Profile_Activity.class));
            }
        });

        ( findViewById(R.id.popup_menu_button2) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.logOut();
                startActivity(new Intent(SettingsActivity.this, Login_Activity.class));
            }
        });

    }

    public void goBack2(View view) {
        startActivity(new Intent(SettingsActivity.this, Profile_Activity.class).putExtra( "user" , ParseUser.getCurrentUser().getUsername() ));
    }
}
