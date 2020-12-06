package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.TinTuc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

import info.hoang8f.widget.FButton;

public class ThemTinTucActivity extends AppCompatActivity {

    private ImageView imgTinTuc, imgBack, imgCam, imgFolder, imgUpload;
    private TextView tvTitle, tvLuuY;
    private EditText edtTen, edtNoiDung;
    private FButton btnThem;

    DatabaseReference mData;
    private StorageReference mStorageRef;
    private int REQUEST_CODE_FOLDER = 123;
    private int REQUEST_CODE_CAMERA = 456;

    String linkimage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tin_tuc);

        mData = FirebaseDatabase.getInstance().getReference();
        //upload ảnh phải có
        mStorageRef = FirebaseStorage.getInstance().getReference();
        AnhXa();

        UploadImage();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTen.getText().toString().trim();
                String noidung = edtNoiDung.getText().toString().trim();
                String tintucId = mData.child("TinTuc").push().getKey();

                if (linkimage == null){
                    Toast.makeText(ThemTinTucActivity.this, "Upload ảnh trước rồi mới thêm!", Toast.LENGTH_SHORT).show();
                }else if (ten == null || ten.equals("")){
                    Toast.makeText(ThemTinTucActivity.this, "Tên của tin tức không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( noidung == null || noidung.equals("")){
                    Toast.makeText(ThemTinTucActivity.this, "Nội dung của tin tức không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else {
                    TinTuc tinTuc = new TinTuc(tintucId,ten,noidung,linkimage);
                    mData.child("TinTuc").child(tintucId).setValue(tinTuc).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ThemTinTucActivity.this, "Thêm thành công.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void UploadImage() {
        imgFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });

        imgCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = mStorageRef.child("image" + calendar.getTimeInMillis() + ".png");

                // Get the data from an ImageView as bytes
                imgTinTuc.setDrawingCacheEnabled(true);
                imgTinTuc.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgTinTuc.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(ThemTinTucActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                        Log.d("AAA", uri + "");
                                linkimage = String.valueOf(uri);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                                        Log.d("AAA", "Failed to get uri");
                            }
                        });
                        Toast.makeText(ThemTinTucActivity.this, "Tải ảnh thành công!", Toast.LENGTH_SHORT).show();
                        tvLuuY.setText("Upload xong!");
                        imgCam.setVisibility(View.GONE);
                        imgFolder.setVisibility(View.GONE);
                        imgUpload.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgTinTuc.setImageBitmap(bitmap);
            tvLuuY.setVisibility(View.VISIBLE);
            imgUpload.setVisibility(View.VISIBLE);
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgTinTuc.setImageBitmap(bitmap);
                tvLuuY.setVisibility(View.VISIBLE);
                imgUpload.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AnhXa() {
        imgTinTuc = findViewById(R.id.imgthemtintuc);
        imgBack = findViewById(R.id.imgbackthemtintuc);
        imgCam = findViewById(R.id.imgcamerathemtintuc);
        imgFolder = findViewById(R.id.imgfolderthemtintuc);
        imgUpload = findViewById(R.id.imguploadthemtintuc);
        tvTitle = findViewById(R.id.tvthemtintuc);
        edtNoiDung = findViewById(R.id.edtnoidungthemtintuc);
        edtTen = findViewById(R.id.edttenthemtintuc);
        btnThem = findViewById(R.id.btnthemtintuc);
        tvLuuY = findViewById(R.id.tvluuythemtintuc);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvTitle.setTypeface(typeface);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}