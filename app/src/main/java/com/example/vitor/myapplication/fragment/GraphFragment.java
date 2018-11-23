package com.example.vitor.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vitor.myapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import static java.util.Objects.*;

public class GraphFragment extends Fragment {
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Avaliações");
    FirebaseAuth mAuth1 = FirebaseAuth.getInstance();
    final String id = requireNonNull(mAuth1.getCurrentUser()).getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph,container,false);
        final BarChart chart = (BarChart) view.findViewById(R.id.chart);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getActivity(), "Valor: " +requireNonNull(dataSnapshot).child(id).child("Avaliação").getValue(), Toast.LENGTH_SHORT).show();
                double aux = (double) dataSnapshot.child(id).child("Avaliação").getValue();

                /*List<BarEntry> entries = new ArrayList<>();
                entries.add(new BarEntry(44f,0));
                entries.add(new BarEntry(43f,1));
                BarDataSet barDataSet = new BarDataSet(entries,"fsd");

                ArrayList<String> Name = new ArrayList<>();
                Name.add("Nigga");
                Name.add("Nigga1");
                BarDataSet barDataSet1 = new BarDataSet(Name,"fsd1");


                BarData Test = new BarData(Name,barDataSet);
                chart.setData(Test);
*/


                //LineDataSet dataSet = new LineDataSet(""+id, (aux);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        return view;
    }
}
