package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;

public class InfoAccountActivity extends AppCompatActivity {

    private ImageView imgClose,imgEdit;
    private CircleImageView imgKhachHang;
    private TextView tvTenKHChinh, tvTenKHPhu, tvSDT, tvGioiTinh, tvSinhNhat, tvEditAccount, tvLoaiNguoiDung, tvMota;
    private LinearLayout layoutDoiMatKhau, layoutDiemLevel, layoutMotaNhanVien;
    private View viewKeNgangMotaNhanVien;
    private ProgressBar progressBarDoiMatKhau, progressBarEditAccount;

    private ImageView imgaccount;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    User user;

    //upload img
    int REQUEST_CODE_FOLDER = 1;
    int REQUEST_CODE_CAMERA = 2;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_account);

        //upload ảnh phải có
        mStorageRef = FirebaseStorage.getInstance().getReference();

        AnhXa();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                assert user != null;

                String loainguoidung= user.getType();
                if (loainguoidung.equals("Khách hàng")){
                    layoutDiemLevel.setVisibility(View.VISIBLE);
                }else {
                    tvLoaiNguoiDung.setText("Nhân viên");
                    viewKeNgangMotaNhanVien.setVisibility(View.VISIBLE);
                    layoutMotaNhanVien.setVisibility(View.VISIBLE);
                    if (user.getNote().equals("default")){
                        tvMota.setText("Fulltime");
                    }else {
                        tvMota.setText(user.getNote());
                    }
                }
                tvTenKHChinh.setText(user.getName());
                tvTenKHPhu.setText(user.getName());
                tvGioiTinh.setText(user.getGender());
                tvSDT.setText(user.getPhone());
                if (user.getImageURL().equals("default")) {
                    imgKhachHang.setImageResource(R.drawable.example);
                } else {
                    Picasso.with(getApplicationContext()).load(user.getImageURL()).into(imgKhachHang);
                }
                if (user.getBirthday().equals("default")){
                    tvSinhNhat.setText("00/00/0000");
                }else {
                    tvSinhNhat.setText(user.getBirthday());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoAccountActivity.this);
                builder.setCancelable(false);
                //set layout cho dialog
                View view = LayoutInflater.from(InfoAccountActivity.this).inflate(R.layout.dialog_edit_img_account, null);
                ImageView imgcamera = view.findViewById(R.id.imgcamera);
                ImageView imgcancel = view.findViewById(R.id.imgcloseuploadimgkhachhang);
                ImageView imgfolder = view.findViewById(R.id.imgfolder);
                imgaccount = view.findViewById(R.id.imguploadkhachhang);
                FButton btnupload = view.findViewById(R.id.btnuploadimgkhachhang);
                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                imgcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                imgfolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_CODE_FOLDER);
                    }
                });

                imgcamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CODE_CAMERA);
                    }
                });

                btnupload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        StorageReference mountainsRef = mStorageRef.child("image" + calendar.getTimeInMillis() + ".png");

                        // Get the data from an ImageView as bytes
                        imgaccount.setDrawingCacheEnabled(true);
                        imgaccount.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) imgaccount.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainsRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(InfoAccountActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
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
                                        String linkimage = String.valueOf(uri);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("imageURL", linkimage);
                                        databaseReference.updateChildren(hashMap);
                                        alertDialog.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Log.d("AAA", "Failed to get uri");
                                    }
                                });
                                Toast.makeText(InfoAccountActivity.this, "Tải ảnh thành công!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

        });

        layoutDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoAccountActivity.this);
                builder.setCancelable(false);
                //set layout cho dialog
                View view = LayoutInflater.from(InfoAccountActivity.this).inflate(R.layout.dialog_change_password, null);
                TextView tvDoiMatKhau = view.findViewById(R.id.tvdoimatkhau);
                ImageView imgClose = view.findViewById(R.id.imgclosechangepass);
                final EditText edtMatKhauCu = view.findViewById(R.id.edtmatkhaucu);
                final EditText edtMatKhauMoi = view.findViewById(R.id.edtmatkhaumoi);
                final EditText edtNhapLaiMatKhau = view.findViewById(R.id.edtnhaplaimatkhau);
                progressBarDoiMatKhau = view.findViewById(R.id.progressbardoimatkhau);
                Button btnDoiMatKhau = view.findViewById(R.id.btndoimatkhau);
                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                //set font tvlogan
                Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
                tvDoiMatKhau.setTypeface(typeface);
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldpass= edtMatKhauCu.getText().toString();
                        String newpass= edtMatKhauMoi.getText().toString();
                        String confirnpass= edtNhapLaiMatKhau.getText().toString();
                        if (oldpass.isEmpty()||newpass.isEmpty()||confirnpass.isEmpty()){
                            Toast.makeText(InfoAccountActivity.this, "Nhập đầy đủ mật khẩu!", Toast.LENGTH_SHORT).show();
                        }else if(newpass.length()<6){
                            Toast.makeText(InfoAccountActivity.this, "Mật khẩu mới phải trên 6 ký tự!", Toast.LENGTH_SHORT).show();
                        }else if(!confirnpass.equals(newpass)){
                            Toast.makeText(InfoAccountActivity.this, "Nhập lại sai mật khẩu!", Toast.LENGTH_SHORT).show();
                        }else {
                            ChagePass(oldpass, confirnpass);
                        }
                    }
                });
            }
        });

        tvEditAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoAccountActivity.this);
                builder.setCancelable(false);
                //set layout cho dialog
                View view = LayoutInflater.from(InfoAccountActivity.this).inflate(R.layout.dialog_edit_account, null);
                TextView tvEditAccount = view.findViewById(R.id.tveditaccount);
                ImageView imgCloseEditAccount = view.findViewById(R.id.imgcloseeditaccount);
                final EditText edtTenEditAccount = view.findViewById(R.id.edtteneditaccount);
                final EditText edtGioiTinhEditAccount = view.findViewById(R.id.edtgioitinheditaccount);
                final EditText edtSDTEditAccount = view.findViewById(R.id.edtsdteditaccount);
                final EditText edtSinhNhatEditAccount = view.findViewById(R.id.edtsinhnhateditaccount);
                final EditText edtMotaEditAccount = view.findViewById(R.id.edtmotaeditaccount);
                LinearLayout layoutMotaEditAccount = view.findViewById(R.id.layoutmotanhanvieneditaccount);
                progressBarEditAccount = view.findViewById(R.id.progressbareditaccount);
                Button btnEditAccount = view.findViewById(R.id.btneditaccount);
                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //gán dữ liệu để sửa
                assert user != null;
                if (user.getType().equals("Nhân viên")){
                    layoutMotaEditAccount.setVisibility(View.VISIBLE);
                    if (user.getNote().equals("default")){
                        edtMotaEditAccount.setText("Fulltime");
                    }else {
                        edtMotaEditAccount.setText(user.getNote());
                    }
                }else {
                    edtMotaEditAccount.setText("No");
                }
                edtTenEditAccount.setText(user.getName());
                edtGioiTinhEditAccount.setText(user.getGender());
                edtSDTEditAccount.setText(user.getPhone());
                if (user.getBirthday().equals("default")){
                    edtSinhNhatEditAccount.setText("00/00/0000");
                }else {
                    edtSinhNhatEditAccount.setText(user.getBirthday());
                }
                //set font tvlogan
                Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
                tvEditAccount.setTypeface(typeface);
                imgCloseEditAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btnEditAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBarEditAccount.setVisibility(View.VISIBLE);
                        String tenmoi= edtTenEditAccount.getText().toString();
                        String gioitinhmoi= edtGioiTinhEditAccount.getText().toString();
                        String sdtmoi= edtSDTEditAccount.getText().toString();
                        String sinhnhatmoi= edtSinhNhatEditAccount.getText().toString();
                        String motamoi= edtMotaEditAccount.getText().toString();

                        EditAccount(tenmoi, gioitinhmoi,sdtmoi,sinhnhatmoi,motamoi);
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    private void EditAccount(String tenmoi, String gioitinhmoi, String sdtmoi, String sinhnhatmoi,String motamoi) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", tenmoi);
        hashMap.put("gender", gioitinhmoi);
        hashMap.put("phone", sdtmoi);
        hashMap.put("birthday", sinhnhatmoi);
        hashMap.put("note", motamoi);
        databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBarEditAccount.setVisibility(View.GONE);
                    Toast.makeText(InfoAccountActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(InfoAccountActivity.this, "Lỗi! Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void ChagePass(String oldpass, final String confirnpass) {
        progressBarDoiMatKhau.setVisibility(View.VISIBLE);
        AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),oldpass);
        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseUser.updatePassword(confirnpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                firebaseAuth.signOut();
                                Intent intent = new Intent(InfoAccountActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(InfoAccountActivity.this, "Đổi mật khẩu thất bại! Thử lại sau!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    progressBarDoiMatKhau.setVisibility(View.GONE);
                    Toast.makeText(InfoAccountActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgaccount.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgaccount.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AnhXa() {
        imgClose = findViewById(R.id.imgcloseinfoaccount);
        imgEdit = findViewById(R.id.imgsuaanhkhachhanginfo);
        imgKhachHang = findViewById(R.id.imgkhachhanginfo);
        tvTenKHChinh = findViewById(R.id.tvtenkhachhangchinhinfo);
        tvTenKHPhu = findViewById(R.id.tvtenkhachhangphuinfo);
        tvSDT = findViewById(R.id.tvsdtkhachhanginfo);
        tvGioiTinh = findViewById(R.id.tvgioitinhkhachhanginfo);
        tvSinhNhat = findViewById(R.id.tvngaysinhkhachhanginfo);
        tvEditAccount = findViewById(R.id.tvsuainfo);
        tvLoaiNguoiDung = findViewById(R.id.tvloainguoidunginfoaccount);
        tvMota = findViewById(R.id.tvmotanhanvieninfo);
        layoutDoiMatKhau = findViewById(R.id.layoutdoimatkhau);
        layoutDiemLevel = findViewById(R.id.layoutdiemlevel);
        layoutMotaNhanVien = findViewById(R.id.layoutmotanhanvieninfo);
        viewKeNgangMotaNhanVien = findViewById(R.id.viewkengang);

        //set font tvlogan
        Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        tvTenKHChinh.setTypeface(typeface);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}