package interfaces;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

public interface OnMapReadyCallback {

	void onMapReady(GoogleMap map);

	void onLocationChanged(Location location);

}
