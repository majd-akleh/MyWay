package com.example.user.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import Classes.Information;
import Utilities.Verdict;
import Utilities.parseutilities.DBManager;


public class Login_Activity extends ActionBarActivity {

    Button sign_in_BTN;
    Button createBTN;
    String username;
    String password;
    DBManager manager;
    ProgressDialog pro ;
    ParseUser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

       init();

    }




    private void init() {
        getSupportActionBar().hide();
        createBTN = (Button) findViewById(R.id.button_sign_up);
        sign_in_BTN = (Button) findViewById(R.id.button_sign_in);
        manager = new DBManager(this);
        manager.initializeConnection();
        user = ParseUser.getCurrentUser() ;
        if( user != null ){
         startActivity( new Intent( this , Profile_Activity.class  ).putExtra( "user" , user.getUsername()) );
        }
    }

    public void Sign_in(View view) {

        pro = new ProgressDialog(this);
        username = ((EditText) findViewById(R.id.editText_user)).getText().toString().toLowerCase();
        password = ((EditText) findViewById(R.id.editText_pass)).getText().toString();
        pro.setMessage(" تسجيل الدخول, الرجاء الانتظار ");
        pro.setTitle( "الرجاء الانتظار" );
        if (username.equals("") || username == null || password.equals("") || password == null) {
            Toast.makeText( getApplicationContext(), "يوجد حقول فارغة ", Toast.LENGTH_LONG).show();
        } else {

           ParseException e =  manager.loginUser( new Information( username , password ) );

             if( e == null ){
                ParseUser user = ParseUser.getCurrentUser();
                 if( (boolean)user.get("emailVerified") )
                          startActivity( new Intent ( this , Profile_Activity.class ).putExtra( "user" , user.getUsername() ) );
                 else {
                     Toast.makeText( getApplicationContext() , "عذرا , لم يتم تأكيد الحساب حتى الان" , Toast.LENGTH_LONG ).show();
                 }
             } else {
               String cause = "";
                 switch ( e.getCode() ) {
                     case ParseException.CONNECTION_FAILED  :{
                              cause = " لايوجد اتصال بالانترنت ";
                         break;
                     }

                     default:{
                          cause = e.getMessage();
                         if( e.getMessage().equals( "invalid login parameters" )  ) {
                          if(  manager.existed( "User" , "username" , username ).isOk()  )  cause = " الرقم السري غير صحيح ";
                            else  cause = " هذا الحساب غير موجود ";
                         }
                     }

                 }
                 Toast.makeText( getApplicationContext() , cause , Toast.LENGTH_LONG ).show();

             }
            pro.dismiss();
        }

    }

    public void CreateAccount(View view) {
         startActivity( new Intent ( this , create_account_activity.class ) );
    }


    public void forget(View view) {
        startActivity( new Intent( Login_Activity.this, ForgetPassword.class ) );
    }
}
