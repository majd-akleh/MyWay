package com.example.user.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

import Utilities.Validation;
import Utilities.parseutilities.DBManager;


public class ChangePasswordActivity extends ActionBarActivity {

    ParseUser user ;
    DBManager manager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        getSupportActionBar().hide();
        manager = new DBManager(this);
        manager.initializeConnection();
        final ProgressDialog pro = new ProgressDialog(getApplicationContext());
        pro.setMessage( "يتم التغيير الرجاء الانتظار" );
        pro.setTitle("يتم التغيير");
        user = ParseUser.getCurrentUser() ;
        if(user == null) startActivity(new Intent( this , Login_Activity.class ));

        findViewById( R.id.change_pass2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //  pro.show();
                 String newPass = ((EditText)findViewById(R.id.newPass)).getText().toString();
                 String conNewPass = ((EditText)findViewById(R.id.conNewPass)).getText().toString();


                  if( !Validation.validatePassword(newPass)  ){
                      Toast.makeText( getApplicationContext() , " كلمة المرور الجديدة قصيرة "  , Toast.LENGTH_LONG ).show();
                      return;
                  }
                    if(   !newPass.equals(conNewPass)  ){
                        Toast.makeText( getApplicationContext() , " كلمتا المرور الجديدتان غير متطابقتين " , Toast.LENGTH_LONG ).show();
                        return;
                      }


                manager.editCurrentUser( "password" , newPass );
                Toast.makeText( getApplicationContext() , "تم تغيير كلمة المرور بنجاح!" , Toast.LENGTH_LONG ).show();
                startActivity( new Intent( ChangePasswordActivity.this  ,Edit_Profile_Activity.class ) );
                pro.dismiss();

            }
        });

    }

    public void cancel(View view) {
        startActivity( new Intent( ChangePasswordActivity.this , Edit_Profile_Activity.class ) );
    }

}
