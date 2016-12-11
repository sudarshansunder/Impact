package com.adityawalvekar.impact.impact;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private Toolbar toolbar;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        PieChart pieChart = (PieChart) rootView.findViewById(R.id.regularityPieChart);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(95f, "Impact Created"));
        entries.add(new PieEntry(5f, "Missed"));
        PieDataSet dataSet = new PieDataSet(entries, "Regularity");
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

        BarChart barChart = (BarChart) rootView.findViewById(R.id.contributionBarChart);
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, 30f));
        barEntries.add(new BarEntry(2f, 40f));
        barEntries.add(new BarEntry(3f, 10f));
        barEntries.add(new BarEntry(4f, 50f));
        BarDataSet barDataSet = new BarDataSet(barEntries, "BarDataSet");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.oldAgeHomeImages);
        ImageView imageView = new ImageView(getActivity());
        ImageViewBitmapLoader imageViewBitmapLoader = new ImageViewBitmapLoader(getContext());
        imageViewBitmapLoader.loadBitmap(R.drawable.oldagehome1, imageView);
        //imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.oldagehome1));
        //imageView.setScaleType(ImageView.ScaleType.CENTER);
        linearLayout.addView(imageView);
        //ImageView imageView1 = new ImageView(getActivity());
        //imageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.oldagehome3));
        //imageView1.setScaleType(ImageView.ScaleType.CENTER);
        //linearLayout.addView(imageView1);
        ImageView imageView2 = new ImageView(getActivity());
        imageViewBitmapLoader.loadBitmap(R.drawable.oldagehome2, imageView2);
        //imageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.oldagehome2));
        //imageView2.setScaleType(ImageView.ScaleType.CENTER);
        linearLayout.addView(imageView2);
        ImageView keepContributingImage = (ImageView) rootView.findViewById(R.id.keepContributingImage);

        //ImageViewBitmapLoader imageViewBitmapLoader = new ImageViewBitmapLoader(getContext());
        imageViewBitmapLoader.loadBitmap(R.drawable.charity11, keepContributingImage);
        return rootView;
    }
}
