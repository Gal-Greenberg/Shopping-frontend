package com.example.shopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private int multipleChoice;
    private boolean withButton;
    boolean withCheckBox;
    private String buttonName;
    private Activity activity;
    private List<?> actions;
    private LayoutInflater inflater;

    private List<Integer> positionCheckedBox;

    public ListViewAdapter(Activity activity) {
        this.activity = activity;
    }

    public ListViewAdapter(Activity activity, List<?> actions, boolean withButton, String buttonName, boolean withCheckBox,
                           int multipleChoice) {
        this.multipleChoice = multipleChoice;
        this.withButton = withButton;
        this.withCheckBox = withCheckBox;
        this.buttonName = buttonName;
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
        final ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.list_view_item, viewGroup, false);
            holder = new ViewHolder();

            holder.index = position;
            holder.ivCheckBox = view.findViewById(R.id.ivCheckBox);
            holder.button = view.findViewById(R.id.info);

            holder.button.setText(buttonName);
            if (!this.withButton)
                holder.button.setVisibility(View.GONE);
            if (!withCheckBox)
                holder.ivCheckBox.setVisibility(View.GONE);

            holder.ivCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (positionCheckedBox.contains(position)) {
                        positionCheckedBox.remove((Object) position);
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

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) activity).selectedInfo = (ElementBoundary) actions.get(holder.index);

                    if (buttonName.matches("info")) {
                        Intent intent = new Intent(activity, InfoActivity.class);
                        intent.putExtras(new Bundle());
                        activity.startActivity(intent);
                    }

                    ((MainActivity) activity).isCreating = false;
                    Intent intent = new Intent(activity, CreateElement.class);
                    intent.putExtras(new Bundle());
                    activity.startActivity(intent);
                }
            });

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        String tempTextCheckBox;
        if (actions.get(0).getClass() == ElementBoundary.class) {
            ElementBoundary tempElementBoundary = (ElementBoundary) actions.get(position);
            tempTextCheckBox = tempElementBoundary.getName();
            if (tempElementBoundary.getType().matches("store"))
                tempTextCheckBox += " in " + tempElementBoundary.getElementAttributes().get("mall");
        } else {
            tempTextCheckBox = (String) actions.get(position);
        }
        holder.ivCheckBox.setText(tempTextCheckBox);

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

        int index;
        CheckBox ivCheckBox;
        Button button;

    }
}
