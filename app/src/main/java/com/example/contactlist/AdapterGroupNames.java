package com.example.contactlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AdapterAddedPhoneContacts;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterGroupNames extends RecyclerView.Adapter<AdapterGroupNames.ViewHolder> {

    ArrayList<ModelGroupName> group_list = new ArrayList<>();
    Context context;
    private OnDeleteClickListener onDeleteClickListener;
    GroupDATABASE groupDATABASE;

    public AdapterGroupNames(ArrayList<ModelGroupName> groupNameList) {
        this.group_list = groupNameList;
    }

    public AdapterGroupNames(Context context, ArrayList<ModelGroupName> group_list, OnDeleteClickListener onDeleteClickListener) {
        this.group_list = group_list;
        this.onDeleteClickListener = onDeleteClickListener;
        this.context = context;
    }

    public void setData(ArrayList<ModelGroupName> data) {
        this.group_list = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterGroupNames.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_name_item_layout, parent, false);
        groupDATABASE = new GroupDATABASE(context);
        return new AdapterGroupNames.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGroupNames.ViewHolder holder, int position) {
        ModelGroupName modelGroupName = group_list.get(position);
        String group_name = modelGroupName.getGroupName();
        String group_id = String.valueOf(modelGroupName.getId());


        //
        ArrayList<ContactModel> groupNumbersList;
        groupNumbersList = groupDATABASE.getAllDataOfGroupNumbers();
        Map<String, List<ContactModel>> groupedMap = new HashMap<>();
        //  String groupNameInspinner = groupNameSpinner.getSelectedItem().toString();
        for (ContactModel contactModel : groupNumbersList) {
            String groupName = contactModel.id;
            String grpid = contactModel.getId();

            if (groupedMap.containsKey(grpid)) {
                groupedMap.get(contactModel.id).add(contactModel);
            } else {
                // groupNameWiseList.add(contactModel);
                ArrayList<ContactModel> groupNameWiseList = new ArrayList<>();
                groupNameWiseList.add(contactModel);
                groupedMap.put(grpid, groupNameWiseList);
            }
        }

        ArrayList<ContactModel> group1Records = (ArrayList<ContactModel>) groupedMap.get(group_id);
        if (group1Records != null) {
            String sizeOfGroup = String.valueOf(group1Records.size());
            holder.tvsizeofgroupMembers.setText(sizeOfGroup);
        } else {
            holder.tvsizeofgroupMembers.setText("0");
        }

        //

        if (group_id != null) {
            holder.tvItemGroupName.setText(modelGroupName.getGroupName());
        }
        holder.ivItemDeleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(Integer.parseInt(group_id), group_name);
                }
                // groupDATABASE.deleteGroup(Integer.parseInt(group_id));

            }
        });
    }

    private void deleteItem(int position) {
        group_list.remove(position);
        notifyDataSetChanged();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int group_id, String group_name);
    }

    @Override
    public int getItemCount() {
        return group_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemGroupName, tvsizeofgroupMembers;
        ImageView ivItemDeleteicon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemGroupName = itemView.findViewById(R.id.tvItemGroupName);
            ivItemDeleteicon = itemView.findViewById(R.id.ivItemDeleteGroupName);
            tvsizeofgroupMembers = itemView.findViewById(R.id.tvsizeofgroupMembers);
        }
    }
}
