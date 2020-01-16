package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Tasks.ActionTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultActivity extends MainActivity {

    ListView listView;
    Button main;

    List<ElementBoundary> allResults;

    int page;
    int sizeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        allResults = new ArrayList<>();
        listView = findViewById(R.id.listView);

        isGetActionResults(true, true);

        final ListViewAdapter arrayAdapter = new ListViewAdapter(this, allResults, true, "info",
                false, 0);
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
                    isScroll = isGetActionResults(false, false);
                    isPrev = true;
                } else if (sizeResult == 10 && (firstItem + visibleItemCount) == totalItems) {
                    page++;
                    isScroll = isGetActionResults(true, false);
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

    public boolean isGetActionResults(boolean isNextPage, boolean isFirstTime) {
        Object result = null;
        try {
            switch (actionNumber) {
                case 0:
                    String[] selectedStoresArr = new String[selectedStores.size()];
                    for (int i = 0; i < selectedStoresArr.length; i++) {
                        selectedStoresArr[i] = selectedStores.get(i);
                    }

                    actionTasks = new ActionTasks(selectedStoresArr);
                    result = actionTasks.execute(BASE_URL + "/actions", DOMAIN, loginUser.getUserId().getEmail(),
                            selectedState.getElementId().getId(), "findStoresInState", "" + page, PAGE_SIZE).get();
                    break;
                case 1:
                    actionTasks = new ActionTasks();
                    result = actionTasks.execute(BASE_URL + "/actions", DOMAIN, loginUser.getUserId().getEmail(),
                            selectedMalls.get(0).getElementId().getId(), "findAllStoresInMall", "" + page, PAGE_SIZE).get();
                    break;
                case 2:
                    String[] selectedCategoryArr = new String[selectedCategory.size()];
                    for (int i = 0; i < selectedCategoryArr.length; i++) {
                        selectedCategoryArr[i] = selectedCategory.get(i);
                    }

                    actionTasks = new ActionTasks(selectedCategoryArr);
                    result = actionTasks.execute(BASE_URL + "/actions", DOMAIN, loginUser.getUserId().getEmail(),
                            selectedMalls.get(0).getElementId().getId(), "findAllStoresInCategoryInMall", "" + page, PAGE_SIZE).get();
                    break;
                case 4:
                    actionTasks = new ActionTasks();
                    result = actionTasks.execute(BASE_URL + "/actions", DOMAIN, loginUser.getUserId().getEmail(),
                            selectedMalls.get(0).getElementId().getId(), "findAllStoresByLikes", "" + page, PAGE_SIZE).get();
                    break;
            }

            if (result.getClass() == String.class) {
                Toast.makeText(getApplicationContext(), (String) ((HashMap) result).get("error"), Toast.LENGTH_LONG).show();
                return false;
            }

            ElementBoundary[] resultArr = (ElementBoundary[]) result;
            sizeResult = resultArr.length;

            if (isNextPage) {
                for (int i = 0; i < sizeResult; i++) {
                    if (!isFirstTime)
                        allResults.remove(i);
                    allResults.add(resultArr[i]);
                }
            } else {
                for (int i = sizeResult - 1; i > -1; i--) {
                    allResults.remove(i);
                    allResults.add(resultArr[i]);
                }
            }
            return true;
        } catch (Exception e) {
            Log.e("ExceptionResultAction", e.getMessage());
            return false;
        }
    }
}
