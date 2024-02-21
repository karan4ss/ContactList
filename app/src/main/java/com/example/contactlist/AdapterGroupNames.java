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
        if (group_name != null) {
            holder.tvItemGroupName.setText(modelGroupName.getGroupName());
        }
        holder.ivItemDeleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(Integer.parseInt(group_id));
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
        void onDeleteClick(int group_id);
    }

    @Override
    public int getItemCount() {
        return group_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemGroupName;
        ImageView ivItemDeleteicon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemGroupName = itemView.findViewById(R.id.tvItemGroupName);
            ivItemDeleteicon = itemView.findViewById(R.id.ivItemDeleteGroupName);
        }
    }
}
