package com.example.user.myproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import Classes.Comment;
import Classes.Information;
import Classes.Post;
import Classes.Wish;
import Utilities.parseutilities.DBManager;


public class TimeLine_Activity extends ActionBarActivity {

    ParseUser user;
    DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line_);
        getSupportActionBar().hide();
        manager = new DBManager(this);
        manager.initializeConnection();
        user = ParseUser.getCurrentUser();

        AddPosts();

    }


    private void AddPosts() {

        List<Post> posts = manager.getTimeLine(user);
        final LinearLayout parent = (LinearLayout) findViewById(R.id.posts_id);
        /*
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        */
        final LayoutInflater inflater = LayoutInflater.from(TimeLine_Activity.this);


        for (Post p : posts) {

            final View v = inflater.inflate(R.layout.post_layout, null);
            v.setId(p.getID());
            parent.addView(v);
            //---------------------------------
            LinearLayout line1 = (LinearLayout) ((LinearLayout) v).getChildAt(0);
            ((ImageView) line1.getChildAt(0)).setImageBitmap(manager.getUserPersonalImage(manager.getParseUserByUserName(p.getUsername())));
            ((TextView) line1.getChildAt(1)).setText(p.getUsername());

            //---------------------------------
           final ImageView line2 = (ImageView) ((LinearLayout) v).getChildAt(1);
            if (p.getImage() != null)
                line2.setImageBitmap(p.getImage());

            line2.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bit = ((BitmapDrawable)line2.getDrawable()).getBitmap();
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bit, "Title", null);

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType( Uri.parse( path ) , "image/*");
                    startActivity(intent);
                }
            });

            //---------------------------------
            final LinearLayout line3;
                line3 = (LinearLayout) ((LinearLayout) v).getChildAt(2);
            ((TextView) line3.getChildAt(0)).setText(p.getLikes() + "");
            ((TextView) line3.getChildAt(2)).setText(p.getDislikes() + "");

            //---------------------------------

            final int id = p.getID();
            (line3.getChildAt(1)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editPostLikes(id, true);
                }
            });
            (line3.getChildAt(3)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editPostLikes(id, false);
                }
            });

            final  Bitmap imge = p.getImage() ;

            (line3.getChildAt(4)).setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   final  ProgressDialog progressDoalog ;
                    progressDoalog = new ProgressDialog(TimeLine_Activity.this);
                    progressDoalog.setMax(100);
                    progressDoalog.setMessage("Its loading....");
                    progressDoalog.setTitle("ProgressDialog bar example");
                    progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDoalog.show();

                   manager.addWish(new Wish(imge, ""));

                    progressDoalog.dismiss();
            Toast.makeText( getApplicationContext(), " تم اضافة الصورة الى الامنيات " , Toast.LENGTH_LONG).show();
                }
            });
            //---------------------------------


            TextView line4 , line5;

                line4 = (TextView) ((LinearLayout) v).getChildAt(3);
                line5 = (TextView) ((LinearLayout) v).getChildAt(4);


            line4.setText(p.getDescription());

            final Number rat = p.getRating();
            final String seas = p.getSeason();
            final int pri = p.getPrice();
            final String name = p.getUsername();

            line5.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            addDetails(id , rat , seas , pri , name);
                }
            });


           final LinearLayout line6 = ((LinearLayout) ( (LinearLayout) v ).getChildAt(5));
            final EditText e = ( (EditText) line6.getChildAt(1));
            line6.getChildAt( 0 ).setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = e.getText().toString();

                    if (!comment.equals("")) {
                        View com_lay = inflater.inflate(R.layout.comment_layout, null);
                        ((LinearLayout) line6.getParent()).addView(com_lay, 6);
                        e.setText("");

                        ((ImageView) ((LinearLayout) com_lay).getChildAt(0)).setImageBitmap(manager.getPersonalImage());
                        LinearLayout com = ((LinearLayout) ((LinearLayout) com_lay).getChildAt(1));

                        ((TextView) (com.getChildAt(0))).setText(name + "");
                        ((TextView) (com.getChildAt(1))).setText(comment);

                        manager.addComment(new Comment(user.getUsername(), comment, new Date()), user, ((LinearLayout) line6.getParent()).getId());

                    }
                }
            });



            for( Comment c : p.getComments() ){
                View com_lay = inflater.inflate(R.layout.comment_layout, null);
                ((LinearLayout)v).addView(com_lay , 6 );
                ((ImageView) ((LinearLayout) com_lay).getChildAt(0)).setImageBitmap(manager.getUserPersonalImage(manager.getParseUserByUserName(c.getCommenter())));
               LinearLayout com = ((LinearLayout) ((LinearLayout) com_lay).getChildAt(1));
                ((TextView)(com.getChildAt(0))).setText( p.getUsername()+"" );
                ((TextView)(com.getChildAt(1))).setText( c.getDescription() );
            }

        }
    }


    private void addDetails(int postID , Number rat , String seas , int pri , String name) {

       final LinearLayout parent = (LinearLayout) findViewById(postID);
       final LayoutInflater inflater = LayoutInflater.from(TimeLine_Activity.this);
       final View v = inflater.inflate(R.layout.details_layout, null);

        parent.removeViewAt( 4 );
        parent.addView(v, 4);

            ((RatingBar) (((LinearLayout) v).getChildAt(0))).setRating(rat.floatValue());
            ( (((LinearLayout) v).getChildAt(0))).setClickable(false);

            ImageView img = (ImageView) (((LinearLayout) v).getChildAt(1));

        switch( seas ){
            case "Autumn" :
          img.setImageDrawable( getResources().getDrawable( R.drawable.automn ) );
                break;
            case "Spring" :
                img.setImageDrawable( getResources().getDrawable( R.drawable.spring ) );
                break;
            case "Winter" :
                img.setImageDrawable( getResources().getDrawable( R.drawable.winter ) );
                break;
            case "Summer" :
                img.setImageDrawable( getResources().getDrawable( R.drawable.summer ) );
                break;

        }



        ((TextView) (((LinearLayout) v).getChildAt(2))).setText( name );

        LinearLayout PRICE = (LinearLayout) (((LinearLayout) v).getChildAt(3));

        ((TextView) (PRICE.getChildAt(1))).setText( pri+"" );

    }

    private void editPostLikes(int ID, boolean increment) {

        LinearLayout line = (LinearLayout) findViewById(ID);
        if( increment && manager.userLikedPost(user , ID) ){  /*  deleteLike()  */  return ;}
        if( !increment && manager.userDislikedPost(user , ID) ){  /*  deletedisLike()  */  return ; }

        if(  increment && manager.userDislikedPost(user , ID) ){
                /*  deletedisLike()  */

        }

        ParseObject post = manager.getPostObjectByID(ID);
        boolean dele = false ;
        if( post.get("image") == null )
            dele = true;

        int index;
        if (increment) {
            index = 0;
            manager.addLike( user , ID );
        } else {
            index = 2;
            manager.addDislike( user , ID );
        }

        TextView likes;
        if (dele) {
            likes = ((TextView) ((LinearLayout) line.getChildAt(1)).getChildAt(index));
        } else {
            LinearLayout d = ((LinearLayout) line.getChildAt(2));
            likes = (TextView) d.getChildAt(index);

        }

        likes.setText(Integer.parseInt(likes.getText().toString()) + 1 + "");


    }

    public void GoPost(View view) {
        startActivity(new Intent(TimeLine_Activity.this, Post_Activity.class));
    }

    public void GoNotifications(View view) {
        startActivity(new Intent(TimeLine_Activity.this, Notification_activity.class));
    }

    public void GoProfile(View view) {
        startActivity(new Intent(TimeLine_Activity.this, Profile_Activity.class).putExtra("user", user.getUsername()));
    }

    public void GoSearch(View view) {
        startActivity(new Intent(TimeLine_Activity.this, Search_Activity.class));
    }
}
