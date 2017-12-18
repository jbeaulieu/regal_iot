package com.regal.iotdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        GridLayout gridLayout1 = (GridLayout) view.findViewById(R.id.grid_layout_1);
        GridLayout gridLayout2 = (GridLayout) view.findViewById(R.id.grid_layout_2);
        GridLayout gridLayout3 = (GridLayout) view.findViewById(R.id.grid_layout_3);
        GridLayout gridLayout4 = (GridLayout) view.findViewById(R.id.grid_layout_4);

        gridLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                HistoryFragment historyFragment = new HistoryFragment();
                fragmentManager.beginTransaction().replace(R.id.defaultLayout, historyFragment, historyFragment.getTag()).addToBackStack(null).commit();
            }
        });

        gridLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                HistoryFragment historyFragment = new HistoryFragment();
                fragmentManager.beginTransaction().replace(R.id.defaultLayout, historyFragment, historyFragment.getTag()).addToBackStack(null).commit();
            }
        });

        gridLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                HistoryFragment historyFragment = new HistoryFragment();
                fragmentManager.beginTransaction().replace(R.id.defaultLayout, historyFragment, historyFragment.getTag()).addToBackStack(null).commit();
            }
        });

        gridLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                HistoryFragment historyFragment = new HistoryFragment();
                fragmentManager.beginTransaction().replace(R.id.defaultLayout, historyFragment, historyFragment.getTag()).addToBackStack(null).commit();
            }
        });

        return view;
    }
}
