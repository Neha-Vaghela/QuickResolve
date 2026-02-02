package com.quickresolve;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quickresolve.Adapter.ComplaintAdapter;
import com.quickresolve.ModelClass.ComplaintModel;

import java.util.ArrayList;

public class ComplaintListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ComplaintModel> list;
    ComplaintAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_list);
        recyclerView = findViewById(R.id.complaintRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new ComplaintAdapter(this, list);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        db.collection("complaints")
                .addSnapshotListener((value, error) -> {
                    list.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        ComplaintModel model = doc.toObject(ComplaintModel.class);
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}