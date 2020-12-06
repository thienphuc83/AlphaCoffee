package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.alphacoffee.model.CuaHang;
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

import info.hoang8f.widget.FButton;

public class ThemCuaHangActivity extends AppCompatActivity {

    private ImageView imgCuaHang, imgBack, imgCam, imgFolder, imgUpload;
    private TextView tvTitle, tvLuuY;
    private EditText edtTen, edtDiaChi, edtGioMoCua, edtSDT;
    private FButton btnThem;

    DatabaseReference mData;
    private StorageReference mStorageRef;
    private int REQUEST_CODE_FOLDER = 123;
    private int REQUEST_CODE_CAMERA = 456;

    String linkimage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cua_hang);

        mData = FirebaseDatabase.getInstance().getReference();
        //upload ảnh phải có
        mStorageRef = FirebaseStorage.getInstance().getReference();
        AnhXa();

        UploadImage();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTen.getText().toString().trim();
                String diachi = edtDiaChi.getText().toString().trim();
                String sdt = edtSDT.getText().toString().trim();
                String giomo = edtGioMoCua.getText().toString().trim();

                String cuahangId = mData.child("TinTuc").push().getKey();

                if (linkimage == null){
                    Toast.makeText(ThemCuaHangActivity.this, "Upload ảnh trước rồi mới thêm!", Toast.LENGTH_SHORT).show();
                }else if (ten == null || ten.equals("")){
                    Toast.makeText(ThemCuaHangActivity.this, "Tên cửa hàng không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( diachi == null || diachi.equals("")){
                    Toast.makeText(ThemCuaHangActivity.this, "Địa chỉ cửa hàng không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( sdt == null || sdt.equals("")){
                    Toast.makeText(ThemCuaHangActivity.this, "Số điện thoại cửa hàng không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( giomo == null || giomo.equals("")){
                    Toast.makeText(ThemCuaHangActivity.this, "Giờ mở cửa hàng không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else {
                    CuaHang cuaHang = new CuaHang(cuahangId,ten,sdt,diachi,giomo,linkimage);
                    mData.child("CuaHang").child(cuahangId).setValue(cuaHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ThemCuaHangActivity.this, "Thêm thành công.", Toast.LENGTH_SHORT).show();
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
                imgCuaHang.setDrawingCacheEnabled(true);
                imgCuaHang.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgCuaHang.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(ThemCuaHangActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ThemCuaHangActivity.this, "Tải ảnh thành công!", Toast.LENGTH_SHORT).show();
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
            imgCuaHang.setImageBitmap(bitmap);
            tvLuuY.setVisibility(View.VISIBLE);
            imgUpload.setVisibility(View.VISIBLE);
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgCuaHang.setImageBitmap(bitmap);
                tvLuuY.setVisibility(View.VISIBLE);
                imgUpload.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AnhXa() {
        imgCuaHang = findViewById(R.id.imgthemcuahang);
        imgBack = findViewById(R.id.imgbackthemcuahang);
        imgCam = findViewById(R.id.imgcamerathemcuahang);
        imgFolder = findViewById(R.id.imgfolderthemcuahang);
        imgUpload = findViewById(R.id.imguploadthemcuahang);
        tvTitle = findViewById(R.id.tvthemcuahang);
        edtDiaChi = findViewById(R.id.edtdiachithemcuahang);
        edtTen = findViewById(R.id.edttenthemcuahang);
        edtGioMoCua = findViewById(R.id.edtgiothemcuahang);
        edtSDT = findViewById(R.id.edtsdtthemcuahang);
        btnThem = findViewById(R.id.btnthemcuahang);
        tvLuuY = findViewById(R.id.tvluuythemcuahang);

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