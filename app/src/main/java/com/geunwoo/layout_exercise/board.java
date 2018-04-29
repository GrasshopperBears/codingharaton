package com.geunwoo.layout_exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class board extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String CURUSERID = getIntent().getStringExtra("CURSID");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        SharedPreferences board = getSharedPreferences("REQUESTLIST", MODE_PRIVATE);
        Map<String,String> boardcollect = (Map<String, String>) board.getAll();
        ArrayList<String> entireboard = new ArrayList<String>();

        Iterator<String> keys = boardcollect.keySet().iterator();
        while(keys.hasNext()){
            String tmpBid = keys.next();
            String bInfo = board.getString(tmpBid, "Excption");
            String tmpSid = bInfo.split("\n")[0];
            entireboard.add(tmpSid + "님의 동행요청");
        }

        ListViewAdapter Adapter = new ListViewAdapter(entireboard);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(board.this, matching.class);
                intent.putExtra("BID", Integer.toString(position));
                intent.putExtra("CURSID", CURUSERID);
                startActivity(intent);
                finish();
            }
        });

    }
}
