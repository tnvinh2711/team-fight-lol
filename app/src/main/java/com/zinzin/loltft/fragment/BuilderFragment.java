package com.zinzin.loltft.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.zinzin.loltft.R;

public class BuilderFragment extends Fragment {

    public static BuilderFragment newInstance() {
        return new BuilderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_builder, container, false);
        return view;
    }
}
