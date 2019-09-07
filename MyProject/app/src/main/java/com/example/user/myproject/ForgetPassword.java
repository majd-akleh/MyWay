package com.example.user.myproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import Utilities.Validation;


public class ForgetPassword extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forget_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goBack2(View view) {
        startActivity( new Intent( ForgetPassword.this , Login_Activity.class ));
    }

    public void restore(View view) {

      String email = ((EditText)findViewById(R.id.restore_email )).getText().toString();

        if(!Validation.validateEmail( email )){
            Toast.makeText( getApplicationContext() , " خطأ في كتابة الايميل "  , Toast.LENGTH_LONG ).show();
            return;
        }

        ParseUser.requestPasswordResetInBackground(  email  ,
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                           Toast.makeText( getApplicationContext() , " تم ارسال طلب التعديل الى ايميلك بنجاح ! " ,  Toast.LENGTH_LONG ).show();
                           startActivity( new Intent( ForgetPassword.this , Login_Activity.class ) );
                        } else {

                            String cause = "";
                            switch ( e.getCode() ) {
                                case ParseException.CONNECTION_FAILED  :{
                                    cause = " لايوجد اتصال بالانترنت ";
                                    break;
                                }

                                case ParseException.EMAIL_NOT_FOUND  :{
                                    cause = " عذرا هذا الايميل غير موجود ";
                                    break;
                                }


                                case ParseException.OBJECT_NOT_FOUND  :{
                                    cause =   " هذا الحساب غير موجود " ;
                                    break;
                                }


                                default:{
                                    cause = e.getMessage();
                                }

                            }
                            Toast.makeText( getApplicationContext() , cause , Toast.LENGTH_LONG ).show();

                        }
                    }
                });

    }
}
