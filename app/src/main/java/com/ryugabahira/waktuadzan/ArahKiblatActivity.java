package com.ryugabahira.waktuadzan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.azan.QiblaUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ArahKiblatActivity extends MainActivity {
    static LatLng newLocation = null;
    private TextView txtkota_H;
    private TextView txtnegara_H;
    private TextView txtinfo_H;
    private LinearLayout layout_warningH;
    private RelativeLayout layout_kiblatH;
    private LinearLayout layout_H;
    private ImageView compas_image_H;
    private ImageView front_image_H;
    private TextView txtMagneticH;
    private ImageView imgdownHlight;
    private ImageView front_image_light;
    protected boolean ismovingcamera;
    private static final int MESSAGE_ID_SAVE_CAMERA_POSITION = 1;
    private static final int MESSAGE_ID_READ_CAMERA_POSITION = 2;
    private CameraPosition lastCameraPosition;
    private Handler handler;
    private GoogleMap myMap;
    static String var_daerahx = "", var_negarax = "", var_kotax = "";
    private TextView judul_pesanx;
    static ArrayList<HashMap<String,String>> arr_list_address = new ArrayList<HashMap<String,String>>();
    List<String> list_address = new ArrayList<String>();
    public boolean updateFinished = true;
    protected AutoCompleteTextView edinput;

    //	private PlacesTask placesTask;
    //	public ParserTask parserTask;

    private BroadcastReceiver receiver_latlang = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = (String) intent.getExtras().get("status");
            if (status.equals("SUCCESS")) {
                //				MainActivity.var_daerah = (String) intent.getExtras().get("var_daerah");
                //				MainActivity.var_negara = (String) intent.getExtras().get("var_negara");
                //				MainActivity.var_kota = (String) intent.getExtras().get("var_kota");
                //				MainActivity.kiblatdegree = (double) intent.getExtras().get("kiblatdegree");
                //				MainActivity.arahkiblat = (String) intent.getExtras().get("arahkiblat");
                //				MainActivity.jarakkekiblat = (String) intent.getExtras().get("jarakkekiblat");
                //				MainActivity.latlangkabah = (String) intent.getExtras().get("latlangkabah");

                //				MainActivity.calculate_adzan(getApplicationContext(), new GregorianCalendar());
            }

            try {
                setting_visual();
            } catch (Exception e) {
            }

        }
    };
    private ImageView imgdownH;

    protected void setting_visual() {
        layout_H.setVisibility(View.GONE);
        layout_kiblatH.setVisibility(View.GONE);
        layout_warningH.setVisibility(View.GONE);

        layout_H.setVisibility(View.VISIBLE);
        //		if (getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_0)
        //			MainActivity.headingsudut = 0;
        //		else if (getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_180)
        //			MainActivity.headingsudut = 180;

        if (MainActivity.lokasiGPS != null && MainActivity.lokasiGPS.getLatitude() != 0.0 && MainActivity.lokasiGPS.getLongitude() != 0.0) {
            String nama_daerah;
            if(MainActivity.var_daerah.equals("") && MainActivity.var_kota.equals(""))
                nama_daerah = getResources().getString(R.string.mainmenu_gps_unknown);
            else if(!MainActivity.var_daerah.equals("") && !MainActivity.var_kota.equals(""))
                nama_daerah = MainActivity.var_daerah + ", " + MainActivity.var_kota;
            else if(!MainActivity.var_daerah.equals(""))
                nama_daerah = MainActivity.var_daerah;
            else
                nama_daerah = MainActivity.var_kota;

            DecimalFormat df = new DecimalFormat("#.##");
            if (!nama_daerah.equals(getResources().getString(R.string.mainmenu_gps_unknown))) {
                txtkota_H.setText(nama_daerah);
            }else txtkota_H.setText(getResources().getString(R.string.mainmenu_gps_unknown));

            if (!MainActivity.var_negara.equals(""))
                txtnegara_H.setText("(" + MainActivity.var_negara + ")");
            else txtnegara_H.setText("");

            txtinfo_H.setText(MainActivity.arahkiblat + "\n" +
                    MainActivity.jarakkekiblat + "\n" +
                    getResources().getString(R.string.shalattools_accuracy) + " " + df.format(MainActivity.lokasiGPS.getAccuracy()) + " m, " +
                    getResources().getString(R.string.shalattools_altitude) + " " + df.format(MainActivity.lokasiGPS.getAltitude()) + " m");

            layout_kiblatH.setVisibility(View.VISIBLE);

            if (MainActivity.compass != null) {
                MainActivity.compass.arrowView = compas_image_H;
                MainActivity.compass.textMagnetic = txtMagneticH;
                MainActivity.compass.kiblatView = front_image_H;
                MainActivity.compass.indicator = imgdownHlight;
                MainActivity.compass.indicator2 = front_image_light;
            }else {
                imgdownHlight.setVisibility(View.GONE);
                front_image_light.setVisibility(View.GONE);
                front_image_H.setVisibility(View.GONE);
                txtMagneticH.setVisibility(View.GONE);
                txtMagneticH.setVisibility(View.GONE);
                    MainActivity.toast_info(ArahKiblatActivity.this, "Sensor Compass tidak tersedia di perangkat anda");
            }

        }else layout_warningH.setVisibility(View.VISIBLE);

    }

    @Override
    public void onPause() {
        super.onPause();

        if (MainActivity.compass != null) MainActivity.compass.stop();

        unregisterReceiver(receiver_latlang);

    }

    @Override
    public void onResume() {
        super.onResume();

        setting_visual();
        if (MainActivity.compass != null) MainActivity.compass.start();

        registerReceiver(receiver_latlang, new IntentFilter("LATLANGRECEIVER"));

    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arah_kiblat);

        try {
            newLocation = new LatLng(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude());
        } catch (Exception e) {
            newLocation = new LatLng(0,0);
        }

        setup_compass_kiblat();

    }

    private void setup_compass_kiblat() {
        layout_H = findViewById(R.id.layout_H);
        layout_kiblatH = findViewById(R.id.layout_kiblatH);
        layout_warningH = findViewById(R.id.layout_warningH);
        Button gpsH = findViewById(R.id.gpsH);
        gpsH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.toast_info("MID", ArahKiblatActivity.this, "", "\n" + getResources().getString(R.string.shalattools_refresh_lokasi) + "\n", 4000);
                MainActivity.askSetting_cek_gps_and_update(ArahKiblatActivity.this);
            }
        });
        Button manualH = findViewById(R.id.manualH);
        manualH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atur_manual_peta();
            }
        });
        ImageView icekgps = findViewById(R.id.icekgps);
        icekgps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.toast_info("MID", ArahKiblatActivity.this, "", "\n" + getResources().getString(R.string.shalattools_refresh_lokasi) + "\n", 4000);
                MainActivity.askSetting_cek_gps_and_update(ArahKiblatActivity.this);

            }
        });
        ImageView imanual = findViewById(R.id.imanual);
        imanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atur_manual_peta();
            }
        });
        imgdownH = findViewById(R.id.imgdownH);
        imgdownHlight = findViewById(R.id.imgdownHlight);
        txtinfo_H = findViewById(R.id.txtinfo_H);
        txtkota_H = findViewById(R.id.txtkota_H);
        txtMagneticH = findViewById(R.id.txtMagneticH);
        txtnegara_H = findViewById(R.id.txtnegara_H);
        compas_image_H = findViewById(R.id.compas_image_H);
        compas_image_H.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_peta();
            }
        });
        front_image_H = findViewById(R.id.front_image_H);
        front_image_H.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_peta();
            }
        });
        front_image_light = findViewById(R.id.front_image_light);
        front_image_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_peta();
            }
        });

            icekgps.setColorFilter(MainActivity.colormatrix_green());
            imanual.setColorFilter(MainActivity.colormatrix_green());
            imgdownH.setColorFilter(MainActivity.colormatrix_green());
            compas_image_H.setColorFilter(MainActivity.colormatrix_green());
            front_image_H.setImageResource(R.drawable.compass_needle_2);

        setting_visual();

    }

    @SuppressWarnings("deprecation")
    protected void show_peta() {
        try {
            if (MainActivity.lokasiGPS.getLatitude() != 0.0 && MainActivity.lokasiGPS.getLongitude() != 0.0
                    && !MainActivity.var_daerah.equals("")) {
                int screenwidth;
                int screenheight;
                if (android.os.Build.VERSION.SDK_INT >= 13) {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    screenwidth = size.x;
                    screenheight = size.y;
                }else {
                    Display display = getWindowManager().getDefaultDisplay();
                    screenwidth = display.getWidth();
                    screenheight = display.getHeight();
                }
                final Double xwidth = screenwidth * 0.8;
                final Double xheight = screenheight * 0.8;

                final Dialog dialogpesanx = new Dialog(this);
                dialogpesanx.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogpesanx.setContentView(R.layout.layout_peta_kiblat);
                dialogpesanx.getWindow().setLayout(xwidth.intValue(), xheight.intValue());
                dialogpesanx.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
                dialogpesanx.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogpesanx.setCancelable(false);
                dialogpesanx.setCanceledOnTouchOutside(false);

                //				FragmentManager myFragmentManager = getFragmentManager();
                //				final MapFragment myMapFragment = (MapFragment) myFragmentManager.findFragmentById(R.id.mapViewKiblat);

                FragmentManager myFragmentManager = getSupportFragmentManager();
                final SupportMapFragment myMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.mapViewKiblat);
                //final GoogleMap myMap = myMapFragment.getMap();
                myMapFragment.getMapAsync(arg0 -> {
                    myMap = arg0;


                    try {
                        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        try {
                            myMap.setMyLocationEnabled(true);
                        } catch (Exception e) {
                        }
                        myMap.setTrafficEnabled(false);
                        myMap.getUiSettings().setCompassEnabled(true);
                        myMap.getUiSettings().setRotateGesturesEnabled(true);
                        myMap.getUiSettings().setTiltGesturesEnabled(true);
                        myMap.getUiSettings().setMyLocationButtonEnabled(true);
                        myMap.getUiSettings().setZoomControlsEnabled(true);
                        myMap.getUiSettings().setAllGesturesEnabled(true);
                        myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                //									LinearLayout info = new LinearLayout(this);
                                //									info.setOrientation(LinearLayout.VERTICAL);
                                //									info.setPadding(20, 20, 20, 20);
                                //									if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                //										info.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.green1)));
                                //									} else {
                                //										info.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.green1)));
                                //									}
                                //
                                //									TextView title = new TextView(this);
                                //									title.setTextColor(ContextCompat.getColor(this, R.color.yellow2));
                                //									title.setGravity(Gravity.CENTER);
                                //									//					title.setTypeface(null, Typeface.BOLD);
                                //									title.setText(marker.getTitle());
                                //
                                //									info.addView(title);
                                //
                                //									title = new TextView(this);
                                //									title.setTextColor(ContextCompat.getColor(this, R.color.putihx));
                                //									title.setGravity(Gravity.CENTER);
                                //									//					title.setTypeface(null, Typeface.BOLD);
                                //									title.setText(marker.getSnippet());
                                //									title.setTextSize(12);
                                //
                                //									info.addView(title);

                                // Getting view from the layout file info_window_layout
                                View info = getLayoutInflater().inflate(R.layout.layout_info_map, null);
                                TextView title_map = info.findViewById(R.id.title_map);
                                TextView desc_map = info.findViewById(R.id.desc_map);
                                ImageView imgmarker = info.findViewById(R.id.imgmarker);
                                title_map.setText(marker.getTitle());
                                desc_map.setText(marker.getSnippet());

                                if (marker.getTitle().equals("Lokasi Anda"))
                                    imgmarker.setImageResource(R.drawable.you3);
                                else imgmarker.setImageResource(R.drawable.kabah2);

                                return info;
                            }
                        });

                        myMap.clear();

                        Bitmap imgKiblat = BitmapFactory.decodeResource(getResources(), R.drawable.kabah);
                        //			imgKiblat = Bitmap.createScaledBitmap(imgKiblat,50,50,true);
                        MarkerOptions markerkabah = new MarkerOptions().
                                position(new LatLng(com.azan.Constants.KAABA_LATITUDE, com.azan.Constants.KAABA_LONGITUDE)).
                                icon(BitmapDescriptorFactory.fromBitmap(imgKiblat)).
                                title("Lokasi Ka'bah.")
                                .snippet("Lat : " + com.azan.Constants.KAABA_LATITUDE + ", " + com.azan.Constants.KAABA_LONGITUDE);
                        myMap.addMarker(markerkabah);

                        Bitmap imgYou = BitmapFactory.decodeResource(getResources(), R.drawable.you);
                        //			imgYou = Bitmap.createScaledBitmap(imgYou,50,50,true);
                        MarkerOptions markerYou = new MarkerOptions().
                                position(new LatLng(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude())).
                                icon(BitmapDescriptorFactory.fromBitmap(imgYou)).
                                title("Lokasi Anda")
                                .snippet("Lat : " + MainActivity.lokasiGPS.getLatitude() + ", " + MainActivity.lokasiGPS.getLongitude());
                        myMap.addMarker(markerYou);

                        LatLngBounds bounds = new LatLngBounds.Builder().
                                include(new LatLng(com.azan.Constants.KAABA_LATITUDE, com.azan.Constants.KAABA_LONGITUDE)).
                                include(new LatLng(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude())).build();

                        myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, xwidth.intValue(), xwidth.intValue(), 100));

                        int PATTERN_DASH_LENGTH_PX = 20;
                        int PATTERN_GAP_LENGTH_PX = 20;
                        PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
                        PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
                        List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

                        PolylineOptions line = new PolylineOptions().
                                add(new LatLng(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude()),
                                        new LatLng(com.azan.Constants.KAABA_LATITUDE, com.azan.Constants.KAABA_LONGITUDE)).
                                width(6).
                                color(ContextCompat.getColor(this, R.color.red1)).
