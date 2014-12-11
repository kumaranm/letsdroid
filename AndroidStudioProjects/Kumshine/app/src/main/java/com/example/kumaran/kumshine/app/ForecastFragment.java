package com.example.kumaran.kumshine.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForecastFragment extends Fragment {

        public ForecastFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            String[] forecastArray ={
              "Today - Sunny - 86/40",
                    "Tomorrow - Cloudy - 90/45",
                    "Wed - Rain - 45/34",
                    "Thurs - Windy - 34/34",
                    "Friday - Sunny - 34/23",
                    "Saturday - Haze - 34/23",
                    "Sunday - Cloudy - 34/3"
             };
            List<String> weeklist = new ArrayList<String>(Arrays.asList(forecastArray));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview
            ,weeklist);

            ListView view = (ListView) rootView.findViewById(R.id.listview_forecast);
            view.setAdapter(adapter);

            return rootView;
        }
    }