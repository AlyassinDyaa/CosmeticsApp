package com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.batyaa.tulan_cosmetics.Adapters.RecyclerAdapter;
import com.example.batyaa.tulan_cosmetics.Admin.DetailActivity;
import com.example.batyaa.tulan_cosmetics.Classes.Product;
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.example.batyaa.tulan_cosmetics.ViewHolders.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class tulanItemsActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener ,NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Product> mProducts;
    DatabaseReference mdb;
    Button searched;

    private void openDetailActivity(String[] data) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("NAME_KEY", data[0]);
        intent.putExtra("DESCRIPTION_KEY", data[1]);
        intent.putExtra("IMAGE_KEY", data[2]);
        intent.putExtra("CATEGORY_KEY", data[3]);
        intent.putExtra("PRICE_KEY", data[4]);
        intent.putExtra("SIZE_KEY", data[5]);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_);

        //**** Setting up the Sort from Newest to oldest
        sharedPreferences = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = sharedPreferences.getString("Sort", "Newest");

        if (mSorting.equals("Newest")) {
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
        mAdapter = new RecyclerAdapter(tulanItemsActivity.this, mProducts);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(tulanItemsActivity.this);

        mStorage = getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("tulan_products_uploaded");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mProducts.clear();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product upload = productSnapshot.getValue(Product.class);
                    upload.setKey(productSnapshot.getKey());
                    mProducts.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(tulanItemsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }


    public void onItemClick(int position) {
        Product clickedProduct = mProducts.get(position);
        String[] productData = {clickedProduct.getName(), clickedProduct.getDescription(), clickedProduct.getImageUrl(), clickedProduct.getCatagory(), clickedProduct.getPrice(), clickedProduct.getSize(), clickedProduct.getStock()};
        openDetailActivity(productData);
    }

    ///*******************************************************************************
    @Override
    public void onShowItemClick(int position) {
        Product clickedProduct = mProducts.get(position);
        String[] productData = {clickedProduct.getName(), clickedProduct.getDescription(), clickedProduct.getImageUrl(), clickedProduct.getCatagory(), clickedProduct.getPrice(), clickedProduct.getSize(), clickedProduct.getStock()};
        openDetailActivity(productData);
    }

    //*************************************************************************************************************************************
    // *************CHECK HERE FOR DELETE FAILURE ***************************
    @Override
    public void onDeleteItemClick(int position) {
        Product selectedItem = mProducts.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(tulanItemsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //*************************************************************************************************************************************
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
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
                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getDescription(), model.getImageUrl(), model.getPrice(), model.getCatagory(), model.getSize(), model.getStock());
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
                        Intent intent = new Intent(view.getContext(), DetailActivity.class);
                        intent.putExtra("image", retImage);
                        intent.putExtra("brand", retBrandName);
                        intent.putExtra("description", retDescription);
                        intent.putExtra("price", retPrice);
                        intent.putExtra("category", retcategory);
                        intent.putExtra("size", retSize);
                        intent.putExtra("stock", retStock);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        //TODO OMY OWN IMPLEMENTATION
                        final String currentBrandName = getItem(position).getName();
                        final String currentdescription = getItem(position).getDescription();
                        final String currentPrice = getItem(position).getPrice();
                        final String currentCategory = getItem(position).getCatagory();
                        final String currentImage = getItem(position).getImageUrl();
                        final String currentSize = getItem(position).getSize();
                        final String currrentStock = getItem(position).getStock();

                        //send to delete Dialog
                        showDeleteDialog(currentBrandName, currentdescription, currentImage, currentPrice, currentCategory, currentSize, currrentStock);

                                /*
                                //**** Show Alert dialog for options
                                AlertDialog.Builder builder = new AlertDialog.Builder(ItemsActivity.this);
                                    String[] options = {"Update Product", "Delete Product"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                 //**** Handle the dialog items
                                            if(which == 0) //== 0 is Update
                                            {
                                                //Update has been Clicked
                                                //Start Activity with putting current Data
                                                Intent intent = new Intent(ItemsActivity.this, adminAddActivity.class);
                                                intent.putExtra("Brand Name", currentBrandName);
                                                intent.putExtra("Description", currentdescription);
                                                intent.putExtra("Price", currentPrice);
                                                intent.putExtra("Category",currentCategory);
                                                intent.putExtra("Image",currentImage);
                                                startActivity(intent);
                                            }
                                            if(which == 1) // == 1 is Delete
                                            {
                                                //send to delete Dialog
                                                showDeleteDialog(currentBrandName, currentdescription, currentImage, currentPrice, currentCategory);
                                            }
                                        }
                                    });
                                    builder.create().show();
                                    */
                    }

                });
                return viewHolder;
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //**********************************************************************************************************************************************
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Product, ViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Product, ViewHolder>(Product.class, R.layout.row_model, ViewHolder.class, mDatabaseRef) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Product model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getDescription(), model.getImageUrl(), model.getPrice(), model.getCatagory(), model.getSize(), model.getStock());
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
                        Intent intent = new Intent(view.getContext(), DetailActivity.class);
                        intent.putExtra("image", retImage);
                        intent.putExtra("brand", retBrandName);
                        intent.putExtra("description", retDescription);
                        intent.putExtra("price", retPrice);
                        intent.putExtra("category", retcategory);
                        intent.putExtra("size", retSize);
                        intent.putExtra("stock", retStock);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        final String currentBrandName = getItem(position).getName();
                        final String currentdescription = getItem(position).getDescription();
                        final String currentPrice = getItem(position).getPrice();
                        final String currentCategory = getItem(position).getCatagory();
                        final String currentImage = getItem(position).getImageUrl();
                        final String currentSize = getItem(position).getSize();
                        final String currentStock = getItem(position).getStock();

                        //send to delete Dialog
                        showDeleteDialog(currentBrandName, currentdescription, currentImage, currentPrice, currentCategory, currentSize, currentStock);

