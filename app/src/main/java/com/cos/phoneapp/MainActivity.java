package com.cos.phoneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.cos.phoneapp.adapter.PhoneAdapter;
import com.cos.phoneapp.model.Phone;
import com.cos.phoneapp.service.PhoneService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity2";
    private Context mContext = MainActivity.this;

    private RecyclerView rvPhone;
    private FloatingActionButton btnAdd;
    private PhoneAdapter phoneAdapter;

    private List<Phone> phones = new ArrayList<>();
    private PhoneService phoneService;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneService = PhoneService.retrofit.create(PhoneService.class);

        init();
        initDate();
        initListener();
    }

    public void updateAndDelete(Phone phone, int position) {

        View dialog = LayoutInflater.from(mContext).inflate(R.layout.detail_phone, null);

        TextInputEditText tvDetailName = dialog.findViewById(R.id.tv_detail_name);
        TextInputEditText tvDetailTel = dialog.findViewById(R.id.tv_detail_tel);

        Log.d(TAG, "update: " + phone);
        String phoneName = phone.getName();

        Call<CMRespDto<Phone>> call = phoneService.findById(phone.getId());
        call.enqueue(new Callback<CMRespDto<Phone>>() {
            @Override
            public void onResponse(Call<CMRespDto<Phone>> call, Response<CMRespDto<Phone>> response) {
                CMRespDto<Phone> cmRespDto = response.body();

                Phone phoneEntity = cmRespDto.getData();
                tvDetailName.setText(phoneEntity.getName());
                tvDetailTel.setText(phoneEntity.getTel());

            }

            @Override
            public void onFailure(Call<CMRespDto<Phone>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);

        dlg.setTitle(phoneName + "?????? ?????????");
        dlg.setView(dialog);
        dlg.setPositiveButton("??????", (dialog1, which) -> {
            Call<CMRespDto<Phone>> call1 = phoneService.deleteById(phone.getId());
            call1.enqueue(new Callback<CMRespDto<Phone>>() {
                @Override
                public void onResponse(Call<CMRespDto<Phone>> call1, Response<CMRespDto<Phone>> response) {
                    CMRespDto<Phone> cmRespDto = response.body();

                    phoneAdapter.removeItem(position);
                    Toast.makeText(MainActivity.this, phoneName + "?????? ????????? ?????? ??????", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<CMRespDto<Phone>> call1, Throwable t) {
                    Toast.makeText(MainActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: delete() ??????");
                    t.printStackTrace();
                }
            });
        });
        dlg.setNegativeButton("??????", (dialog1, which) -> {
            Phone phoneUpdate = new Phone(
                    tvDetailName.getText().toString(),
                    tvDetailTel.getText().toString()
            );

            Call<CMRespDto<Phone>> call1 = phoneService.update(phone.getId(), phoneUpdate);
            call1.enqueue(new Callback<CMRespDto<Phone>>() {
                @Override
                public void onResponse(Call<CMRespDto<Phone>> call1, Response<CMRespDto<Phone>> response) {
                    CMRespDto<Phone> cmRespDto = response.body();

                    phoneAdapter.setItem(position, phoneUpdate);
                    Toast.makeText(MainActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<CMRespDto<Phone>> call1, Throwable t) {
                    Toast.makeText(MainActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: update() ??????");
                    t.printStackTrace();
                }
            });
        });
        dlg.show();
    }

    private void initDate() {
        Call<CMRespDto<List<Phone>>> call = phoneService.findAll();

        call.enqueue(new Callback<CMRespDto<List<Phone>>>() {
            @Override
            public void onResponse(Call<CMRespDto<List<Phone>>> call, Response<CMRespDto<List<Phone>>> response) {

                CMRespDto<List<Phone>> cmRespDto = response.body();

                if (cmRespDto.getCode() == 1) {
                    phones = cmRespDto.getData();
                    Log.d(TAG, "?????????: " + phones);
                    phoneAdapter.setItems(phones);
                }
            }

            @Override
            public void onFailure(Call<CMRespDto<List<Phone>>> call, Throwable t) {
                Log.d(TAG, "onFailure: findAll() ??????___");
                t.printStackTrace();
            }
        });
    }


    private void init() {
        btnAdd = findViewById(R.id.btn_add);
        rvPhone = findViewById(R.id.rv_phone);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvPhone.setLayoutManager(manager);
        phoneAdapter = new PhoneAdapter(phones, MainActivity.this);
        rvPhone.setAdapter(phoneAdapter);
    }

    public void initListener() {

        btnAdd.setOnClickListener(v -> {

            View dialog = v.inflate(v.getContext(), R.layout.detail_phone, null);

            TextInputEditText tvDetailName = dialog.findViewById(R.id.tv_detail_name);
            TextInputEditText tvDetailTel = dialog.findViewById(R.id.tv_detail_tel);

            AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());

            dlg.setTitle("????????? ??????");
            dlg.setView(dialog);
            dlg.setPositiveButton("??????", null);
            dlg.setNegativeButton("??????", (dialog1, which) -> {

                if (tvDetailName.getText().toString().equals("") && tvDetailTel.getText().toString().equals("")) {
                    Toast.makeText(mContext, "?????? ?????? ??????????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                } else {

                    Phone phoneSave = new Phone(
                            tvDetailName.getText().toString(),
                            tvDetailTel.getText().toString()
                    );
                    Call<CMRespDto<Phone>> call = phoneService.save(phoneSave);
                    call.enqueue(new Callback<CMRespDto<Phone>>() {
                        @Override
                        public void onResponse(Call<CMRespDto<Phone>> call, Response<CMRespDto<Phone>> response) {
                            Toast.makeText(MainActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                            phoneAdapter.addItem(phoneSave);
                            initDate();
                        }

                        @Override
                        public void onFailure(Call<CMRespDto<Phone>> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: save() ??????");
                            t.printStackTrace();
                        }
                    });
                }
            });
            dlg.show();
        });
    }
}