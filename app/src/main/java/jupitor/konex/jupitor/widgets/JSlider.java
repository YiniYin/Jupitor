package jupitor.konex.jupitor.widgets;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.views.Slider;

import jupitor.konex.jupitor.R;

public class JSlider extends Slider {
    private String description;
    private OnValueChangedListener onValueChangedListener;

    public JSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSlider withDescription(String description) {
        this.description = description;
        return this;
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    public JSlider withOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
        return this;
    }

    private int getLayout() {
        return R.layout.jslider;
    }

    public void setView(LayoutInflater inflater, View containerView) {

    }

    private static  class ViewHolder {
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView description;
        private Slider slider;

        private ViewHolder(View view) {
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.name = (TextView) view.findViewById(R.id.name);
            this.description = (TextView) view.findViewById(R.id.description);
            this.slider = (Slider) view.findViewById(R.id.slider);
        }
    }
}
