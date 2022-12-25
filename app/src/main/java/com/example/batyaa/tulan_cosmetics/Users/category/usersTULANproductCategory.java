package com.example.batyaa.tulan_cosmetics.Users.category;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.batyaa.tulan_cosmetics.Adapters.CategoryRecyclerAdapter;
import com.example.batyaa.tulan_cosmetics.Adapters.RecyclerAdapter;
import com.example.batyaa.tulan_cosmetics.Adapters.usersTULANCategoryRecyclerAdapter;
import com.example.batyaa.tulan_cosmetics.Classes.category;
import com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY.adminAddAboutUS;
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.example.batyaa.tulan_cosmetics.Users.guestAboutUsActivity;
import com.example.batyaa.tulan_cosmetics.Users.guestFeedbackActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class usersTULANproductCategory extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener
{

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
        setContentView(R.layout.activity_users_tulanproduct_category);

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
        usersTULANCategoryRecyclerAdapter adapter = new usersTULANCategoryRecyclerAdapter(this, mProducts);
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


        return super.onOptionsItemSelected(item);
    }

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
                        Toast.makeText(usersTULANproductCategory.this, "Enter All the correct information please", Toast.LENGTH_SHORT).show();
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


    //*************************************************************************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    //*************************************************************************************************************************************

}
