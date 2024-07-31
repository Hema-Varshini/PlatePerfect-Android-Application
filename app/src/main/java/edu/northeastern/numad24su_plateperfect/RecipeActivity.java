package edu.northeastern.numad24su_plateperfect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

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

        // Load image
        Picasso.get().load(recipe.getImageUrl()).into(recipeImage);

        // Set recipe name
        recipeName.setText(recipe.getName());

        // Set up like button click listener
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.likeRecipe(likeButton);
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
    }
}
