package cqk.usst.androidexame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import cqk.usst.androidexame.entities.FileInfo;
import cqk.usst.androidexame.services.DownloadService;


public class DownloadFragment extends Fragment {

    private Button downloadButton;
    private Button pauseButton;
    private ProgressBar fileProgressBar;
    public DownloadFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, null);
        downloadButton = view.findViewById(R.id.down_button);
        pauseButton = view.findViewById(R.id.pause_button);
        fileProgressBar = view.findViewById(R.id.file_progressBar);
        fileProgressBar.setMax(100);

        final FileInfo fileInfo = new FileInfo(0,"big_buck_bunny.mp4",
                "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
                0,0);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"You Click DownLoad Button,Begin DownLoad!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo", fileInfo);
                getActivity().startService(intent);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"You Click Pause Button,Stop DownLoad!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo", fileInfo);
                getActivity().startService(intent);
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_UPDATE);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        return view;
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished", 0);
                fileProgressBar.setProgress(finished);
            }
        }
    };
}
