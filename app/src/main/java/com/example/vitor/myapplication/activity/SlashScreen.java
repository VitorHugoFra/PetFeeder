package com.example.vitor.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.vitor.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SlashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.slashscreen);
        LogoLauncher logoLauncher = new LogoLauncher();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Toast.makeText(getApplicationContext(), "Bem vindo de volta " + user.getEmail() + "!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SlashScreen.this, Principal.class);
            startActivity(intent);
            SlashScreen.this.finish();
        }
        else {
            logoLauncher.start();
        }


    }
    private class LogoLauncher extends Thread{
        public void run(){
            try{
                sleep(2000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

            Intent intent = new Intent(SlashScreen.this, LoginActivity.class);
            startActivity(intent);
            SlashScreen.this.finish();
        }
    }
}