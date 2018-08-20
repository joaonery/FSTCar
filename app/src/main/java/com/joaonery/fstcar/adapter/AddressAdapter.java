package com.joaonery.fstcar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joaonery.fstcar.R;
import com.joaonery.fstcar.model.Address;

import java.util.ArrayList;


public class AddressAdapter extends RecyclerView.Adapter{

    private Context context;
    private ArrayList<Address> adresses;

    private static ClickListener clickListener;

    public AddressAdapter(Context context, ArrayList<Address> adresses) {
        this.context = context;
        this.adresses = adresses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;
        Address a = adresses.get(position);

        switch (a.getState()){
            case "AC":
                h.ivState.setImageResource(R.drawable.ac);
                break;
            case "AL":
                h.ivState.setImageResource(R.drawable.al);
                break;
            case "AM":
                h.ivState.setImageResource(R.drawable.am);
                break;
            case "AP":
                h.ivState.setImageResource(R.drawable.ap);
                break;
            case "BA":
                h.ivState.setImageResource(R.drawable.ba);
                break;
            case "CE":
                h.ivState.setImageResource(R.drawable.ce);
                break;
            case "DF":
                h.ivState.setImageResource(R.drawable.df);
                break;
            case "ES":
                h.ivState.setImageResource(R.drawable.es);
                break;
            case "GO":
                h.ivState.setImageResource(R.drawable.go);
                break;
            case "MA":
                h.ivState.setImageResource(R.drawable.ma);
                break;
            case "MG":
                h.ivState.setImageResource(R.drawable.mg);
                break;
            case "MS":
                h.ivState.setImageResource(R.drawable.ms);
                break;
            case "MT":
                h.ivState.setImageResource(R.drawable.mt);
                break;
            case "PA":
                h.ivState.setImageResource(R.drawable.pa);
                break;
            case "PB":
                h.ivState.setImageResource(R.drawable.pb);
                break;
            case "PE":
                h.ivState.setImageResource(R.drawable.pe);
                break;
            case "PI":
                h.ivState.setImageResource(R.drawable.pi);
                break;
            case "PR":
                h.ivState.setImageResource(R.drawable.pr);
                break;
            case "RJ":
                h.ivState.setImageResource(R.drawable.rj);
                break;
            case "RN":
                h.ivState.setImageResource(R.drawable.rn);
                break;
            case "RO":
                h.ivState.setImageResource(R.drawable.ro);
                break;
            case "RR":
                h.ivState.setImageResource(R.drawable.rr);
                break;
            case "RS":
                h.ivState.setImageResource(R.drawable.rs);
                break;
            case "SC":
                h.ivState.setImageResource(R.drawable.sc);
                break;
            case "SE":
                h.ivState.setImageResource(R.drawable.se);
                break;
            case "SP":
                h.ivState.setImageResource(R.drawable.sp);
                break;
            case "TO":
                h.ivState.setImageResource(R.drawable.to);
                break;
            default:
                h.ivState.setImageResource(R.drawable.state_placeholder);
        }

        h.tvZipCode.setText(a.getZipCode());
        h.tvState.setText(a.getState());
        h.tvCity.setText(a.getCity());
        h.tvDistrict.setText(a.getDistrict());
        h.tvStreet.setText(a.getStreet());
        h.tvNumber.setText(String.valueOf(a.getNumber()));
    }

    @Override
    public int getItemCount() {
        return adresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private final ImageView ivState;
        private final TextView tvZipCode;
        private final TextView tvState;
        private final TextView tvCity;
        private final TextView tvDistrict;
        private final TextView tvStreet;
        private final TextView tvNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            ivState = itemView.findViewById(R.id.ac_iv_state);
            tvZipCode= itemView.findViewById(R.id.ac_tv_zip_code);
            tvState = itemView.findViewById(R.id.ac_tv_state);
            tvCity = itemView.findViewById(R.id.ac_tv_city);
            tvDistrict = itemView.findViewById(R.id.ac_tv_district);
            tvStreet = itemView.findViewById(R.id.ac_tv_street);
            tvNumber = itemView.findViewById(R.id.ac_tv_number);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener){
        AddressAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}