package com.elm.healthcareapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.elm.healthcareapp.R;
import com.elm.healthcareapp.adapter.BloodDonorAdapter;
import com.elm.healthcareapp.model.BloodDonationRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class BloodFragment extends Fragment {
    RecyclerView rv;
    ArrayList<BloodDonationRequest>bloodDonationRequests;
    BloodDonorAdapter bloodDonorAdapter;
    FloatingActionButton add;
    FirebaseFirestore firestore;
    EditText fname,lname,age,phone,address;
    Spinner gender,bloodType;
    Button ac,cancel;
    String name1,name2,ageg,phonen,addd,gend,btype;
    AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_blood, container, false);
        rv = view.findViewById(R.id.recyle_view);
        add=  view.findViewById(R.id.fab);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        firestore = FirebaseFirestore.getInstance();
        bloodDonationRequests = new ArrayList<BloodDonationRequest>();
        bloodDonorAdapter = new BloodDonorAdapter(bloodDonationRequests,getContext());
        rv.setAdapter(bloodDonorAdapter);

        EventChangeLisnter();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });



        return view;
    }

    private void EventChangeLisnter() {
        firestore.collection("blood")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() ==DocumentChange.Type.ADDED){
                                bloodDonationRequests.add(dc.getDocument().toObject(BloodDonationRequest.class));
                            }
                            bloodDonorAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }
    private void add(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View customD = getLayoutInflater().inflate(R.layout.add_blood_request,null);

        fname = customD.findViewById(R.id.fisrtName_edt_do);
        lname = customD.findViewById(R.id.lastName_edt_do);
        age = customD.findViewById(R.id.age_edt_do);
        phone = customD.findViewById(R.id.phone_edt_do);
        address = customD.findViewById(R.id.address_do);
        gender = customD.findViewById(R.id.gender_do);
        bloodType = customD.findViewById(R.id.blood_type_do);

        name1 = fname.getText().toString();
        name2 = lname.getText().toString();
        ageg = age.getText().toString();
        phonen = phone.getText().toString();
        addd = address.getText().toString();
        ArrayAdapter<CharSequence> adapterGender= ArrayAdapter.createFromResource(getContext(),R.array.gender, android.R.layout.simple_spinner_dropdown_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapterGender);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gend = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapterBlood= ArrayAdapter.createFromResource(getContext(),R.array.blood, android.R.layout.simple_spinner_dropdown_item);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodType.setAdapter(adapterBlood);
        bloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                btype = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ac =customD.findViewById(R.id.add_btn);
        cancel =customD.findViewById(R.id.cancel_button);
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference = firestore.collection("blood")
                        .document();
                BloodDonationRequest bloodDonationRequest = new BloodDonationRequest(
                        fname.getText().toString(),
                        lname.getText().toString(),
                        phone.getText().toString(),
                        age.getText().toString(),
                        btype,
                        gend,
                        address.getText().toString()
                );
                documentReference.set(bloodDonationRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "added succfully", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();

            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        builder.setView(customD);
        dialog = builder.create();
        dialog.show();
    }
}