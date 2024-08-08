package edu.northeastern.numad24su_plateperfect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private static final String TAG = "RecipeActivity";
    private Recipe recipe;
    private boolean isLiked = false;
    private List<String> userNames = new ArrayList<>();
    private String currentUser;
    private DatabaseReference mdatabase;
    private ImageView recipeImage;
    private TextView recipeName;
    private ImageButton likeButton;
    private String fetchedRecipeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // For demonstration purposes, hardcoding the currentUser
        currentUser = "Shashank";

        fetchUserNames();

        // Set up UI elements
        recipeImage = findViewById(R.id.recipe_image);
        likeButton = findViewById(R.id.like_button);
        ImageButton shareButton = findViewById(R.id.share_button);
        ImageButton playButton = findViewById(R.id.play_button);
        recipeName = findViewById(R.id.recipe_name);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        ImageButton sendButton = findViewById(R.id.send_button);

        // Send to user
        sendButton.setOnClickListener(v -> {
            sendThisRecipe();
        });

        // Set up like button click listener
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked = !isLiked;
                updateLikeStatus(isLiked);
                likeButton.setImageResource(isLiked ? R.drawable.ic_heart_filled : R.drawable.ic_heart_unfilled);
            }
        });

        // Set up share button click listener
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.shareRecipe(RecipeActivity.this);
            }
        });

        // Set up play button click listener
        View.OnClickListener playVideoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getVideoUrl()));
                startActivity(intent);
            }
        };
        playButton.setOnClickListener(playVideoListener);
        recipeImage.setOnClickListener(playVideoListener);

        // Initialize Firebase Database reference
        mdatabase = FirebaseDatabase.getInstance("https://plateperfect-a2e82-default-rtdb.firebaseio.com/").getReference();

        // Fetch data from Firebase
        fetchRecipeData();

        mdatabase.child("latest/" + currentUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // new child added is new message received
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Toast.makeText(RecipeActivity.this, "New Message Received", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void fetchRecipeData() {
        // Fetch the image link and recipe name from Firebase
        mdatabase.child("recipes").child("1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageLink = dataSnapshot.child("Image_link").getValue(String.class);
                String name = dataSnapshot.child("Name").getValue(String.class);

                if (imageLink != null && name != null) {
                    // Update the UI with the fetched data
                    Picasso.get().load(imageLink).into(recipeImage);
                    recipeName.setText(name);
                    fetchedRecipeName = name;

                    // Set up TabLayout and ViewPager
                    TabLayout tabLayout = findViewById(R.id.tab_layout);
                    ViewPager viewPager = findViewById(R.id.view_pager);
                    final RecipePagerAdapter adapter = new RecipePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), fetchedRecipeName);
                    viewPager.setAdapter(adapter);
                    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            viewPager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                        }
                    });

                    // Check initial like status
                    checkInitialLikeStatus();
                } else {
                    Log.e(TAG, "Image link or name is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to fetch data", databaseError.toException());
            }
        });
    }

    private void fetchUserNames() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.getKey();
                    userNames.add(username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("RecipeActivity", "Error fetching user names", databaseError.toException());
            }
        });
    }

    private void sendThisRecipe() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.send_recipe, null);
        builder.setView(dialogView);

        Spinner usernameSpinner = dialogView.findViewById(R.id.usernameSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RecipeActivity.this, android.R.layout.simple_spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usernameSpinner.setAdapter(adapter);

        builder.setView(dialogView);
        builder.setTitle("Whom do you want to send?")
                .setPositiveButton("Send", (dialog, which) -> {
                    String selectedUsername = usernameSpinner.getSelectedItem().toString();
                    if (!selectedUsername.isEmpty()) {
                        sendMessageToUser(selectedUsername);
                    } else {
                        Toast.makeText(this, "Please select a user", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void sendMessageToUser(String selectedUsername) {
        String chatroomId = getChatroomId(currentUser, selectedUsername);
        // Get a reference to the chats node
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chatrooms/" + chatroomId + "/chats");

        // Create a new message object
        ChatMessage message = new ChatMessage("Test Message", currentUser, selectedUsername, System.currentTimeMillis());

        // Get a new key for the message
        String messageId = chatsRef.push().getKey();

        // Set the message data
        chatsRef.child(messageId).setValue(message);

        // Get a reference to the latest node for the receiver
        DatabaseReference latestRef = FirebaseDatabase.getInstance().getReference("latest/" + selectedUsername);

        // Set the message data
        latestRef.child(currentUser).setValue(message);
    }

    private String getChatroomId(String currentUser, String otherUser) {
        if (currentUser.hashCode() < otherUser.hashCode()) {
            return currentUser + "_" + otherUser;
        } else {
            return otherUser + "_" + currentUser;
        }
    }

    private void updateLikeStatus(boolean liked) {
        DatabaseReference likeRef = mdatabase.child("likes").child(currentUser).child(recipe.getId());
        likeRef.child("liked").setValue(liked ? 1 : 0);
    }

    private void checkInitialLikeStatus() {
        DatabaseReference likeRef = mdatabase.child("likes").child(currentUser).child(recipe.getId());
        likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isLiked = snapshot.child("liked").getValue(Integer.class) == 1;
                    likeButton.setImageResource(isLiked ? R.drawable.ic_heart_filled : R.drawable.ic_heart_unfilled);
                } else {
                    // Initialize the like status in Firebase if it doesn't exist
                    likeRef.child("liked").setValue(0);
                    isLiked = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to check initial like status", error.toException());
            }
        });
    }
}
