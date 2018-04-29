package com.geunwoo.layout_exercise;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class matching extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    LinearLayout Userlocation;
    Double start_lat;
    Double start_long;
    Double dest_lat;
    Double dest_long;
    TextView UserInfo;
    TextView content;
    Button OK;
    Button NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        String CURUSERID = getIntent().getStringExtra("CURSID");
        String BID = getIntent().getStringExtra("BID");
        SharedPreferences pref
                = getSharedPreferences("REQUESTLIST", MODE_PRIVATE);
        SharedPreferences users
                = getSharedPreferences("USERINFO", MODE_PRIVATE);
        String uploadInfo = pref.getString(BID, "");
        String uploaderID = uploadInfo.split("\n")[0];
        String[] uploaderInfo = users.getString(uploaderID, "").split("\n");

        String sex = "";

        if(Integer.parseInt(uploaderInfo[4]) == 0)
            sex = "남자";
        else
            sex = "여자";

        Intent intent = getIntent();
        final TripUser selectedUser = (TripUser) intent.getSerializableExtra("선택된 유저");

        Double[] start = selectedUser.getStart_location();
        start_lat = start[0];
        start_long = start[1];

        Double[] dest = selectedUser.getDestination();
        dest_lat = dest[0];
        dest_long = dest[1];

        Userlocation = findViewById(R.id.userlocation);
        UserInfo = findViewById(R.id.content);
        content = findViewById(R.id.content);
        OK = findViewById(R.id.OK);
        NO = findViewById(R.id.NO);

        content.setText(selectedUser.forMatching());
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.usermap);
        mapFragment.getMapAsync(this);

        //동행 등록자 정보
        UserInfo.setText("나이 : " + userInfo[3] + '\n' + "성별 : " + sex + '\n' + "여행 스타일 : " + userInfo[5]);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "매칭이 완료되었습니다. " +
                        "주어진 연락처로 연락해주세요.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(matching.this, MainActivity.class);
                startActivity(intent);
            }
        });

        NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "다른 유저를 찾습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //나의 위치 설정
        LatLng position = new LatLng(start_lat, start_long);
        LatLng destination = new LatLng(dest_lat, dest_long);

        MarkerOptions current = new MarkerOptions();
        current.position(position);
        googleMap.addMarker(current).showInfoWindow();
        current.position(destination);
        googleMap.addMarker(current).showInfoWindow();
        Userlocation.setVisibility(View.VISIBLE);

        //화면중앙의 위치와 카메라 줌비율
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 13));

    }
}
