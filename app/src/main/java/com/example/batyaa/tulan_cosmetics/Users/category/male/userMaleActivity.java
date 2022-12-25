package com.example.batyaa.tulan_cosmetics.Users.category.male;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.batyaa.tulan_cosmetics.Adapters.RecyclerAdapter;
import com.example.batyaa.tulan_cosmetics.Classes.Product;
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.example.batyaa.tulan_cosmetics.Users.category.female.usersFemaleActivity;
import com.example.batyaa.tulan_cosmetics.Users.category.usersProductCategory;
import com.example.batyaa.tulan_cosmetics.Users.category.usersTULANproductCategory;
import com.example.batyaa.tulan_cosmetics.Users.guestAboutUsActivity;
import com.example.batyaa.tulan_cosmetics.Users.guestDetailActivity;
import com.example.batyaa.tulan_cosmetics.Users.guestFeedbackActivity;
import com.example.batyaa.tulan_cosmetics.ViewHolders.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class userMaleActivity extends AppCompatActivity  implements RecyclerAdapter.OnItemClickListener ,NavigationView.OnNavigationItemSelectedListener{


    SharedPreferences sharedPreferences;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Product> mProducts;

    private void openDetailActivity(String[] data){
        Intent intent = new Intent(this, guestDetailActivity.class);
        intent.putExtra("NAME_KEY",data[0]);
        intent.putExtra("DESCRIPTION_KEY",data[1]);
        intent.putExtra("IMAGE_KEY",data[2]);
        intent.putExtra("CATEGORY_KEY",data[3]);
        intent.putExtra("PRICE_KEY",data[4]);
        intent.putExtra("SIZE_KEY", data[5]);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_female);

        //**** Setting up the Sort from Newest to oldest
        sharedPreferences = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = sharedPreferences.getString("Sort", "Newest");

        if (mSorting.equals("Newest"))
        {
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
        } else if (mSorting.equals("Oldest")) {

            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(false);
        }
        //***************************************************

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager (this));
        mRecyclerView.setLayoutManager(linearLayoutManager);


        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mProducts = new ArrayList<>();
        mAdapter = new RecyclerAdapter(userMaleActivity.this, mProducts);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(userMaleActivity.this);

        mStorage = getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("male");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



    }


    public void onItemClick(int position)
    {
        Product clickedProduct = mProducts.get(position);
        String[] productData={clickedProduct.getName(), clickedProduct.getDescription(), clickedProduct.getImageUrl(),  clickedProduct.getCatagory(), clickedProduct.getPrice() , clickedProduct.getSize(), clickedProduct.getStock()};
        openDetailActivity(productData);
    }
    ///*******************************************************************************
    @Override
    public void onShowItemClick(int position)
    {
        Product clickedProduct = mProducts.get(position);
        String[] productData = {clickedProduct.getName(), clickedProduct.getDescription(), clickedProduct.getImageUrl(),  clickedProduct.getCatagory(), clickedProduct.getPrice(), clickedProduct.getSize(), clickedProduct.getStock()};
        openDetailActivity(productData);
    }
    //*************************************************************************************************************************************
    // *************CHECK HERE FOR DELETE FAILURE ***************************
    @Override
    public void onDeleteItemClick(int position)
    {
        Product selectedItem = mProducts.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(userMaleActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //*************************************************************************************************************************************
    protected void onDestroy() {
        super.onDestroy();

    }
    //*************************************************************************************************************************************
    private void fireBaseSearch(final String message) {

        //If category was all Caps and user input text in search Bar in lower or in caps then the category will show
        // BUT the category must all be in caps from addition of admin
        String query = message.toLowerCase();

        Query firebaseSearchQuery = mDatabaseRef.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerAdapter<Product, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ViewHolder>(Product.class, R.layout.row_model, ViewHolder.class, firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Product model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getDescription(), model.getImageUrl(), model.getPrice(), model.getCatagory(),model.getSize(), model.getStock());
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
                        Intent intent = new Intent(view.getContext(),guestDetailActivity.class);
                        intent.putExtra("image", retImage);
                        intent.putExtra("brand", retBrandName);
                        intent.putExtra("description", retDescription);
                        intent.putExtra("price" , retPrice);
                        intent.putExtra("category", retcategory);
                        intent.putExtra("size",retSize);
                        intent.putExtra("stock", retStock);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }

                });
                return  viewHolder;
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    //**********************************************************************************************************************************************
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<Product, ViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Product, ViewHolder>(Product.class, R.layout.row_model, ViewHolder.class,mDatabaseRef)
        {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Product model, int position)
            {
                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getDescription(), model.getImageUrl(), model.getPrice(), model.getCatagory(),model.getSize(), model.getStock());
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
                    }
                });
                return  viewHolder;
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fireBaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                fireBaseSearch(newText);
                return false;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //*************************************************************************************************************************************
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //*************************************************************************************************************************************
    private void showSortDialog()
    {
        String[] sortOpyions = {"Newest","Oldest"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By").setIcon(R.drawable.ic_sort_black_24dp).setItems(sortOpyions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
    //*************************************************************************************************************************************
    //  @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_productCategory_guest)
        {
            Intent add = new Intent(this, usersProductCategory.class);
            startActivity(add);
        }
        if (id == R.id.nav_items_tulanCatgeory_guest)
        {
            Intent logout = new Intent(this, usersTULANproductCategory.class);
            startActivity(logout);
            //page navigation animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (id == R.id.aboutUsGuest) {
            Intent itemP = new Intent(this, guestAboutUsActivity.class);
            startActivity(itemP);
        }
        if (id == R.id.request)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.perfume_product_request, null);

            final EditText message = (EditText) mView.findViewById(R.id.requestMessageEditText);
            Button messageButton = (Button) mView.findViewById(R.id.sendRequestButton);

            messageButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(!message.getText().toString().isEmpty())
                    {
                        try
                        {
                            Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                            String url = "https://api.whatsapp.com/send?phone=" + "+962778110011" + "&text=" + URLEncoder.encode("Hello; " + "\n" + "Could you sell " + " " + message.getText().toString() , "UTF-8");
                            sendMsg.setPackage("com.whatsapp");
                            sendMsg.setData(Uri.parse(url));
                            if (sendMsg.resolveActivity(getPackageManager()) != null)
                            {
                                startActivity(sendMsg);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(userMaleActivity.this, "Enter All the correct information please", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setView(mView);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (id == R.id.feedback)
        {
            Intent logout = new Intent(this, guestFeedbackActivity.class);
            startActivity(logout);
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
            Intent leave= new Intent(this, LoginActivity.class);
            startActivity(leave);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
