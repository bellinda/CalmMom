package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.dialogs.ListDialog;
import com.angelova.w510.calmmom.models.ExaminationDocument;

import java.util.List;

/**
 * Created by W510 on 27.7.2018 г..
 */

public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder> {

    private List<ExaminationDocument> documentsList;
    private ListDialog.DialogClickListener listener;

    public DialogListAdapter(List<ExaminationDocument> documentsList, ListDialog.DialogClickListener listener) {
        this.documentsList = documentsList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExaminationDocument doc = documentsList.get(position);
        holder.documentView.setText(String.format("\u25ba %s", doc.getName()) );
        holder.documentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(doc);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView documentView;

        public ViewHolder(View view) {
            super(view);
            documentView = (TextView) view.findViewById(R.id.list_item);
        }
    }
}
