package jupitor.konex.jupitor.utils.ui;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mikepenz.materialdrawer.model.BaseDrawerItem;
import com.mikepenz.materialdrawer.util.PressedEffectStateListDrawable;
import com.mikepenz.materialdrawer.util.UIUtils;

import jupitor.konex.jupitor.R;

public class SeekBarDrawerItem extends BaseDrawerItem<SeekBarDrawerItem> {
    private String description;
    private boolean seekBarEnabled = true;
    private int descriptionRes = -1;
    private int progressValue = 0;
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = null;


    public SeekBarDrawerItem withDescription(String description) {
        this.description = description;
        return this;
    }

    public SeekBarDrawerItem withDescription(int descriptionRes) {
        this.descriptionRes = descriptionRes;
        return this;
    }

    public SeekBarDrawerItem withProgressValue(int progressValue) {
        this.progressValue = progressValue;
        return this;
    }

    public SeekBarDrawerItem withSwitchEnabled(boolean seekBarEnabled) {
        this.seekBarEnabled = seekBarEnabled;
        return this;
    }

    public SeekBarDrawerItem withOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDescriptionRes() {
        return descriptionRes;
    }

    public void setDescriptionRes(int descriptionRes) {
        this.descriptionRes = descriptionRes;
    }

    public int getProgressValue() {
        return this.progressValue;
    }

    @Override
    public String getType() {
        return "SEEK_BAR_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_seek_bar;
    }

    @Override
    public View convertView(LayoutInflater layoutInflater, View convertView, ViewGroup parent) {
        Context ctx = parent.getContext();

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(getLayoutRes(), parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int selectedColor = UIUtils.decideColor(ctx, getSelectedColor(), getSelectedColorRes(),
                com.mikepenz.materialdrawer.R.attr.material_drawer_selected,
                com.mikepenz.materialdrawer.R.color.material_drawer_selected);


        int color;
        if (this.isEnabled()) {
            color = UIUtils.decideColor(ctx, getTextColor(), getTextColorRes(),
                    com.mikepenz.materialdrawer.R.attr.material_drawer_primary_text,
                    com.mikepenz.materialdrawer.R.color.material_drawer_primary_text);
        } else {
            color = UIUtils.decideColor(ctx, getDisabledTextColor(), getDisabledTextColorRes(),
                    com.mikepenz.materialdrawer.R.attr.material_drawer_hint_text,
                    com.mikepenz.materialdrawer.R.color.material_drawer_hint_text);
        }
        int selectedTextColor = UIUtils.decideColor(ctx, getSelectedTextColor(), getSelectedTextColorRes(),
                com.mikepenz.materialdrawer.R.attr.material_drawer_selected_text,
                com.mikepenz.materialdrawer.R.color.material_drawer_selected_text);
        //get the correct color for the icon
        int iconColor;
        if (this.isEnabled()) {
            iconColor = UIUtils.decideColor(ctx, getIconColor(), getIconColorRes(),
                    com.mikepenz.materialdrawer.R.attr.material_drawer_primary_icon,
                    com.mikepenz.materialdrawer.R.color.material_drawer_primary_icon);
        } else {
            iconColor = UIUtils.decideColor(ctx, getDisabledIconColor(), getDisabledIconColorRes(),
                    com.mikepenz.materialdrawer.R.attr.material_drawer_hint_text,
                    com.mikepenz.materialdrawer.R.color.material_drawer_hint_text);
        }
        int selectedIconColor = UIUtils.decideColor(ctx, getSelectedIconColor(), getSelectedIconColorRes(),
                com.mikepenz.materialdrawer.R.attr.material_drawer_selected_text,
                com.mikepenz.materialdrawer.R.color.material_drawer_selected_text);

        //set the background for the item
        UIUtils.setBackground(viewHolder.view, UIUtils.getDrawerItemBackground(selectedColor));

        //set the text for the name
        if (this.getNameRes() != -1) {
            viewHolder.name.setText(this.getNameRes());
        } else {
            viewHolder.name.setText(this.getName());
        }

        //set the text for the description or hide
        viewHolder.description.setVisibility(View.VISIBLE);
        if (this.getDescriptionRes() != -1) {
            viewHolder.description.setText(this.getDescriptionRes());
        } else if (this.getDescription() != null) {
            viewHolder.description.setText(this.getDescription());
        } else {
            viewHolder.description.setVisibility(View.GONE);
        }

        viewHolder.seekBarView.setOnSeekBarChangeListener(seekBarChangeListener);
        viewHolder.seekBarView.setEnabled(seekBarEnabled);

        //set the colors for textViews
        viewHolder.name.setTextColor(UIUtils.getTextColorStateList(color, selectedTextColor));
        viewHolder.description.setTextColor(UIUtils.getTextColorStateList(color, selectedTextColor));

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
            viewHolder.description.setTypeface(getTypeface());
        }

        //get the drawables for our icon
        Drawable icon = UIUtils.decideIcon(ctx, getIcon(), getIIcon(), getIconRes(), iconColor, isIconTinted());
        Drawable selectedIcon = UIUtils.decideIcon(ctx, getSelectedIcon(), getIIcon(), getSelectedIconRes(),
                selectedIconColor, isIconTinted());

        //if we have an icon then we want to set it
        if (icon != null) {
            //if we got a different color for the selectedIcon we need a StateList
            if (selectedIcon != null) {
                viewHolder.icon.setImageDrawable(UIUtils.getIconStateList(icon, selectedIcon));
            } else if (isIconTinted()) {
                viewHolder.icon.setImageDrawable(new PressedEffectStateListDrawable(icon, iconColor, selectedIconColor));
            } else {
                viewHolder.icon.setImageDrawable(icon);
            }
            //make sure we display the icon
            viewHolder.icon.setVisibility(View.VISIBLE);
        } else {
            //hide the icon
            viewHolder.icon.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView description;
        private SeekBar seekBarView;

        private ViewHolder(View view) {
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.name = (TextView) view.findViewById(R.id.name);
            this.description = (TextView) view.findViewById(R.id.description);
            this.seekBarView = (SeekBar) view.findViewById(R.id.seekBarView);
        }
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progressVal, boolean fromUser) {
            progressValue = progressVal;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            description = progressValue + " meters";
        }
    };
}