/*
                                    //**** Show Alert dialog for options
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemsActivity.this);
                                    String[] options = {"Update Product", "Delete Product"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //**** Handle the dialog items
                                            if(which == 0) //== 0 is Update
                                            {
                                                //Update has been Clicked
                                                //Start Activity with putting current Data
                                                Intent intent = new Intent(ItemsActivity.this, adminAddActivity.class);
                                                intent.putExtra("Brand Name", currentBrandName);
                                                intent.putExtra("Description", currentdescription);
                                                intent.putExtra("Price", currentPrice);
                                                intent.putExtra("Category",currentCategory);
                                                intent.putExtra("Image",currentImage);
                                                startActivity(intent);
                                            }
                                            if(which == 1) // == 1 is Delete
                                            {
                                                //send to delete Dialog
                                                showDeleteDialog(currentBrandName, currentdescription, currentImage, currentPrice, currentCategory);
                                            }
                                        }
                                    });
                                    builder.create().show();
                                    */
                    }
                });
                return viewHolder;
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter1);
    }

    //*************************************************************************************************************************************
    //**** DELETE A PRODUCT
    private void showDeleteDialog(final String currentBrandName, String currentdescription, final String currentImage, String currentPrice, String currentCategory, String currentSize, String currentStock) {
        AlertDialog.Builder builder = new AlertDialog.Builder(tulanItemsActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to remove this product?");

        //Set if answer is YES
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //user pressed yes intends to delete post
                Query mQuery = mDatabaseRef.orderByChild("name").equalTo(currentBrandName);
                mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();    // remove Values from Firebase where the Brand Name Matches
                        }
                        //shwoing a message that the post(s) are deleted
                        Toast.makeText(tulanItemsActivity.this, "Product Deleted Successfully..", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // if anything goes wrong
                        Toast.makeText(tulanItemsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                //delete image by using the reference URL from Firebase
                StorageReference mImageRefe = getInstance().getReferenceFromUrl(currentImage);
                mImageRefe.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Delete was successful
                        Toast.makeText(tulanItemsActivity.this, "Item was Deleted successfully...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Unable to delete
                        Toast.makeText(tulanItemsActivity.this, "Can't delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //set if answer is NO
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //user pressed no does not want to delete
                dialog.dismiss();
            }
        });
        //**** SHOW THE DIALOG
        builder.create().show();
        ;
    }

    //*************************************************************************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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

    //*************************************************************************************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.nav_admin_Category) {
           /* Intent itemP = new Intent(tulanItemsActivity.this, CatgeoryActivity.class);
            startActivity(itemP);*/
        }
        if (id == R.id.action_sort) {
            showSortDialog();
            return true;
        }
        if(id == R.id.action_add)
        {
            startActivity(new Intent(tulanItemsActivity.this, adminTulanAddActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //*************************************************************************************************************************************
    private void showSortDialog() {
        String[] sortOpyions = {"Newest", "Oldest"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By").setIcon(R.drawable.ic_sort_black_24dp).setItems(sortOpyions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Sort", "Newest");
                    editor.apply();
                    recreate();
                } else if (which == 1) {
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.logout) {
            Intent logout = new Intent(tulanItemsActivity.this, LoginActivity.class);
            startActivity(logout);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/*
    public void fireBaseProductSearch(String textInput)
    {
        Query firebaseSearchQuery = mDatabaseRef.orderByChild("catagory").startAt(textInput).endAt((textInput) + "\uf8ff");

        FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class, R.layout.row_model, ProductViewHolder.class, firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.setDetails(getApplicationContext() ,model.getName(), model.getDescription(), model.getPrice(), model.getCatagory(), model.getImageUrl());
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ProductViewHolder extends  RecyclerView.ViewHolder{

        View mView;

        public ProductViewHolder(View ViewItem)
        {
            super(ViewItem);
            mView = ViewItem;
        }
        public void setDetails(Context ctx,String name, String descriptions, String price, String Cate, String imageP)
        {
            TextView productBrandName = (TextView) mView.findViewById(R.id.nameTextView);
            TextView productDescriptiom = (TextView) mView.findViewById(R.id.descriptionTextView);
            TextView productPrice = (TextView) mView.findViewById(R.id.priceRTextView);
            TextView productCategory = (TextView) mView.findViewById(R.id.categoryTextView);
            ImageView productImage = (ImageView) mView.findViewById(R.id.cardImageView);


            productBrandName.setText(name);
            productDescriptiom.setText(descriptions);
            productPrice.setText(price);
            productCategory.setText(Cate);
            Picasso.get().load(imageP).placeholder(R.drawable.load).fit().centerCrop().into(productImage);
        }
    }
*/
}