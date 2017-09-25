package com.regal.iotdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class HistoryFragment extends Fragment {
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

        View view =  inflater.inflate(R.layout.fragment_history, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 74.66),
                new DataPoint(1, 74.66),
                new DataPoint(2, 74.66),
                new DataPoint(3, 74.66),
                new DataPoint(4, 74.48),
                new DataPoint(5, 74.48),
                new DataPoint(6, 78.26),
                new DataPoint(7, 78.8),
                new DataPoint(8, 78.98),
                new DataPoint(9, 78.98),
                new DataPoint(10, 78.62),
                new DataPoint(11, 78.26),
                new DataPoint(12, 78.26),
                new DataPoint(13, 77.90),
                new DataPoint(14, 77.72),
                new DataPoint(15, 77.72),
                new DataPoint(16, 77.54),
                new DataPoint(17, 77.36),
                new DataPoint(18, 77.18),
                new DataPoint(19, 77.00),
                new DataPoint(20, 76.82),
                new DataPoint(21, 76.64),
                new DataPoint(22, 76.28),
                new DataPoint(23, 76.28),
                new DataPoint(24, 76.1),
                new DataPoint(25, 76.28),
                new DataPoint(26, 76.1),
                new DataPoint(27, 75.92),
                new DataPoint(28, 75.92),
                new DataPoint(29, 74.84),
                new DataPoint(30, 74.38),
                new DataPoint(31, 74.66),
                new DataPoint(32, 74.48),
                new DataPoint(33, 74.3),
                new DataPoint(34, 73.22),
                new DataPoint(35, 73.22),
        });
        graph.addSeries(series);

        return view;
    }
}