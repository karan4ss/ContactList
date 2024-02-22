package com.example;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.ContactAddedModelClass;
import com.example.contactlist.ContactModel;
import com.example.contactlist.R;

import java.util.ArrayList;

public class AdapterAddedPhoneContacts extends RecyclerView.Adapter<AdapterAddedPhoneContacts.ViewHolder> {
    // public static AdapterAddedPhoneContacts.OnDeleteClickListener OnDeleteClickListener;
    Activity activity;
    ArrayList<ContactModel> arrayListOfAddeduser;
    private OnDeleteClickListener onDeleteClickListener;

    public AdapterAddedPhoneContacts(Activity activity, ArrayList<ContactModel> arrayListofaddedUsers) {
        this.activity = activity;
        this.arrayListOfAddeduser = arrayListofaddedUsers;
        notifyDataSetChanged();
    }

    public AdapterAddedPhoneContacts(Activity activity, ArrayList<ContactModel> arrayListOfAddeduser, OnDeleteClickListener onDeleteClickListener) {
        this.activity = activity;
        this.arrayListOfAddeduser = arrayListOfAddeduser;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mainpage_recycler_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactModel contactModel = arrayListOfAddeduser.get(position);
        String groupNumberId = contactModel.getGrpnumberid();
        holder.tvItemName.setText(contactModel.getName());
        holder.tvItemNumber.setText(contactModel.getNumber());

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  deleteItem(position);
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(Integer.parseInt(groupNumberId));
                }


            }
        });


    }

    private void deleteItem(int position) {
        arrayListOfAddeduser.remove(position);
        notifyDataSetChanged();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }


    @Override
    public int getItemCount() {
        if (arrayListOfAddeduser == null) {
            return 0;
        } else {
            return arrayListOfAddeduser.size();
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemNumber;
        ImageView ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemNumber = itemView.findViewById(R.id.tvItemNumber);
            ivDelete = itemView.findViewById(R.id.ivdelete);
            /*ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener!=null){
                        onDeleteClickListener.onDeleteClick(getAdapterPosition());
                    }
                }
            });*/
        }
    }
}
