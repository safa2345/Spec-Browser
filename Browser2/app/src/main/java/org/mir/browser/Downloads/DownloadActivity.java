package org.mir.browser.Downloads;

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

public class DownloadActivity extends AppCompatActivity {
    private RecyclerView downloadRecView;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    ArrayList<DownloadModel> download;
    private String userId = "Safa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        downloadRecView = findViewById(R.id.download_recyclerview);

        rootNode = FirebaseDatabase.getInstance("https://browser-c0dd5-default-rtdb.asia-southeast1.firebasedatabase.app");
        reference = rootNode.getReference("Users/" + userId + "/Downloads");

        download = new ArrayList<>();

        DownloadAdapter adapter = new DownloadAdapter();
        adapter.setDownloadList(download);

        downloadRecView.setAdapter(adapter);
        downloadRecView.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DownloadModel downloadModel = dataSnapshot.getValue(DownloadModel.class);
                    download.add(downloadModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}