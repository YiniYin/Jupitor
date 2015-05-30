package jupitor.konex.jupitor;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.List;


public class MapFragment extends Fragment implements SensorEventListener, GoogleApiClient.ConnectionCallbacks
        ,GoogleApiClient.OnConnectionFailedListener,LocationListener,OnMapReadyCallback {
    private final String TAG = "JupiterMap";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static boolean magicCameraOn = false;
    private static boolean searchRadiusOn = false;
    private static LatLng mLatLng;
    private static final float SEARCH_RADIUS = 700;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private SensorManager mSensorManager;
    private Sensor mVectorSensor;
    private float[] mRotationMatrix = new float[16];
    private float[] mValues = new float[3];
    private float mAzimuth;
    private float mTilt;
    private double TARGET_OFFSET_METERS = 10;

    private TextView mMessageView;

    private ValueAnimator vAnimator;
    private Circle mCircle;

    private OnFragmentInteractionListener mListener;

    public static MapFragment newInstance(int sectionNumber) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSensor();
        getApiClient();
        createLocationRequest();
    }

    private void getSensor() {
        mSensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        mVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);        
    }

    private void getApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMap();
        mMap.setMyLocationEnabled(true);
        mGoogleApiClient.connect();
    }

    private void getMap() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    private void setDefaultMarker() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
        mMap.setMyLocationEnabled(false);
        killSearchRadius();
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(consts.auckland_geolocation, 13));
        drawCameraMarkers(map);
    }

    private void drawCameraMarkers(GoogleMap map) {
        List<Camera> speedCameras = Camera.listAll(Camera.class);
        for (Camera speedCamera : speedCameras) {
            map.addMarker(new MarkerOptions()
            .title(getSpeedCameraTitle(speedCamera))
            .snippet(getSpeedCameraSnippet(speedCamera))
            .position(new LatLng(speedCamera.latitude, speedCamera.longitude)));
        }
    }

    private String getSpeedCameraSnippet(Camera speedCamera) {
        return speedCamera.street + "," + speedCamera.suburb;
    }

    private String getSpeedCameraTitle(Camera speedCamera) {
        String title = speedCamera.type + " " + "camera";
        title = speedCamera.speed_limit == 0 ? title : title + " " + speedCamera.speed_limit + "km";
        return title;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        //mLocationView.setText("Location received: " + location.toString());
        mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 13));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mLatLng != null && checkReady()) {
            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            SensorManager.getOrientation(mRotationMatrix, mValues);
            mAzimuth = (float) Math.toDegrees(mValues[0]);
            mTilt = (float) Math.toDegrees(mValues[1]);

            CameraPosition cameraPosition = CameraPosition.builder().
                    tilt(clamp(0,mTilt,(float)67.5)).
                    bearing(mAzimuth).
                    zoom(13+5*(mTilt/90)).
                    target(SphericalUtil.computeOffset(mLatLng, TARGET_OFFSET_METERS, mAzimuth)).
                    build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

//    @Override
//    public void onDisconnected() {
//        // Do nothing
//    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this.getActivity(), R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static float clamp(float x, float min, float max) {
        if (x > max) return max;
        if (x < min) return min;
        return x;
    }

    private void toggleSearchRadius(boolean glow, MenuItem item) {
        if (mLatLng == null || item == null) {
            searchRadiusOn = false;
            item.setIcon(R.drawable.ic_action_search_radius_off);
            killSearchRadius();
            return;
        }

        if (glow) {
            item.setIcon(R.drawable.ic_action_search_radius_on);
            mCircle = mMap.addCircle(new CircleOptions()
                    .center(mLatLng)
                    .radius(SEARCH_RADIUS)
                    .strokeColor(Color.GRAY));
            vAnimator = ValueAnimator.ofFloat(0f,100f);
            vAnimator.setRepeatCount(ValueAnimator.INFINITE);
            vAnimator.setRepeatMode(ValueAnimator.RESTART);
            vAnimator.setDuration(2300);
            vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animationFraction = animation.getAnimatedFraction();
                    mCircle.setRadius(animationFraction*SEARCH_RADIUS);
                    mCircle.setStrokeWidth(1+animationFraction*7);
                }
            });
            vAnimator.start();
        } else {
            item.setIcon(R.drawable.ic_action_search_radius_off);
            killSearchRadius();
        }
    }

    private void killSearchRadius() {
        if (vAnimator != null) {
            vAnimator.removeAllUpdateListeners();
            vAnimator.end();
            mCircle.remove();
        }
    }

    private void onMagicCameraClicked(MenuItem item) {
        if (magicCameraOn)
            toggleMagicCamera(magicCameraOn = false, item);
        else
            toggleMagicCamera(magicCameraOn = true, item);
    }

    private void toggleMagicCamera(boolean magicOn, MenuItem item) {
        if (item == null) {
            magicCameraOn = false;
            item.setIcon(R.drawable.ic_action_magic_camera_off);
            mSensorManager.unregisterListener(this);
            return;
        }

        if (magicOn) {
            item.setIcon(R.drawable.ic_action_magic_camera_on);
            mSensorManager.registerListener(this, mVectorSensor, 16000);
        } else {
            item.setIcon(R.drawable.ic_action_magic_camera_off);
            mSensorManager.unregisterListener(this);
        }
    }
}
