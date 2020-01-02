package com.example.shopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    int multipleChoice;
    boolean withInfo;
    Activity activity;
    List<String> actions;
    LayoutInflater inflater;

    List<Integer> positionCheckedBox;

    public ListViewAdapter(Activity activity) {
        this.activity = activity;
    }

    public ListViewAdapter(Activity activity, List<String> actions, boolean withInfo, int multipleChoice) {
        this.multipleChoice = multipleChoice;
        this.withInfo = withInfo;
        this.activity = activity;
        this.actions = actions;

        positionCheckedBox = new ArrayList<>();

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.list_view_item, viewGroup, false);
            holder = new ViewHolder();

            holder.ivCheckBox = view.findViewById(R.id.ivCheckBox);
            holder.info = view.findViewById(R.id.info);

            if (!this.withInfo)
                holder.info.setVisibility(View.GONE);

            holder.ivCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (positionCheckedBox.contains(position)) {
                        positionCheckedBox.remove(positionCheckedBox.indexOf(position));
                        compoundButton.setChecked(false);

                        return;
                    }

                    if (multipleChoice == 1)
                        oneChoice(position, compoundButton);
                    if (multipleChoice == 2)
                        twoChoice(position, compoundButton);
                    if (multipleChoice == 0) // multiple choice
                        positionCheckedBox.add(position);
                }
            });

            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    //TODO put on bundle the element info/ element id
                    Intent intent = new Intent(activity, InfoActivity.class);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        String temp = actions.get(position);
        holder.ivCheckBox.setText(temp);

        return view;
    }

    public void oneChoice(int position, CompoundButton cb) {
        if (positionCheckedBox.size() == 0) {
            positionCheckedBox.add(position);
            cb.setChecked(true);
            return;
        }
        cb.setChecked(false);
        Toast.makeText(activity, "you can only choose one action", Toast.LENGTH_LONG).show();
    }

    public void twoChoice(int position, CompoundButton cb) {
        if (positionCheckedBox.size() < 2) {
            positionCheckedBox.add(position);
            cb.setChecked(true);
            return;
        }
        cb.setChecked(false);
        Toast.makeText(activity, "you can only choose two action", Toast.LENGTH_LONG).show();
    }

    public List<Integer> getCheckedBox() {
        return positionCheckedBox;
    }

    class ViewHolder {

        CheckBox ivCheckBox;
        Button info;

    }
}
