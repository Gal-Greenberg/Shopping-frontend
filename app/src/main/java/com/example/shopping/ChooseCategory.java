package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

import java.util.ArrayList;
import java.util.List;

public class ChooseCategory extends MainActivity {

    ImageButton user;

    ListView listView;
    Button shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseCategory.this, UserInfo.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        Object result = null;
        elementTasks = new ElementTasks();
        try {
            result = elementTasks.execute("category", "get",
                    BASE_URL + "/elements/{userDomain}/{userEmail}/byParent/{parentDomain}/{parentId}", DOMAIN,
                    loginUser.getUserId().getEmail(), DOMAIN, selectedMalls[0].getParentElement().getElementId().getId()).get();
        } catch (Exception e) {
            Log.e("ExceptionChooseCategory", e.getMessage());
        }

        if (result.getClass() == String.class) {
            Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        ElementBoundary[] resultElementBoundary = (ElementBoundary[]) result;

        final ArrayList<String> allCategory = new ArrayList<>();
        for (ElementBoundary category: resultElementBoundary) {
            String resultCategory = category.getElementAttributes().get("category").toString();
            if (!allCategory.contains(resultCategory)){
                allCategory.add(resultCategory);
            }
        }

        listView = findViewById(R.id.listView);
        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allCategory, false, "", true, 0);
        listView.setAdapter(arrayAdapter);

        shopping = findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> positionCheckedBox = arrayAdapter.getCheckedBox();
                if (positionCheckedBox.size() == 0) {
                    Toast.makeText(getApplicationContext(), "please choose at least one category", Toast.LENGTH_LONG).show();
                    return;
                }

                int size = positionCheckedBox.size();
                selectedCategory = new String[size];
                for (int i = 0; i < size; i++)
                    selectedCategory[i] = allCategory.get(positionCheckedBox.get(i));

                Intent intent = new Intent(ChooseCategory.this, ResultActivity.class);
                intent.putExtras( new Bundle());
                startActivity(intent);
            }
        });
    }
}
