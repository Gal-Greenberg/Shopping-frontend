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

public class ChooseStores extends MainActivity {

    ImageButton user;

    ListView listView;
    Button shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_stores);

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseStores.this, UserInfo.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.listView);

        Object result = null;
        elementTasks = new ElementTasks();
        try {
            result = elementTasks.execute("stores", "get", BASE_URL + "/elements/{userDomain}/{userEmail}/byType/{type}",
                    DOMAIN, loginUser.getUserId().getEmail(), "store").get();
        } catch (Exception e) {
            Log.e("ExceptionChooseStores", e.getMessage());
        }

        if (result.getClass() == String.class) {
            Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        ElementBoundary[] resultElementBoundary = (ElementBoundary[]) result;

        final ArrayList<String> allStores = new ArrayList<>();
        for (ElementBoundary store: resultElementBoundary) {
            if (!allStores.contains(store.getName()) && store.getElementAttributes().get("state").equals(selectedState)) {
                allStores.add(store.getName());
            }
        }

        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allStores, false, "", true, 0);
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
