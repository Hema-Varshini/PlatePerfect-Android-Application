package edu.northeastern.numad24su_plateperfect;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RecipePagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private String recipeName;

    public RecipePagerAdapter(@NonNull FragmentManager fm, int numOfTabs, String recipeName) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
        this.recipeName = recipeName;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DetailsFragment(); // Update this to pass recipe details if needed
            case 1:
                return IngredientsFragment.newInstance(recipeName);
            case 2:
                return InstructionsFragment.newInstance(recipeName);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
