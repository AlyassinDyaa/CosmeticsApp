package com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.Classes.Product;
import com.example.batyaa.tulan_cosmetics.R;
import com.example.batyaa.tulan_cosmetics.Adapters.RecyclerAdapter;
import com.example.batyaa.tulan_cosmetics.Users.guestAboutUsActivity;
import com.example.batyaa.tulan_cosmetics.Users.guestDetailActivity;
import com.example.batyaa.tulan_cosmetics.Users.guestFeedbackActivity;
import com.example.batyaa.tulan_cosmetics.ViewHolders.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class guestListActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener ,NavigationView.OnNavigationItemSelectedListener {

    GridLayoutManager gridLayoutManager;
    SharedPreferences sharedPreferences;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Product> mProducts;

    private void openDetailActivity(String[] data)
    {
        Intent intent = new Intent(this, guestDetailActivity.class);
        intent.putExtra("NAME_KEY",data[0]);
        intent.putExtra("DESCRIPTION_KEY",data[1]);
        intent.putExtra("IMAGE_KEY",data[2]);
        intent.putExtra("CATEGORY_KEY",data[3]);
        intent.putExtra("PRICE_KEY",data[4]);
        intent.putExtra("SIZE_KEY",data[5]);
        intent.putExtra("STOCK KEY", data[6]);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_list);

        sharedPreferences = getSharedPreferences("SortSettings",MODE_PRIVATE);
        String mSorting = sharedPreferences.getString("Sort", "Newest");

        if(mSorting.equals("Newest"))
        {
            /*
            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);*
            */
            gridLayoutManager = new GridLayoutManager(guestListActivity.this,3);
            gridLayoutManager.setReverseLayout(true);
        }



        mRecyclerView = findViewById(R.id.mRecyclerView);


        RecyclerAdapter mAdapter = new RecyclerAdapter(this,mProducts);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setAdapter(mAdapter);


        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mProducts = new ArrayList<>();
        //mAdapter = new RecyclerAdapter(guestListActivity.this, mProducts);
      //  mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(guestListActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("all_items");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                mProducts.clear();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren())
                {
                    Product upload = productSnapshot.getValue(Product.class);
                    upload.setKey(productSnapshot.getKey());
                    mProducts.add(upload);
                }
                //mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(guestListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }




    // *************CHECK HERE FOR DELETE FAILURE ***************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               fireBaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fireBaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        if(id == R.id.action_sort)
        {
            showSortDialog();
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showSortDialog()
    {
        String[] sortOpyions = {"Newest","Oldest"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By").setIcon(R.drawable.ic_sort_black_24dp).setItems(sortOpyions, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(which == 0)
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Sort", "Newest");
                    editor.apply();
                    recreate();
                }
                else if(which == 1)
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Sort", "Oldest");
                    editor.apply();
                    recreate();
                }
            }
        });
        builder.show();
    }

    private void fireBaseSearch(final String message) {

        //If category was all Caps and user input text in search Bar in lower or in caps then the category will show
        // BUT the category must all be in caps from addition of admin
        String query = message.toLowerCase();

        Query firebaseSearchQuery = mDatabaseRef.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerAdapter<Product, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ViewHolder>(Product.class, R.layout.row_model, ViewHolder.class, firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Product model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getDescription(), model.getImageUrl(), model.getPrice(), model.getCatagory(),model.getSize(),model.getStock());
            }
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //**** Get Data from Firebase
                        String retBrandName = getItem(position).getName();
                        String retDescription = getItem(position).getDescription();
                        String retImage = getItem(position).getImageUrl();
                        String retPrice = getItem(position).getPrice();
                        String retcategory = getItem(position).getCatagory();
                        String retSize = getItem(position).getSize();
                        String retStock = getItem(position).getStock();

                        //Pass the data to teh Detail Activity
                        Intent intent = new Intent(view.getContext(), guestDetailActivity.class);
                        intent.putExtra("image", retImage);
                        intent.putExtra("brand", retBrandName);
                        intent.putExtra("description", retDescription);
                        intent.putExtra("price" , retPrice);
                        intent.putExtra("category", retcategory);
                        intent.putExtra("size", retSize);
                        intent.putExtra("stock" , retStock);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        //TODO OMY OWN IMPLEMENTATION
                    }
                });
                return  viewHolder;
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerAdapter<Product, ViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Product, ViewHolder>(Product.class, R.layout.row_model, ViewHolder.class,mDatabaseRef)
        {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Product model, int position)
            {
                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getDescription(), model.getImageUrl(), model.getPrice(), model.getCatagory(), model.getSize(),model.getStock());
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        //**** Get Data from Firebase
                        String retBrandName = getItem(position).getName();
                        String retDescription = getItem(position).getDescription();
                        String retImage = getItem(position).getImageUrl();
                        String retPrice = getItem(position).getPrice();
                        String retcategory = getItem(position).getCatagory();
                        String retSize = getItem(position).getSize();
                        String retStock = getItem(position).getStock();
                        //Pass the data to teh Detail Activity
                        Intent intent = new Intent(view.getContext(), guestDetailActivity.class);
                        intent.putExtra("image", retImage);
                        intent.putExtra("brand", retBrandName);
                        intent.putExtra("description", retDescription);
                        intent.putExtra("price" , retPrice);
                        intent.putExtra("category", retcategory);
                        intent.putExtra("size", retSize);
                        intent.putExtra("stock" , retStock);

                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position)
                    {
                        //TODO OMY OWN IMPLEMENTATION
                    }
                });
                return  viewHolder;
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter1);
    }
    //  @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.aboutUsGuest)
        {
            Intent itemP = new Intent(guestListActivity.this, guestAboutUsActivity.class);
            startActivity(itemP);
            //page navigation animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (id == R.id.feedback)
        {
            Intent logout = new Intent(guestListActivity.this, guestFeedbackActivity.class);
            startActivity(logout);
            //page navigation animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (id == R.id.share)
        {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            String shareBody = "Your body here";
            String shareSub = "Your Subject here";
            share.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            share.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(share,"Share using"));
        }
        if (id == R.id.leave)
        {
            Intent leave= new Intent(guestListActivity.this, LoginActivity.class);
            startActivity(leave);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onItemClick(int position)
    {
        Product clickedProduct = mProducts.get(position);
        String[] productData={clickedProduct.getName(), clickedProduct.getDescription(), clickedProduct.getImageUrl(),  clickedProduct.getCatagory(), clickedProduct.getPrice()};
        openDetailActivity(productData);
    }

    @Override
    public void onShowItemClick(int position)
    {
        Product clickedProduct = mProducts.get(position);
        String[] productData = {clickedProduct.getName(), clickedProduct.getDescription(), clickedProduct.getImageUrl(),  clickedProduct.getCatagory(), clickedProduct.getPrice()};
        openDetailActivity(productData);
    }
    @Override
    public void onDeleteItemClick(int position)
    {
    }

}
