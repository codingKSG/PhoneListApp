package com.cos.phoneapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.phoneapp.MainActivity;
import com.cos.phoneapp.R;
import com.cos.phoneapp.model.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ContactViewHolder> {

    private static final String TAG = "PhoneAdapter";
    private List<Phone> phones;
    private MainActivity mainActivity;

    public PhoneAdapter(List<Phone> phones, MainActivity mainActivity) {
        this.phones = phones;
        this.mainActivity = mainActivity;
    }

    public void setItems(List<Phone> phones) {
        this.phones = phones;
        notifyDataSetChanged();
    }

    public void setItem(int position, Phone phone) {
        phones.get(position).setName(phone.getName());
        phones.get(position).setTel(phone.getTel());
        notifyDataSetChanged();
    }

    public void addItem(Phone Phone) {
        this.phones.add(Phone);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.phones.remove(position);
        notifyDataSetChanged();
    }

    public void removeAll() {
        this.phones = new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Phone phone = phones.get(position);
        holder.setItem(phone);
        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "onBindViewHolder: "+phones);
            Log.d(TAG, "onBindViewHolder: "+phone);
            mainActivity.updateAndDelete(phone, position);
        });
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvPhone;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tv_name);
            this.tvPhone = itemView.findViewById(R.id.tv_tel);
        }

        public void setItem(Phone Phone) {
            tvName.setText(Phone.getName());
            tvPhone.setText(Phone.getTel());
        }

    }
}
