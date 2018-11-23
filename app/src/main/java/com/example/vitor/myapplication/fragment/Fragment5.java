package com.example.vitor.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.vitor.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Fragment5 extends Fragment{
    double Nestrelas;
    private RatingBar r1;
    private Button b1;
    Map Avaliacao = new HashMap();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Avaliações");
    FirebaseAuth mAuth1 = FirebaseAuth.getInstance();
    final String id = Objects.requireNonNull(mAuth1.getCurrentUser()).getUid();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment5, container, false);
        b1 = view.findViewById(R.id.buttonava);
        r1 = view.findViewById(R.id.ratingBar);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nestrelas = r1.getRating();
                Avaliacao.put("Avaliação", Nestrelas);
                myRef.child(id).setValue(Avaliacao);

                switch (v.getId()) {
                    case R.id.buttonava:
                        if (Nestrelas >= 2.5) {
                            Toast.makeText(getActivity(), "Obrigado! Fazemos o melhor para nossos clientes", Toast.LENGTH_SHORT).show();

                        } else if (Nestrelas < 2.5) {
                            Toast.makeText(getActivity(), "Que Pena! Vamos melhorar", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(getActivity(), "Erro", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return view;
    }

}
