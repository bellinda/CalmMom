package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Theme;

import java.util.List;
import java.util.Locale;

public class ThemesAdapter extends RecyclerView.Adapter<ThemesAdapter.ViewHolder> {

    private List<Theme> themes;
    private Context context;

    public ThemesAdapter(List<Theme> themes, Context context) {
        this.themes = themes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.themes_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Theme theme = themes.get(position);

        if (theme.getTitleEn() != null && !theme.getTitleEn().isEmpty()) {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("bg")) {
                holder.mTitleView.setText(theme.getTitle());
            } else {
                holder.mTitleView.setText(theme.getTitleEn());
            }
        } else {
            holder.mTitleView.setText(theme.getTitle());
        }

        holder.mAuthorView.setText(theme.getAuthor());
        holder.mDateView.setText(theme.getLastAnsweredOn());
        if (theme.getAnswers() != null) {
            holder.mAnswersCountView.setText(String.format(Locale.getDefault(), "%d", theme.getAnswers().size()));
        } else {
            holder.mAnswersCountView.setText("0");
        }
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleView;
        private TextView mAuthorView;
        private TextView mDateView;
        private TextView mAnswersCountView;

        public ViewHolder(View view) {
            super(view);

            mTitleView = (TextView) view.findViewById(R.id.title_view);
            mAuthorView = (TextView) view.findViewById(R.id.author_view);
            mDateView = (TextView) view.findViewById(R.id.date_view);
            mAnswersCountView = (TextView) view.findViewById(R.id.answers_count_view);
        }
    }
}
