package com.lithium.MyChat.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.lithium.MyChat.R;

import org.litepal.LitePal;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;
    private CheckBox autoLogin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        accountEdit = (EditText) findViewById(R.id.account);

        passwordEdit = (EditText) findViewById(R.id.password);

        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        autoLogin = (CheckBox) findViewById(R.id.auto_login);

        login = (Button) findViewById(R.id.button_login);

        final boolean isRemember = pref.getBoolean("remember_password", false);
        boolean isAutoLogin = pref.getBoolean("auto_login", false);

        if(isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
//            autoLogin.setChecked(true);

        } if(isAutoLogin && isRemember){
            rememberPass.setChecked(true);
            autoLogin.setChecked(true);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
//            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                LitePal.getDatabase();

                if (account.equals("admin") && password.equals("123")) {
                    editor = pref.edit();
                    if(autoLogin.isChecked()){
                        editor.putBoolean("remember_password", true);
                        editor.putBoolean("auto_login", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                        editor.commit();
                    }if (rememberPass.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    }
//                    else if ( rememberPass.isChecked() && ){
//                        Intent intent = new Intent(LoginActivity.this, FriendList.class);
//                        startActivity(intent);
//                    }
                    else {editor.clear();}
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "username or password is wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


