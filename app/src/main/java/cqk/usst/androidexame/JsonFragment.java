package cqk.usst.androidexame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cqk.usst.androidexame.entities.FileInfo;


public class JsonFragment extends Fragment {
    private TextView showObjectTextView;
    private TextView showJsonTextView;

    public JsonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_json, null);
        showObjectTextView = view.findViewById(R.id.show_object_textView);
        showJsonTextView = view.findViewById(R.id.show_json_textView);

        FileInfo fileInfo = new FileInfo(
                1,
                "fileName",
                "fileUrl",
                100,
                50
        );
        showObjectTextView.setText(fileInfo.toString());
        showJsonTextView.setText(JSON.toJSONString(fileInfo));

        return view;
    }

}
