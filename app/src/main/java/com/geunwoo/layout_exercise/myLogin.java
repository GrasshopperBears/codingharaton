package com.geunwoo.layout_exercise;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class myLogin extends AppCompatActivity {

    String sid = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_login);

        Button loginButton = (Button) findViewById(R.id.id_sign_in_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editsid = (EditText) findViewById(R.id.id);
                EditText editpassword = (EditText) findViewById(R.id.password);
                sid = editsid.getText().toString();
                password = editpassword.getText().toString();

                SharedPreferences pref
                        = getSharedPreferences("USERINFO", 0);

                if(sid.length() == 0 || password.length() == 0)
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호 모두 입력해주세요.", Toast.LENGTH_LONG).show();

                if(!pref.contains(sid)){
                    Toast.makeText(getApplicationContext(), "등록되지 않은 아이디입니다.", Toast.LENGTH_LONG).show();
                }
                else{
                    String value = pref.getString("USERINFO", "");

                    value = value.split("::")[0];
                    if(password.length() == 0)
                        Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                    else if(password != value)
                        Toast.makeText(getApplicationContext(), "옳지 않은 비밀번호입니다."+value+" but "+password, Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });



    }
}
