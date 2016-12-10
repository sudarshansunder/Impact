package com.adityawalvekar.impact.impact;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        PieChart pieChart = (PieChart) rootView.findViewById(R.id.regularityPieChart);
        ArrayList<PieEntry> entries =  new ArrayList<>();
        entries.add(new PieEntry(95f,"Impact Created"));
        entries.add(new PieEntry(5f,"Missed"));
        PieDataSet dataSet = new PieDataSet(entries,"Regularity");
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

        BarChart barChart = (BarChart) rootView.findViewById(R.id.contributionBarChart);
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f,30f));
        barEntries.add(new BarEntry(2f,40f));
        barEntries.add(new BarEntry(3f,10f));
        barEntries.add(new BarEntry(4f,50f));
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
        imageViewBitmapLoader.loadBitmap(R.drawable.charity11,keepContributingImage);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
