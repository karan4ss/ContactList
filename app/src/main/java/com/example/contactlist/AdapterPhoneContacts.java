package com.example.contactlist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterPhoneContacts extends RecyclerView.Adapter<AdapterPhoneContacts.ViewHolder> {

    Activity activity;
    ArrayList<ContactModel> arrayList;
    ArrayList<ContactAddedModelClass> arrayListForAddingContacts = new ArrayList<ContactAddedModelClass>();
    ArrayList<String> mergerdArrayist = new ArrayList<String>();
    private OnItemClickListener listener;

    public AdapterPhoneContacts(Activity activity, ArrayList<ContactModel> arrayList,OnItemClickListener listener) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.listener = listener;
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
        ContactModel contactModel = arrayList.get(position);
        ContactAddedModelClass contactAddedModelClass = new ContactAddedModelClass();

        holder.tvItemName.setText((CharSequence) contactModel.getName());
        holder.tvItemNumber.setText(contactModel.getNumber());


        holder.selectContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contactAddedModelClass.setName(String.valueOf(arrayList.get(position).getName()));
                contactAddedModelClass.setPhone_number(arrayList.get(position).getNumber());
                if (isChecked){
                    arrayListForAddingContacts.add(contactAddedModelClass);
                    Toast.makeText(activity, "Added...!", Toast.LENGTH_SHORT).show();
                }else {
                    arrayListForAddingContacts.remove(contactAddedModelClass);
                    Toast.makeText(activity, "Removed...!", Toast.LENGTH_SHORT).show();
                }


                if (listener!=null){
                    listener.onItemClick(arrayListForAddingContacts);
                }

            }
        });


        // mergerdArrayist.addAll(arrayListForAddingContacts);


    }

    public interface OnItemClickListener {
        void onItemClick(ArrayList<ContactAddedModelClass> dataList);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemNumber;
        AppCompatButton btnAddSingleContacts;
        CheckBox selectContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemNumber = itemView.findViewById(R.id.tvItemNumber);
            btnAddSingleContacts = itemView.findViewById(R.id.btnAddSingleContacts);
            selectContact = itemView.findViewById(R.id.checkbox);


        }
    }
}
