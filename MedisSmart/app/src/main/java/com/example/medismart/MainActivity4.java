package com.example.medismart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

import java.io.InputStream;

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
        mainContent = findViewById(R.id.main_content);

        showLoadingIndicator();

        String imageUriString = getIntent().getStringExtra("image_uri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            displayRotatedImage(imageUri); // Panggil metode untuk menampilkan gambar yang diputar
        }

        String apiResponse = getIntent().getStringExtra("detected_condition");
        if (apiResponse != null) {
            displayDetectedCondition(apiResponse);
        } else {
            tvDetectionLabel.setText("Tidak ada deteksi penyakit.");
            hideLoadingIndicator();
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

    private void displayRotatedImage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Buat objek Matrix untuk rotasi
            Matrix matrix = new Matrix();
            matrix.postRotate(90); // Putar 90 derajat

            // Buat Bitmap baru berdasarkan Bitmap asli yang sudah diputar
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            // Tampilkan Bitmap yang sudah diputar pada ImageView
            imageView.setImageBitmap(rotatedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal memuat gambar.", Toast.LENGTH_SHORT).show();
        }
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
