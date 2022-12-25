package com.example.batyaa.tulan_cosmetics.Users;

import android.content.Context;
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
import com.example.batyaa.tulan_cosmetics.Admin.tulanCategories;
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.example.batyaa.tulan_cosmetics.Users.category.usersProductCategory;
import com.example.batyaa.tulan_cosmetics.Users.category.usersTULANproductCategory;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.net.URLEncoder;

public class guestAboutUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView facebook, instagram , snapchat;
    ImageView fb, sc, ig;
    private LoginActivity login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_aboutus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        facebook = findViewById(R.id.tulanFacebookLink);
        instagram = findViewById(R.id.tulanInstagramLink);
        snapchat = findViewById(R.id.tulanSnapChatLink);

        fb = findViewById(R.id.faceLogo);
        sc = findViewById(R.id.snapLogo);
       // ig = findViewById(R.id.invisible);
    }

    /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }*/

    public void moveToFacebook(View view)
    {
            Intent facebookIntent = openFacebook(guestAboutUsActivity.this);
            startActivity(facebookIntent);
    }
    public  static  Intent openFacebook(Context ctx)
    {
        try
        {
            ctx.getPackageManager().getPackageInfo("com.facebook.katana",0);
            return  new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/376227335860239"));
        }
        catch (Exception e)
        {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://web.facebook.com/TULAN-113774045804362/"));
        }
    }

    public void DevFace(View view)
    {
        Intent facebookIntent = ourFacaebook(guestAboutUsActivity.this);
        startActivity(facebookIntent);
    }
    public static Intent ourFacaebook(Context ctx)
    {
        try
        {
            ctx.getPackageManager().getPackageInfo("com.facebook.katana",0);
            return  new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/376227335860239"));
        }
        catch (Exception e)
        {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://web.facebook.com/AJForProgramming/?ref=bookmarks"));
        }
    }


    public void makeCall(View view) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:+962778110011"));
        startActivity(i);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //  @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_productCategory_guest)
        {
            Intent add = new Intent(guestAboutUsActivity.this, usersProductCategory.class);
            startActivity(add);
        }
        if (id == R.id.nav_items_tulanCatgeory_guest)
        {
            Intent logout = new Intent(guestAboutUsActivity.this, usersTULANproductCategory.class);
            startActivity(logout);
            //page navigation animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (id == R.id.aboutUsGuest) {
            Intent itemP = new Intent(guestAboutUsActivity.this, guestAboutUsActivity.class);
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
                        Toast.makeText(guestAboutUsActivity.this, "Enter All the correct information please", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setView(mView);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (id == R.id.feedback)
        {
            Intent logout = new Intent(guestAboutUsActivity.this, guestFeedbackActivity.class);
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
            Intent leave= new Intent(guestAboutUsActivity.this, LoginActivity.class);
            startActivity(leave);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
