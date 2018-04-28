package com.geunwoo.layout_exercise;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class signIn extends AppCompatActivity {

    ImageButton button;
    public static int PICK_PERSONAL_PHOTO =100;
    private RadioGroup checkSex, checkStyle;
    private RadioButton male, female;
    private CheckBox style1, style2, style3, style4;
    private int sex = -1;
    private String personalStyle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        button = (ImageButton) findViewById(R.id.myPhoto);
        Button signIn = (Button) findViewById(R.id.signIn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editsid = (EditText) findViewById(R.id.sid);
                EditText editpassword = (EditText) findViewById(R.id.password);
                EditText editname = (EditText) findViewById(R.id.name);
                EditText editphone = (EditText) findViewById(R.id.phone);
                EditText editage = (EditText) findViewById(R.id.age);

                String sid = editsid.getText().toString();
                String password = editpassword.getText().toString();
                String name = editname.getText().toString();
                String phone = editphone.getText().toString();
                String age = editage.getText().toString();

                checkStyle = (RadioGroup) findViewById(R.id.styleGroup);

                male = (RadioButton) findViewById(R.id.male);
                female = (RadioButton) findViewById(R.id.female);
                style1 = (CheckBox) findViewById(R.id.checkBox1);
                style2 = (CheckBox) findViewById(R.id.checkBox2);
                style3 = (CheckBox) findViewById(R.id.checkBox3);
                style4 = (CheckBox) findViewById(R.id.checkBox4);

                if(male.isChecked())
                    sex = 0;
                else if(female.isChecked())
                    sex = 1;

                if(style1.isChecked())
                    personalStyle += ("먹방");
                if(style2.isChecked())
                    personalStyle += ("액티비티");
                if(style3.isChecked())
                    personalStyle += ("자연경관");
                if(style4.isChecked())
                    personalStyle += ("문화체험");

                if(sid.length() ==0 || password.length() ==0 || name.length() ==0 || phone.length() ==0 || age.length() ==0 || sex == -1 || personalStyle.length() == 0){
                    Toast.makeText(getApplicationContext(), "모든 정보를 입력해주세요!", Toast.LENGTH_LONG).show();
                }
                else{
                    // 1. get Shared Preference
                    SharedPreferences sharedPreference
                            = getSharedPreferences("USERINFO", 0);

                    // 2. get Editor
                    SharedPreferences.Editor editor = sharedPreference.edit();

                    // 3. set Key values
                    editor.putString(sid, password + "::" + name + "::" + phone + "::" + age + "::" + String.valueOf(sex) + "::" + personalStyle);

                    // 4. commit the values
                    editor.commit();

                    Toast.makeText(getApplicationContext(), "회원가입을 축하드립니다!",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(signIn.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PERSONAL_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PICK_PERSONAL_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                button.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
