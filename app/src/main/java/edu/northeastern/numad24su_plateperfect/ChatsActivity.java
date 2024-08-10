package edu.northeastern.numad24su_plateperfect;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;

public class ChatsActivity extends AppCompatActivity implements IMessageDisplayListener {

    private RecyclerView recyclerview;
    private RecyclerView.Adapter recyclerviewAdapter;
    private DatabaseReference userdatabaseReference;
    private ArrayList<User> usersList;
    private String currentUser;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    Toast.makeText(this, "Notification access granted !!",Toast.LENGTH_SHORT);
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Toast.makeText(this, "Denied !!",Toast.LENGTH_SHORT);
                }
            });
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
        askNotificationPermission();

        //get the fcm token
        getTheFCMToken();
    }

    private void getTheFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    private static final String TAG = "FCM TOKEN";

                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d(TAG,token);

                        // Retrieve the username from shared preferences or another source
                        String username = currentUser; // Implement this method to get the username

                        if (username != null) {
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("fcmTokens").child(username);
                            userRef.setValue(token)
                                    .addOnCompleteListener(fcmtask -> {
                                        if (fcmtask.isSuccessful()) {
                                            Log.d(TAG, "Token stored successfully.");
                                        } else {
                                            Log.d(TAG, "Failed to store token: " + task.getException().getMessage());
                                        }
                                    });
                        }
                        Toast.makeText(ChatsActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void populateRecyclerView() {
        userdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();  // Clear the list before adding new data
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user.getUsername() != null && !user.getUsername().equals(currentUser)) {
                        usersList.add(user);
                    }
                }
//                usersList.add(new User("shank"));
//                usersList.add(new User("Test"));
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
        User user = usersList.get(pos);
        //navigate to chat activity
        Intent intent = new Intent(this, MessageActivity.class);
        passUserModelAsIntent(intent,user);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void passUserModelAsIntent(Intent intent, User user) {
        intent.putExtra("sender",user.getUsername());
//        intent.putExtra("phone",user.getPhone());
//        intent.putExtra("userId",model.getUserId());
//        intent.putExtra("fcmToken",model.getFcmToken());
    }
}