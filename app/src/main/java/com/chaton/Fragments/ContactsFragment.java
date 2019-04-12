package com.chaton.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaton.Adapters.ContactsAdapter;
import com.chaton.R;
import com.chaton.Classes.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {
    private RecyclerView mContactListView;
    private ContactsAdapter mContactsAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContactListView = (RecyclerView) getView().findViewById(R.id.contactsListView);
        final List<Users> users = new ArrayList<>();
     //   users.add(new Users("asadsaleem@gmail.com","Asad Saleem",null));
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Users name = ds.getValue(Users.class);
                    mContactsAdapter.add(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mUsersDatabaseReference.addValueEventListener(eventListener);
        mContactsAdapter = new ContactsAdapter(users,getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mContactListView.setLayoutManager(manager);
        mContactListView.setItemAnimator(new DefaultItemAnimator());
        mContactListView.setAdapter(mContactsAdapter);

    }
}
