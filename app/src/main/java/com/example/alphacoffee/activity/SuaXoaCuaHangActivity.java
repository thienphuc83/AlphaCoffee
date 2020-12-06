package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

import info.hoang8f.widget.FButton;

public class SuaXoaCuaHangActivity extends AppCompatActivity {
    private ImageView imgCuaHang, imgBack, imgCam, imgFolder, imgUpload;
    private TextView tvLuuY;
    private EditText edtTen, edtDiaChi, edtGioMoCua, edtSDT;
    private FButton btnSua, btnXoa;

    DatabaseReference mData;
    private StorageReference mStorageRef;
    private int REQUEST_CODE_FOLDER = 11;
    private int REQUEST_CODE_CAMERA = 22;

    String linkimage = null;
    CuaHang cuaHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_xoa_cua_hang);

        mData = FirebaseDatabase.getInstance().getReference();
        //upload ảnh phải có
        mStorageRef = FirebaseStorage.getInstance().getReference();

        AnhXa();

        GetIntent();

        UploadImage();

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTen.getText().toString().trim();
                String diachi = edtDiaChi.getText().toString().trim();
                String sdt = edtSDT.getText().toString().trim();
                String giomo = edtGioMoCua.getText().toString().trim();

                if (linkimage == null) {
                    Toast.makeText(SuaXoaCuaHangActivity.this, "Upload ảnh trước rồi mới cập nhật!", Toast.LENGTH_SHORT).show();
                } else if (ten == null || ten.equals("")) {
                    Toast.makeText(SuaXoaCuaHangActivity.this, "Tên cửa hàng không được bỏ trống!", Toast.LENGTH_SHORT).show();
                } else if (diachi == null || diachi.equals("")) {
                    Toast.makeText(SuaXoaCuaHangActivity.this, "Địa chỉ cửa hàng không được bỏ trống!", Toast.LENGTH_SHORT).show();
                } else if (sdt == null || sdt.equals("")) {
                    Toast.makeText(SuaXoaCuaHangActivity.this, "Số điện thoại cửa hàng không được bỏ trống!", Toast.LENGTH_SHORT).show();
                } else if (giomo == null || giomo.equals("")) {
                    Toast.makeText(SuaXoaCuaHangActivity.this, "Giờ mở cửa hàng không được bỏ trống!", Toast.LENGTH_SHORT).show();
                } else {
                    UpdateTinTuc(ten, diachi, sdt, giomo, linkimage);
                }
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(SuaXoaCuaHangActivity.this);
                builder.setCancelable(true);
                //set layout cho dialog
                View view = LayoutInflater.from(SuaXoaCuaHangActivity.this).inflate(R.layout.dialog_xoa, null);
                TextView tvCo = view.findViewById(R.id.tvco);
                TextView tvKhong = view.findViewById(R.id.tvkhong);
                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                tvCo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.child("CuaHang").child(cuaHang.getCuaHangId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SuaXoaCuaHangActivity.this, "Xóa thành công.", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    finish();
                                }
                            }
                        });
                    }
                });

                tvKhong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

    }

    private void GetIntent() {
        cuaHang = (CuaHang) getIntent().getSerializableExtra("suaxoacuahang");

        Picasso.with(this).load(cuaHang.getHinhAnh()).into(imgCuaHang);
        linkimage = cuaHang.getHinhAnh();
        edtTen.setText(cuaHang.getTenCuaHang());
        edtDiaChi.setText(cuaHang.getDiaChi());
        edtSDT.setText(cuaHang.getSoDienThoai());
        edtGioMoCua.setText(cuaHang.getGioMoCua());

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
                        Toast.makeText(SuaXoaCuaHangActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SuaXoaCuaHangActivity.this, "Tải ảnh thành công!", Toast.LENGTH_SHORT).show();
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

    private void UpdateTinTuc(String tenmoi, String diachimoi, String sdtmoi, String giomoi, String hinhmoi) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("tenCuaHang", tenmoi);
        hashMap.put("diaChi", diachimoi);
        hashMap.put("hinhAnh", hinhmoi);
        hashMap.put("soDienThoai", sdtmoi);
        hashMap.put("gioMoCua", giomoi);
        mData.child("CuaHang").child(cuaHang.getCuaHangId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SuaXoaCuaHangActivity.this, "Cập nhật cửa hàng thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SuaXoaCuaHangActivity.this, "Lỗi! Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AnhXa() {
        imgCuaHang = findViewById(R.id.imgsuaxoacuahang);
        imgBack = findViewById(R.id.imgbacksuaxoacuahang);
        imgCam = findViewById(R.id.imgcamerasuaxoacuahang);
        imgFolder = findViewById(R.id.imgfoldersuaxoacuahang);
        imgUpload = findViewById(R.id.imguploadsuaxoacuahang);
        edtGioMoCua = findViewById(R.id.edtgiosuaxoacuahang);
        edtTen = findViewById(R.id.edttensuaxoacuahang);
        edtDiaChi = findViewById(R.id.edtdiachisuaxoacuahang);
        edtSDT = findViewById(R.id.edtsdtsuaxoacuahang);
        btnSua = findViewById(R.id.btnsuacuahang);
        btnXoa = findViewById(R.id.btnxoacuahang);
        tvLuuY = findViewById(R.id.tvluuysuaxoacuahang);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}