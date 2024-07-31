package edu.northeastern.numad24su_plateperfect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IngredientsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        TextView ingredientsTextView = view.findViewById(R.id.ingredients_text);
        // Set ingredients text
        ingredientsTextView.setText("Rice - 2 bowls\nOnion - 2\nTomatoes - 2");
        return view;
    }
}
