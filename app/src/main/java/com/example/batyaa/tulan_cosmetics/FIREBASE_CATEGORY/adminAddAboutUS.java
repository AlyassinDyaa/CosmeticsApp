package com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.batyaa.tulan_cosmetics.Admin.CatgeoryActivity;
import com.example.batyaa.tulan_cosmetics.Admin.tulanCategories;
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.example.batyaa.tulan_cosmetics.Users.guestAboutUsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class adminAddAboutUS extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST1 = 2;
    private static final int PICK_IMAGE_REQUEST2 = 3;

    EditText englishDescription , arabicDescription;
    ImageView IG, FB, SC;
    Button ig, sc, fb;
    Button updateBtn;
    private Uri one,two,three;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_about_us);

        //Initializing Views
        englishDescription = findViewById(R.id.enterAboutUsDescriptionEnglish);
        arabicDescription = findViewById(R.id.enterAboutUsDescriptionArabic);
        IG = findViewById(R.id.aboutUsChangeInstagramLogo);
        FB = findViewById(R.id.changeFacebookLogo);
        SC = findViewById(R.id.changeSnapChatLogo);
        updateBtn = findViewById(R.id.updateAboutUs);
        fb = findViewById(R.id.fbBtn);
        ig = findViewById(R.id.igBtn);
        sc = findViewById(R.id.scBtn);

        mStorageRef = getInstance().getReference("aboutUs");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("aboutUs");

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser1();
            }
        });

        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser2();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        //*******************************************************************************************************************************************************************
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
private String getFileExtension(Uri uri)
{
    ContentResolver cR = getContentResolver();
    MimeTypeMap mime = MimeTypeMap.getSingleton();
    return mime.getExtensionFromMimeType(cR.getType(uri));
}

    private void uploadFile()
    {
        if (one != null )
        {
            StorageReference storageReference = getInstance().getReference().child("aboutUs");

            final StorageReference imageFilePath = storageReference.child(one.getLastPathSegment());

            imageFilePath.putFile(one).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(adminAddAboutUS.this, "Product Upload successful", Toast.LENGTH_LONG).show();

                               // aboutUsUpdate upload = new aboutUsUpdate( uri.toString());
                                String uploadId = mDatabaseRef.push().getKey();
                              //  mDatabaseRef.child(uploadId).setValue(upload);
                                //uploadProgressBar.setVisibility(View.INVISIBLE);
                                openImagesActivity();
                        }
                    });
                }
            });
        }
        else
            {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
        /*
            if (two != null) {
                StorageReference storageReference = getInstance().getReference().child("aboutUs");

                final StorageReference imageFilePath = storageReference.child(one.getLastPathSegment());

                imageFilePath.putFile(two).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(adminAddAboutUS.this, "Product Upload successful", Toast.LENGTH_LONG).show();

                                aboutUsUpdate upload = new aboutUsUpdate(englishDescription.getText().toString().trim(), arabicDescription.getText().toString(), one.toString(), two.toString(), three.toString());
                                String uploadId = mDatabaseRef.push().getKey();
                               // mDatabaseRef.child(uploadId).setValue(upload);
                                //uploadProgressBar.setVisibility(View.INVISIBLE);
                                openImagesActivity();

                            }
                        });
                    }
                });
            } else {
                Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
            }
            if (three != null) {
                StorageReference storageReference = getInstance().getReference().child("aboutUs");

                final StorageReference imageFilePath = storageReference.child(one.getLastPathSegment());

                imageFilePath.putFile(three).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(adminAddAboutUS.this, "Product Upload successful", Toast.LENGTH_LONG).show();

                                aboutUsUpdate upload = new aboutUsUpdate(englishDescription.getText().toString().trim(), arabicDescription.getText().toString(), one.toString(), two.toString(), three.toString());
                                String uploadId = mDatabaseRef.push().getKey();
                                 mDatabaseRef.child(uploadId).setValue(upload);
                                //uploadProgressBar.setVisibility(View.INVISIBLE);
                                openImagesActivity();

                            }
                        });
                    }
                });
            } else {
                Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
            }
*/
        }


    private void openImagesActivity()
    {
        Intent intent = new Intent(this, guestAboutUsActivity.class);
        startActivity(intent);
    }

    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void openFileChooser1()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST1);
    }
    private void openFileChooser2()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            one = data.getData();
            Picasso.get().load(one).into(IG);
        }
        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            three = data.getData();
            Picasso.get().load(three).into(FB);
        }
        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK   && data != null && data.getData() != null)
        {
            two = data.getData();
            Picasso.get().load(two).into(SC);
        }
    }

    //*******************************************************************************************************************************************************************
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

    //*******************************************************************************************************************************************************************
    //  @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_admin_Category)
        {
            Intent itemP = new Intent(adminAddAboutUS.this, CatgeoryActivity.class);
            startActivity(itemP);
        }
        if (id == R.id.nav_admin_tulan_Category) {
            Intent itemP = new Intent(adminAddAboutUS.this, tulanCategories.class);
            startActivity(itemP);
        }

        if (id == R.id.logout)
        {
            Intent logout = new Intent(adminAddAboutUS.this, LoginActivity.class);
            startActivity(logout);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
