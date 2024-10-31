package com.example.medismart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity4 extends AppCompatActivity {

    private TextView tvDetectionLabel;
    private Button btnMedicineRecommendation, btnContactDoctor;
    private ImageView imageView;

    // Daftar penyakit yang akan dipilih secara acak
    private String[] conditions = {"Panu", "Cacar", "Jerawat", "Kutil", "Herpes"};
    private String randomCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        tvDetectionLabel = findViewById(R.id.tv_detection_label);
        btnMedicineRecommendation = findViewById(R.id.btn_medicine_recommendation);
        btnContactDoctor = findViewById(R.id.btn_contact_doctor);
        imageView = findViewById(R.id.imageView);

        // Ambil Uri gambar yang diteruskan
        String imageUriString = getIntent().getStringExtra("image_uri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Tidak ada gambar yang diterima", Toast.LENGTH_SHORT).show();
        }

        randomCondition = getRandomCondition();
        tvDetectionLabel.setText("Terdeteksi: " + randomCondition);

        btnMedicineRecommendation.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
            intent.putExtra("detected_condition", randomCondition);
            startActivity(intent);
        });

        btnContactDoctor.setOnClickListener(v -> {
            // Tambahkan logika untuk menghubungi dokter di sini
            Toast.makeText(this, "Fitur menghubungi dokter belum tersedia", Toast.LENGTH_SHORT).show();
        });
    }

    private String getRandomCondition() {
        Random random = new Random();
        return conditions[random.nextInt(conditions.length)];
    }
}
