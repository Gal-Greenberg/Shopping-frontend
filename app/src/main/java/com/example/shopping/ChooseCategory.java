package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.shopping.ElementBoundary.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

import java.util.ArrayList;

public class ChooseCategory extends MainActivity {

    ListView listView;
    Button shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        ElementBoundary[] results;
        elementTasks = new ElementTasks();
        try {
            results = (ElementBoundary[]) elementTasks.execute("category", "get",
                    BASE_URL + "/elements/{userDomain}/{userEmail}/byParent/{parentDomain}/{parentId}", DOMAIN, stringEmail,
                    DOMAIN, selectedMalls[0].getParentElement().getElementId().getId()).get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final ArrayList<String> allCategory = new ArrayList<>();
        for (ElementBoundary result: results) {
            String resultCategory = result.getElementAttributes().get("category").toString();
            if (!allCategory.contains(resultCategory)){
                allCategory.add(resultCategory);
            }
        }

        listView = findViewById(R.id.listView);
        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allCategory, false, 0);
        listView.setAdapter(arrayAdapter);

        shopping = findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChooseCategory.this, ResultActivity.class);
                intent.putExtras( new Bundle());
                startActivity(intent);
            }
        });
    }
}
