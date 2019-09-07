package com.example.user.myproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import Classes.Wish;
import Utilities.parseutilities.DBManager;


public class Profile_Activity extends ActionBarActivity {

    int light_id = R.id.img_wish ;
    final int WISHES = 1;
    final int ALBUMS = 2;
    final int PICTURES = 3;
    ParseUser user , current;
    LinearLayout light;
    DBManager manager ;
    List<Bitmap> pictures , wishes ;
    LinearLayout pictures_parent ;
    LayoutInflater inflater ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_);

       init();

    }

     void init(){
        getSupportActionBar().hide();
        manager = new DBManager(this);
        manager.initializeConnection();

         String userName = (String)getIntent().getSerializableExtra( "user" );
         user = manager.getParseUserByUserName(userName);
         current = ParseUser.getCurrentUser();

       if(!user.getUsername().equals(current.getUsername()))
         if( !manager.checkFollowingRelation( current , user )   ) {
             (findViewById( R.id.add_button )).setVisibility(View.VISIBLE);
             (findViewById( R.id.add_button )).setClickable(true);
             (findViewById( R.id.profile1 )).setOnClickListener( new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     startActivity( new Intent( Profile_Activity.this , Profile_Activity.class ).putExtra( "user" ,  ParseUser.getCurrentUser().getUsername() ) );
                 }
             });
         } else {
             (findViewById( R.id.add_button )).setVisibility(View.VISIBLE);
             (findViewById( R.id.add_button )).setClickable(true);
             ( (ImageView) (findViewById( R.id.add_button ))).setImageDrawable(getResources().getDrawable(R.drawable.dis));
             (findViewById( R.id.profile1 )).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     startActivity(new Intent(Profile_Activity.this, Profile_Activity.class).putExtra("user", ParseUser.getCurrentUser().getUsername()));
                 }
             });
         }

         light = (LinearLayout) findViewById( R.id.img_wish );
         light.setBackgroundColor(getResources().getColor(R.color.light_lime));

        if( user == null ) startActivity( new Intent( Profile_Activity.this , Login_Activity.class  ));

        ((TextView) findViewById(R.id.username_profile)).setText(user.getUsername());

            if (user.containsKey("description"))
                ((TextView) findViewById(R.id.description)).setText(user.get("description").toString());

                ((ImageView) findViewById(R.id.personalImage)).setImageBitmap(manager.getUserPersonalImage(user));

           if( user.containsKey( "name" ) ){
               ((TextView)findViewById(R.id.name_profile)).setText( user.get("name").toString() );
           }
            ((TextView) findViewById(R.id.followings)).setText(manager.getUserFollowers(user).size() + "");
            ((TextView) findViewById(R.id.followers)).setText(manager.getUserFollowings(user).size() + "");
            ((TextView) findViewById(R.id.shares2)).setText(manager.getUserPosts(user , 100).size() + "");


            (findViewById(light_id)).setBackgroundColor(getResources().getColor(R.color.light_lime));

            // initialise  inflater
            pictures_parent = (LinearLayout) findViewById(R.id.pictures_);
            inflater = (LayoutInflater) getApplicationContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            inflater = LayoutInflater.from(this);

            // get all wishes from the database.
            wishes = manager.getAllUserWishes(user);
            // get all pictures from the database.
            pictures = manager.getAllUserBitmaps(user, "pictures");

         FillPictures(WISHES);

    }

    private void FillPictures( int number ) {

        List<Bitmap> pics = new ArrayList<>(0);
      switch (number){
          case WISHES : {
                pics = wishes;
              break;
          }
          case ALBUMS : {
              //TODO Albums
              //pics = albums;
              break;
          }
          case PICTURES : {
              pics = pictures;
              break;
          }
      }

        for(int i = 0 ; i < pics.size() ; i++){
            final View v = inflater.inflate(R.layout.pictures_layout, null);
            pictures_parent.addView(v);

            LinearLayout line1 = (LinearLayout) ((LinearLayout) v).getChildAt( 0 );
            ( (ImageView) line1.getChildAt( 0 )).setImageBitmap(  pics.get(i++)  );
            if(i < pics.size())
            ( (ImageView) line1.getChildAt( 1 )).setImageBitmap(  pics.get(i++)  );
            if(i < pics.size())
            ( (ImageView) line1.getChildAt( 2 )).setImageBitmap(  pics.get(i)  );
        }

    }


    public void ShowWishList(View view) {
        light.setBackgroundColor(getResources().getColor(R.color.light_silver));
        light = (LinearLayout) findViewById( R.id.img_wish );
        light.setBackgroundColor(getResources().getColor(R.color.light_lime));
        if( light_id != R.id.img_wish ) {
            pictures_parent.removeAllViews();
            FillPictures(  WISHES ) ;
            light_id = R.id.img_wish;
        }
    }

    public void ShowAlbums(View view) {
        light.setBackgroundColor(getResources().getColor(R.color.light_silver));
        light = (LinearLayout) findViewById( R.id.img_alb );
        light.setBackgroundColor(getResources().getColor(R.color.light_lime));

        //------TODO just for now-------
        pictures_parent.removeAllViews();

    }

    public void ShowPictures(View view) {
        light.setBackgroundColor(getResources().getColor(R.color.light_silver));
        light = (LinearLayout) findViewById( R.id.img_pic );
        light.setBackgroundColor(getResources().getColor(R.color.light_lime));

        if( light_id != R.id.img_pic ) {
            pictures_parent.removeAllViews();
            FillPictures(  PICTURES  ) ;
            light_id = R.id.img_pic;
        }
    }


    public void GoHome(View view) {
        startActivity( new Intent( Profile_Activity.this , TimeLine_Activity.class ) );
    }

    public void GoPost(View view) {
        startActivity( new Intent( Profile_Activity.this , Post_Activity.class ));
    }

    public void GoNotifications(View view) {
        startActivity( new Intent( Profile_Activity.this , Notification_activity.class ) );
    }

    public void settings2(View view) {
         startActivity( new Intent( this , SettingsActivity.class ) );
    }

    public void Follow(View view) {

       if( manager.checkFollowingRelation( ParseUser.getCurrentUser() , user) ){

           // TODO
         /* delete relation  */
           ((ImageView) (findViewById(R.id.add_button))).setImageDrawable(getResources().getDrawable(R.drawable.add_button));
           Toast.makeText(getApplicationContext(), user.getUsername() + " انت لم تعد تتبع ", Toast.LENGTH_LONG).show();


       } else {

           manager.addFollowing(user.getUsername());
           ((ImageView) (findViewById(R.id.add_button))).setImageDrawable(getResources().getDrawable(R.drawable.dis));
           Toast.makeText(getApplicationContext(), user.getUsername() + " انت الان تتبع ", Toast.LENGTH_LONG).show();
       }
    }
}

