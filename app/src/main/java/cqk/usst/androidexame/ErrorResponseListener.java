package cqk.usst.androidexame;

import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by 10033 on 2018/4/10.
 */

public class ErrorResponseListener implements Response.ErrorListener {
    private TextView volley_result;

    public ErrorResponseListener(TextView volley_result) {
        this.volley_result = volley_result;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        volley_result.setText("error load:"+error);
    }
}
