package com.example.user.myproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.FileDescriptor;
import java.io.IOException;

import Classes.Information;
import Utilities.Validation;
import Utilities.Verdict;
import Utilities.parseutilities.DBManager;


public class Edit_Profile_Activity extends ActionBarActivity {

    ParseUser user;
    Intent intent;
    DBManager manager;
    RadioGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit__profile_);


        getSupportActionBar().hide();
        manager = new DBManager(this);
        manager.initializeConnection();
        user = ParseUser.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(Edit_Profile_Activity.this, Login_Activity.class));
        }
        group = (RadioGroup) findViewById(R.id.radio_grp);

        ((ImageView) findViewById(R.id.personalImage2)).setImageBitmap(manager.getPersonalImage());
        if (user.containsKey("name"))
            ((EditText) findViewById(R.id.just_name)).setHint((String) user.get("name"));
        ((TextView) findViewById(R.id.username_profile)).setText(user.getUsername());
        ((EditText) findViewById(R.id.email__)).setHint(user.getEmail());
        if (user.containsKey("description"))
            ((EditText) findViewById(R.id.description_text)).setHint((String) user.get("description"));


        if (user.containsKey("gender")) {
            if ((boolean) user.get("gender")) {
                group.check(R.id.female);
            } else {
                group.check(R.id.male);
            }
        }


    }


    public void GoHome(View view) {

        startActivity(new Intent(Edit_Profile_Activity.this, TimeLine_Activity.class));
    }

    public void GoPost(View view) {

        startActivity(new Intent(Edit_Profile_Activity.this, Post_Activity.class));

    }

    public void GoNotifications(View view) {

        startActivity(new Intent(Edit_Profile_Activity.this, Notification_activity.class));

    }

    public void GoProfile(View view) {

        startActivity( new Intent(  Edit_Profile_Activity.this , Profile_Activity.class  ).putExtra( "user" , user.getUsername()) );

    }

    public void SaveChanges(View view) {


        String newEmail = ((EditText) findViewById(R.id.email__)).getText().toString();
        // String newUsername =  ((EditText)findViewById( R.id.username_profile )).getText().toString();
        String newName = ((EditText) findViewById(R.id.just_name)).getText().toString();
        String newDesc = ((EditText) findViewById(R.id.description_text)).getText().toString();

        boolean right_email;


        if (!newEmail.equals("")) {
            right_email = Validation.validateEmail(newEmail);
            if (right_email) {
                  if( manager.existed("User" , "email" , newEmail).isOk() ){
                      Toast.makeText(getApplicationContext(), " هذا البريد الالكتروني موجود مسبقا " , Toast.LENGTH_LONG).show();
                      return;
                  }else {
                      manager.editCurrentUser("email", newEmail);
                  }
            } else {
                Toast.makeText(getApplicationContext(), " خطأ في كتابة البريد الالكتروني " , Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (!newDesc.equals("")) {
            manager.editCurrentUser("description", newDesc);
        }


        if (!newName.equals("")) {
            manager.editCurrentUser("name", newName);
        }
        ParseException e = null ;
        if(image != null)
        e =  manager.setPersonalImage( image );

        if( e == null ) {
            startActivity(new Intent( Edit_Profile_Activity.this, TimeLine_Activity.class));
        } else {
            switch(e.getCode()){
                case ParseException.OBJECT_TOO_LARGE :{

                    break;
                }
                case ParseException.CONNECTION_FAILED :{
                    Toast.makeText( getApplicationContext() , " عذرا هذا الملف كبير جدا "  , Toast.LENGTH_LONG ).show();
                    startActivity( new Intent( this , Login_Activity.class ) );
                }
            }
        }

        Toast.makeText(getApplicationContext(), "تم التعديل بنجاح ", Toast.LENGTH_LONG).show();
        startActivity( new Intent(  Edit_Profile_Activity.this , Profile_Activity.class  ).putExtra( "user" , user.getUsername()) );

    }


    public void Cancel(View view) {
        startActivity( new Intent(  Edit_Profile_Activity.this , Profile_Activity.class  ).putExtra( "user" , user.getUsername()) );
    }

    public void changePass(View view) {
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    Uri URIimage;
    final int PICK_FILE_RESULT_CODE = 1;
    final int PICK_IMAGE_CAMERA_CODE = 2;

    public void EditImage(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE_RESULT_CODE);

    }
    Bitmap image  = null ;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FILE_RESULT_CODE:{
                if (resultCode == RESULT_OK) {
                    URIimage = data.getData();
                    try {
                        image = getBitmapFromUri(URIimage);
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), " هذا الملف غير موجود ", Toast.LENGTH_LONG).show();
                    }

                    ((ImageView) findViewById(R.id.personalImage2)).setImageBitmap(image);

                }
                break;}


            case PICK_IMAGE_CAMERA_CODE:{
                if (resultCode == RESULT_OK) {
                    URIimage = data.getData();

                    try {
                        image = getBitmapFromUri(URIimage);
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), " هذا الملف غير موجود ", Toast.LENGTH_LONG).show();
                    }

                    ((ImageView) findViewById(R.id.personalImage2)).setImageBitmap(image);

                }
                break;}
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


}
