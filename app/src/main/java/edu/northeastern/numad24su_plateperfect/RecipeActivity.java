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
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class RecipeActivity extends AppCompatActivity {

    private Recipe recipe;
    private boolean isLiked = false;
    private List<String> userNames = new ArrayList<>();
    private String currentUser;
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        //currentUser = getIntent().getStringExtra("currentUser");
        currentUser = "Shashank";

        fetchUserNames();
        // Initialize the recipe (this should be retrieved from your database or passed via intent)
        Map<String, String> ingredients = new HashMap<>();
        ingredients.put("Rice", "2 bowls");
        ingredients.put("Onion", "2");
        ingredients.put("Tomatoes", "2");
        recipe = new Recipe("1", "Pasta", "Delicious homemade pasta", ingredients, "Step by step instructions");

        // Set up UI elements
        ImageView recipeImage = findViewById(R.id.recipe_image);
        ImageButton likeButton = findViewById(R.id.like_button);
        ImageButton shareButton = findViewById(R.id.share_button);
        ImageButton playButton = findViewById(R.id.play_button);
        TextView recipeName = findViewById(R.id.recipe_name);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        ImageButton sendButton = findViewById(R.id.send_button);

        //send to user
        sendButton.setOnClickListener(v -> {
            sendThisRecipe();
        });

        // Load image
        Picasso.get().load(recipe.getImageUrl()).into(recipeImage);

        // Set recipe name
        recipeName.setText(recipe.getName());

        // Set up like button click listener
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recipe.likeRecipe(likeButton);
                isLiked = !isLiked;
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

        // Set up TabLayout and ViewPager
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Instructions"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final RecipePagerAdapter adapter = new RecipePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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
        mdatabase = FirebaseDatabase.getInstance().getReference();
        mdatabase.child("latest/" + currentUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //new child added is new message received

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Toast.makeText(RecipeActivity.this, "new Message Recived", Toast.LENGTH_SHORT).show();
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
                Log.e("SendStickerActivity", "Error fetching user names",
                        databaseError.toException());
            }
        });
    }

    private void sendThisRecipe() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.send_recipe, null);
        builder.setView(dialogView);

        Spinner usernameSpinner = dialogView.findViewById(R.id.usernameSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RecipeActivity.this,
                android.R.layout.simple_spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usernameSpinner.setAdapter(adapter);

        builder.setView(dialogView);
        builder.setTitle("Whom do you want to send ?")
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
        String chatroomid = getChatroomId(currentUser, selectedUsername);
        // Get a reference to the chats node
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chatrooms/" + chatroomid + "/chats");

        // Create a new message object
        ChatMessage message = new ChatMessage("Test Message", currentUser, selectedUsername,
                System.currentTimeMillis());

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


}
