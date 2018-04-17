package com.walkPark.walkinthepark.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.walkPark.walkinthepark.R;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.roger.catloadinglibrary.CatLoadingView;
import com.walkPark.walkinthepark.Prefs;
import com.walkPark.walkinthepark.backend.ProfileInterface;
import com.walkPark.walkinthepark.backend.WalkInTheParkRetrofit;
import com.walkPark.walkinthepark.models.Profile;
import com.walkPark.walkinthepark.models.ProfileResponse;
import com.walkPark.walkinthepark.models.Route;
import com.walkPark.walkinthepark.models.WeeklySteps;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @BindView(R.id.image) ImageView profile;
    @BindView(R.id.barChart) BarChart mChart;
    @BindView(R.id.textMonth) TextView textMonth;
    @BindView(R.id.textCurrentSteps) TextView textCurrentSteps;
    @BindView(R.id.textExpectedSteps) TextView textExpectedSteps;
    @BindView(R.id.textCalories) TextView textCalories;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.height) TextView height;
    private final String TAG = getClass().getName();
    @BindView(R.id.weight) TextView weight;

    CatLoadingView mView;

    private Profile player;
    private List<Route> completedRouteList;
    private List<WeeklySteps> weeklyStepsList;
    private Unbinder unbinder;

    public static ProfileFragment newInstance(Bundle args) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, root);

        mView = new CatLoadingView();
        mView.show(getChildFragmentManager(), "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 1000);

        return root;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void initData() {
        final ProfileInterface profileInterface = WalkInTheParkRetrofit
                .getInstance()
                .create(ProfileInterface.class);

        Call<ProfileResponse> call = profileInterface.getProfile(Integer.parseInt(Prefs.getUserProfile().getPlayer_id()));
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    player = response.body().getPlayer();
                    if(response.body().getRoutes()!=null) {
                        completedRouteList = new ArrayList<>();
                        completedRouteList.addAll(response.body().getRoutes());
                    }
                    if(response.body().getPlayer().getWeekly_steps()!=null) {
                        weeklyStepsList = new ArrayList<>();
                        weeklyStepsList.addAll(response.body().getPlayer().getWeekly_steps());
                    }
                    initUI();
                } else {
                    Toast.makeText(getContext(), "Error loading",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading API",
                        Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void initUI() {
        name.setText(player.getName());
        Glide.with(this)
                .load(Prefs.getSmallThumbnail())
                .asBitmap()
                .into(profile);

        if(player.getTotal_calories()!=null) {
            textCalories.setText(Integer.toString(player.getTotal_calories()));
        } else {
            textCalories.setText("0");
        }
        if(player.getHeight()!=null) {
            height.setText(Double.toString(player.getHeight()));
        } else {
            height.setText("NA");
        }
        if(player.getHeight()!=null) {
            weight.setText(Double.toString(player.getWeight()));
        } else {
            weight.setText("NA");
        }

        SimpleDateFormat df = new SimpleDateFormat("MMMM");
        textMonth.setText(df.format(new Date()));

        if(player.getMonthly_steps_lefts()!= null) {
            textExpectedSteps.setText(Integer.toString(player.getMonthly_steps_lefts()));
        } else {
            textExpectedSteps.setText("0");
        }

        if(player.getCurrent_month_total_steps()!= null) {
            textCurrentSteps.setText(Integer.toString(player.getCurrent_month_total_steps()));
        } else {
            textCurrentSteps.setText("0");
        }

        initBarChart();

        mView.dismiss();
    }

    private void initBarChart(){
        mChart.setBackgroundColor(Color.WHITE); //Color.parseColor("#732C7B")
        mChart.setExtraTopOffset(-10f);
        mChart.setExtraBottomOffset(10f);
        mChart.setExtraLeftOffset(30f);
        mChart.setExtraRightOffset(30f);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(7);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        YAxis left = mChart.getAxisLeft();
        left.setDrawLabels(false);
        left.setSpaceTop(15f);
        left.setSpaceBottom(15f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        left.setTextColor(Color.LTGRAY);
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        mChart.getAxisLeft().setXOffset(16f);
        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT
        final List<Data> data = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("d-MMM");
        SimpleDateFormat apiDF = new SimpleDateFormat("yyyy-MM-dd");

        weeklyStepsList.size();
        for(int i = 0; i < weeklyStepsList.size(); i++) {
            String date = "";
            try{
                date = df.format(apiDF.parse(weeklyStepsList.get(i).getDate()));
            } catch (ParseException e) {
            }
            data.add(new Data(i, weeklyStepsList.get(i).getSteps(), date));
        }

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return data.get(Math.min(Math.max((int) value, 0), data.size() -1 )).xAxisValue;
            }
        });

        setData(data);
    }

    private void setData(List<Data> dataList) {

        ArrayList<BarEntry> values = new ArrayList<BarEntry>();
        List<Integer> colors = new ArrayList<Integer>();

        int purple = Color.rgb(115, 44, 123);

        for (int i = 0; i < dataList.size(); i++) {
            Data d = dataList.get(i);
            BarEntry entry = new BarEntry(d.xValue, d.yValue);
            values.add(entry);
            colors.add(purple);
        }

        BarDataSet set;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(values, "Values");
            set.setColors(colors);
            set.setValueTextColors(colors);

            BarData data = new BarData(set);
            data.setValueTextSize(13f);
            data.setBarWidth(0.8f);
            data.setValueFormatter(new ValueFormatter());
            mChart.setData(data);
            mChart.invalidate();
        }
    }

    private class Data {

        public String xAxisValue;
        public int yValue;
        public int xValue;

        public Data(int xValue, int yValue, String xAxisValue) {
            this.xAxisValue = xAxisValue;
            this.yValue = yValue;
            this.xValue = xValue;
        }
    }

    private class ValueFormatter implements IValueFormatter
    {

        private DecimalFormat mFormat;

        public ValueFormatter() {
            mFormat = new DecimalFormat("######");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }
}
