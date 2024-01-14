package org.mir.browser.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mir.browser.R;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecView;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    ArrayList<HistoryModel> history;
    private String userId = "Safa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecView = findViewById(R.id.history_recyclerview);

        rootNode = FirebaseDatabase.getInstance("https://browser-c0dd5-default-rtdb.asia-southeast1.firebasedatabase.app");
        reference = rootNode.getReference("Users/" + userId + "/History");

        history = new ArrayList<>();

        HistoryAdapter adapter = new HistoryAdapter();
        adapter.setHistoryList(history);

        historyRecView.setAdapter(adapter);
        historyRecView.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HistoryModel historyModel = dataSnapshot.getValue(HistoryModel.class);
                    history.add(historyModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}