package com.example.batyaa.tulan_cosmetics.Admin;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY.adminAddAboutUS;
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener
{
    TextView nameDetailTextView, descriptionDetailTextView, categoryDetailTextView, priceDetailTextView, sizeDetailTextView, stockTextView;
    ImageView mDetailImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nameDetailTextView = findViewById(R.id.nameDetailTextView);
        descriptionDetailTextView = findViewById(R.id.descriptionDetailTextViewDetails);
        priceDetailTextView = findViewById(R.id.priceRTextViewDetails);
        categoryDetailTextView = findViewById(R.id.categoryTextViewDetails);
        mDetailImageView = findViewById(R.id.DetailImageView);
        sizeDetailTextView = findViewById(R.id.sizeDetailTextView);
        stockTextView = findViewById(R.id.stockTextDetailview);
//RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT


        //**** Finding ID's from Detail Layout
        //**** Initialize the Views
        nameDetailTextView =  findViewById(R.id.nameDetailTextView);
        descriptionDetailTextView =  findViewById(R.id.descriptionDetailTextViewDetails);
        priceDetailTextView = findViewById(R.id.priceRTextViewDetails);
        categoryDetailTextView =  findViewById(R.id.categoryTextViewDetails);
        mDetailImageView =  findViewById(R.id.DetailImageView);
        sizeDetailTextView = findViewById(R.id.sizeDetailTextView);
        stockTextView = findViewById(R.id.stockTextDetailview);

        //**** Get data from Intents
        String useBrandName = getIntent().getStringExtra("brand");
        String useImage = getIntent().getStringExtra("image");
        String useDescription = getIntent().getStringExtra("description");
        String usePrice = getIntent().getStringExtra("price");
        String useCategory = getIntent().getStringExtra("category");
        String useSize = getIntent().getStringExtra("size");
        String useStock = getIntent().getStringExtra("stock");

        //**** Set data to the view
        nameDetailTextView.setText(useBrandName);
        Picasso.get().load(useImage).placeholder(R.drawable.load).fit().centerCrop().into(mDetailImageView);
        descriptionDetailTextView.setText(useDescription);
        priceDetailTextView.setText(usePrice);
        categoryDetailTextView.setText(useCategory);
        sizeDetailTextView.setText(useSize);
        stockTextView.setText(useStock);

        //**** Navigation Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        //**** Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_admin_Category)
        {
            Intent itemP = new Intent(DetailActivity.this, CatgeoryActivity.class);
            startActivity(itemP);
        }
        if (id == R.id.nav_admin_tulan_Category) {
            Intent itemP = new Intent(DetailActivity.this, tulanCategories.class);
            startActivity(itemP);
        }

        if (id == R.id.logout)
        {
            Intent logout = new Intent(DetailActivity.this, LoginActivity.class);
            startActivity(logout);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        return super.onOptionsItemSelected(item);
    }
}
