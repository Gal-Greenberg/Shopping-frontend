package com.example.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    ListView listView;
    Button main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listView = findViewById(R.id.listView);

        //TODO: get all results from backend and put in allMalls
        final ArrayList<String> allResults = new ArrayList<>();
        allResults.add("1");
        allResults.add("2");
        allResults.add("3");
        allResults.add("4");
        allResults.add("5");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allResults);
        listView.setAdapter(arrayAdapter);

        main = findViewById(R.id.main);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(ResultActivity.this, ChooseAction.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
