package com.saeinwoojoo.android.wifianalyzer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class ListViewFragment extends BaseFragment {

    private static final String TAG = "ListViewFragment";

    private ListView mListView;
    protected AccessPointListAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list_view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "------- onCreate()...");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "------- onCreateView()...");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "------- onViewCreated()...");

        view.setTag(TAG);
        mListView = view.findViewById(R.id.lv_access_points);
        mAdapter = new AccessPointListAdapter(mAccessPoints, R.layout.access_point_card_item);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener((parent, itemView, position, id) -> {
            Log.d(TAG, "------- Element " + position + " is clicked.");
            AccessPoint accessPoint = mAccessPoints.get(position);
            showToast(accessPoint.ssid + " at position " + position + " is clicked.");
            // TODO: Add a pop-up dialog that displays the details of the selected access point.
        });

        mBtnScan = view.findViewById(R.id.btn_scan);
        if (null != mBtnScan) {
            mBtnScan.setOnClickListener(v -> {
                scanWifiAccessPoints();
            });
        }
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
            } else {
                showToast(R.string.failed_to_scan_wifi, Toast.LENGTH_SHORT, Gravity.CENTER);

                // When failed to scan Wi-Fi, show configured Wi-Fi access point(s).
                List<WifiConfiguration> wifiConfigurations = mWiFiManager.getConfiguredNetworks();
                if (null != wifiConfigurations && !wifiConfigurations.isEmpty()) {
                    mAccessPoints.clear();

                    for (WifiConfiguration configuration : wifiConfigurations)
                        mAccessPoints.add(new AccessPoint(configuration));

                    if (mAccessPoints.isEmpty()) {
                        if (null != getActivity()) {
                            showToast(R.string.no_configured_wifi, Toast.LENGTH_SHORT, Gravity.CENTER);
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
            if (null != mWiFiManager && null != mListView && null != mAdapter && null != intent) {
                Log.d(TAG, "------- mBroadcastReceiver#onReceive(): Action = " + intent.getAction());

                if (containFlags(intent, Intent.FLAG_RECEIVER_FOREGROUND)) {
                    Log.d(TAG, "------- mBroadcastReceiver#onReceive(): Intent.FLAG_RECEIVER_FOREGROUND = YES");
                    if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equalsIgnoreCase(intent.getAction())) {
                        handleScanResult();
                    }
                } else {
                    Log.d(TAG, "------- mBroadcastReceiver#onReceive(): Intent.FLAG_RECEIVER_FOREGROUND = NO");
                    if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equalsIgnoreCase(intent.getAction())) {
                        final BroadcastReceiver.PendingResult pendingResult = goAsync();
                        ScanResultTask asyncTask = new ScanResultTask(pendingResult, intent);
                        asyncTask.execute();
                    }
                }
            }
        }
    };

    private void handleScanResult() {
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
                showToast(R.string.no_scan_result, Toast.LENGTH_SHORT, Gravity.CENTER);
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

    private class ScanResultTask extends AsyncTask<Void, Integer, Boolean> {

        private final BroadcastReceiver.PendingResult pendingResult;
        private final Intent intent;

        private ScanResultTask(BroadcastReceiver.PendingResult pendingResult, Intent intent) {
            this.pendingResult = pendingResult;
            this.intent = intent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            StringBuilder sb = new StringBuilder();
            sb.append("Action: " + intent.getAction() + "\n");
            sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME) + "\n");
            String log = sb.toString();
            Log.d(TAG, log);

            List<ScanResult> scanResults = mWiFiManager.getScanResults();
            if (null != scanResults && !scanResults.isEmpty()) {
                mAccessPoints.clear();

                for (ScanResult result : scanResults) {
                    // Ignore hidden and ad-hoc networks.
                    if (TextUtils.isEmpty(result.SSID) || result.capabilities.contains("[IBSS]")) {
                        continue;
                    }
                    Log.d(TAG, "------- mBroadcastReceiver#onReceive(): Security = " + result.capabilities);
                    mAccessPoints.add(new AccessPoint(result));
                }

                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean scanResult) {
            super.onPostExecute(scanResult);

            if (scanResult) {
                if (mAccessPoints.isEmpty()) {
                    showToast(R.string.no_scan_result, Toast.LENGTH_SHORT, Gravity.CENTER);
                } else {
                    if (1 < mAccessPoints.size())
                        Collections.sort(mAccessPoints, new SortByRSSI());
                    mAdapter.notifyDataSetChanged();
                    mListView.smoothScrollToPosition(0);
                }
            } else {
                showToast(R.string.failed_to_scan_wifi, Toast.LENGTH_SHORT, Gravity.CENTER);
            }

            if (null != mBtnScan)
                mBtnScan.setEnabled(true);

            if (null != getActivity())
                ((MainActivity) getActivity()).showOrHideProgressBarScanning(View.GONE);

            // Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish();
        }
    }
}
