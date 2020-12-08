package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.CuaHang;
import com.example.alphacoffee.model.SanPham;
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

public class SuaXoaMenuActivity extends AppCompatActivity {

    private ImageView imgSanPham, imgBack, imgCam, imgFolder, imgUpload;
    private TextView tvLuuY;
    private EditText edtTen, edtSizeL, edtSizeM, edtSizeS, edtGioiThieu, edtTopping, edtGiaTopping, edtLoai;
    private FButton btnSua, btnXoa;

    DatabaseReference mData;
    private StorageReference mStorageRef;
    private int REQUEST_CODE_FOLDER = 69;
    private int REQUEST_CODE_CAMERA = 96;

    String linkimage = null;
    SanPham sanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_xoa_menu);

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
                String giaL = edtSizeL.getText().toString().trim();
                String giaM = edtSizeM.getText().toString().trim();
                String giaS = edtSizeS.getText().toString().trim();
                String gioithieu = edtGioiThieu.getText().toString().trim();
                String topping = edtTopping.getText().toString().trim();
                String giatopping = edtGiaTopping.getText().toString().trim();
                String like = "0";
                String loai = edtLoai.getText().toString().trim();

                if (linkimage == null){
                    Toast.makeText(SuaXoaMenuActivity.this, "Upload ảnh trước rồi mới thêm!", Toast.LENGTH_SHORT).show();
                }else if (ten == null || ten.equals("")){
                    Toast.makeText(SuaXoaMenuActivity.this, "Tên của sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( giaL == null || giaL.equals("")){
                    Toast.makeText(SuaXoaMenuActivity.this, "Giá size lớn sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( giaM == null || giaM.equals("")){
                    Toast.makeText(SuaXoaMenuActivity.this, "Giá size vừa sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( giaS == null || giaS.equals("")){
                    Toast.makeText(SuaXoaMenuActivity.this, "Giá size nhỏ sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( gioithieu == null || gioithieu.equals("")){
                    Toast.makeText(SuaXoaMenuActivity.this, "Giới thiệu sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( topping == null || topping.equals("")){
                    Toast.makeText(SuaXoaMenuActivity.this, "Topping sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if (giatopping == null || giatopping.equals("")){
                    Toast.makeText(SuaXoaMenuActivity.this, "Giá topping sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else if ( loai == null || loai.equals("")){
                    Toast.makeText(SuaXoaMenuActivity.this, "Loại sản phẩm không được bỏ trống!", Toast.LENGTH_SHORT).show();
                }else {
                    UpdateSanPham(ten,giaL,giaM,giaS,gioithieu,topping,giatopping,like,loai,linkimage);
                }
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(SuaXoaMenuActivity.this);
                builder.setCancelable(true);
                //set layout cho dialog
                View view = LayoutInflater.from(SuaXoaMenuActivity.this).inflate(R.layout.dialog_xoa, null);
                TextView tvCo = view.findViewById(R.id.tvco);
                TextView tvKhong = view.findViewById(R.id.tvkhong);
                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                tvCo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.child("SanPham").child(sanPham.getProductId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SuaXoaMenuActivity.this, "Xóa thành công.", Toast.LENGTH_SHORT).show();
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
        sanPham = (SanPham) getIntent().getSerializableExtra("suaxoamenu");

        Picasso.with(this).load(sanPham.getImageURL()).into(imgSanPham);
        linkimage = sanPham.getImageURL();
        edtTen.setText(sanPham.getName());
        edtSizeL.setText(sanPham.getPriceL());
        edtSizeM.setText(sanPham.getPriceM());
        edtSizeS.setText(sanPham.getPriceS());
        edtGioiThieu.setText(sanPham.getNote());
        edtTopping.setText(sanPham.getTopping());
        edtGiaTopping.setText(sanPham.getPriceTopping());
        edtLoai.setText(sanPham.getType());
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
                        Toast.makeText(SuaXoaMenuActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SuaXoaMenuActivity.this, "Tải ảnh thành công!", Toast.LENGTH_SHORT).show();
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

    private void UpdateSanPham(String tenmoi, String giaLmoi, String giaMmoi, String giaSmoi, String gioithieumoi, String toppingmoi, String giatoppingmoi, String likemoi, String loaimoi, String hinhmoi) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", tenmoi);
        hashMap.put("priceL", giaLmoi);
        hashMap.put("priceM", giaMmoi);
        hashMap.put("priceS", giaSmoi);
        hashMap.put("note", gioithieumoi);
        hashMap.put("topping", toppingmoi);
        hashMap.put("priceTopping", giatoppingmoi);
        hashMap.put("like", likemoi);
        hashMap.put("type", loaimoi);
        hashMap.put("imageURL", hinhmoi);

        mData.child("SanPham").child(sanPham.getProductId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SuaXoaMenuActivity.this, "Cập nhật cửa hàng thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SuaXoaMenuActivity.this, "Lỗi! Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AnhXa() {
        imgSanPham = findViewById(R.id.imgsuaxoamenu);
        imgBack = findViewById(R.id.imgbacksuaxoamenu);
        imgCam = findViewById(R.id.imgcamerasuaxoamenu);
        imgFolder = findViewById(R.id.imgfoldersuaxoamenu);
        imgUpload = findViewById(R.id.imguploadsuaxoamenu);
        tvLuuY = findViewById(R.id.tvluuysuaxoamenu);
        edtTen = findViewById(R.id.edttensuaxoamenu);
        edtSizeL = findViewById(R.id.edtgiasizelonsuaxoamenu);
        edtSizeM = findViewById(R.id.edtgiasizevuasuaxoamenu);
        edtSizeS = findViewById(R.id.edtgiasizenhosuaxoamenu);
        edtGioiThieu = findViewById(R.id.edtgioithieusuaxoamenu);
        edtTopping = findViewById(R.id.edttoppingsuaxoamenu);
        edtGiaTopping = findViewById(R.id.edtgiatoppingsuaxoamenu);
        edtLoai = findViewById(R.id.edtloaisuaxoamenu);
        btnSua = findViewById(R.id.btnsuamenu);
        btnXoa = findViewById(R.id.btnxoamenu);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}