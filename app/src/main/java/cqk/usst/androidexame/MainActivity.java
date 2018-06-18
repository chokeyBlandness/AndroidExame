package cqk.usst.androidexame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup mainRadioGroup;
    private RadioButton downloadRadioButton;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainRadioGroup = findViewById(R.id.main_radioGroup);
        downloadRadioButton = findViewById(R.id.download_radioButton);
        fragmentManager = getSupportFragmentManager();
        mainRadioGroup.setOnCheckedChangeListener(this);
        downloadRadioButton.setChecked(true);
        changFragment(new DownloadFragment(),false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.download_radioButton:
                changFragment(new DownloadFragment(),true);
                break;
            case R.id.volley_radioButton:
                changFragment(new VolleyFragment(), true);
                break;
            case R.id.json_radioButton:
                changFragment(new JsonFragment(), true);
                break;
            default:
                break;
        }
    }

    public void changFragment(Fragment fragment,boolean isFirst) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment);
        if (!isFirst) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
