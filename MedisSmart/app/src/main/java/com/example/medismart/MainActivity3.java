package com.example.medismart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    private SurfaceView surfaceView;
    private Camera camera;
    private Button buttonCamera;
    private Button buttonGallery;

    private static final int GALLERY_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        surfaceView = findViewById(R.id.surface_view);
        buttonCamera = findViewById(R.id.button_camera);
        buttonGallery = findViewById(R.id.button_gallery);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            setupCamera();
        }

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void setupCamera() {
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();

        // Atur ukuran pratinjau
        Camera.Size bestPreviewSize = getBestResolution(parameters.getSupportedPreviewSizes(), surfaceView.getWidth(), surfaceView.getHeight());
        if (bestPreviewSize != null) {
            parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
            surfaceView.getHolder().setFixedSize(bestPreviewSize.width, bestPreviewSize.height);
        }

        // Atur ukuran foto tertinggi
        Camera.Size highestResolution = getHighestResolution(parameters.getSupportedPictureSizes());
        if (highestResolution != null) {
            parameters.setPictureSize(highestResolution.width, highestResolution.height);
        }

        // Fokus otomatis
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        // Pengaturan lainnya
        parameters.setJpegQuality(100);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);

        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                if (holder.getSurface() == null) {
                    return;
                }
                try {
                    camera.stopPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                }
            }
        });
    }

    private Camera.Size getBestResolution(List<Camera.Size> sizes, int width, int height) {
        double targetRatio = (double) width / height;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) < 0.1) {
                if (Math.abs(size.height - height) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - height);
                }
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - height) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - height);
                }
            }
        }
        return optimalSize;
    }

    private Camera.Size getHighestResolution(List<Camera.Size> sizes) {
        Camera.Size highest = null;
        for (Camera.Size size : sizes) {
            if (highest == null || (size.width * size.height > highest.width * highest.height)) {
                highest = size;
            }
        }
        return highest;
    }

    private void captureImage() {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                // Simpan gambar ke file sementara
                try {
                    File file = new File(getExternalFilesDir(null), "captured_image.jpg");
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.close();

                    // Mengirim Uri gambar ke aktivitas berikutnya
                    Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
                    intent.putExtra("image_uri", Uri.fromFile(file).toString());
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                camera.startPreview();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                // Mengirim bitmap ke aktivitas berikutnya
                Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream); // Menggunakan JPEG dan kualitas 80
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image_data", byteArray);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            setupCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
