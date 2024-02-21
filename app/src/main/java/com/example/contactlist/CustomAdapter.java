package com.example.contactlist;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter implements CustomAdapterk {
    private Context context;
    private List<ContactAddedModelClass> itemList;
    private SparseBooleanArray selectedItems;

    public CustomAdapter(Context context, List<ContactAddedModelClass> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.selectedItems = new SparseBooleanArray();
    }


    // Other overridden methods...

 @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate your item layout
        // ...

        // Retrieve the checkbox from the layout
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);

        // Set the checkbox state based on the selectedItems array
        checkBox.setChecked(selectedItems.get(position, false));

        // Set a listener to handle checkbox changes
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the selectedItems array based on checkbox changes
            if (isChecked) {
                selectedItems.put(position, true);
            } else {
                selectedItems.delete(position);
            }
        });

        // Return the convertView
        return convertView;
    }

    // Method to get the selected items
    public List<ContactAddedModelClass> getSelectedItems() {
        List<ContactAddedModelClass> selectedList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (selectedItems.get(i, false)) {
                selectedList.add(itemList.get(i));
            }
        }
        return selectedList;
    }
}
