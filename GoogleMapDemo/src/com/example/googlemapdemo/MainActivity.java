package com.example.googlemapdemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements LocationListener, OnInfoWindowClickListener, OnMarkerClickListener {

	GoogleMap googleMap;
	SupportMapFragment supportMapFragment;
	ArrayList<Marker> myMapData;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Initalization();
	}

	@Override
	protected void onResume() {
		super.onResume();
		LoadMap();
	}

	private void Initalization() {
		supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		googleMap = supportMapFragment.getMap();
	}

	private void LoadMap() {
		String addressText = "";
		myMapData = new ArrayList<Marker>();
		double defaultLatitude = 0, defaultLongitude = 0;
		try {
			LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
			android.location.Location loc = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			googleMap.clear();
			if (loc != null) {
				for(int i=0;i<5;i++){
					LatLng Location = new LatLng(loc.getLatitude()+i,
							loc.getLongitude()-i);
					defaultLatitude = Location.latitude;
					defaultLongitude = Location.longitude;
					double latitude = defaultLatitude, longitude = defaultLongitude;
					Geocoder geocoder = new Geocoder(this, Locale.getDefault());
					try {
						List<Address> addresses = geocoder.getFromLocation(
								latitude, longitude, 1);
						if (addresses != null && addresses.size() > 0) {
							Address address = addresses.get(0);
							for (int lineIndex = 0; lineIndex < address
									.getMaxAddressLineIndex();

							lineIndex++) {
								addressText = addressText +

								address.getAddressLine(lineIndex) + ", ";
							}
							addressText = addressText + address.getLocality() + ", " + address.getCountryName();
							
							myMapData.add(googleMap.addMarker(new MarkerOptions().position(
									Location).title	(addressText)));
							Location = new LatLng(latitude, longitude);
							addressText = "";
						}

					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
			
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);
			LatLngBounds.Builder b = new LatLngBounds.Builder();
			if(myMapData.size() > 0){
				for (Marker m : myMapData) {
				    b.include(m.getPosition());
				}
				LatLngBounds bounds = b.build();
				CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 35,35,5);
				googleMap.animateCamera(cu);
			}
			
			googleMap.setOnMarkerClickListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		
		//final DataClass data = myMapData.get(marker);
		
		final Dialog d = new Dialog(MainActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //d.setTitle("Select");
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        d.setContentView(R.layout.marker_press);
        
        final LatLng l = marker.getPosition();
        
        TextView tvLocationName = (TextView) d.findViewById(R.id.tvLocationName);
        TextView tvlatitude = (TextView) d.findViewById(R.id.tvlatitude);
        TextView tvLongitude = (TextView) d.findViewById(R.id.tvLongitude);
        
        tvLocationName.setText(marker.getTitle());
        tvlatitude.setText(""+l.latitude);
        tvLongitude.setText(""+l.longitude);

        d.show();
		return true;
	}

}
