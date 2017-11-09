package com.regal.iotdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

        final TextView mTextView = (TextView) view.findViewById(R.id.resultview);
        final TextView mTextView2 = (TextView) view.findViewById(R.id.resultview2);

        final GraphView graph = (GraphView) view.findViewById(R.id.graph);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String url = "http://10.34.251.55:3000/api/";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mTextView.setText("Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("Response: " + error.toString());
            }
        });

        queue.add(jsObjRequest);

        String mJSONURLString = "http://10.34.251.55:3000/api/recents/temperature/15";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, mJSONURLString,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try{
                    DataPoint[] temperatureDataPoints = new DataPoint[response.length()];

                    for(int i=0; i<response.length(); i++) {

                        // Get the current json object
                        JSONObject temperatureObject = response.getJSONObject(i);

                        // Temperature records are retrieved by date descending (newest -> oldest), so we need to reverse the order before graphing over time
                        // To do this, place the nth retrieved record into place [(length - 1) - n] in the array
                        // n is 0-based, but length() is not, hence the -1 offset below
                        temperatureDataPoints[response.length() - 1 - i] = new DataPoint(response.length() - 1 - i, temperatureObject.getDouble("temperature"));
                    }

                    mTextView2.setText(temperatureDataPoints[0].toString() + "; " + temperatureDataPoints[1].toString() + "; " + temperatureDataPoints[2].toString() + "; " + temperatureDataPoints[3].toString() + "; " + temperatureDataPoints[4].toString());

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(temperatureDataPoints);
                    graph.addSeries(series);

                } catch (JSONException e) {
                    e.printStackTrace();
                    mTextView2.setText("Unable to parse temperature readings");
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                // Do something when error occurred
                mTextView2.setText("Response: " + error.toString());
            }
        });

        queue.add(jsonArrayRequest);

        return view;
    }
}