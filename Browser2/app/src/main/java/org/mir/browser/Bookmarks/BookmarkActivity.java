package org.mir.browser.Bookmarks;

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

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView bookmarkRecView;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    ArrayList<BookmarkModel> bookmark;
    private String userId = "Safa";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        bookmarkRecView = findViewById(R.id.bookmark_recyclerview);

        rootNode = FirebaseDatabase.getInstance("https://browser-c0dd5-default-rtdb.asia-southeast1.firebasedatabase.app");
        reference = rootNode.getReference("Users/" + userId + "/Bookmarks");

        bookmark = new ArrayList<>();

        BookmarkAdapter adapter = new BookmarkAdapter();
        adapter.setHistoryList(bookmark);

        bookmarkRecView.setAdapter(adapter);
        bookmarkRecView.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookmarkModel bookmarkModel = dataSnapshot.getValue(BookmarkModel.class);
                    bookmark.add(bookmarkModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
