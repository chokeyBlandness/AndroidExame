package cqk.usst.androidexame;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import cqk.usst.androidexame.services.DownloadService;


public class VolleyFragment extends Fragment implements View.OnClickListener{
    private String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
    private Button getButton;
    private Button postButton;
    private Button jsonButton;
    private TextView resultTextView;
    private RequestQueue requestQueue;

    public VolleyFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volley, null);
        getButton = view.findViewById(R.id.get_button);
        getButton.setOnClickListener(this);
        postButton = view.findViewById(R.id.post_button);
        postButton.setOnClickListener(this);
        jsonButton = view.findViewById(R.id.json_button);
        jsonButton.setOnClickListener(this);
        resultTextView = view.findViewById(R.id.result_textView);

        requestQueue = Volley.newRequestQueue(getActivity());
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_button:
                get();
                break;
            case R.id.post_button:
                post();
                break;
            case R.id.json_button:
                json();
                break;
            default:
                break;
        }
    }

    void get() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            resultTextView.setText(response);
                    }
                }, new ErrorResponseListener(resultTextView)
        );
        requestQueue.add(stringRequest);
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setAction(DownloadService.ACTION_UPDATE);
        intent.putExtra("finished", 0);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        getActivity(),
                        0,
                        new Intent(getActivity(),MainActivity.class),
                        0);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(getActivity())
                    .setTicker("you have a new message")
                    .setContentTitle("title")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText("request url by get")
                    .setContentIntent(pendingIntent)
                    .setNumber(1)
                    .build();
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1,notification);
    }

    void post() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resultTextView.setText(response);
                    }
                },new ErrorResponseListener(resultTextView));
        requestQueue.add(stringRequest);
    }

    void json(){
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST,
                        url,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            resultTextView.setText(response.getJSONArray("trailers").get(0).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new ErrorResponseListener(resultTextView));
        requestQueue.add(jsonObjectRequest);
    }

}
