package com.example.runningchallenge.fragments;


import com.example.runningchallenge.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentResult extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View viewResult = inflater.inflate(R.layout.fragment_result, container, false);
    	return viewResult;
    }
}
