package com.saeinwoojoo.android.wifianalyzer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 777;

    private LinearLayout mLlScanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "------- onCreate()...");

        setContentView(R.layout.activity_main);

        /* Two permissions belows are need in version 23, 24 and 25 because
            the AOSP tracker issue 185370 WifiManager#getScanResults() returns
            an empty array list if GPS is turned off and the app does not have
            Location service permission. */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "------- onCreate(): Location permission is not granted.");
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        } else {
            // permission was previously granted or it's in the legacy device
            Log.i(TAG, "------- onCreate(): Location permission was previously granted or it's in the legacy device.");
        }

        mLlScanning = findViewById(R.id.ll_scanning);

        RadioButton radioBtnRecyclerViewFragment = findViewById(R.id.rb_recycler_view);
        radioBtnRecyclerViewFragment.setOnClickListener(v -> {
            Fragment topFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (topFragment instanceof RecyclerViewFragment) {
                Log.d(TAG, "------- radioBtnRecyclerViewFragment::onClick() - current top fragment is RecyclerViewFragment.");
                return;
            }
            launchRecyclerViewFragment();
        });

        RadioButton radioBtnListViewFragment = findViewById(R.id.rb_list_view);
        radioBtnListViewFragment.setOnClickListener(v -> {
            Fragment topFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (topFragment instanceof ListViewFragment) {
                Log.d(TAG, "------- radioBtnListViewFragment::onClick() - current top fragment is ListViewFragment.");
                return;
            }
            launchListViewFragment();
        });

        if (savedInstanceState == null)
            launchRecyclerViewFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "------- onStart()...");
    }

    /**
     * Called after {@link #onStop} when the current activity is being
     * re-displayed to the user (the user has navigated back to it).  It will
     * be followed by {@link #onStart} and then {@link #onResume}.
     *
     * <p>For activities that are using raw {@link Cursor} objects (instead of
     * creating them through
     * {@link #managedQuery(Uri, String[], String, String[], String)},
     * this is usually the place
     * where the cursor should be requeried (because you had deactivated it in
     * {@link #onStop}.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onStop
     * @see #onStart
     * @see #onResume
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "------- onRestart()...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "------- onResume()...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "------- onPause()...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "------- onStop()...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "------- onDestroy()...");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d(TAG, "------- onTrimMemory(): level = " + level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "------- onConfigurationChanged()...");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "------- onBackPressed()...");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "------- onSaveInstanceState(Bundle outState)");
    }

    /**
     * This is the same as {@link #onSaveInstanceState} but is called for activities
     * created with the attribute {@link android.R.attr#persistableMode} set to
     * <code>persistAcrossReboots</code>. The {@link PersistableBundle} passed
     * in will be saved and presented in {@link #onCreate(Bundle, PersistableBundle)}
     * the first time that this activity is restarted following the next device reboot.
     *
     * @param outState           Bundle in which to place your saved state.
     * @param outPersistentState State which will be saved across reboots.
     * @see #onSaveInstanceState(Bundle)
     * @see #onCreate
     * @see #onRestoreInstanceState(Bundle, PersistableBundle)
     * @see #onPause
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d(TAG, "------- onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)");
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     *
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "------- onRestoreInstanceState(Bundle savedInstanceState)");
    }

    /**
     * This is the same as {@link #onRestoreInstanceState(Bundle)} but is called for activities
     * created with the attribute {@link android.R.attr#persistableMode} set to
     * <code>persistAcrossReboots</code>. The {@link PersistableBundle} passed
     * came from the restored PersistableBundle first
     * saved in {@link #onSaveInstanceState(Bundle, PersistableBundle)}.
     *
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}.
     *
     * <p>If this method is called {@link #onRestoreInstanceState(Bundle)} will not be called.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @param persistentState    the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onRestoreInstanceState(Bundle)
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.d(TAG, "------- onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState)");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "------- onRequestPermissionsResult(): Location permission is granted.");
            } else {
                Log.e(TAG, "------- onRequestPermissionsResult(): Location permission is not granted.");
                ToastUtil.showText(getApplicationContext(), R.string.plz_grant_location_permission,
                        Toast.LENGTH_LONG, Gravity.CENTER);
                finish();
            }
        }
    }

    public void showOrHideProgressBarScanning(int visibility) {
        mLlScanning.setVisibility(visibility);
    }

    private void launchRecyclerViewFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void launchListViewFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ListViewFragment fragment = new ListViewFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}