package com.example.a.przepisowo.dialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.a.przepisowo.Constans;
import com.example.a.przepisowo.model.Categories;

import java.util.ArrayList;
import java.util.List;

public class FilterDialogFragment extends DialogFragment {


    ArrayList<Integer> mSelectedItems;
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    Categories categories;
    ArrayList<String> filteredCategories;
    boolean[] isCheckedItem;
    boolean onlyMine;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        categories = (Categories) this.getArguments().getSerializable(Constans.CATEGORIES_OBJECT);
        filteredCategories = this.getArguments().getStringArrayList(Constans.FILTERED_CATEGORIES);
        onlyMine = this.getArguments().getBoolean(Constans.ONLY_MINE);
        CharSequence[] filters = new CharSequence[categories.getDishTypes().size()+1];
        isCheckedItem = new boolean[categories.getDishTypes().size()+1];
        for(int i=0; i < categories.getDishTypes().size(); i++){
            filters[i] = categories.getDishTypes().get(i);
        }
        if(filteredCategories != null) {
            for (String name : categories.getDishTypes()) {
                for (String filteredName : filteredCategories) {
                    if (filteredName.equals(name)) {
                        isCheckedItem[categories.getDishTypes().indexOf(name)] = true;
                    }
                }
            }
        }
        filters[categories.getDishTypes().size()] = "Tylko moje";
        isCheckedItem[categories.getDishTypes().size()] = onlyMine;


        mSelectedItems = new ArrayList<>();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Filtry")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(filters, isCheckedItem,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                    isCheckedItem[which] = true;
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                    isCheckedItem[which] = false;
                                }

                            }
                        })
                // Set the action buttons
                .setPositiveButton("Filtruj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        mListener.onDialogPositiveClick(FilterDialogFragment.this);

                    }
                })
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(FilterDialogFragment.this);

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(this.toString()
                    + " must implement NoticeDialogListener");
        }

    }

    public List<String> getmSelectedItems() {
        List<String> selectedCategories = new ArrayList<>();
        for(int in = 0; in < mSelectedItems.size(); in++){

                if (mSelectedItems.get(in).equals(4)){
                    selectedCategories.add("Tylko moje");
                } else {
                    selectedCategories.add(categories.getDishTypes().get(mSelectedItems.get(in)));
                }
            }
        return selectedCategories;
    }

    public boolean isOnlyMine() {
        return this.onlyMine;
    }
}
