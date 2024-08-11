package edu.northeastern.numad24su_plateperfect;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView rvParentRecyclerView;
    ArrayList<rvParentModelClass> rvParentModelClassArrayList;
    ArrayList<rvChildModelClass> rvTrendingRecipeArrayList;
    ArrayList<rvChildModelClass> rvQuickMealsArrayList;
    ArrayList<rvChildModelClass> rvNewRecipeArrayList;
    ArrayList<rvChildModelClass> rvDinnerArrayList;
    aParentAdapterClass parentAdapterClass;
    private DatabaseReference databaseReferenceImages;
    private List<rvChildModelClass> imageDataList = new ArrayList<>();

    Map<String, ArrayList<rvChildModelClass>> categoryDictionary;
    private static final int RANDOM_RECIPE_COUNT = 10;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Retrieve arguments if any
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);;
        View searchBox = view.findViewById(R.id.search_box);

        searchBox.setOnClickListener(v -> {
            // Intent to open the new activity
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReferenceImages = FirebaseDatabase.getInstance().getReference("PlatePerfect");

        rvParentRecyclerView = view.findViewById(R.id.rv_parent);
        rvParentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        rvParentModelClassArrayList = new ArrayList<>();
        categoryDictionary = new HashMap<>();

        fillTheRecyclerView();

        parentAdapterClass = new aParentAdapterClass(rvParentModelClassArrayList, getContext());
        rvParentRecyclerView.setAdapter(parentAdapterClass);
    }

    private void fillTheRecyclerView() {
        // Initialize individual category lists
        rvDinnerArrayList = new ArrayList<>();
        rvNewRecipeArrayList = new ArrayList<>();
        rvTrendingRecipeArrayList = new ArrayList<>();
        rvQuickMealsArrayList = new ArrayList<>();

        fetchImagesData(rvNewRecipeArrayList, "New Recipe");
        fetchImagesData(rvDinnerArrayList, "Dinner Ready");
        fetchImagesData(rvTrendingRecipeArrayList, "Trending Recipe");
        fetchImagesData(rvQuickMealsArrayList, "Quick Meals");

        // Populate the parent list with categories
        rvParentModelClassArrayList.add(new rvParentModelClass("Trending Recipe", rvTrendingRecipeArrayList));
        rvParentModelClassArrayList.add(new rvParentModelClass("New Recipe", rvNewRecipeArrayList));
        rvParentModelClassArrayList.add(new rvParentModelClass("Dinner Ready", rvDinnerArrayList));
        rvParentModelClassArrayList.add(new rvParentModelClass("Quick Meals", rvQuickMealsArrayList));
    }

    private void fetchImagesData(ArrayList<rvChildModelClass> fillCategoryList, String categoryKey) {
        databaseReferenceImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    rvChildModelClass imageData = snapshot.getValue(rvChildModelClass.class);
                    imageDataList.add(imageData);
                }

                List<rvChildModelClass> randomRecipes = getRandomRecipes(imageDataList, RANDOM_RECIPE_COUNT);
                fillCategoryList.clear();
                fillCategoryList.addAll(randomRecipes);

                // Update the adapter
                parentAdapterClass.notifyDataSetChanged();

                // Log the fetched data
                for (rvChildModelClass imageData : fillCategoryList) {
                    Log.d("ImagesFragment", "Category: " + categoryKey + " Name: " + imageData.getName() + ", Image Link: " + imageData.getImage_Link());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ImagesFragment", "Failed to read data", databaseError.toException());
            }
        });
    }

    private List<rvChildModelClass> getRandomRecipes(List<rvChildModelClass> sourceList, int count) {
        List<rvChildModelClass> shuffledList = new ArrayList<>(sourceList);
        Collections.shuffle(shuffledList);
        return shuffledList.subList(0, Math.min(count, shuffledList.size()));
    }
}
