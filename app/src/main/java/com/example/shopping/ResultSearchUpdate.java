package com.example.shopping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

import java.util.Arrays;
import java.util.List;

public class ResultSearchUpdate extends MainActivity {

    ImageButton user;

    TextView title;
    ListView listView;

    int sizeResult;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search_update);

        sizeResult = resultSearchUpdate.size();

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultSearchUpdate.this, UserInfo.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        title = findViewById(R.id.title);
        title.setText("Hello, " + loginUser.getUsername());

        listView = findViewById(R.id.listView);
        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, resultSearchUpdate, true, "Update", false, 0);
        listView.setAdapter(arrayAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int visibleItemCount, int totalItems) {
                boolean isScroll = false;
                boolean isPrev = false;
                if (firstItem == 0 && page > 0) {
                    page--;
                    isScroll = isGetActionResults(false);
                    isPrev = true;
                } else if (sizeResult == 10 && (firstItem + visibleItemCount) == totalItems) {
                    page++;
                    isScroll = isGetActionResults(true);
                }

                if (isScroll) {
                    listView.setAdapter(arrayAdapter);
                    listView.setSelection(totalItems);
                    arrayAdapter.notifyDataSetChanged();

                    if (isPrev) {
                        listView.setSelectionAfterHeaderView();
                    }
                }
            }
        });
    }

    public boolean isGetActionResults(boolean isNextPage) {
        elementTasks = new ElementTasks();
        Object result = null;
        try {
            result = elementTasks.execute("byType", "get", BASE_URL +
                            "/elements/{userDomain}/{userEmail}/byType/{type}?size={size}&page={page}", DOMAIN,
                    loginUser.getUserId().getEmail(), resultSearchUpdate.get(0).getType(), PAGE_SIZE, "" + page).get();

            if (result.getClass() == String.class) {
                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                return false;
            }

            ElementBoundary[] resultArr = (ElementBoundary[]) result;
            sizeResult = resultArr.length;

            if (isNextPage) {
                for (int i = 0; i < sizeResult; i++) {
                    resultSearchUpdate.remove(i);
                    resultSearchUpdate.add(resultArr[i]);
                }
            } else {
                for (int i = sizeResult - 1; i > -1; i--) {
                    resultSearchUpdate.remove(i);
                    resultSearchUpdate.add(resultArr[i]);
                }
            }
            return true;
        } catch (Exception e) {
            Log.e("ExceptionSearchUpdate", e.getMessage());
            return false;
        }
    }
}
