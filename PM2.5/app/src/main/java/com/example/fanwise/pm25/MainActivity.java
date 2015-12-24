package com.example.fanwise.pm25;

import com.example.fanwise.pm25.domain.PM25;
import com.example.fanwise.pm25.service.AirServiceClient;
import com.example.library.PullToRefreshView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Fanwise on 2015/12/22.
 */
public class MainActivity extends AppCompatActivity {

    public static final int REFRESH_DELAY = 2000;

    public static final String KEY_POSITION = "position";
    public static final String KEY_QUALITY = "quality";
    public static final String KEY_AQI = "aqi";
    public static final String KEY_PM25 = "pm25";

    protected List<Map<String, String>> mPMList;
    private PullToRefreshView mPullToRefreshView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        
        AirServiceClient.getInstance().requestPM25(new Callback<List<PM25>>() {
            @Override
            public void onResponse(Response<List<PM25>> response, Retrofit retrofit) {

                Map<String, String> map;
                mPMList = new ArrayList<>();

                for (int i = 0; i < response.body().size() - 1; i++) {
                    map = new HashMap<>();
                    map.put(KEY_POSITION, response.body().get(i).getPositionName());
                    map.put(KEY_AQI,response.body().get(i).getAqi());
                    map.put(KEY_QUALITY,response.body().get(i).getQuality());
                    map.put(KEY_PM25,response.body().get(i).getPm25());
                    mPMList.add(map);
                }

                mPullToRefreshView.setRefreshing(false);

                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                recyclerView.setAdapter(new PMAdapter());
            }

            @Override
            public void onFailure(Throwable t) {
                Map<String, String> map;
                mPMList = new ArrayList<>();

                map = new HashMap<>();
                map.put(KEY_POSITION,"onFailure");
                mPMList.add(map);


                mPullToRefreshView.setRefreshing(false);

                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                recyclerView.setAdapter(new PMAdapter());
            }
        });

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setRefreshing(true);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AirServiceClient.getInstance().requestPM25(new Callback<List<PM25>>() {
                            @Override
                            public void onResponse(Response<List<PM25>> response, Retrofit retrofit) {

                                Map<String, String> map;
                                mPMList = new ArrayList<>();

                                for (int i = 0; i < response.body().size() - 1; i++) {
                                    map = new HashMap<>();
                                    map.put(KEY_POSITION, response.body().get(i).getPositionName());
                                    map.put(KEY_AQI,response.body().get(i).getAqi());
                                    map.put(KEY_QUALITY,response.body().get(i).getQuality());
                                    map.put(KEY_PM25,response.body().get(i).getPm25());
                                    mPMList.add(map);
                                }

                                mPullToRefreshView.setRefreshing(false);

                                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                                recyclerView.setAdapter(new PMAdapter());
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Map<String, String> map;
                                mPMList = new ArrayList<>();

                                map = new HashMap<>();
                                map.put(KEY_POSITION,"onFailure");
                                mPMList.add(map);


                                mPullToRefreshView.setRefreshing(false);

                                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                                recyclerView.setAdapter(new PMAdapter());
                            }
                        });
                    }
                }, REFRESH_DELAY);
            }
        });


    }

    private class PMAdapter extends RecyclerView.Adapter<PMHolder> {

        @Override
        public PMHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);
            return new PMHolder(view);
        }

        @Override
        public void onBindViewHolder(PMHolder holder, int pos) {
            Map<String, String> data = mPMList.get(pos);
            holder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return mPMList.size();
        }
    }

    private class PMHolder extends RecyclerView.ViewHolder {

        private View mRootView;
        private TextView TextViewPosition;
        private TextView TextViewAqi;
        private TextView TextViewQuality;
        private TextView TextViewPM25;

        private Map<String, String> mData;

        public PMHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            TextViewPosition = (TextView) itemView.findViewById(R.id.tv_position);
            TextViewAqi = (TextView) itemView.findViewById(R.id.tv_aqi);
            TextViewQuality = (TextView) itemView.findViewById(R.id.tv_quality);
            TextViewPM25 = (TextView) itemView.findViewById(R.id.tv_pm25);
        }

        public void bindData(Map<String, String> data) {
            mData = data;
            if(mData.get(KEY_POSITION) != "onFailure") {
                TextViewPosition.setText(mData.get(KEY_POSITION));
                TextViewAqi.setText("空气指数：" + mData.get(KEY_AQI));
                TextViewQuality.setText("质量状况：" + mData.get(KEY_QUALITY));
                TextViewPM25.setText("PM2.5浓度：" + mData.get(KEY_PM25) + "μg/m³");
                int nAqi = Integer.parseInt(mData.get(KEY_AQI));

                if (nAqi < 51)
                    mRootView.setBackgroundResource(R.color.excellent);
                else if (nAqi < 101)
                    mRootView.setBackgroundResource(R.color.good);
                else if (nAqi < 151)
                    mRootView.setBackgroundResource(R.color.pollute_lightly);
                else if (nAqi < 201)
                    mRootView.setBackgroundResource(R.color.pollute_moderately);
                else if (nAqi < 301)
                    mRootView.setBackgroundResource(R.color.pollute_heavily);
                else
                    mRootView.setBackgroundResource(R.color.pollute_severely);
            }else {
                TextViewPosition.setText("获取数据失败");
            }
        }
    }
}
