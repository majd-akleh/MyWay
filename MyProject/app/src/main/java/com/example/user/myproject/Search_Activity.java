package com.example.user.myproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.List;

import Classes.Information;
import Classes.Post;
import Classes.User;
import Utilities.parseutilities.DBManager;


public class Search_Activity extends ActionBarActivity {

   LinearLayout people , shares ;
   ParseUser user ;
   DBManager manager ;
    SearchView search ;
    boolean searhBY = false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);
        init();
    }

   private void init() {
       getSupportActionBar().hide();
       manager = new DBManager(this);
       manager.initializeConnection();
       user = ParseUser.getCurrentUser();
       if (user == null) {
           startActivity(new Intent(this, Login_Activity.class));
       }

       people = (LinearLayout) findViewById(R.id.peo);
       shares = (LinearLayout) findViewById(R.id.sha);
       people.setBackgroundColor(getResources().getColor(R.color.light_lime));
       people.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               searhBY = false;
               people.setBackgroundColor(getResources().getColor(R.color.light_lime));
               shares.setBackgroundColor(getResources().getColor(R.color.light_silver));

           }
       });

       shares.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               searhBY = true;
               shares.setBackgroundColor(getResources().getColor(R.color.light_lime));
               people.setBackgroundColor(getResources().getColor(R.color.light_silver));
           }
       });


       //--------------------------------------------------------------------------
       search = (SearchView) findViewById(R.id.search_bar);


       search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               addSearchResult(newText);
               return false;
           }
       });

   }


    private void  addSearchResult( String query ){

        List<ParseUser> users = manager.searchPeople(query);
        LinearLayout parent = (LinearLayout) findViewById(R.id.search_result);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
         inflater = LayoutInflater.from(this);

        parent.removeAllViews();
        for( ParseUser u : users) {

            final View v = inflater.inflate(R.layout.search_result_layout, null);
            parent.addView(v);
            final ParseUser U = u ;
            v.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        startActivity( new Intent(  Search_Activity.this , Profile_Activity.class  ).putExtra( "user" ,  U.getUsername()) );
                }
            });

            ((LinearLayout) v).getChildAt( 0 ).setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity( new Intent(  Search_Activity.this , Profile_Activity.class  ).putExtra( "user" ,  U.getUsername()) );
                }
            });

            ImageView line1 = (ImageView) ((LinearLayout) v).getChildAt( 0 );
                line1.setImageBitmap( manager.getUserPersonalImage(u) );

            LinearLayout line2 = (LinearLayout) ((LinearLayout) v).getChildAt( 1 );
            ((TextView)line2.getChildAt( 0 )).setText(  u.getUsername()  );
            if(u.containsKey( "city" ))
            ((TextView)line2.getChildAt( 1 )).setText(  (String)u.get("city")  );


        }

    }

    public void GoHome(View view) {
        startActivity( new Intent( Search_Activity.this , TimeLine_Activity.class ) );
    }

    public void GoNotifications(View view) {
        startActivity( new Intent( Search_Activity.this , Notification_activity.class ) );
    }

    public void GoProfile(View view) {
        startActivity( new Intent(  Search_Activity.this , Profile_Activity.class  ).putExtra( "user" , user.getUsername()) );
    }

   private List<User> getUserByLocation(  ){
        // TODO mars
       return null;
   }

    private List<User> getPostsByLocation(  ){
        // TODO mars
        return null;
    }

    public void settings(View view) {
        startActivity( new Intent( this , SettingsActivity.class ) );
    }
}
