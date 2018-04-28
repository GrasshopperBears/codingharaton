package com.geunwoo.layout_exercise;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Registration extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    GoogleMap googleMap;
    LocationManager locationManager;
    RelativeLayout boxMap;
    Button accept;
    CheckBox man;
    CheckBox women;
    CheckBox age10;
    CheckBox age20;
    CheckBox age30;
    CheckBox guide;
    EditText hope;

    private Double mLatitude = 0.0;
    private Double mLongitude = 0.0;
    int gender;
    int age;

    private static final int PLACE_PICKER_REQUEST = 1;

    Double locationLatitude;
    Double locationLongitude;

//    private SharedPreferences user = getSharedPreferences("USERINFO", MODE_PRIVATE);
//    private SharedPreferences board = getSharedPreferences("BOARD", MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        boxMap = findViewById(R.id.boxMap);
        accept = findViewById(R.id.accept);
        man = findViewById(R.id.man);
        women = findViewById(R.id.women);
        age10 = findViewById(R.id.age10);
        age20 = findViewById(R.id.age20);
        age30 = findViewById(R.id.age30);
        hope = findViewById(R.id.hope);
        guide = findViewById(R.id.checkBox2);
        final CheckBox checker = findViewById(R.id.checker);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
            finish();
        }

        //마시멜로 이상이면 권한 요청하기
        if (Build.VERSION.SDK_INT >= 23) {
            //권한이 없는 경우
            if (ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Registration.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            //권한이 있는 경우
            else {
                requestMyLocation();
            }
        }
        //마시멜로 아래
        else {
            requestMyLocation();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (man.isChecked())
                    gender = 0;
                if (women.isChecked())
                    gender = 1;

                if (age10.isChecked())
                    age = 10;
                if (age20.isChecked())
                    age = 20;
                if (age30.isChecked())
                    age = 30;

                String hopeful = hope.getText().toString();
                String answer = "";

                if (locationLatitude == null || locationLongitude == null) {
                    Toast.makeText(getApplicationContext(), "도착지점을 표시해주세요.",
                            Toast.LENGTH_SHORT).show();
                }
                else if (mLatitude == 0.0 || mLongitude == 0.0){
                    Toast.makeText(getApplicationContext(), "시작 지점를 표시해주세요.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    answer += String.format("출발지점: (%.2f,%.2f) \n", mLatitude, mLongitude);
                    answer += String.format("도착지점: (%.2f,%.2f) \n", locationLatitude, locationLongitude);
                    answer += String.format("나이: %d대 \n", age);
                    answer += String.format("성별: %s \n", (gender == 0 ? "남자" : "여자"));
                    if (hopeful.length() > 0)
                        answer += String.format("기타 사항: %s", hopeful);

                    if (guide.isChecked()) {
                        show(answer);
                    } else
                        Toast.makeText(getApplicationContext(), "약관에 동의해주세요.",
                                Toast.LENGTH_SHORT).show();
                }


            }
        });

        PlaceAutocompleteFragment fragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();


                String attributions = (String) place.getAttributions();
                if (attributions == null) {
                    attributions = "";
                }

                pickMark(place.getLatLng(),name.toString(),address.toString()); // 받아온 정보에서 위치, 이름 , 주소 받아와서 마크 찍기


                Log.d("Place_Pick","2");
                Log.d("Place_Pick",attributions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            }

            @Override
            public void onError(Status status) {

            }
        });

        int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) // PlacePicker 끝날 때 정보 받아오기
    {
        if(requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK)
        {
            final Place place = PlacePicker.getPlace(this, data); // 정보 받아오기
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();


            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }

            pickMark(place.getLatLng(),name.toString(),address.toString()); // 받아온 정보에서 위치, 이름 , 주소 받아와서 마크 찍기


            Log.d("Place_Pick","2");
            Log.d("Place_Pick",attributions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    } // 구글 플레이스 정보 가져오기


    private void pickMark(final LatLng LL,String name, String address) // 위도 경도, 이름 주소 받아서 마커 찍는 함수
    {
        MarkerOptions markerOptions = new MarkerOptions(); // 옵션 설정 해놓을 변수
        markerOptions.position(LL); // 위치 적용
//        markerOptions.title(String.format(Locale.KOREA,"%.3f",LL.latitude)+","+String.format(Locale.KOREA,"%.3f",LL.longitude));
        markerOptions.title(name); // 이름
//        markerOptions.snippet(address.substring(0,20)); // 주소 넣음
        markerOptions.snippet(address); // 주소 넣음
        markerOptions.draggable(true); // 드래그 가능하도록
        locationLatitude = markerOptions.getPosition().latitude;
        locationLongitude = markerOptions.getPosition().longitude;

        //색 다르게
        googleMap.addMarker(markerOptions).setDraggable(true);
        googleMap.addMarker(markerOptions).showInfoWindow(); // 맵에 추가

    } // pickMark

    void show(String answer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("입력 내용을 확인해주세요.");
        builder.setMessage(answer);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //동행 등록 정보를 저장


                        Toast.makeText(getApplicationContext(), "등록이 완료되었습니다.",
                                Toast.LENGTH_LONG).show();
                        //
                        //
                        // 여기에 SID 구현해주어야 함.
                        //
                        // String userinfo = user.getString(SID, "Fuck");
//                        userinfo += "::" + age + "::" + gender + "::" + mLatitude.toString() + "::" + mLongitude.toString()
//                                + "::" + locationLatitude.toString() + "::" + locationLongitude.toString() + hope.getText().toString();
//
//                        SharedPreferences.Editor editor = board.edit();
//
//                        editor.putString(SID, userinfo); //First라는 key값으로 infoFirst 데이터를 저장한다.
//                        editor.commit(); //완료한다.
//

                        Intent intent = new Intent(Registration.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });

        builder.show();
    }

    //권한 요청후 응답 콜백
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //ACCESS_COARSE_LOCATION 권한
        if (requestCode == 1) {
            //권한받음
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestMyLocation();
            }
            //권한못받음
            else {
                Toast.makeText(this, "권한없음", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void requestMyLocation() {
        if (ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //요청
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, locationListener);
    }

    //위치정보 구하기 리스너
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //나의 위치를 한번만 가져오기 위해
            locationManager.removeUpdates(locationListener);

            //위도 경도
            mLatitude = location.getLatitude();   //위도
            mLongitude = location.getLongitude(); //경도

            //맵생성
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            //콜백클래스 설정
            mapFragment.getMapAsync(Registration.this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("gps", "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    //구글맵 생성 콜백
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        //지도타입 - 일반
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.getUiSettings().setCompassEnabled(true);

        //나의 위치 설정
        final LatLng position = new LatLng(mLatitude , mLongitude);

        MarkerOptions current = new MarkerOptions();
        current.position(position);
        googleMap.addMarker(current).showInfoWindow();

        //화면중앙의 위치와 카메라 줌비율
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        //지도 보여주기
        boxMap.setVisibility(View.VISIBLE);

        final GoogleMap mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                mOptions.title("해당 지점");
                if(mLatitude== 0.0 || mLongitude == 0.0){
                    mLatitude = point.latitude;
                    mLongitude = point.longitude;

                    mOptions.snippet(mLatitude.toString() + "," + mLongitude.toString());

                    mOptions.position(new LatLng(mLatitude, mLongitude));
                    googleMap.addMarker(mOptions);
                }
                else if (locationLongitude == null || locationLatitude == null){
                locationLatitude = point.latitude;
                locationLongitude = point.longitude;

                mOptions.snippet(locationLatitude.toString() + "," + locationLongitude.toString());

                mOptions.position(new LatLng(locationLatitude, locationLongitude));
                googleMap.addMarker(mOptions);}

                return;
            }
        });

        LatLng KAIST = new LatLng(36.369647, 127.364068);
        mMap.addMarker(new MarkerOptions().position(KAIST).title("Marker in KAIST"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(KAIST));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);

        MarkerOptions marker = new MarkerOptions();
        marker.position(KAIST);
        googleMap.addMarker(marker).showInfoWindow();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 마커 클릭시 호출되는 콜백 메서드
                if(marker.getPosition().equals(new LatLng(mLatitude, mLongitude))) {
                    marker.remove();
                    mLatitude = 0.0;
                    mLongitude = 0.0;
                    Toast.makeText(getApplicationContext(),
                            "시작점 설정이 취소되었습니다."
                            , Toast.LENGTH_LONG).show();
                    return false;
                }
                else {
                    marker.remove();
                    locationLatitude = null;
                    locationLongitude = null;
                    Toast.makeText(getApplicationContext(),
                            "도착점 설정이 취소되었습니다."
                            , Toast.LENGTH_LONG).show();
                    return false;
                }

            }
        });
    }

    public void onMapClick(LatLng point){

        // 현재 위도와 경도에서 화면 포인트를 알려준다
        Point screenPt = googleMap.getProjection().
                toScreenLocation(point);

        // 현재 화면에 찍힌 포인트로 부터 위도와 경도를 알려준다.
        LatLng latLng = googleMap.getProjection().
                fromScreenLocation(screenPt);



        Log.d("맵좌표","좌표: 위도(" + String.valueOf(point.latitude) + "), 경도(" + String.valueOf(point.longitude) + ")");
        Log.d("화면좌표","화면좌표: X(" + String.valueOf(screenPt.x) + "), Y(" + String.valueOf(screenPt.y) + ")");

    }


}
