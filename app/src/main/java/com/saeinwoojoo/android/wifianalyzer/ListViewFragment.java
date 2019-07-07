package com.saeinwoojoo.android.wifianalyzer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListViewFragment extends Fragment {

    private static final String TAG = "ListViewFragment";

    private WifiManager mWiFiManager;
    private List<AccessPoint> mAccessPoints = new ArrayList<>();
    private AccessPointListAdapter mAdapter;
    private ListView mListView;
    private Button mBtnScan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "------- onCreate()...");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "------- onCreateView()...");

        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        rootView.setTag(TAG);

        mListView = rootView.findViewById(R.id.lv_access_points);
        mAdapter = new AccessPointListAdapter(mAccessPoints, R.layout.access_point_card_item);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "------- Element " + position + " is clicked.");
            AccessPoint accessPoint = mAccessPoints.get(position);
            ToastUtil.showText(getActivity(),
                    accessPoint.ssid + " at position " + position + " is clicked.",
                    Toast.LENGTH_SHORT);
            // TODO: Add a pop-up dialog that displays the details of the selected access point.
        });

        mBtnScan = rootView.findViewById(R.id.btn_scan);
        if (null != mBtnScan) {
            mBtnScan.setOnClickListener(v -> {
                scanWifiAccessPoints();
            });
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "------- onViewCreated()...");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "------- onActivityCreated()...");

        if (null != getActivity()) {
            mWiFiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(
                    Context.WIFI_SERVICE);
            if (null != mWiFiManager && !mWiFiManager.isWifiEnabled())
                mWiFiManager.setWifiEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "------- onResume()...");

        if (null != getActivity())
            getActivity().registerReceiver(mBroadcastReceiver,
                    new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if (null != mBtnScan)
            mBtnScan.performClick();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "------- onPause()...");

        if (null != getActivity())
            getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "------- onSaveInstanceState()...");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "------- onViewStateRestored()...");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "------- onAttach()...");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "------- onStart()...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "------- onStop()...");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "------- onDestroyView()...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "------- onDestroy()...");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "------- onDetach()...");
    }

    private void scanWifiAccessPoints() {
        if (null != mWiFiManager) {
            if (!mWiFiManager.isWifiEnabled())
                mWiFiManager.setWifiEnabled(true);

            boolean bResult = mWiFiManager.startScan();
            if (bResult) {
                if (null != getActivity())
                    ((MainActivity) getActivity()).showOrHideProgressBarScanning(View.VISIBLE);
                mBtnScan.setEnabled(false);
                        /*ToastUtil.showText(getActivity(),
                                R.string.scanning_wifi,
                                Toast.LENGTH_SHORT, Gravity.CENTER);*/
            } else {
                ToastUtil.showText(getActivity(),
                        R.string.failed_to_scan_wifi,
                        Toast.LENGTH_SHORT, Gravity.CENTER);

                // When failed to scan Wi-Fi, show configured Wi-Fi access point(s).
                List<WifiConfiguration> wifiConfigurations = mWiFiManager.getConfiguredNetworks();
                if (null != wifiConfigurations && !wifiConfigurations.isEmpty()) {
                    mAccessPoints.clear();

                    for (WifiConfiguration configuration : wifiConfigurations)
                        mAccessPoints.add(new AccessPoint(configuration));

                    if (mAccessPoints.isEmpty()) {
                        if (null != getActivity()) {
                            ToastUtil.showText(getActivity().getApplicationContext(),
                                    R.string.no_configured_wifi,
                                    Toast.LENGTH_SHORT, Gravity.CENTER);
                        }
                        Log.i(TAG, "------- mBtnScan::onClick() - No configured Wi-Fi");
                    } else {
                        mAdapter.notifyDataSetChanged();
                        mListView.smoothScrollToPosition(0);
                    }
                }
                if (null != getActivity())
                    ((MainActivity) getActivity()).showOrHideProgressBarScanning(View.GONE);
            }
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != mWiFiManager && null != mListView && null != mAdapter && null != intent
                    && WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equalsIgnoreCase(intent.getAction())) {
                List<ScanResult> scanResults = mWiFiManager.getScanResults();
                if (null != scanResults && !scanResults.isEmpty()) {
                    mAccessPoints.clear();

                    for (ScanResult result : scanResults) {
                        // Ignore hidden and ad-hoc networks.
                        if (TextUtils.isEmpty(result.SSID)
                                || result.capabilities.contains("[IBSS]")) {
                            continue;
                        }
                        mAccessPoints.add(new AccessPoint(result));
                    }

                    if (mAccessPoints.isEmpty()) {
                        ToastUtil.showText(getActivity(),
                                R.string.no_scan_result,
                                Toast.LENGTH_SHORT, Gravity.CENTER);
                    } else {
                        if (mAccessPoints.size() > 1)
                            Collections.sort(mAccessPoints, new SortByRSSI());
                        mAdapter.notifyDataSetChanged();
                        mListView.smoothScrollToPosition(0);
                    }
                }

                if (null != mBtnScan)
                    mBtnScan.setEnabled(true);

                if (null != getActivity())
                    ((MainActivity) getActivity()).showOrHideProgressBarScanning(View.GONE);
            }
        }
    };
}
