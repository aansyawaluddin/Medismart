package com.example.medismart;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medismart.adapter.MedicineAdapter;
import com.example.medismart.model.Medicine;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity5 extends AppCompatActivity {

    private RecyclerView rvMedicine;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList;
    private DatabaseReference databaseReference;
    private String detectedCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        rvMedicine = findViewById(R.id.rv_medicine);
        rvMedicine.setLayoutManager(new LinearLayoutManager(this));

        detectedCondition = getIntent().getStringExtra("detected_condition");

        databaseReference = FirebaseDatabase.getInstance().getReference("obat");

        medicineList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicineList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Medicine medicine = snapshot.getValue(Medicine.class);
                    if (medicine != null && medicine.getPenyakit().equalsIgnoreCase(detectedCondition)) {
                        medicineList.add(medicine);
                    }
                }
                medicineAdapter = new MedicineAdapter(MainActivity5.this, medicineList);
                rvMedicine.setAdapter(medicineAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
