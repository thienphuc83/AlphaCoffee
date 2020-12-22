package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphacoffee.R;
import com.example.alphacoffee.adapter.SanPhamOrderAdapter;
import com.example.alphacoffee.model.Bill;
import com.example.alphacoffee.model.CuaHang;
import com.example.alphacoffee.model.SanPhamOrder;
import com.example.alphacoffee.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BillActivity extends AppCompatActivity {

    private TextView tvGioHang, tvTichDiemBill, tvTongTienOrderBill,
             tvTenKHBill, tvKieuNhanBill, tvThanhToanBill, tvChonCuaHang;
    private ImageView imgBackBill;
    private RelativeLayout layoutOrderBill;
    private EditText edtGhiChuBill;
    private Spinner spinnerCuaHang;
    private RecyclerView rvSanPhamOrderBill;

    SanPhamOrderAdapter sanPhamOrderAdapter;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference mData;
    DatabaseReference databaseReference;

    User user;

    long tongTien = 0;
    int diem = 0;

    String tenKH = null;
    String idKH = null;
    String tenCH = null;

    String diaDiemUong = "";
    String thanhToanBang = "";

    private ArrayList<String> mangTenCuaHang = new ArrayList<>();

    boolean nhapChon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        mData = FirebaseDatabase.getInstance().getReference();

        //lấy thằng user
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                assert user != null;

                tenKH = user.getName();
                idKH = user.getUserId();
                tvTenKHBill.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AnhXa();

        CheckData();

        TongTien();

        ChonCuaHang();

        // vuốt để xoa sản phẩm order
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ProductActivity.mangsanphamorder.remove(position);
                Toast.makeText(BillActivity.this, "Đã xóa sản phẩm khỏi giỏ hàng!", Toast.LENGTH_SHORT).show();
                sanPhamOrderAdapter.notifyDataSetChanged();

            }
        });
        itemTouchHelper.attachToRecyclerView(rvSanPhamOrderBill);


        //order
        layoutOrderBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProductActivity.mangsanphamorder.size() > 0) {
                    // bắt đk để insert bill
                    if (nhapChon == false){
                        Toast.makeText(BillActivity.this, "Bạn chưa chọn cửa hàng!", Toast.LENGTH_SHORT).show();
                    } else if (diaDiemUong.equals("")) {
                        Toast.makeText(BillActivity.this, "Bạn chưa chọn kiểu nhận order!", Toast.LENGTH_SHORT).show();
                    } else if (thanhToanBang.equals("")) {
                        Toast.makeText(BillActivity.this, "Bạn chưa chọn kiểu thanh toán!", Toast.LENGTH_SHORT).show();
                    } else {
                        // insert bill vào firebaes để get key ra làm idBill và
                        // gán vào idbill của sanphamorder để sau này mở hóa đơn sẽ get sanphamorder theo bill
                        String idbill = mData.child("Bill").push().getKey();

                        //insert sanphamorder có thêm idbill trước rồi mới insert bill sau
                        for (int i = 0; i < ProductActivity.mangsanphamorder.size(); i++) {
//                    Toast.makeText(BillActivity.this, ProductActivity.mangsanphamorder.get(i).getName()+"", Toast.LENGTH_SHORT).show();

                            SanPhamOrder sanPhamOrder = new SanPhamOrder(
                                    ProductActivity.mangsanphamorder.get(i).getId(),
                                    ProductActivity.mangsanphamorder.get(i).getName(),
                                    ProductActivity.mangsanphamorder.get(i).getPrice(),
                                    ProductActivity.mangsanphamorder.get(i).getSize(),
                                    ProductActivity.mangsanphamorder.get(i).getTopping(),
                                    ProductActivity.mangsanphamorder.get(i).getImageURL(),
                                    ProductActivity.mangsanphamorder.get(i).getSoluong(),
                                    idbill);

                            mData.child("SanPhamOrder").push().setValue(sanPhamOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                    Toast.makeText(BillActivity.this, "Thêm thành công SanPhamOrder", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                        // Bắt đầu lấy dữ liệu để insert data cho bill
                        String ghichu = edtGhiChuBill.getText().toString();
                        // ngày tạo
                        Calendar calendar = new GregorianCalendar();
                        String ngaytao = String.valueOf(calendar.getTime());

                        // thêm all data vào đối tượng bill
                        Bill bill = new Bill(idbill, "default", ngaytao, tongTien, "default",
                                diaDiemUong, ghichu, thanhToanBang, String.valueOf(diem),
                                idKH, tenKH, tenCH, "default","default");

                        //insert bill vào firebase
                        mData.child("Bill").child(idbill).setValue(bill).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(BillActivity.this, "Order thành công!", Toast.LENGTH_SHORT).show();
                                    // kết thúc màn hình sau khi đặt hàng
//                                    startActivity(new Intent(BillActivity.this, ProductActivity.class));
                                    ProductActivity.mangsanphamorder.clear();
                                    finish();

                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(BillActivity.this, "Bạn chưa chọn món!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void ChonCuaHang(){
        mData.child("CuaHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mangTenCuaHang.clear();
                for (DataSnapshot item : snapshot.getChildren() ){
                    //
                    mangTenCuaHang.add(item.child("tenCuaHang").getValue(String.class));
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(BillActivity.this,android.R.layout.simple_spinner_dropdown_item,mangTenCuaHang);
                spinnerCuaHang.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnerCuaHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(BillActivity.this, mangTenCuaHang.get(position), Toast.LENGTH_SHORT).show();
                tenCH = mangTenCuaHang.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void TongTien() {
        int sl = 0;
        for (int i = 0; i < ProductActivity.mangsanphamorder.size(); i++) {
            tongTien += ProductActivity.mangsanphamorder.get(i).getPrice();
            sl += ProductActivity.mangsanphamorder.get(i).getSoluong();

        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTongTienOrderBill.setText(decimalFormat.format(tongTien) + " đ");
        diem = sl * 2;
        tvTichDiemBill.setText(diem + "");
    }

    private void CheckData() {
        if (ProductActivity.mangsanphamorder.size() <= 0) {
            sanPhamOrderAdapter.notifyDataSetChanged();

        } else {
            sanPhamOrderAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void AnhXa() {
        tvGioHang = findViewById(R.id.tvgiohang);
        tvTichDiemBill = findViewById(R.id.tvtichdiembill);
        tvTongTienOrderBill = findViewById(R.id.tvtongtienbill);
        tvTenKHBill = findViewById(R.id.tvtenkhachhangbill);
        tvKieuNhanBill = findViewById(R.id.tvkieunhanbill);
        tvThanhToanBill = findViewById(R.id.tvthanhtoanbangbill);
        tvChonCuaHang = findViewById(R.id.tvchoncuahangbill);
        imgBackBill = findViewById(R.id.imgbackgiohang);
        layoutOrderBill = findViewById(R.id.layoutorderbill);
        edtGhiChuBill = findViewById(R.id.edtghichubill);
        rvSanPhamOrderBill = findViewById(R.id.rvsanphamorder);
        spinnerCuaHang = findViewById(R.id.spinnercuahangbill);


        imgBackBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvGioHang.setTypeface(typeface);

        //show sanphamorder ra list
        sanPhamOrderAdapter = new SanPhamOrderAdapter(this, ProductActivity.mangsanphamorder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvSanPhamOrderBill.setLayoutManager(layoutManager);
        rvSanPhamOrderBill.setAdapter(sanPhamOrderAdapter);


        // mở spinner chọn cửa hàng
        tvChonCuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhapChon = true;
                tvChonCuaHang.setVisibility(View.GONE);
                spinnerCuaHang.setVisibility(View.VISIBLE);
            }
        });


        //set kiểu nhận order cho bill
        tvKieuNhanBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(BillActivity.this);
                builder.setCancelable(false); // bấm ngoài vùng
                //set layout cho dialog
                View view = LayoutInflater.from(BillActivity.this).inflate(R.layout.dialog_kieunhanorder, null);
                TextView tvTaiQuan = view.findViewById(R.id.tvtaiquankieunhanorderbill);
                TextView tvDemVe = view.findViewById(R.id.tvdemvekieunhanorderbill);
                ImageView imgClose = view.findViewById(R.id.imgclosekieunhanorderbill);

                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                tvTaiQuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        diaDiemUong = "Dùng tại quán";
                        tvKieuNhanBill.setText("Tại quán");
                        tvKieuNhanBill.setTextColor(getResources().getColor(R.color.colorTheme));
                        alertDialog.dismiss();
                    }
                });
                tvDemVe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        diaDiemUong = "Mua mang về";
                        tvKieuNhanBill.setText("Mang về");
                        tvKieuNhanBill.setTextColor(getResources().getColor(R.color.colorTheme));
                        alertDialog.dismiss();
                    }
                });
            }
        });

        //set phương thức thanh toán cho bill
        tvThanhToanBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(BillActivity.this);
                builder.setCancelable(false); // bấm ngoài vùng
                //set layout cho dialog
                View view = LayoutInflater.from(BillActivity.this).inflate(R.layout.dialog_thanhtoan, null);
                LinearLayout tvTienMat = view.findViewById(R.id.layoutthanhtoanbangtienmat);
                LinearLayout tvMoMo = view.findViewById(R.id.layoutthanhtoanbangmomo);
                ImageView imgClose = view.findViewById(R.id.imgclosethanhtoanbill);

                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                tvTienMat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thanhToanBang = "Tiền mặt";
                        tvThanhToanBill.setText("Tiền mặt");
                        tvThanhToanBill.setTextColor(getResources().getColor(R.color.colorTheme));
                        alertDialog.dismiss();
                    }
                });
//                tvMoMo.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        thanhToanBang = "Momo";
//                        tvThanhToanBill.setText("Momo");
//                        tvThanhToanBill.setTextColor(getResources().getColor(R.color.colorTheme));
//                        alertDialog.dismiss();
//                    }
//                });
            }
        });

    }
}