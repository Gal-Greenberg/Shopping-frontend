package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shopping.ElementBoundary.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

import java.util.ArrayList;
import java.util.List;

public class ChooseStores extends MainActivity {

    ListView listView;
    Button shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_stores);

        listView = findViewById(R.id.listView);

        ElementBoundary[] results;
        elementTasks = new ElementTasks();
        try {
            results = (ElementBoundary[]) elementTasks.execute("stores", "get",
                    BASE_URL + "/elements/{userDomain}/{userEmail}/byType/{type}", DOMAIN, stringEmail, "store").get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final ArrayList<String> allStores = new ArrayList<>();
        for (ElementBoundary result: results) {
            if (!allStores.contains(result.getName()) && result.getElementAttributes().get("state").equals(selectedState)) {
                allStores.add(result.getName());
            }
        }

        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allStores, false, 0);
        listView.setAdapter(arrayAdapter);

        shopping = findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> positionCheckedBox = arrayAdapter.getCheckedBox();
                if (positionCheckedBox.size() == 0) {
                    Toast.makeText(getApplicationContext(), "please choose at least one store", Toast.LENGTH_LONG).show();
                    return;
                }

                int size = positionCheckedBox.size();
                selectedStores = new String[size];
                for (int i = 0; i < size; i++)
                    selectedStores[i] = allStores.get(positionCheckedBox.get(i));

                Intent intent = new Intent(ChooseStores.this, ResultActivity.class);
                intent.putExtras( new Bundle());
                startActivity(intent);
            }
        });
    }
}
