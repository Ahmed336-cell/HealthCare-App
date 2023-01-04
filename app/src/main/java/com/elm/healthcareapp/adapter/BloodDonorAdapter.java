package com.elm.healthcareapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elm.healthcareapp.R;
import com.elm.healthcareapp.model.BloodDonationRequest;

import java.util.List;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.ViewHolder> {
    List<BloodDonationRequest>bloodDonationRequests;
    Context context;
    public BloodDonorAdapter(List<BloodDonationRequest>bloodDonationRequests,Context context){
        this.bloodDonationRequests = bloodDonationRequests;
        this.context = context;


    }

    @NonNull
    @Override
    public BloodDonorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_cardview, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull BloodDonorAdapter.ViewHolder holder, int position) {
        String tot = bloodDonationRequests.get(position).getfName() + " "+bloodDonationRequests.get(position).getlName();
        holder.name.setText(tot);
        holder.bloodType.setText(bloodDonationRequests.get(position).getBloodType());
        holder.address.setText(bloodDonationRequests.get(position).getAddress());
        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = "+2"+bloodDonationRequests.get(position).getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                context.startActivity(intent);
            }
        });

        if (holder.bloodType.getText().toString().equals("A+")){
            holder.status.setText("common");
        }else if(holder.bloodType.getText().toString().equals("O+")){
            holder.status.setText("common");

        }else if(holder.bloodType.getText().toString().equals("O-")){
            holder.status.setText("common");

        }else if(holder.bloodType.getText().toString().equals("A-")){
            holder.status.setText("common");

        }else if (holder.bloodType.getText().toString().equals("B+")){
            holder.status.setText("rare");

        }else if (holder.bloodType.getText().toString().equals("B-")){
            holder.status.setText("very rare");

        }else if (holder.bloodType.getText().toString().equals("AB+")){
            holder.status.setText("rare");

        }else{
            holder.status.setText("very rare");

        }
    }

    @Override
    public int getItemCount() {
        return bloodDonationRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView bloodType , name,address,status;
        Button contact;
        ImageView location,data;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodType = itemView.findViewById(R.id.blood_type_donor);
            name = itemView.findViewById(R.id.contact_name);
            address = itemView.findViewById(R.id.location);
            status  = itemView.findViewById(R.id.blood_status);
            contact = itemView.findViewById(R.id.contact_btn);
            location  = itemView.findViewById(R.id.icon_location);
            data = itemView.findViewById(R.id.contact_icon);


        }
    }
}
