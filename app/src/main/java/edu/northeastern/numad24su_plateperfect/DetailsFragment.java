package edu.northeastern.numad24su_plateperfect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        TextView detailsTextView = view.findViewById(R.id.details_text);
        // Set details text
        detailsTextView.setText("Pasta originated from Italy and is a staple food of Italian cuisine.");
        return view;
    }
}
