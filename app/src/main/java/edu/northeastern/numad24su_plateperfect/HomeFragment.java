package edu.northeastern.numad24su_plateperfect;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView rvParentRecyclerView;
    ArrayList<rvParentModelClass> rvParentModelClassArrayList;
    ArrayList<rvChildModelClass> rvHighProteinArrayList;
    ArrayList<rvChildModelClass> rvQuickMealsArrayList;
    ArrayList<rvChildModelClass> rvGormetArrayList;
    ArrayList<rvChildModelClass> rvDinnerArrayList;
    aParentAdapterClass parentAdapterClass;

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
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillTheRecyclerView();
        rvParentRecyclerView = view.findViewById(R.id.rv_parent);
        rvParentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        parentAdapterClass= new aParentAdapterClass(rvParentModelClassArrayList, getContext());
        rvParentRecyclerView.setAdapter(parentAdapterClass);
        parentAdapterClass.notifyDataSetChanged();



    }

    private void fillTheRecyclerView() {
        //Individual Categories go here
        rvDinnerArrayList = new ArrayList<>();
        rvGormetArrayList = new ArrayList<>();
        rvHighProteinArrayList = new ArrayList<>();
        rvQuickMealsArrayList = new ArrayList<>();
        //Parent to be filled goes here
        rvParentModelClassArrayList = new ArrayList<>();
        //ToDO fetch from firebase
    }
}