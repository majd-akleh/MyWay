package com.example.user.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import Classes.Information;
import Utilities.Validation;
import Utilities.Verdict;
import Utilities.parseutilities.DBManager;


public class create_account_activity extends ActionBarActivity {

    String email;
    String username;
    String pass;
    String confirm;
    Button createBTN;
    RadioGroup radio_grp;
    RadioButton radio_btn;
    boolean gender = true;
    DBManager manager;
    Information info;
    ProgressDialog pro;
    Verdict valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        pro = new ProgressDialog(this);
        manager = new DBManager(this);
        manager.initializeConnection();
        getSupportActionBar().hide();
        (findViewById(R.id.editText_pass2_)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                   Toast.makeText( getApplicationContext() , "كلمة المرور يجب ان تكون حروف وارقام و اكثر من 8 حروف" ,Toast.LENGTH_LONG).show();
            }
        });
        createBTN = (Button) findViewById(R.id.button_sign_up2);
        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // pro.setMessage( "الرجاء الانتظار" );
               // pro.setTitle( "تسجيل الدخول " );
              //  pro.setIndeterminate(true);

               // pro.show();
                Validation.WaitTask();

                email = ((EditText) findViewById(R.id.editText_email2_)).getText().toString().toLowerCase();
                username = ((EditText) findViewById(R.id.editText_username_)).getText().toString().toLowerCase();
                pass = ((EditText) findViewById(R.id.editText_pass2_)).getText().toString();
                confirm = ((EditText) findViewById(R.id.editText_confirm_)).getText().toString();
                radio_grp = (RadioGroup) findViewById(R.id.radio_grp);
                int rad_id = radio_grp.getCheckedRadioButtonId();
                if (rad_id == R.id.female) gender = true;
                else gender = false;


                info = new Information(username, pass, email, gender);

                valid = info.lexicalValidation();

                int status = valid.getStatus();
                if (!valid.isOk() || !pass.equals(confirm) || email.equals(pass)) {

                    if (status % 2 != 0) {
                        Toast.makeText(getApplicationContext(), " خطأ في كتابة اسم المستخدم ", Toast.LENGTH_LONG).show();
                    }
                    if ((status >> 1) % 2 != 0) {
                        Toast.makeText(getApplicationContext(), "  كلمة السر قصيرة  ", Toast.LENGTH_LONG).show();
                    }
                    if ((status >> 2) % 2 != 0) {
                        Toast.makeText(getApplicationContext(),  " يوجد خطأ في كتابة البريد الالكتروني " , Toast.LENGTH_LONG).show();
                    }
                    if (!pass.equals(confirm)) {
                        Toast.makeText(getApplicationContext(), " كلمتا السر غير متطابقتان ", Toast.LENGTH_LONG).show();
                    }
                    if (email.equals(pass)) {
                        Toast.makeText(getApplicationContext(),  " لا يجب ان تكون كلمة المرور نفس البريد الالكتروني "  , Toast.LENGTH_LONG).show();
                    }


                } else {

                    ParseException e = manager.addUser(info);
                    if (e == null) {
                        Toast.makeText( getApplicationContext() , " الرجاء أكد حسابك بأقرب وقت " , Toast.LENGTH_LONG ).show();
                         startActivity( new Intent( create_account_activity.this , Profile_Activity.class  ).putExtra( "user" ,  ParseUser.getCurrentUser().getUsername()) );
                    } else {
                        String cause = "";
                        switch (e.getCode()) {
                            case ParseException.CONNECTION_FAILED: {
                                cause = " لايوجد اتصال بالانترنت ";
                                break;
                            }

                            case ParseException.USERNAME_TAKEN: {
                                cause = " اسم المستخدم هذا موجود مسبقا ";
                                break;
                            }


                            case ParseException.EMAIL_TAKEN: {
                                cause = " هذا البريد الالكتروني موجود مسبقا ";
                                break;
                            }


                            default: {
                                cause = e.getMessage();
                            }

                        }
                        Toast.makeText(getApplicationContext(), cause, Toast.LENGTH_LONG).show();

                    }

                } // pro.dismiss();
            }
        });


    }


    public void goBack(View view) {
        startActivity( new Intent( this , Login_Activity.class ) );
    }
}

