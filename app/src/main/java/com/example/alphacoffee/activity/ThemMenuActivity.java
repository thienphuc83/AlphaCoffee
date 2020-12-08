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
import com.example.alphacoffee.model.SanPham;
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

public class ThemMenuActivity extends AppCompatActivity {


    private ImageView imgSanPham, imgBack, imgCam, imgFolder, imgUpload;
    private TextView tvTitle, tvLuuY;
    private EditText edtTen, edtSizeL, edtSizeM, edtSizeS, edtGioiThieu, edtTopping, edtGiaTopping, edtLike, edtLoai;
    private FButton btnThem;

    DatabaseReference mData;
    private StorageReference mStorageRef;
    private int REQUEST_CODE_FOLDER = 69;
    private int REQUEST_CODE_CAMERA = 96;

    String linkimage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_menu);

        mData = FirebaseDatabase.getInstance().getReference();
        //upload ảnh phải có
        mStorageRef = FirebaseStorage.getInstance().getReference();

        AnhXa();

        UploadImage();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTen.getText().toString().trim();
                String giaL = edtSizeL.getText().toString().trim();
                String giaM = edtSizeM.getText().toString().trim();
                String giaS = edtSizeS.getText().toString().trim();
                String gioithieu = edtGioiThieu.getText().toString().trim();
                String topping = edtTopping.getText().toString().trim();
                String giatopping = edtGiaTopping.getText().toString().trim();
                String like = "0";
                String loai = edtLoai.getText().toString().trim();



                String sanphamId = mData.child("SanPham").push().getKey();

                if (linkimage == null){
                    Toast.makeText(ThemMenuActivity.this, "Upload ảnh trước rồi mới thêm!", Toast.LENGTH_SHORT).show();
                }else if (ten == null || ten.equals("")){
                    Toast.makeText(ThemMenuActivity.this, "Tên của sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( giaL == null || giaL.equals("")){
                    Toast.makeText(ThemMenuActivity.this, "Giá size lớn sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( giaM == null || giaM.equals("")){
                    Toast.makeText(ThemMenuActivity.this, "Giá size vừa sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( giaS == null || giaS.equals("")){
                    Toast.makeText(ThemMenuActivity.this, "Giá size nhỏ sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( gioithieu == null || gioithieu.equals("")){
                    Toast.makeText(ThemMenuActivity.this, "Giới thiệu sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( topping == null || topping.equals("")){
                    Toast.makeText(ThemMenuActivity.this, "Topping sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if (giatopping == null || giatopping.equals("")){
                    Toast.makeText(ThemMenuActivity.this, "Giá topping sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( loai == null || loai.equals("")){
                    Toast.makeText(ThemMenuActivity.this, "Loại sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else {
                    SanPham sanPham = new SanPham(sanphamId,ten,giaL,giaM,giaS,gioithieu,topping,giatopping,like,loai,linkimage);
                    mData.child("SanPham").child(sanphamId).setValue(sanPham).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ThemMenuActivity.this, "Thêm thành công.", Toast.LENGTH_SHORT).show();
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
                imgSanPham.setDrawingCacheEnabled(true);
                imgSanPham.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgSanPham.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(ThemMenuActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ThemMenuActivity.this, "Tải ảnh thành công!", Toast.LENGTH_SHORT).show();
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
            imgSanPham.setImageBitmap(bitmap);
            tvLuuY.setVisibility(View.VISIBLE);
            imgUpload.setVisibility(View.VISIBLE);
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgSanPham.setImageBitmap(bitmap);
                tvLuuY.setVisibility(View.VISIBLE);
                imgUpload.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void AnhXa() {
        imgSanPham = findViewById(R.id.imgthemmenu);
        imgBack = findViewById(R.id.imgbackthemmenu);
        imgCam = findViewById(R.id.imgcamerathemmenu);
        imgFolder = findViewById(R.id.imgfolderthemmenu);
        imgUpload = findViewById(R.id.imguploadthemmenu);
        tvTitle = findViewById(R.id.tvthemmenu);
        tvLuuY = findViewById(R.id.tvluuythemmenu);
        edtTen = findViewById(R.id.edttenthemmenu);
        edtSizeL = findViewById(R.id.edtgiasizelonthemmenu);
        edtSizeM = findViewById(R.id.edtgiasizevuathemmenu);
        edtSizeS = findViewById(R.id.edtgiasizenhothemmenu);
        edtGioiThieu = findViewById(R.id.edtgioithieuthemmenu);
        edtTopping = findViewById(R.id.edttoppingthemmenu);
        edtGiaTopping = findViewById(R.id.edtgiatoppingthemmenu);
        edtLoai = findViewById(R.id.edtloaithemmenu);
        btnThem = findViewById(R.id.btnthemmenu);

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