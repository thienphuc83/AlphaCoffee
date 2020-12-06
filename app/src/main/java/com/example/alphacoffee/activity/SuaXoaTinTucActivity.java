package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

import info.hoang8f.widget.FButton;

public class SuaXoaTinTucActivity extends AppCompatActivity {

    private ImageView imgTinTuc, imgBack, imgCam, imgFolder, imgUpload;
    private TextView tvLuuY;
    private EditText edtTen, edtNoiDung;
    private FButton btnSua, btnXoa;

    DatabaseReference mData;
    private StorageReference mStorageRef;
    private int REQUEST_CODE_FOLDER = 11;
    private int REQUEST_CODE_CAMERA = 22;

    String linkimage = null;
    TinTuc tinTuc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_xoa_tin_tuc);

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
                String noidung = edtNoiDung.getText().toString().trim();

                if (linkimage == null){
                    Toast.makeText(SuaXoaTinTucActivity.this, "Upload ảnh trước rồi mới cập nhật!", Toast.LENGTH_SHORT).show();
                }else if (ten == null || ten.equals("")){
                    Toast.makeText(SuaXoaTinTucActivity.this, "Tên của tin tức không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( noidung == null || noidung.equals("")){
                    Toast.makeText(SuaXoaTinTucActivity.this, "Nội dung của tin tức không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else {
                    UpdateTinTuc(ten, noidung, linkimage);
                }
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(SuaXoaTinTucActivity.this);
                builder.setCancelable(true);
                //set layout cho dialog
                View view = LayoutInflater.from(SuaXoaTinTucActivity.this).inflate(R.layout.dialog_xoa, null);
                TextView tvCo = view.findViewById(R.id.tvco);
                TextView tvKhong = view.findViewById(R.id.tvkhong);
                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                tvCo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.child("TinTuc").child(tinTuc.getTinTucId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SuaXoaTinTucActivity.this, "Xóa thành công.", Toast.LENGTH_SHORT).show();
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
        tinTuc = (TinTuc) getIntent().getSerializableExtra("suaxoatintuc");

        Picasso.with(this).load(tinTuc.getHinhAnh()).into(imgTinTuc);
        linkimage = tinTuc.getHinhAnh();
        edtTen.setText(tinTuc.getTenTinTuc());
        edtNoiDung.setText(tinTuc.getNoiDung());

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
                        Toast.makeText(SuaXoaTinTucActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SuaXoaTinTucActivity.this, "Tải ảnh thành công!", Toast.LENGTH_SHORT).show();
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

    private void UpdateTinTuc( String tentintucmoi, String noidungtintucmoi,String hinhanhtintucmoi) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("tenTinTuc", tentintucmoi);
        hashMap.put("noiDung", noidungtintucmoi);
        hashMap.put("hinhAnh", hinhanhtintucmoi);
        mData.child("TinTuc").child(tinTuc.getTinTucId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SuaXoaTinTucActivity.this, "Cập nhật tin tức thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(SuaXoaTinTucActivity.this, "Lỗi! Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AnhXa() {
        imgTinTuc = findViewById(R.id.imgsuaxoatintuc);
        imgBack = findViewById(R.id.imgbacksuaxoatintuc);
        imgCam = findViewById(R.id.imgcamerasuaxoatintuc);
        imgFolder = findViewById(R.id.imgfoldersuaxoatintuc);
        imgUpload = findViewById(R.id.imguploadsuaxoatintuc);
        edtNoiDung = findViewById(R.id.edtnoidungsuaxoatintuc);
        edtTen = findViewById(R.id.edttensuaxoatintuc);
        btnSua = findViewById(R.id.btnsuatintuc);
        btnXoa = findViewById(R.id.btnxoatintuc);
        tvLuuY = findViewById(R.id.tvluuysuaxoatintuc);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}