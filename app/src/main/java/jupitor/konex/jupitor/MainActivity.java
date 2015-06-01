package jupitor.konex.jupitor;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;

import jupitor.konex.jupitor.dataAccess.UserPreference;


public class MainActivity extends AppCompatActivity {

    private Drawer result = null;
    private AccountHeader headerResult = null;
    private static final int PROFILE_SETTING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jupitor);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        initProfile(savedInstanceState);
        Toolbar toolbar = initToolbar();
        initDrawer(savedInstanceState, toolbar);
        initMap();
    }

    private void initMap() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_map_container, MapFragment.newInstance(1))
                .commit();
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Bundle savedInstanceState, Toolbar toolbar) {
        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SwitchDrawerItem()
                                .withName("Camera sound warning")
                                .withIcon(CommunityMaterial.Icon.cmd_bell)
                                .withChecked(getSwitchDrawerItemValue("Camera sound warning"))
                                .withOnCheckedChangeListener(onCheckedChangeListener),
                        new SwitchDrawerItem()
                                .withName("Camera vibrate warning")
                                .withIcon(CommunityMaterial.Icon.cmd_vibrate)
                                .withChecked(getSwitchDrawerItemValue("Camera vibrate warning"))
                                .withOnCheckedChangeListener(onCheckedChangeListener))
                .withAnimateDrawerItems(true)
                .withSavedInstance(savedInstanceState)
                .build();
    }

    private boolean getSwitchDrawerItemValue(String itemName) {
        UserPreference pref = (UserPreference.find(UserPreference.class, "NAME = ?", itemName)).get(0);
        return Boolean.valueOf(pref.value);
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {

                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() +
                        " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    private void initProfile(Bundle savedInstanceState) {
        final IProfile myProfile = new ProfileDrawerItem()
                .withName("Yini Yin")
                .withEmail("yinnski@gmail.com")
                .withIcon(getResources()
                        .getDrawable(R.drawable.profile6));

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        myProfile,
                        new ProfileSettingDrawerItem()
                                .withName("Add Account")
                                .withDescription("Add new GitHub Account")
                                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add)
                                        .actionBarSize()
                                        .paddingDp(5)
                                        .colorRes(R.color.material_drawer_primary_text))
                                .withIdentifier(PROFILE_SETTING),

                        new ProfileSettingDrawerItem()
                                .withName("Manage Account")
                                .withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            IProfile newProfile = new ProfileDrawerItem()
                                    .withNameShown(true)
                                    .withName("Batman")
                                    .withEmail("batman@gmail.com")
                                    .withIcon(getResources()
                                            .getDrawable(R.drawable.profile5));
                            if (headerResult.getProfiles() != null) {
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.main, menu);
//            restoreActionBar();
//            return true;
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
