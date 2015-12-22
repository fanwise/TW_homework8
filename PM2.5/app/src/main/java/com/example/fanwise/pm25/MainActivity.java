package com.example.fanwise.pm25;

import com.example.fanwise.pm25.domain.PM25;
import com.example.fanwise.pm25.service.AirServiceClient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Fanwise on 2015/12/22.
 */
public class MainActivity extends Activity{
    private TextView pm25TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm25TextView = (TextView) findViewById(R.id.text_view_pm25);

        AirServiceClient.getInstance().requestPM25(new Callback<List<PM25>>() {
            @Override
            public void onResponse(Response<List<PM25>> response, Retrofit retrofit) {
                showSuccessScreen(response);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void showSuccessScreen(Response<List<PM25>> response) {
        if (response != null) {
            populate(response.body());
        }
    }

    private void populate(List<PM25> data) {
        if (data != null && !data.isEmpty()) {
            PM25 pm25;// = data.get(0);
            String curPMState = "";
            for (int i = 0; i <data.size(); i++) {
                pm25 = data.get(i);
                curPMState = curPMState + pm25.getPositionName() + " 空气指数：" + pm25.getAqi()
                                                                 + " 质量状况：" + pm25.getQuality()
                                                                 + " PM2.5浓度：" + pm25.getPm25()
                                                                 + "μg/m³" + "\n";
            }

            pm25TextView.setText(curPMState);

        }
    }
}
