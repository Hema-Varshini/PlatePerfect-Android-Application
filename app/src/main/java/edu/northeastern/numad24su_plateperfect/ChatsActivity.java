package edu.northeastern.numad24su_plateperfect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class ChatsActivity extends AppCompatActivity implements IMessageDisplayListener {

    private RecyclerView recyclerview;
    private RecyclerView.Adapter recyclerviewAdapter;
    private DatabaseReference userdatabaseReference;
    private ArrayList<User> usersList;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate();
        Intent intent = getIntent();
        //currentUser = intent.getStringExtra("currentUser");
        currentUser = "Shashank";
        usersList = new ArrayList<User>();
        userdatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        recyclerview = findViewById(R.id.chatsRecyclerView);
        populateRecyclerView();
        recyclerviewAdapter = new ChatsActivityAdapter(this, usersList,this);

        recyclerview.setAdapter(recyclerviewAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

    }

    private void populateRecyclerView() {
        userdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();  // Clear the list before adding new data
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class);
                    User userModel = new User(username);
                    if (username != null && !username.equals(currentUser)) {
                        usersList.add(userModel);
                    }
                }
                usersList.add(new User("shank"));
                usersList.add(new User("Test"));
                recyclerviewAdapter.notifyDataSetChanged();  // Notify the adapter to refresh the ListView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inflate() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onUserClicked(int pos, String string) {
        // send the username of the user clicked
        // send the current user logged in
        // Based sender and receiver activity should fetch the message history between them
        // recipe ID shared , should have intent which open the recipe id from it.
    }
}