package com.example.contactlist;

import static com.example.contactlist.R.color.teal_700;
import static com.example.contactlist.R.color.white;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    ArrayList<ContactModel> groupList;
    Integer groupSize;
    int mainPostition;

    public AdapterPhoneContacts(Activity activity, ArrayList<ContactModel> arrayList, OnItemClickListener listener, ArrayList<ContactModel> groupList) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.listener = listener;
        this.groupList = groupList;
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
        groupSize = groupList.size();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPhoneContacts.ViewHolder holder, int position) {
        ContactModel contactModel = filteredList.get(position);
        ContactAddedModelClass contactAddedModelClass = new ContactAddedModelClass();

        //  holder.tvItemName.setText((CharSequence) contactModel.getName());
        
        String originalText = contactModel.getName();
        int maxCharactersToShow = 20;

        if (originalText.length() > maxCharactersToShow) {
            String truncatedText = originalText.substring(0, maxCharactersToShow) + "...";
            holder.tvItemName.setText(truncatedText);
        } else {
            holder.tvItemName.setText(originalText);
        }


        holder.tvItemNumber.setText(contactModel.getNumber());
        ContactModel clickedItem1 = filteredList.get(position);
        int mainPostition1 = arrayList.indexOf(clickedItem1);
        //if (!groupList.isEmpty()) {
        if (arrayList.get(mainPostition1).isIsselectContact()) {
            holder.clmainofDevicePhoneContact.setBackgroundColor(activity.getResources().getColor(teal_700));
            contactAddedModelClass.setName(String.valueOf(filteredList.get(position).getName()));
            contactAddedModelClass.setPhone_number(filteredList.get(position).getNumber());
            holder.selectContact.setChecked(true);
        } else {
            holder.clmainofDevicePhoneContact.setBackgroundColor(activity.getResources().getColor(white));
            holder.selectContact.setChecked(false);

        }

    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty() || query.equals(null) || query.equals("")) {
            filteredList.addAll(arrayList);
            flag = false;
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
        ContactModel clickedItem = filteredList.get(position);
        mainPostition = arrayList.indexOf(clickedItem);
        if (arrayList.get(mainPostition).isIsselectContact()) {
            selectedPositions.remove(Integer.valueOf(position));
            arrayList.get(mainPostition).setIsselectContact(false);
            arrayListForAddingContacts.remove(contactAddedModelClass);
           // Toast.makeText(activity, "Removed...!", Toast.LENGTH_SHORT).show();
            groupSize--;
        } else {
            //  if (!groupList.isEmpty()) {
            if (groupSize < 200) {
                String mobNo = filteredList.get(position).getNumber();
                boolean isNamePresent = false;

                for (ContactAddedModelClass contact : groupList) {
                    if (((ContactModel) contact).getNumber().equals(mobNo)) {
                        isNamePresent = true;
                        break;
                    }
                }
                if (!isNamePresent) {
                    selectedPositions.add(position);
                    arrayList.get(mainPostition).setIsselectContact(true);
                    arrayListForAddingContacts.add(contactAddedModelClass);
                   // Toast.makeText(activity, "Added...!", Toast.LENGTH_SHORT).show();
                    groupSize++;
                } else {
                    Toast.makeText(activity, "Already Exist...!", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(activity, "Group Limit exceed", Toast.LENGTH_SHORT).show();
            }
        } /*else {
                if (groupSize < 200) {

                    if (filteredList.get(position).isIsselectContact()) {
                        arrayList.get(mainPostition).setIsselectContact(false);
                        filteredList.get(position).setIsselectContact(false);
                        arrayListForAddingContacts.remove(contactAddedModelClass);
                        Toast.makeText(activity, "Removed...!", Toast.LENGTH_SHORT).show();
                        groupSize--;
                    } else {
                        arrayList.get(mainPostition).setIsselectContact(true);
                        filteredList.get(position).setIsselectContact(true);
                        arrayListForAddingContacts.add(contactAddedModelClass);
                        Toast.makeText(activity, "Added...!", Toast.LENGTH_SHORT).show();
                        groupSize++;
                    }


                } else {
                    Toast.makeText(activity, "limit cross", Toast.LENGTH_SHORT).show();
                }

            }*/


        //  }
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


        }
    }
}
