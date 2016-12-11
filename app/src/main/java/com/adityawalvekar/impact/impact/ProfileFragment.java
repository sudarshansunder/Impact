package com.adityawalvekar.impact.impact;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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

        Description description = new Description();
        description.setText(" ");
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(95f, "Impact Created"));
        entries.add(new PieEntry(5f, "Missed"));
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{Color.parseColor("#90CAF9"), Color.parseColor("#FF5722")});
        dataSet.setValueTextColor(Color.parseColor("#000000"));
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setDescription(description);
        pieChart.invalidate();

        LineChart lineChart = (LineChart) rootView.findViewById(R.id.contributionLineChart);
        List<Entry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, 30f));
        barEntries.add(new BarEntry(2f, 40f));
        barEntries.add(new BarEntry(3f, 10f));
        barEntries.add(new BarEntry(4f, 50f));
        LineDataSet setComp1 = new LineDataSet(barEntries, "Impact created over 4 weeks");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.setDescription(description);
        lineChart.invalidate();


        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.oldAgeHomeImages);
        ImageView imageView = new ImageView(getActivity());
        ImageViewBitmapLoader imageViewBitmapLoader = new ImageViewBitmapLoader(getContext());
        imageViewBitmapLoader.loadBitmap(R.drawable.oldagehome1, imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        linearLayout.addView(imageView);
        ImageView imageView2 = new ImageView(getActivity());
        imageViewBitmapLoader.loadBitmap(R.drawable.oldagehome2, imageView2);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        linearLayout.addView(imageView2);
        return rootView;
    }
}
