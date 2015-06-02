package jupitor.konex.jupitor.utils.ui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mikepenz.materialdrawer.model.BaseDrawerItem;

public class SeekBarDrawerItem extends BaseDrawerItem<SeekBarDrawerItem> {
    private SeekBar seekBar;
    private TextView textView;

    @Override
    public String getType() {
        return null;
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public View convertView(LayoutInflater layoutInflater, View view, ViewGroup viewGroup) {
        return null;
    }
}
