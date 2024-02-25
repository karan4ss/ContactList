package com.example.contactlist;

import static com.example.contactlist.R.color.teal_700;
import static com.example.contactlist.R.color.white;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterPhoneContacts extends RecyclerView.Adapter<AdapterPhoneContacts.ViewHolder> {

    Activity activity;
    ArrayList<ContactModel> arrayList;
    ArrayList<ContactModel> filteredList;
    private List<Integer> selectedPositions = new ArrayList<>();
    ArrayList<ContactAddedModelClass> arrayListForAddingContacts = new ArrayList<ContactAddedModelClass>();
    ArrayList<String> mergerdArrayist = new ArrayList<String>();
    private OnItemClickListener listener;
    boolean isSelect = false;
    boolean flag = false;

    public AdapterPhoneContacts(Activity activity, ArrayList<ContactModel> arrayList, OnItemClickListener listener) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.listener = listener;
        this.filteredList = new ArrayList<>(arrayList);
        notifyDataSetChanged();
    }

/*
    public AdapterPhoneContacts(ArrayList<ContactModel> arrayList, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }
*/

    @NonNull
    @Override
    public AdapterPhoneContacts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_from_phone, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPhoneContacts.ViewHolder holder, int position) {
        ContactModel contactModel = filteredList.get(position);
        ContactAddedModelClass contactAddedModelClass = new ContactAddedModelClass();

        holder.tvItemName.setText((CharSequence) contactModel.getName());
        holder.tvItemNumber.setText(contactModel.getNumber());


       /* holder.selectContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contactAddedModelClass.setName(String.valueOf(arrayList.get(position).getName()));
                contactAddedModelClass.setPhone_number(arrayList.get(position).getNumber());
                if (isChecked) {
                    arrayListForAddingContacts.add(contactAddedModelClass);
                    Toast.makeText(activity, "Added...!", Toast.LENGTH_SHORT).show();
                } else {
                    arrayListForAddingContacts.remove(contactAddedModelClass);
                    Toast.makeText(activity, "Removed...!", Toast.LENGTH_SHORT).show();
                }


                if (listener != null) {
                    listener.onItemClick(arrayListForAddingContacts);
                }

            }
        });*/


       /* holder.clmainofDevicePhoneContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactAddedModelClass.setName(String.valueOf(arrayList.get(position).getName()));
                contactAddedModelClass.setPhone_number(arrayList.get(position).getNumber());

                if (selectedPositions.contains(position)) {
                    holder.clmainofDevicePhoneContact.setBackgroundColor(activity.getResources().getColor(teal_700));
                    arrayListForAddingContacts.add(contactAddedModelClass);
                    Toast.makeText(activity, "Added...!", Toast.LENGTH_SHORT).show();
                } else {
                    holder.clmainofDevicePhoneContact.setBackgroundColor(activity.getResources().getColor(white));
                    arrayListForAddingContacts.remove(contactAddedModelClass);
                    Toast.makeText(activity, "Removed...!", Toast.LENGTH_SHORT).show();
                }
                // Notify that the data set has changed
                notifyDataSetChanged();


                if (listener != null) {
                    listener.onItemClick(arrayListForAddingContacts);
                }

            }
        });*/


        //if (selectedPositions.contains(position)) {

        if (selectedPositions.contains(position)) {
            holder.clmainofDevicePhoneContact.setBackgroundColor(activity.getResources().getColor(teal_700));
            contactAddedModelClass.setName(String.valueOf(filteredList.get(position).getName()));
            contactAddedModelClass.setPhone_number(filteredList.get(position).getNumber());
            /*arrayListForAddingContacts.add(contactAddedModelClass);
            Toast.makeText(activity, "Added...!", Toast.LENGTH_SHORT).show();*/
            holder.selectContact.setChecked(true);
        } else {
            holder.clmainofDevicePhoneContact.setBackgroundColor(activity.getResources().getColor(white));
            holder.selectContact.setChecked(false);
           /* arrayListForAddingContacts.remove(contactAddedModelClass);
            Toast.makeText(activity, "Removed...!", Toast.LENGTH_SHORT).show();*/
        }

        // mergerdArrayist.addAll(arrayListForAddingContacts);


    }

    public void filter(String query) {

        filteredList.clear();
        if (query.isEmpty() || query.equals(null) || query.equals("")) {
            filteredList.addAll(arrayList);
        } else {
            for (ContactModel contact : arrayList) {
                if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(contact);
                    flag = true;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        ContactAddedModelClass contactAddedModelClass = filteredList.get(position);
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(Integer.valueOf(position));
            arrayListForAddingContacts.remove(contactAddedModelClass);
            Toast.makeText(activity, "Removed...!", Toast.LENGTH_SHORT).show();
        } else {
            selectedPositions.add(position);
            arrayListForAddingContacts.add(contactAddedModelClass);
            Toast.makeText(activity, "Added...!", Toast.LENGTH_SHORT).show();
        }
        if (listener != null) {
            listener.onItemClick(arrayListForAddingContacts);
        }
        notifyItemChanged(position);
    }

    public interface OnItemClickListener {
        void onItemClick(ArrayList<ContactAddedModelClass> dataList);
    }

    @Override
    public int getItemCount() {
        if (filteredList != null) {
            return filteredList.size();
        } else {
            return arrayList.size();
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemNumber;
        AppCompatButton btnAddSingleContacts;
        CheckBox selectContact;
        public ConstraintLayout clmainofDevicePhoneContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemNumber = itemView.findViewById(R.id.tvItemNumber);
            btnAddSingleContacts = itemView.findViewById(R.id.btnAddSingleContacts);
            selectContact = itemView.findViewById(R.id.checkbox);
            clmainofDevicePhoneContact = itemView.findViewById(R.id.clmainofDevicePhoneContact);
            ContactAddedModelClass contactAddedModelClass = new ContactAddedModelClass();

          /*  clmainofDevicePhoneContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    // Check if the item is already selected
                    if (position == selectedItem) {
                        // Deselect the item
                        selectedItem = RecyclerView.NO_POSITION;
                        clmainofDevicePhoneContact.setBackgroundColor(activity.getResources().getColor(white));
                        arrayListForAddingContacts.remove(contactAddedModelClass);
                        Toast.makeText(activity, "Removed...!", Toast.LENGTH_SHORT).show();

                    } else {
                        // Select the item
                        selectedItem = position;
                        clmainofDevicePhoneContact.setBackgroundColor(activity.getResources().getColor(teal_700));
                        arrayListForAddingContacts.add(contactAddedModelClass);
                        Toast.makeText(activity, "Added...!", Toast.LENGTH_SHORT).show();
                    }

                    // Notify that the data set has changed
                    notifyDataSetChanged();
                }

            });*/


        }
    }
}
