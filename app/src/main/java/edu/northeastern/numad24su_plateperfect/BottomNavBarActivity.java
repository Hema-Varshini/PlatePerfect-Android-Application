package edu.northeastern.numad24su_plateperfect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.northeastern.numad24su_plateperfect.databinding.ActivityBottomNavBarBinding;

public class BottomNavBarActivity extends AppCompatActivity {
    ActivityBottomNavBarBinding binding;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    Toast.makeText(this, "Notification access granted !!", Toast.LENGTH_SHORT);
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Toast.makeText(this, "Denied !!", Toast.LENGTH_SHORT);
                }
            });
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityBottomNavBarBinding.inflate(getLayoutInflater());
        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");

        setContentView(binding.getRoot());
        binding.bottomNavigationView.setSelectedItemId(R.id.homeMenu);
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            Bundle bundle = new Bundle();
            bundle.putString("currentUser", currentUser);
            if (item.getItemId() == R.id.homeMenu) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.chatsMenu) {
                selectedFragment = new ChatsFragment();
            } else {
                selectedFragment = new ProfileFragment();
            }
            selectedFragment.setArguments(bundle);
            replaceFragment(selectedFragment);
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
                        Log.d(TAG, token);

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
                        Toast.makeText(BottomNavBarActivity.this, token, Toast.LENGTH_SHORT).show();
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

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_layout, fragment);
        fragmentTransaction.commit();
    }
}