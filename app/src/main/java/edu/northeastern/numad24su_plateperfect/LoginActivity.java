package edu.northeastern.numad24su_plateperfect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private final String Tag = "Firebase Login Activity";
    private EditText username;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        username = findViewById(R.id.editTextUsername);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        //Intent intent = new Intent(FirebaseRTDBLoginActivity.this, FirebaseRTDBMessageHistoryRecyclerViewActivity.class);
        //startActivity(intent);
        btnLogin.setOnClickListener(v -> {
            Log.i(Tag, "Firebase Button Clicked !!");
            String currentUser = username.getText().toString().trim();
            if (!currentUser.isEmpty()) {
                addNewUsernameToDatabase(currentUser);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addNewUsernameToDatabase(String currentUser){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean usernameExists = false;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String existingUsername = userSnapshot.child("username").getValue(String.class);
                    Log.e("firebase", "existing username "+existingUsername);
                    if (existingUsername != null && existingUsername.equals(currentUser)) {
                        usernameExists = true;
                        break;
                    }
                }

                if (usernameExists) {
                    Toast.makeText(LoginActivity.this, "Username exists", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(FirebaseRTDBLoginActivity.this, FirebaseRTDBMessageHistoryRecyclerViewActivity.class);
//                    intent.putExtra("currentUser",currentUser);
//                    startActivity(intent);
                } else {
                    addUserToDatabase(currentUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addUserToDatabase(String username) {
        String userId = databaseReference.push().getKey();
        if (userId != null) {
            databaseReference.child(userId).child("username").setValue(username)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Username added successfully", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(FirebaseRTDBLoginActivity.this, FirebaseRTDBMessageHistoryRecyclerViewActivity.class);
//                            intent.putExtra("currentUser",username);
//                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to add username", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}