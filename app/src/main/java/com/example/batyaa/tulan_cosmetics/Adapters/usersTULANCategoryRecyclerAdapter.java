package com.example.batyaa.tulan_cosmetics.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.batyaa.tulan_cosmetics.Classes.category;
import com.example.batyaa.tulan_cosmetics.R;
import com.example.batyaa.tulan_cosmetics.Users.category.tulanFemale.usersTulanFemaleLISTActivity;
import com.example.batyaa.tulan_cosmetics.Users.category.tulanMale.usersTulanMaleLISTActivity;
import com.example.batyaa.tulan_cosmetics.Users.category.tulanOffers.usersTulanOffersLISTActivity;
import com.example.batyaa.tulan_cosmetics.category.Offers.offersListActivity;
import com.example.batyaa.tulan_cosmetics.category.TulanMale.tulanMaleListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class usersTULANCategoryRecyclerAdapter extends RecyclerView.Adapter<usersTULANCategoryRecyclerAdapter.UsersTULANCategoryRecyclerViewHolder>
{

    private final Context mContext;
    private final List<category> products;
    private  RecyclerAdapter.OnItemClickListener mListener;

    public usersTULANCategoryRecyclerAdapter( Context mContext, List<category> products) {
        this.mContext = mContext;
        this.products = products;
    }

    @NonNull
    @Override
    public UsersTULANCategoryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.category_cardview, viewGroup, false);
        UsersTULANCategoryRecyclerViewHolder holder = new UsersTULANCategoryRecyclerViewHolder(view);
        return  new UsersTULANCategoryRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersTULANCategoryRecyclerViewHolder holder, final int i) {
        category product = products.get(i);
        holder.catTitleTextView.setText(product.getTitle());
        Picasso.get().load(product.getImage()).placeholder(R.drawable.load).fit().centerCrop().into(holder.catImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    Intent intent = new Intent(mContext, usersTulanMaleLISTActivity.class);
                    mContext.startActivity(intent);
                }
                if (i == 1) {
                    Intent intent = new Intent(mContext, usersTulanFemaleLISTActivity.class);
                    mContext.startActivity(intent);
                }
                if (i == 2) {
                    Intent intent = new Intent(mContext, usersTulanOffersLISTActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class UsersTULANCategoryRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener
    {
        public TextView catTitleTextView;
        public ImageView catImageView;

        public UsersTULANCategoryRecyclerViewHolder(View itemView)
        {
            super(itemView);
            catTitleTextView = itemView.findViewById ( R.id.catTitle_id );
            catImageView = itemView.findViewById(R.id.imageCategory);
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
}
