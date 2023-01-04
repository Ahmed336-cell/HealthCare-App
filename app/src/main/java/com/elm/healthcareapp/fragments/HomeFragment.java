package com.elm.healthcareapp.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elm.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class HomeFragment extends Fragment {
    TextView name,age,gender,bloodtype;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    String fname,lname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        name = view.findViewById(R.id.userName);
        age = view.findViewById(R.id.userage);
        gender = view.findViewById(R.id.usergender);
        bloodtype = view.findViewById(R.id.user_blood_type);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        fname = value.getString("fName");
                        lname = value.getString("lName");
                        String fm = fname+" "+lname;
                        name.setText(fm);
                        bloodtype.setText(value.getString("bloodType"));
                        gender.setText(value.getString("gender"));


                    }
                });


        return view;
    }
}