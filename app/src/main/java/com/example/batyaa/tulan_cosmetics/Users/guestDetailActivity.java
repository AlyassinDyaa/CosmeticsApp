package com.example.batyaa.tulan_cosmetics.Users;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batyaa.tulan_cosmetics.Admin.CatgeoryActivity;
import com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY.guestTulanListActivity;
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.example.batyaa.tulan_cosmetics.Users.category.usersProductCategory;
import com.example.batyaa.tulan_cosmetics.Users.category.usersTULANproductCategory;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;

public class guestDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView nameDetailTextView, descriptionDetailTextView, categoryDetailTextView, priceDetailTextView, sizeDetailTextView, stockDetailTextView;
    ImageView mDetailImageView;
    Button requestBtn;

    TextView inputUerText;
    public String retrieve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_detail);

        nameDetailTextView = findViewById(R.id.nameDetailTextView);
        descriptionDetailTextView = findViewById(R.id.descriptionDetailTextViewDetails);
        priceDetailTextView = findViewById(R.id.priceRTextViewDetails);
        categoryDetailTextView = findViewById(R.id.categoryTextViewDetails);
        mDetailImageView = findViewById(R.id.DetailImageView);
        sizeDetailTextView = findViewById(R.id.sizeDetailTextView);
        stockDetailTextView = findViewById(R.id.stockTextDetailview);
        requestBtn = findViewById(R.id.requestP);
        inputUerText = findViewById(R.id.textUsreInput);
//RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT


        //**** Finding ID's from Detail Layout
        //**** Initialize the Views
        nameDetailTextView = findViewById(R.id.nameDetailTextView);
        descriptionDetailTextView = findViewById(R.id.descriptionDetailTextViewDetails);
        priceDetailTextView = findViewById(R.id.priceRTextViewDetails);
        categoryDetailTextView = findViewById(R.id.categoryTextViewDetails);
        mDetailImageView = findViewById(R.id.DetailImageView);
        sizeDetailTextView = findViewById(R.id.sizeDetailTextView);
        stockDetailTextView = findViewById(R.id.stockTextDetailview);

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
        stockDetailTextView.setText(useStock);


        //**** Navigation Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        requestBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(guestDetailActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.alert_dialog_stockto_purchase, null);
                final EditText inputUser = (EditText) mView.findViewById(R.id.edit_amount);
                Button requestBtn = (Button) mView.findViewById(R.id.buttonRequest);

                requestBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        if (!inputUser.getText().toString().isEmpty())
                        {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(guestDetailActivity.this);
                            View mViewTwo = getLayoutInflater().inflate(R.layout.enter_address, null);

                            final TextView addressDialog = mViewTwo.findViewById(R.id.addressInput);
                            final TextView zipCodeDialog = mViewTwo.findViewById(R.id.zipCodeInput);
                            final TextView cityDialog = mViewTwo.findViewById(R.id.cityInput);
                            final TextView apartmentDialog = mViewTwo.findViewById(R.id.apartmentNumberInput);
                            final TextView mobileNumberDialog = mViewTwo.findViewById(R.id.phoneNumberInput);
                            Button requestItem = mViewTwo.findViewById(R.id.orderButton);

                            requestItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    if (!addressDialog.getText().toString().isEmpty() || cityDialog.getText().toString().isEmpty() || mobileNumberDialog.getText().toString().isEmpty())
                                    {
                                        try
                                        {
                                            Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                                            String url = "https://api.whatsapp.com/send?phone=" + "+962778110011" + "&text=" + URLEncoder.encode("Hello; " + "\n"   + "I would like to purchase " + inputUser.getText().toString() + " of " + nameDetailTextView.getText().toString() + " to this address please: "
                                                     + "\n" + addressDialog.getText().toString()
                                                     + "," + " " + cityDialog.getText().toString()
                                                     + "\n" + "Apartment Number: " + apartmentDialog.getText().toString()
                                                     + "\n" + "Zipcode: " + zipCodeDialog.getText().toString()
                                                     + "\n"  + "My telephone number is: "+ mobileNumberDialog.getText().toString(), "UTF-8");
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
                                        Toast.makeText(guestDetailActivity.this, "Enter All the correct information please", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            builder2.setView(mViewTwo);
                            AlertDialog dialog = builder2.create();
                            dialog.show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Enter a correct number of items", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                builder.setView(mView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        //**** Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_productCategory_guest) {
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
        if (id == R.id.aboutUsGuest)
        {
            Intent itemP = new Intent(guestDetailActivity.this, guestAboutUsActivity.class);
            startActivity(itemP);
            //page navigation animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                        Toast.makeText(guestDetailActivity.this, "Enter All the correct information please", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setView(mView);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (id == R.id.feedback)
        {
            Intent logout = new Intent(guestDetailActivity.this, guestFeedbackActivity.class);
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
            Intent leave= new Intent(guestDetailActivity.this, LoginActivity.class);
            startActivity(leave);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
