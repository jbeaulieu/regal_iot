package com.regal.iotdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.regal.iotdemo.SeekArc;
import com.regal.iotdemo.SeekArc.OnSeekArcChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class StatusFragment extends Fragment {

    private SeekArc mSeekArc;
    private TextView mSeekArcProgress;
    private LinearLayout mRPMButtonLayout;
    private String initialRPM;
    private Button SetRPMButton;
    private Button ResetRPMButton;

    public static StatusFragment newInstance() {
        StatusFragment fragment = new StatusFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_status, container, false);

        mSeekArc = (SeekArc) view.findViewById(R.id.seekArc);
        mSeekArcProgress = (TextView) view.findViewById(R.id.rpmView);
        mRPMButtonLayout = (LinearLayout) view.findViewById(R.id.setRPMButtonLayout);
        //initialRPM = view.findViewById(R.id.setRPMButtonLayout).toString();
        SetRPMButton = (Button) view.findViewById(R.id.setButton);
        ResetRPMButton = (Button) view.findViewById(R.id.resetButton);

        mSeekArc.setOnSeekArcChangeListener(new OnSeekArcChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onProgressChanged(SeekArc seekArc, int progress,
                                          boolean fromUser) {
                mSeekArcProgress.setText(String.valueOf(progress));
                mRPMButtonLayout.setVisibility(View.VISIBLE);

            }
        });

        SetRPMButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Motor RPM Set",
                        Toast.LENGTH_LONG).show();
                mRPMButtonLayout.setVisibility(View.INVISIBLE);
            }
        });

        ResetRPMButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Motor RPM reset to default",
                        Toast.LENGTH_LONG).show();
                mRPMButtonLayout.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }
}