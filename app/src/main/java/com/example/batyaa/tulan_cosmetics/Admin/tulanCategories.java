package com.example.batyaa.tulan_cosmetics.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.batyaa.tulan_cosmetics.Adapters.CategoryRecyclerAdapter;
import com.example.batyaa.tulan_cosmetics.Adapters.RecyclerAdapter;
import com.example.batyaa.tulan_cosmetics.Adapters.tulanCategoriesAdapter;
import com.example.batyaa.tulan_cosmetics.Classes.category;
import com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY.adminAddAboutUS;
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class tulanCategories extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;
    private CategoryRecyclerAdapter mAdapter;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<category> mProducts;
    public category i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tulan_categories);


        mProducts = new ArrayList<>();
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProducts.add(
                new category(
                        "MALE",
                        R.drawable.male,
                        0
                ));
        mProducts.add(
                new category(
                        "FEMALE",
                        R.drawable.female,
                        1
                ));
        mProducts.add(
                new category(
                        "OFFERS",
                        R.drawable.offers,
                        2
                ));
        tulanCategoriesAdapter adapter = new tulanCategoriesAdapter(this, mProducts);
        mRecyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onItemClick(int position)
    {

    }
    ///*******************************************************************************
    @Override
    public void onShowItemClick(int position)
    {

    }
    //*************************************************************************************************************************************
    // *************CHECK HERE FOR DELETE FAILURE ***************************
    @Override
    public void onDeleteItemClick(int position)
    {

    }
    //*************************************************************************************************************************************
    protected void onDestroy() {
        super.onDestroy();

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
        if(id == R.id.action_add)
        {
//            startActivity(new Intent(tulanCategories.this, adminAddCategoryActivity.class));
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_admin_Category) {
            Intent itemP = new Intent(this, CatgeoryActivity.class);
            startActivity(itemP);
        }
        if (id == R.id.nav_admin_tulan_Category) {
            Intent itemP = new Intent(this, tulanCategories.class);
            startActivity(itemP);
        }
        if (id == R.id.logout)
        {
            Intent logout = new Intent(this, LoginActivity.class);
            startActivity(logout);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //*************************************************************************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    //*************************************************************************************************************************************


}
