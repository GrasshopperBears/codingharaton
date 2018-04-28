package com.geunwoo.layout_exercise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class board extends AppCompatActivity {

    static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        ArrayList<TripUser> userlist = (ArrayList<TripUser>) intent.getSerializableExtra("유저 목록");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        ListViewAdapter Adapter = new ListViewAdapter(userlist);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TripUser selectedUser = (TripUser) parent.getItemAtPosition(position);
                Intent intent = new Intent(board.this, matching.class);
                intent.putExtra("선택된 유저", selectedUser);
                startActivity(intent);
            }
        });
    }
}
