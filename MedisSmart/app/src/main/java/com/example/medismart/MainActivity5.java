package com.example.medismart;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
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

    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList;
    private DatabaseReference databaseReference;
    private String detectedCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        // Get the detected condition from Activity4
        detectedCondition = getIntent().getStringExtra("detected_condition");
        Log.d("ppp",detectedCondition);
        if (detectedCondition == null || detectedCondition.isEmpty()) {
            Toast.makeText(this, "Tidak ada kondisi yang terdeteksi", Toast.LENGTH_SHORT).show();
            detectedCondition = "";
        }


        recyclerView = findViewById(R.id.rv_medicine);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        medicineList = new ArrayList<>();
        medicineAdapter = new MedicineAdapter(this, medicineList);
        recyclerView.setAdapter(medicineAdapter);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("obat");

        fetchMedicineData();
    }

    private void fetchMedicineData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicineList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Medicine medicine = dataSnapshot.getValue(Medicine.class);

                    // Check if the disease matches the detected condition
                    if (medicine != null && medicine.getPenyakit().equalsIgnoreCase(detectedCondition)) {
                        medicineList.add(medicine);
                    }
                }

                if (medicineList.isEmpty()) {
                    Toast.makeText(MainActivity5.this, "No medicine found for " + detectedCondition, Toast.LENGTH_SHORT).show();
                }

                medicineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity5.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity5", "Database error: " + error.getMessage());
            }
        });
    }
}