//											geodesic(true).
        pattern(PATTERN_POLYGON_ALPHA)
                                .zIndex(30)
                                ;

                        myMap.addPolyline(line);
                    } catch (Exception e) {
                        MainActivity.toast_info(this, "Google Maps Error");
                        return;
                    }

                });

                TextView judul_pesan = dialogpesanx.findViewById(R.id.judul_pesan);
                judul_pesan.setText(arahkiblat + "\n" + MainActivity.jarakkekiblat);
                Button btn1 = dialogpesanx.findViewById(R.id.btn1);
                btn1.setText(getResources().getString(R.string.str_btnok));
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //						FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.remove(myMapFragment);
                        ft2.commit();

                        dialogpesanx.dismiss();
                    }
                });
                Button btn2 = dialogpesanx.findViewById(R.id.btn2);
                btn2.setVisibility(View.GONE);

                dialogpesanx.show();
            }
        } catch (Resources.NotFoundException e) {
        } catch (Exception e1) {
        }
    }

    public class MyGeocoderx extends AsyncTask<String, String, String> {
        private Context contextx;
        int maxResult = 1;
        private String nama_daerah;

        public MyGeocoderx(Context ctx) {
            // TODO Auto-generated constructor stub
            this.contextx = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            URL geocodeUrl;
            HttpURLConnection connection;
            int responseCode = 0;
            var_daerahx = "";
            var_negarax = "";
            var_kotax = "";
            nama_daerah = "";
            String vsub = "", var_kota2 = "";
            Geocoder geocoder = new Geocoder(contextx, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(newLocation.latitude, newLocation.longitude, 1);
                if (addresses.size() > 0) {
                    var_daerahx = addresses.get(0).getLocality();
                    vsub = addresses.get(0).getSubLocality();
                    var_negarax = addresses.get(0).getCountryName();
                    var_kotax = addresses.get(0).getSubAdminArea();
                    var_kota2 = addresses.get(0).getAdminArea();

                }
            } catch (IOException e) {
                try {
                    geocodeUrl = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + newLocation.latitude
                            + "," + newLocation.longitude + "&sensor=false&key=" + contextx.getResources().getString(R.string.google_api_key_geocode)); //keygeocoding
                    connection = (HttpURLConnection)geocodeUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = null;

                        InputStream inputStream = connection.getInputStream();
                        StringBuffer buffer = new StringBuffer();
                        if (inputStream == null) {
                            // Nothing to do.
                            return null;
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {

                            buffer.append(line + "\n");
                        }

                        if (buffer.length() == 0) {
                            return null;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(buffer.toString());
                            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                                JSONArray results = jsonObject.getJSONArray("results");
                                if (results.length() > 0) {
                                    for (int i = 0; i < results.length() && i < maxResult; i++) {
                                        JSONObject result = results.getJSONObject(i);
                                        JSONArray components = result.getJSONArray("address_components");
                                        for (int a = 0; a < components.length(); a++) {
                                            JSONObject component = components.getJSONObject(a);
                                            JSONArray types = component.getJSONArray("types");
                                            for (int j = 0; j < types.length(); j++) {
                                                String type = types.getString(j);
                                                if (type.equals("administrative_area_level_4")) {
                                                    vsub = component.getString("long_name");
                                                } else if (type.equals("administrative_area_level_3")) {
                                                    var_daerahx = component.getString("long_name");
                                                } else if (type.equals("administrative_area_level_2")) {
                                                    var_kotax = component.getString("long_name");
                                                } else if (type.equals("administrative_area_level_1")) {
                                                    var_kota2 = component.getString("long_name");
                                                } else if (type.equals("country")) {
                                                    var_negarax = component.getString("long_name");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException ez) {
                        }

                    }

                } catch (MalformedURLException e2) {
                } catch (ProtocolException e1) {
                } catch (IOException ec) {
                }
            }

            if (var_daerahx == null || var_daerahx.equals("null")) var_daerahx = "";
            if (vsub == null || vsub.equals("null")) vsub = "";

            if (var_daerahx.equals("") && vsub.equals(""))
                var_daerahx = "";
            else if (!var_daerahx.equals("") && !vsub.equals("") && !vsub.equals(var_daerahx))
                var_daerahx = vsub + ", " + var_daerahx;
            else if (var_daerahx.equals("") && !vsub.equals(""))
                var_daerahx = vsub;


            if (var_kotax == null || var_kotax.equals("null")) var_kotax = "";
            if (var_kota2 == null || var_kota2.equals("null")) var_kota2 = "";

            if (var_kotax.equals("") && var_kota2.equals(""))
                var_kotax = "";
            else if (!var_kotax.equals("") && !var_kota2.equals("") && !var_kota2.equals(var_kotax))
                var_kotax = var_kota2 + ", " + var_kotax;
            else if (var_kotax.equals("") && !var_kota2.equals(""))
                var_kotax = var_kota2;

            if(var_daerahx.equals("") && var_kotax.equals(""))
                nama_daerah = contextx.getResources().getString(R.string.mainmenu_gps_unknown);
            else if(!var_daerahx.equals("") && !var_kotax.equals(""))
                nama_daerah = var_daerahx + ", " + var_kotax;
            else if(!var_daerahx.equals(""))
                nama_daerah = var_daerahx;
            else
                nama_daerah = var_kotax;

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                DecimalFormat df = new DecimalFormat("#.##");
                double kiblatdegree = QiblaUtils.qibla(newLocation.latitude, newLocation.longitude);
                String arahkiblat = getResources().getString(R.string.shalattools_arahkiblat)
                        + " " + df.format(kiblatdegree)
                        + getResources().getString(R.string.shalattools_dariutara)
                        + " " + QiblaUtils.direction(newLocation,
                        new LatLng(com.azan.Constants.KAABA_LATITUDE, com.azan.Constants.KAABA_LONGITUDE));
                String jarakkekiblat = getResources().getString(R.string.shalattools_jarakkekiblat)
                        + " " + df.format(QiblaUtils.HitungJarak(newLocation.latitude, newLocation.longitude) / 1000)
                        + " Km";

                String ttl = contextx.getResources().getString(R.string.shalattools_manual_title);

                if (nama_daerah.equals(contextx.getResources().getString(R.string.mainmenu_gps_unknown)))
                    judul_pesanx.setText(ttl + "\n" + contextx.getResources().getString(R.string.mainmenu_gps_unknown));
                else
                    judul_pesanx.setText(ttl  + "\n" +
                            nama_daerah + " (" + var_negarax + ")\n" + arahkiblat + "\n" + jarakkekiblat);
            } catch (Exception e) {
            }


        }
        ///////////////////////////////////////
    }

    private class GetPlaces extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... placesURL) {
            //fetch markerOptions
            updateFinished = false;
            StringBuilder placesBuilder = new StringBuilder();
            for (String placeSearchURL : placesURL) {
                try {
                    URL requestUrl = new URL(placeSearchURL);
                    HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = null;

                        InputStream inputStream = connection.getInputStream();
                        if (inputStream == null) {
                            return "";
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {

                            placesBuilder.append(line + "\n");
                        }

                        if (placesBuilder.length() == 0) {
                            return "";
                        }

                    } else {
                        return "";
                    }
                } catch (MalformedURLException e) {
                    return "";
                } catch (IOException e) {
                    return "";
                }
            }
            return placesBuilder.toString();
        }

        //process data retrieved from doInBackground
        protected void onPostExecute(String result) {
            //parse place data returned from Google Places
            try {
                //parse JSON

                //create JSONObject, pass stinrg returned from doInBackground
                JSONObject resultObject = new JSONObject(result);
                //get "results" array
                JSONArray placesArray = resultObject.getJSONArray("predictions");

                arr_list_address.clear();
                list_address.clear();
                for (int p = 0; p < placesArray.length(); p++) {
                    //parse each place
                    //if any values are missing we won't show the marker
                    String place_id= "";
                    String description = "";

                    try {
                        //get place at this index
                        JSONObject placeObject = placesArray.getJSONObject(p);
                        description = placeObject.getString("description");
                        place_id = placeObject.getString("place_id");
                    } catch (JSONException jse) {
                    }

                    //add ke list
                    if (!place_id.equals("")) {
                        HashMap<String, String> map_list = new HashMap<String, String>();
                        map_list.put("place_id", place_id);
                        map_list.put("description", description);
                        //						map_list.put("latitude", "" + placeLL.latitude);
                        //						map_list.put("longitude", "" + placeLL.longitude);

                        arr_list_address.add(map_list);
                        list_address.add(description);

                    }
                }
                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(ArahKiblatActivity.this, R.layout.custom_text ,list_address);
                edinput.setAdapter(dataAdapter1);
                edinput.showDropDown();

                updateFinished=true;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private class GetPlacesLatLang extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... placesURL) {
            //fetch markerOptions
            StringBuilder placesBuilder = new StringBuilder();
            for (String placeSearchURL : placesURL) {
                try {
                    URL requestUrl = new URL(placeSearchURL);
                    HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = null;

                        InputStream inputStream = connection.getInputStream();
                        if (inputStream == null) {
                            return "";
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {

                            placesBuilder.append(line + "\n");
                        }

                        if (placesBuilder.length() == 0) {
                            return "";
                        }

                    } else {
                        return "";
                    }
                } catch (MalformedURLException e) {
                    return "";
                } catch (IOException e) {
                    return "";
                }
            }
            return placesBuilder.toString();
        }

        //process data retrieved from doInBackground
        protected void onPostExecute(String result) {
            //parse place data returned from Google Places
            LatLng placeLL = null;
            try {
                //parse JSON

                //create JSONObject, pass stinrg returned from doInBackground
                JSONObject resultObject = new JSONObject(result);
                //get "results" array
                JSONObject placesArray = resultObject.getJSONObject("result").getJSONObject("geometry")
                        .getJSONObject("location");
                placeLL = new LatLng(Double.valueOf(placesArray.getString("lat")),
                        Double.valueOf(placesArray.getString("lng")));

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (placeLL != null)
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLL, 10));

        }

    }

    @SuppressWarnings("deprecation")
    protected void atur_manual_peta() {
        final Dialog dialogpesan = new Dialog(this);
        dialogpesan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpesan.setContentView(R.layout.layout_dialog_message);
        dialogpesan.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogpesan.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
        dialogpesan.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogpesan.setCancelable(true);
        dialogpesan.setCanceledOnTouchOutside(false);

        TextView judul_pesan = dialogpesan.findViewById(R.id.judul_pesan);
        TextView isi_pesan = dialogpesan.findViewById(R.id.isi_pesan);
        judul_pesan.setText(getResources().getString(R.string.shalattools_kiblat_manualtitle));
        isi_pesan.setText(getResources().getString(R.string.shalattools_kiblat_manualinfo));

        Button btn2 = dialogpesan.findViewById(R.id.btn2);
        btn2.setText(getResources().getString(R.string.str_btnbatal));
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogpesan.dismiss();

            }
        });

        Button btn1 = dialogpesan.findViewById(R.id.btn1);
        btn1.setText(getResources().getString(R.string.str_btnlanjut));
        btn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            public void onClick(View v) {
                dialogpesan.dismiss();

                int screenwidth;
                int screenheight;
                if (android.os.Build.VERSION.SDK_INT >= 13) {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    screenwidth = size.x;
                    screenheight = size.y;
                }else {
                    Display display = getWindowManager().getDefaultDisplay();
                    screenwidth = display.getWidth();
                    screenheight = display.getHeight();
                }
                Double xwidth = screenwidth * 0.8;
                Double xheight = screenheight * 0.8;

                final Dialog dialogpesanx = new Dialog(ArahKiblatActivity.this);
                dialogpesanx.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogpesanx.setContentView(R.layout.layout_peta_kiblat);
                dialogpesanx.getWindow().setLayout(xwidth.intValue(), xheight.intValue());
                dialogpesanx.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
                dialogpesanx.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogpesanx.setCancelable(false);
                dialogpesanx.setCanceledOnTouchOutside(false);

                list_address.clear();
                edinput = dialogpesanx.findViewById(R.id.edinput);
                edinput.setVisibility(View.VISIBLE);
                edinput.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        final int DRAWABLE_LEFT = 0;
                        final int DRAWABLE_TOP = 1;
                        final int DRAWABLE_RIGHT = 2;
                        final int DRAWABLE_BOTTOM = 3;

                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if(event.getRawX() >= (edinput.getRight() - edinput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                edinput.setText("");
                                list_address.clear();
                                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(ArahKiblatActivity.this, R.layout.custom_text ,list_address);
                                edinput.setAdapter(dataAdapter1);

                                return true;
                            }

                        }

                        if (!edinput.getText().toString().trim().equals("")) edinput.showDropDown();

                        return false;
                    }
                });
                edinput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                        String selection = (String)parent.getItemAtPosition(position);

                        if (MainActivity.is_NetworkAvailable(ArahKiblatActivity.this)) {
                            //				        	MainActivity.toast_info(this, position + " - " + selection);
                            String key = "key=" + getResources().getString(R.string.google_api_key_place);//key google places

                            String placeid = "place_id=" + arr_list_address.get(position).get("place_id");

                            String placesSearchStr = "https://maps.googleapis.com/maps/api/place/details/" +
                                    "json?" + placeid +"&"+key;

                            GetPlacesLatLang myplaces = new GetPlacesLatLang();
                            if (Build.VERSION.SDK_INT >= 11) {
                                myplaces.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, placesSearchStr);
                            } else {
                                myplaces.execute(selection);
                            }
                        }else
                            MainActivity.toast_info(ArahKiblatActivity.this, "Koneksi internet tidak ditemukan.\nMohon aktifkan koneksi internet anda.");

                    }
                });
                edinput.setThreshold(1);
                edinput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            if (edinput.getText().toString().length() >= 2)
                                if (updateFinished) {
                                    if (MainActivity.is_NetworkAvailable(ArahKiblatActivity.this)) {
                                        // Obtain browser key from https://code.google.com/apis/console
                                        String key = "key=" + getResources().getString(R.string.google_api_key_place);//key google places

                                        String input="";

                                        try {
                                            input = "input=" + URLEncoder.encode(edinput.getText().toString().trim(), "utf-8");
                                        } catch (UnsupportedEncodingException e1) {
                                            e1.printStackTrace();
                                        }

                                        if (!input.equals("")) {
                                            String placesSearchStr = "https://maps.googleapis.com/maps/api/place/autocomplete/" +
                                                    "json?" + input+"&"+key;

                                            GetPlaces myplaces = new GetPlaces();
                                            if (Build.VERSION.SDK_INT >= 11) {
                                                myplaces.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, placesSearchStr);
                                            } else {
                                                myplaces.execute(placesSearchStr);
                                            }
                                        }

                                    }else {
                                            MainActivity.toast_info(ArahKiblatActivity.this, "Koneksi internet tidak ditemukan.\nMohon aktifkan koneksi internet anda.");
                                    }

                                    return true;
                                }

                        }
                        return false;
                    }
                });

                judul_pesanx = dialogpesanx.findViewById(R.id.judul_pesan);
                judul_pesanx.setText(getResources().getString(R.string.shalattools_manual_title));

                //				FragmentManager myFragmentManager = getFragmentManager();
                FragmentManager myFragmentManager = getSupportFragmentManager();

                //				final MapFragment myMapFragment = (MapFragment) myFragmentManager.findFragmentById(R.id.mapViewKiblat);
                final SupportMapFragment myMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.mapViewKiblat);
                //				final GoogleMap myMap = myMapFragment.getMap();
                myMapFragment.getMapAsync(arg0 -> {
                    myMap = arg0;


                    final int maxResult = 1;
                    handler = new Handler() {
                        public void handleMessage(Message msg) {
                            if (msg.what == MESSAGE_ID_SAVE_CAMERA_POSITION) {
                                lastCameraPosition = myMap.getCameraPosition();
                            } else if (msg.what == MESSAGE_ID_READ_CAMERA_POSITION) {
                                if (lastCameraPosition.equals(myMap.getCameraPosition())) {
                                    //					                    Log.d(LOG, "Camera position stable");
                                    newLocation = myMap.getCameraPosition().target;

                                    if (newLocation != null)
                                        if (newLocation.latitude != 0.0 && newLocation.longitude != 0.0) {
                                            if (MainActivity.is_NetworkAvailable(ArahKiblatActivity.this)) {
                                                MyGeocoderx mygeo = new MyGeocoderx(ArahKiblatActivity.this);
                                                if (Build.VERSION.SDK_INT >= 11) {
                                                    mygeo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                } else {
                                                    mygeo.execute();
                                                }
                                            }

                                        }
                                }
                            }
                        }
                    };

                    try {
                        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        try {
                            myMap.setMyLocationEnabled(true);
                        } catch (Exception e) {
                        }
                        myMap.setTrafficEnabled(false);
                        myMap.getUiSettings().setCompassEnabled(true);
                        myMap.getUiSettings().setRotateGesturesEnabled(true);
                        myMap.getUiSettings().setTiltGesturesEnabled(true);
                        myMap.getUiSettings().setMyLocationButtonEnabled(true);
                        myMap.getUiSettings().setZoomControlsEnabled(true);
                        myMap.getUiSettings().setAllGesturesEnabled(true);

                        myMap.clear();

                        myMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                            @Override
                            public void onCameraChange(CameraPosition arg0) {
                                handler.removeMessages(MESSAGE_ID_SAVE_CAMERA_POSITION);
                                handler.removeMessages(MESSAGE_ID_READ_CAMERA_POSITION);
                                handler.sendEmptyMessageDelayed(MESSAGE_ID_SAVE_CAMERA_POSITION, 300);
                                handler.sendEmptyMessageDelayed(MESSAGE_ID_READ_CAMERA_POSITION, 600);
                            }
                        });

                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude()), 10));

                    } catch (Exception e) {
                        MainActivity.toast_info(ArahKiblatActivity.this, "Google Maps Error");
                    }
                });

                Button btn1 = dialogpesanx.findViewById(R.id.btn1);
                btn1.setText(getResources().getString(R.string.str_btnsimpan));
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //simpan
                        if (newLocation != null && newLocation.latitude != 0.0 && newLocation.longitude != 0.0 ) {
                            //							FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.remove(myMapFragment);
                            ft2.commit();

                            dialogpesanx.dismiss();

                            MainActivity.lokasiGPS.setLatitude(newLocation.latitude);
                            MainActivity.lokasiGPS.setLongitude(newLocation.longitude);

                            DecimalFormat df = new DecimalFormat("#.##");
                            MainActivity.kiblatdegree = QiblaUtils.qibla(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude());
                            MainActivity.arahkiblat = getResources().getString(R.string.shalattools_arahkiblat)
                                    + " " + df.format(MainActivity.kiblatdegree)
                                    + getResources().getString(R.string.shalattools_dariutara)
                                    + " " + QiblaUtils.direction(newLocation,
                                    new LatLng(com.azan.Constants.KAABA_LATITUDE, com.azan.Constants.KAABA_LONGITUDE));
                            MainActivity.jarakkekiblat = getResources().getString(R.string.shalattools_jarakkekiblat)
                                    + " " + df.format(QiblaUtils.HitungJarak(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude()) / 1000)
                                    + " Km";

                            MainActivity.var_daerah = var_daerahx;
                            MainActivity.var_negara = var_negarax;
                            MainActivity.var_kota = var_kotax;

                            SharedPreferences.Editor editor = getSharedPreferences("iMoslem", Context.MODE_PRIVATE).edit();
                            editor.putString("lastlat", "" + MainActivity.lokasiGPS.getLatitude());
                            editor.putString("lastlong", "" + MainActivity.lokasiGPS.getLongitude());
                            editor.putString("var_daerah", MainActivity.var_daerah);
                            editor.putString("var_negara", MainActivity.var_negara);
                            editor.putString("var_kota", MainActivity.var_kota);
                            editor.putString("arahkiblat", MainActivity.arahkiblat);
                            editor.putString("jarakkekiblat", MainActivity.jarakkekiblat);
                            editor.commit();

                            if (MainActivity.alarmmanagerAdzan != null) {
                                MainActivity.getjadwalshalat(ArahKiblatActivity.this);

                            }else MainActivity.start_alarm_adzan(ArahKiblatActivity.this);

                            Intent intentx = new Intent("LATLANGRECEIVER"); //FILTER is a string to identify this intent
                            intentx.putExtra("status", "SUCCESS");
                            //				intentx.putExtra("var_daerah", var_daerah);
                            //				intentx.putExtra("var_negara", var_negara);
                            //				intentx.putExtra("var_kota", var_kota);
                            //
                            //				intentx.putExtra("kiblatdegree", kiblatdegree);
                            //				intentx.putExtra("arahkiblat", arahkiblat);
                            //				intentx.putExtra("jarakkekiblat", jarakkekiblat);
                            //				intentx.putExtra("latlangkabah", latlangkabah);

                            sendBroadcast(intentx);
                        } else MainActivity.toast_info(ArahKiblatActivity.this, "Cannot Save location");
                    }
                });
                Button btn2 = dialogpesanx.findViewById(R.id.btn2);
                btn2.setText(getResources().getString(R.string.str_btnbatal));
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //						FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.remove(myMapFragment);
                        ft2.commit();

                        dialogpesanx.dismiss();
                    }
                });
                ImageView imarker = dialogpesanx.findViewById(R.id.imarker);
                imarker.setVisibility(View.VISIBLE);

                dialogpesanx.show();
            }
        });

        dialogpesan.show();

    }
}
