package jupitor.konex.jupitor;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.Slider;


public class SettingsFragment extends Fragment {
    private Slider distanceWarningSlider;

    public static SettingsFragment newInstance(int sectionNumber) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(consts.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        distanceWarningSlider = (Slider) view.findViewById(R.id.distanceWarningSlider);
        distanceWarningSlider.setOnValueChangedListener(onValueChangedListener);
    }

    private Slider.OnValueChangedListener onValueChangedListener = new Slider.OnValueChangedListener() {
        @Override
        public void onValueChanged(int newValue) {
            Log.i("Jupitor", "distanceWarningSlider: " + newValue);
        }
    };
}
