package jupitor.konex.jupitor.utils;


import android.location.Location;
import java.util.List;
import jupitor.konex.jupitor.dataAccess.Camera;

public class SpeedCameraWarningHelper {

    public static Camera getApproachingCamera(Location currentLoc, Location prevLoc) {
        Location speedCameraLoc = new Location("Default Location Provider");
        Camera approachingCamera = new Camera();
        float prevDistance, currentDistance, shortestDistance = Float.MAX_VALUE;
        double latUpBond = currentLoc.getLatitude() + 0.01, latLowBond = currentLoc.getLatitude() - 0.01;
        double lngRightBond = currentLoc.getLongitude() + 0.01, lngLeftBond = currentLoc.getLongitude() - 0.01;

        List<Camera> speedCameras = Camera.find(Camera.class,
                "LATITUDE >= ? and LATITUDE <= ? and LONGITUDE >= ? and LONGITUDE <= ?",
                String.valueOf(latLowBond), String.valueOf(latUpBond), String.valueOf(lngLeftBond), String.valueOf(lngRightBond));

        for (Camera speedCamera : speedCameras) {
            speedCameraLoc.setLatitude(speedCamera.latitude);
            speedCameraLoc.setLongitude(speedCamera.longitude);
            currentDistance = currentLoc.distanceTo(speedCameraLoc);
            prevDistance = prevLoc.distanceTo(speedCameraLoc);

            if (currentDistance <= prevDistance && currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                approachingCamera = speedCamera;
            }
        }

        return approachingCamera;
    }
}
