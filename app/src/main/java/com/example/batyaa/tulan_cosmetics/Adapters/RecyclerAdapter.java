package com.example.batyaa.tulan_cosmetics.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.batyaa.tulan_cosmetics.Classes.Product;
import com.example.batyaa.tulan_cosmetics.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public  class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>
{
    private Context mContext;
    private List<Product> products;
    private OnItemClickListener mListener;

    public RecyclerAdapter(Context context, List<Product> uploads)
    {
        mContext = context;
        products = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_model, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position)
    {
        Product currentProduct = products.get(position);
        holder.nameTextView.setText(currentProduct.getName());
        holder.descriptionTextView.setText(currentProduct.getDescription());
      //holder.dateTextView.setText(getDateToday());
        holder.priceTextView.setText(currentProduct.getPrice());
        holder.categoryTV.setText(currentProduct.getCatagory());

       //Log.e("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO",currentProduct.getImageUrl());
        Picasso.get().load(currentProduct.getImageUrl()).placeholder(R.drawable.load).fit().centerCrop().into(holder.ImageView);
    }

    @Override
    public int getItemCount()
    {
        return products.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener
    {
        public TextView nameTextView,descriptionTextView,dateTextView,priceTextView , categoryTV;
        public ImageView ImageView;

        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            nameTextView = itemView.findViewById ( R.id.nameTextView );
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
           // dateTextView = itemView.findViewById(R.id.dateTextView);
            ImageView = itemView.findViewById(R.id.cardImageView);
            priceTextView =itemView.findViewById(R.id.priceRTextView);
            categoryTV = itemView.findViewById(R.id.categoryTextView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
        {
            menu.setHeaderTitle("Select Action");
            MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
            if (mListener != null)
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    switch (item.getItemId())
                    {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }


}