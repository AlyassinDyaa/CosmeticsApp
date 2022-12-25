package com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.batyaa.tulan_cosmetics.Admin.CatgeoryActivity;
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class adminAddCategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText title;
    ImageView image;
    Button uplaod;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_category);

        title = findViewById(R.id.addCategoryTitle);
        image = findViewById(R.id.addCategoryImage);
        uplaod = findViewById(R.id.uploadNewCategory);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });



        uplaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        mStorageRef = getInstance().getReference("id");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("id");


    }
    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    //*******************************************************************************************************************************************************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
             mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(image);
        }
    }


    public  void uploadFile()
    {
        if(mImageUri != null)
        {
            StorageReference storageReference = getInstance().getReference().child("id");

            final StorageReference imageFilePath = storageReference.child(mImageUri.getLastPathSegment());

            imageFilePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(adminAddCategoryActivity.this, "Product Upload successful", Toast.LENGTH_LONG).show();

                              //  category upload = new category(title.getText().toString().trim(), uri.toString(), title.getText().toString().toLowerCase());
                                String uploadId = mDatabaseRef.push().getKey();
                               // mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    });
                }
            });
        }
        else
        {
            Toast.makeText(this, "You haven't Selected Any file selected", Toast.LENGTH_SHORT).show();
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



        if (id == R.id.nav_admin_Category) {
            Intent itemP = new Intent(adminAddCategoryActivity.this, CatgeoryActivity.class);
            startActivity(itemP);
        }
        if (id == R.id.nav_admin_aboutUs) {
            Intent itemP = new Intent(adminAddCategoryActivity.this, adminAddAboutUS.class);
            startActivity(itemP);
        }
        if (id == R.id.logout)
        {
            Intent logout = new Intent(adminAddCategoryActivity.this, LoginActivity.class);
            startActivity(logout);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
