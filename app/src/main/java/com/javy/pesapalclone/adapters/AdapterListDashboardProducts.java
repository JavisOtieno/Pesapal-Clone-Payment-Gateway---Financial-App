package com.javy.pesapalclone.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.javy.pesapalclone.R;
import com.javy.pesapalclone.models.Product;

import java.util.ArrayList;
import java.util.List;

public class AdapterListDashboardProducts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Product obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListDashboardProducts(Context context, List<Product> items) {
        this.items = items;
        ctx = context;
    }
    public AdapterListDashboardProducts(List<Product> items) {
        this.items = items;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView actualTarget;
        public TextView amount;
        public TextView status;
        public View lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            actualTarget = (TextView) v.findViewById(R.id.actualTarget);
            amount = (TextView) v.findViewById(R.id.amount);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            status = (TextView) v.findViewById(R.id.status);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_product, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Product p = items.get(position);
            view.name.setText(p.name);
//            view.date.setText(p.target_type);
            view.amount.setText(p.remaining_amount);
            view.actualTarget.setText("Total Value: "+p.target_amount);
//            view.status.setText(p.target_type);
//            view.date.setText(p.phone);

            //Tools.displayImageRound(ctx, view.image, p.image);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
