package com.example.vitor.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vitor.myapplication.R;
import com.example.vitor.myapplication.fragment.Fragment1;
import com.example.vitor.myapplication.fragment.Fragment2;
import com.example.vitor.myapplication.fragment.Fragment5;
import com.example.vitor.myapplication.fragment.HomeFragment;
import com.example.vitor.myapplication.fragment.GraphFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.squareup.picasso.Picasso;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    MenuItem Perfil;
    MenuItem Sair;
    FirebaseUser user1 = mAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View head = navigationView.getHeaderView(0);
        TextView textsubTitle = head.findViewById(R.id.nav_header_subtitle);
        textsubTitle.setText(user1.getEmail());
        final ImageView Imagem =head.findViewById(R.id.imageView);

        final TextView textTitle = head.findViewById(R.id.nav_header_title);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String id = mAuth.getCurrentUser().getUid();

                String value = String.valueOf(dataSnapshot.child(id).child("Name").getValue());
                String value1 = String.valueOf(dataSnapshot.child(id).child("UrlImagem").getValue());

                Picasso.with(Principal.this).load(value1).into(Imagem);
                textTitle.setText(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Erro" , Toast.LENGTH_LONG).show();

            }
        });


        //BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        //bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Perfil = menu.add("Account");
        Perfil.setIcon(R.drawable.ic_account);
        Perfil.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        Perfil.setShowAsAction(Perfil.SHOW_AS_ACTION_ALWAYS);
        Sair = menu.add("Sair");
        Sair.setIcon(R.drawable.exit);
        Sair.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        Sair.setShowAsAction(Sair.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item == Perfil) {
            startActivity(new Intent(this, Profile.class));
            finish();
            return true;
        }else if(item == Sair){
            Toast.makeText(getApplicationContext(), "Saindo..." , Toast.LENGTH_LONG).show();
            mAuth.signOut();
            Intent intent = new Intent(Principal.this, LoginActivity.class);
            startActivity(intent);
            Principal.this.finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.home:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Fragment1()).commit();
                break;
            case R.id.Pet:
                Toast.makeText(getApplicationContext(), "Em breve" , Toast.LENGTH_LONG).show();

                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Fragment1()).commit();*/
                break;
            case R.id.nav_Time:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Fragment2()).commit();
                break;
            case R.id.tools:
                startActivity(new Intent(Principal.this, SettingsActivity.class));

                break;
            case R.id.avaliar:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Fragment5()).commit();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    final FirebaseUser user = mAuth.getCurrentUser();

                    switch (item.getItemId()) {
                        case R.id.Home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.person:
                            selectedFragment = new GraphFragment();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };*/


}


