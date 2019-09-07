package com.example.user.myproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

import Classes.Post;
import Utilities.parseutilities.DBManager;


public class Post_Activity extends ActionBarActivity {

    String season;
    ParseUser user;
    DBManager manager;
    int season_id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        init();
    }


    private void init(){

        getSupportActionBar().hide();
        manager = new DBManager( this );
        manager.initializeConnection();
        user = ParseUser.getCurrentUser() ;

        season = "Summer";
        season_id = R.id.summer_id;
        (findViewById(season_id)).setAlpha(1f);
        (findViewById(R.id.autumn_id)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        season = "Autumn";
                        ( findViewById(R.id.autumn_id)).setAlpha(1f);
                        ( findViewById(season_id)).setAlpha(0.2f);
                        season_id = R.id.autumn_id;


                    }
                });

        (findViewById(R.id.winter_id)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        season = "Winter";
                        (findViewById(R.id.winter_id)).setAlpha(1f);
                        (findViewById(season_id)).setAlpha(0.2f);
                        season_id = R.id.winter_id;
                    }
                });

        (findViewById(R.id.spring_id)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        season = "Spring";
                        (findViewById(R.id.spring_id)).setAlpha(1f);
                        (findViewById(season_id)).setAlpha(0.2f);
                        season_id = R.id.spring_id;
                    }
                });

        (findViewById(R.id.summer_id)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        season = "Summer";
                        (findViewById(R.id.summer_id)).setAlpha(1f);
                        (findViewById(season_id)).setAlpha(0.2f);
                        season_id = R.id.summer_id;
                    }
                });
    }


    Bitmap image = null;
    public void SavePost(View view) {

    Post post;
    String desc =  ((EditText)findViewById(R.id.desc_id) ).getText().toString() ;
    String pr = ((EditText) findViewById(R.id.price_id)).getText().toString() ;

      float rating =   ((RatingBar)findViewById(R.id.rating)).getRating();


        if( desc.equals("") ){
            Toast.makeText( getApplicationContext() , "الرجاء إضافة وصف" , Toast.LENGTH_LONG ).show();
           return;
        }
        if( pr.equals("") ){
            Toast.makeText( getApplicationContext() ,  " الرجاء إضافة سعر"  , Toast.LENGTH_LONG ).show();
            return;
        }
        int price = 0 ;
        try{
             price = Integer.parseInt( pr );
        }catch(NumberFormatException e){
            Toast.makeText( getApplicationContext() , " خانة السعر يجب ان تحوي على ارقام فقط " , Toast.LENGTH_LONG ).show();
            return;
        }

        if( image == null ){
            Toast.makeText( getApplicationContext() ,  " الرجاء إضافة صورة " , Toast.LENGTH_LONG ).show();
            return;
        }


       post = new Post( manager.getNextID() ,  user.getUsername() , desc , image , season , rating , price , new Date()  );
       ParseException e = manager.addPost(post, user);
        if( e == null ) {
            if(image != null) manager.addImage(user , "pictures" , image);
            startActivity(new Intent(Post_Activity.this, TimeLine_Activity.class));
        } else {
             switch(e.getCode()){
                 case ParseException.OBJECT_TOO_LARGE :{
                    // Toast.makeText( getApplicationContext() , " عذرا هذا الملف كبير جدا "  , Toast.LENGTH_LONG ).show();
                     break;
                 }
                 case ParseException.CONNECTION_FAILED :{
                     Toast.makeText( getApplicationContext() , " عذرا هذا الملف كبير جدا "  , Toast.LENGTH_LONG ).show();
                     break;
                 }
             }
        }
    }


    Uri URIimage;
    final int PICK_FILE_RESULT_CODE = 1;

    public void ChooseImage(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE_RESULT_CODE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    URIimage = data.getData();


                    try {
                        image = getBitmapFromUri(URIimage);
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), " هذا الملف غير موجود ", Toast.LENGTH_LONG).show();
                    }

                    ((ImageView) findViewById(R.id.post_img)).setImageBitmap(image);

                }
                break;
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }




    public void Cancel(View view) {
        startActivity( new Intent(Post_Activity.this , TimeLine_Activity.class  ) );
    }


}
