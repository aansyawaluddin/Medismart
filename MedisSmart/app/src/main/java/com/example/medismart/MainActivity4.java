package com.example.medismart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medismart.server.ApiResponse;
import com.google.gson.Gson;

public class MainActivity4 extends AppCompatActivity {

    private TextView tvDetectionLabel;
    private Button btnMedicineRecommendation, btnContactDoctor;
    private ImageView imageView;
    private ProgressBar progressBar;
    private View mainContent; // Kontainer untuk elemen utama (selain ProgressBar)
    private String detected_obat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        tvDetectionLabel = findViewById(R.id.tv_detection_label);
        btnMedicineRecommendation = findViewById(R.id.btn_medicine_recommendation);
        btnContactDoctor = findViewById(R.id.btn_contact_doctor);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progress_bar);
        mainContent = findViewById(R.id.main_content); // Asumsi ini adalah parent layout dari semua elemen utama

        showLoadingIndicator(); // Tampilkan ProgressBar saat memulai

        String imageUriString = getIntent().getStringExtra("image_uri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView.setImageURI(imageUri);
        }

        String apiResponse = getIntent().getStringExtra("detected_condition");
        if (apiResponse != null) {
            displayDetectedCondition(apiResponse);
        } else {
            tvDetectionLabel.setText("Tidak ada deteksi penyakit.");
            hideLoadingIndicator(); // Sembunyikan ProgressBar jika tidak ada data
        }

        btnMedicineRecommendation.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
            intent.putExtra("detected_condition", detected_obat);
            startActivity(intent);
        });

        btnContactDoctor.setOnClickListener(v -> {
            Toast.makeText(this, "Fitur menghubungi dokter belum tersedia", Toast.LENGTH_SHORT).show();
        });
    }

    private void displayDetectedCondition(String apiResponse) {
        Gson gson = new Gson();
        ApiResponse response = gson.fromJson(apiResponse, ApiResponse.class);

        if (response.getDetected_classes() != null && !response.getDetected_classes().isEmpty()) {
            detected_obat = response.getDetected_classes().get(0); // Ambil kondisi pertama
            tvDetectionLabel.setText("Terdeteksi: " + detected_obat);
            Log.d("pppp", detected_obat);
        } else {
            tvDetectionLabel.setText("Tidak ada deteksi penyakit.");
        }

        hideLoadingIndicator(); // Sembunyikan ProgressBar setelah data diproses
    }

    private void showLoadingIndicator() {
        progressBar.setVisibility(View.VISIBLE); // Tampilkan ProgressBar
        mainContent.setVisibility(View.GONE); // Sembunyikan tampilan utama
    }

    private void hideLoadingIndicator() {
        progressBar.setVisibility(View.GONE); // Sembunyikan ProgressBar
        mainContent.setVisibility(View.VISIBLE); // Tampilkan kembali tampilan utama
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity4.this, MainActivity3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Menutup aktivitas saat ini
    }
}
