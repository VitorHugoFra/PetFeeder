package com.example.vitor.myapplication.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vitor.myapplication.R;
import com.example.vitor.myapplication.extra.Upload;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;



public class Profile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseAuth mAuth1 = FirebaseAuth.getInstance();
    FirebaseUser user1 = mAuth1.getCurrentUser();
    static String Name = "Sem Nome";
    private ImageView mImageView;
    MenuItem Voltar;
    Uri mImageUri;
    EditText nameProfile, nacionality, namePet;
    Spinner especie,sexo;
    ProgressBar mProgressBar;
    static Map mapa = new HashMap();
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameProfile = findViewById(R.id.nameUsers);
        nacionality = findViewById(R.id.nationality);
        namePet = findViewById(R.id.NamePet);
        especie = findViewById(R.id.spinner);
        sexo = findViewById(R.id.Sexo);

        mStorageRef = FirebaseStorage.getInstance().getReference("User");

        Button mButtonChooseImage = findViewById(R.id.button_edit);
        mImageView = findViewById(R.id.imageView);

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        FloatingActionButton saveProfile = findViewById(R.id.saveProfile);
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user()) {
                    Toast.makeText(getApplicationContext(), "Save...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Profile.this, Principal.class);
                    startActivity(intent);
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        Voltar = menu.add("back");
        Voltar.setIcon(R.drawable.exit);
        Voltar.getIcon().setColorFilter(Color.WHITE,PorterDuff.Mode.SRC_ATOP);
        Voltar.setShowAsAction(Voltar.SHOW_AS_ACTION_ALWAYS);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(item == Voltar){
            startActivity(new Intent(this, Principal.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean user() {
        String nameProfile1 = nameProfile.getText().toString().trim();
        String type = especie.getSelectedItem().toString();
        String namepet = namePet.getText().toString().trim();
        String country = nacionality.getText().toString().trim();
        String genre = sexo.getSelectedItem().toString();
        final String id = mAuth1.getCurrentUser().getUid();
        View focusView = null;

        if (!TextUtils.isEmpty(nameProfile1) && !TextUtils.isEmpty(namepet) && !TextUtils.isEmpty(country)) {
            mapa.put("Name",nameProfile1);
            mapa.put("Especie", type);
            mapa.put("Namepet",namepet);
            mapa.put("País", country);
            mapa.put("Genêro",genre);
            Name = nameProfile1;
            if (user1 != null) {
                uploadFile(id);
            }
            return true;
        } else if (!TextUtils.isEmpty(nameProfile1) && TextUtils.isEmpty(namepet) && !TextUtils.isEmpty(country)) {
            namePet.setError(getString(R.string.error_field_required));
            focusView = namePet;
            focusView.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(nameProfile1) && !TextUtils.isEmpty(namepet) && !TextUtils.isEmpty(country)){
            nameProfile.setError(getString(R.string.error_field_required));
            focusView = nameProfile;getString(R.string.error_field_required);
            focusView.requestFocus();
            return false;
        }else if (!TextUtils.isEmpty(nameProfile1) && !TextUtils.isEmpty(namepet) && TextUtils.isEmpty(country)) {
            nacionality.setError(getString(R.string.error_field_required));
            focusView = nacionality;
            focusView.requestFocus();
            return false;
        }else{
            namePet.setError(getString(R.string.error_field_required));
            focusView = namePet;
            focusView.requestFocus();
            nameProfile.setError(getString(R.string.error_field_required));
            focusView = nameProfile;
            focusView.requestFocus();
            nacionality.setError(getString(R.string.error_field_required));
            focusView = nacionality;
            focusView.requestFocus();
            return false;
        }
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(mImageView);

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(final String id){
        if(mImageUri != null){
            StorageReference fileRef = mStorageRef.child(id +"." +getFileExtension(mImageUri));
            if(fileRef.putFile(mImageUri).isSuccessful()){
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Upload upload = new Upload(id +"." +getFileExtension(mImageUri), uri.toString());
                        mapa.put("NameImagem",upload.getName());
                        mapa.put("UrlImagem", uri.getPath());
                        if(mapa.size() == 7){
                            myRef.child(id).setValue(mapa);
                        }

                    }
                });

            }
            /*fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //taskSnapshot.getUploadSessionUri().toString() Aqui
                    //taskSnapshot.getStorage().getDownloadUrl().toString();//Aqui
                    //taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult().toString();//Aqui
                    //taskSnapshot.getMetadata().getReference().getDownloadUrl().isSuccessful();//Aqui
                    //mImageUri.getUserInfo().toString();


                            Upload upload = new Upload(id +"." +getFileExtension(mImageUri), uri.toString());
                            mapa.put("NameImagem",upload.getName());
                            mapa.put("UrlImagem", upload.getImageUrl());


                    c

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
                }
            });*/

        }else {
            Toast.makeText(getApplicationContext(), "Sem arquivo", Toast.LENGTH_LONG).show();

        }

    }
}
