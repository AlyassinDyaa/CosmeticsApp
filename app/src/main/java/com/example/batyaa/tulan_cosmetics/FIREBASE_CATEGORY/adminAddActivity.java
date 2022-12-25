package com.example.batyaa.tulan_cosmetics.FIREBASE_CATEGORY;


import android.app.ProgressDialog;
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
import com.example.batyaa.tulan_cosmetics.LoginActivity;
import com.example.batyaa.tulan_cosmetics.R;
import com.example.batyaa.tulan_cosmetics.Classes.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


public class adminAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private static final int PICK_IMAGE_REQUEST = 1;


    private EditText sizeEditText;
    private Button chooseImageBtn;
    private Button uploadBtn;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private ImageView chosenImageView;
    private ProgressBar uploadProgressBar;
    private EditText priceEDT;
    private Uri mImageUri;
    private EditText cateET;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private ImageView image;
    private EditText stockInventory;
    ProgressDialog mProgressDialog;

    String cBrandTitle, cDescription, cPrice, cCategory, cImage, cSize, cStock;

    String mStorageFolderPath = "all_items/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        chooseImageBtn = findViewById(R.id.button_choose_image);
        uploadBtn = findViewById(R.id.uploadBtn);
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById ( R.id.descriptionEditText );
        chosenImageView = findViewById(R.id.chosenImageView);
      //  uploadProgressBar = findViewById(R.id.progress_bar);
        priceEDT = findViewById(R.id.priceEditText);

        cateET = findViewById(R.id.categoryEditText);
        image = findViewById(R.id.imageS);
        sizeEditText = findViewById(R.id.sizeEditText);
        stockInventory = findViewById(R.id.stockEditText);
        mProgressDialog = new ProgressDialog(adminAddActivity.this);

        mStorageRef = getInstance().getReference("id");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("id");

        chooseImageBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openFileChooser();
            }
        });
//*******************************************************************************************************************************************************************
        uploadBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mUploadTask != null && mUploadTask.isInProgress())
                {
                    Toast.makeText(adminAddActivity.this, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    //**** If we came here from "ADD" button title will be UPLOAD
                    //**** If we came here from "UPDATE" title of button will be UPDATE
                    if (uploadBtn.getText().equals("Upload"))
                    {
                        //call method to upload data to firebase
                        uploadFile();
                    }
                    else
                    {
                        //**** Start the update
                        //  beginUpdateProcess();
                    }
                }
            }
        });
//*******************************************************************************************************************************************************************
        //**** If data from intent is null we'll get the data
        Bundle intent = getIntent().getExtras();
        if(intent != null)
        {
            //get and store data
            cBrandTitle = intent.getString("Brand Name");
            cDescription = intent.getString("Description");
            cPrice = intent.getString("Price");
            cCategory = intent.getString("Category");
            cImage = intent.getString("Image");
            cSize = intent.getString("Size");
            cStock = intent.getString("stock");

            //**** set the data to the views
            nameEditText.setText(cBrandTitle);
            descriptionEditText.setText(cDescription);
            priceEDT.setText(cPrice);
            cateET.setText(cCategory);
            sizeEditText.setText(cSize);
            stockInventory.setText(cStock);
            Picasso.get().load(cImage).placeholder(R.drawable.load).fit().centerCrop().into(chosenImageView);
            //**** Change button of action bar and button
            uploadBtn.setText("Update");
        }

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
    //*******************************************************************************************************************************************************************


    //*******************************************************************************************************************************************************************
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
            Picasso.get().load(mImageUri).into(chosenImageView);
        }
    }
    //*******************************************************************************************************************************************************************
    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //*******************************************************************************************************************************************************************
    public  void uploadFile()
    {


        if(mImageUri != null) {
            // Handle progress ui views (progressbar popup loading ... )
            mProgressDialog.setTitle("Uploading...");
            mProgressDialog.show();
         /*   uploadProgressBar.setVisibility(View.VISIBLE);
            uploadProgressBar.setIndeterminate(true);*/

            StorageReference storageReference = getInstance().getReference().child("id");

            final StorageReference imageFilePath = storageReference.child(mImageUri.getLastPathSegment());

            imageFilePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(adminAddActivity.this, "Product Upload successful", Toast.LENGTH_LONG).show();
                            if (priceEDT.getText().toString() == "") {
                                Product upload = new Product(nameEditText.getText().toString().trim(), uri.toString(), descriptionEditText.getText().toString(), "NULL", cateET.getText().toString(), cateET.getText().toString().toLowerCase(), sizeEditText.getText().toString(), stockInventory.getText().toString());
                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(upload);
                                //uploadProgressBar.setVisibility(View.INVISIBLE);
                                mProgressDialog.dismiss();
                                openImagesActivity();
                            } else {
                                Product upload = new Product(nameEditText.getText().toString().trim(), uri.toString(), descriptionEditText.getText().toString(), priceEDT.getText().toString(), cateET.getText().toString(), cateET.getText().toString().toLowerCase(), sizeEditText.getText().toString(), stockInventory.getText().toString());
                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(upload);
                                // uploadProgressBar.setVisibility(View.INVISIBLE);
                                mProgressDialog.dismiss();
                                openImagesActivity();
                            }
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
    private void openImagesActivity()
    {
        Intent intent = new Intent(this, categoryDetailRecyclerActvity.class);
        startActivity(intent);
    }

    /*  @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }*/
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
        Intent itemP = new Intent(adminAddActivity.this, CatgeoryActivity.class);
        startActivity(itemP);
    }
        if (id == R.id.logout)
        {
            Intent logout = new Intent(adminAddActivity.this, LoginActivity.class);
            startActivity(logout);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}