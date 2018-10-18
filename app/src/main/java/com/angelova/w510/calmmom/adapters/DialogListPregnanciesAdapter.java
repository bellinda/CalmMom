package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.dialogs.ListPregnanciesDialog;
import com.angelova.w510.calmmom.models.Pregnancy;

import java.util.List;

public class DialogListPregnanciesAdapter extends RecyclerView.Adapter<DialogListPregnanciesAdapter.ViewHolder> {

    private List<Pregnancy> pregnanciesList;
    private ListPregnanciesDialog.DialogClickListener listener;
    private Context mContext;

    public DialogListPregnanciesAdapter(Context context, List<Pregnancy> pregnanciesList, ListPregnanciesDialog.DialogClickListener listener) {
        this.pregnanciesList = pregnanciesList;
        this.listener = listener;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Pregnancy pregnancy = pregnanciesList.get(position);
        if (pregnancy.getPregnancyOutcome() != null) {
            holder.documentView.setText(String.format("\u25ba %s %s", mContext.getString(R.string.dialog_list_pregnancies_item_text), pregnancy.getPregnancyOutcome().getDate()));
        } else {
            holder.documentView.setText(String.format("\u25ba %s %s", mContext.getString(R.string.dialog_list_pregnancies_estimated), pregnancy.getEstimatedDeliveryDate()));
        }
        holder.documentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(pregnancy);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pregnanciesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView documentView;

        public ViewHolder(View view) {
            super(view);
            documentView = (TextView) view.findViewById(R.id.list_item);
        }
    }
}
