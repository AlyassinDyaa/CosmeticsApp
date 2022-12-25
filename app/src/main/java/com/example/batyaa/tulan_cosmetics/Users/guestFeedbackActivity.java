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
import android.widget.TextView;
import android.widget.Toast;

import com.example.batyaa.tulan_cosmetics.Admin.CatgeoryActivity;
import com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY.guestTulanListActivity;
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.example.batyaa.tulan_cosmetics.Users.category.usersProductCategory;
import com.example.batyaa.tulan_cosmetics.Users.category.usersTULANproductCategory;

import java.net.URLEncoder;

public class guestFeedbackActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText email, message ,subject;
    Button mBtn;

    TextView call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_feedback);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email =(EditText)findViewById(R.id.email);
        message = (EditText)findViewById(R.id.insertMessage);
        subject = (EditText) findViewById(R.id.InsertSubject);
        mBtn = (Button)findViewById(R.id.sendButton);


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    private void sendMail()
    {
        String recipientList = email.getText().toString();
        String[] recipients = {"tulandeiranieh@gmail.com"};

        String subjects = subject.getText().toString();
        String fullMessage = message.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subjects);
        intent.putExtra(Intent.EXTRA_TEXT, fullMessage);

        intent.setType("message/rfc822");
        startActivity(intent.createChooser(intent,"Choose an app"));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


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

        return super.onOptionsItemSelected(item);
    }

    //  @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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
        if (id == R.id.aboutUsGuest) {
            Intent itemP = new Intent(guestFeedbackActivity.this, guestAboutUsActivity.class);
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
                        Toast.makeText(guestFeedbackActivity.this, "Enter All the correct information please", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setView(mView);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (id == R.id.feedback)
        {
            Intent logout = new Intent(guestFeedbackActivity.this, guestFeedbackActivity.class);
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
            Intent leave= new Intent(guestFeedbackActivity.this, LoginActivity.class);
            startActivity(leave);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
