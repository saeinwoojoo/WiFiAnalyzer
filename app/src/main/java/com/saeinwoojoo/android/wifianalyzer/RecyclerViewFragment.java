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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewFragment extends BaseFragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private LayoutManagerType mCurrentLayoutManagerType;

    private RecyclerView mRecyclerView;
    private AccessPointAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private WifiManager mWiFiManager;
    private List<AccessPoint> mAccessPoints = new ArrayList<>();
    private Button mBtnScan;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycler_view;
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
        mRecyclerView = view.findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (null != savedInstanceState) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.
                    getSerializable(KEY_LAYOUT_MANAGER);
        }

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new AccessPointAdapter(mAccessPoints, R.layout.access_point_card_item);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(mOnItemClickListener);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        RadioButton rbLinearLayout = view.findViewById(R.id.rb_linear_layout);
        rbLinearLayout.setOnClickListener(v -> {
            Log.d(TAG, "------- mLinearLayoutRadioButton::onClick()");
            setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        });

        RadioButton rbGridLayoutRadioButton = view.findViewById(R.id.rb_grid_layout);
        rbGridLayoutRadioButton.setOnClickListener(v -> {
            Log.d(TAG, "------- mGridLayoutRadioButton:onClick()");
            setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
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
        if (null != savedInstanceState) {
            // Save currently selected layout manager.
            savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "------- onViewStateRestored()...");

        if (null != savedInstanceState) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.getSerializable(
                    KEY_LAYOUT_MANAGER);

            if (null == mCurrentLayoutManagerType)
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

            setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        }
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

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (null != mRecyclerView.getLayoutManager()) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
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
                                R.string.scanning_wifi, Toast.LENGTH_SHORT, Gravity.CENTER);*/
            } else {
                /*ToastUtil.showText(getActivity(),
                        R.string.failed_to_scan_wifi, Toast.LENGTH_SHORT, Gravity.CENTER);*/
                showToast(R.string.failed_to_scan_wifi, Toast.LENGTH_SHORT, Gravity.CENTER);

                // When failed to scan Wi-Fi, show configured Wi-Fi access point(s).
                List<WifiConfiguration> wifiConfigurations = mWiFiManager.getConfiguredNetworks();
                if (null != wifiConfigurations && !wifiConfigurations.isEmpty()) {
                    mAccessPoints.clear();

                    for (WifiConfiguration configuration : wifiConfigurations)
                        mAccessPoints.add(new AccessPoint(configuration));

                    if (mAccessPoints.isEmpty()) {
                        if (null != getActivity()) {
                            /*ToastUtil.showText(getActivity().getApplicationContext(),
                                    R.string.no_configured_wifi, Toast.LENGTH_SHORT, Gravity.CENTER);*/
                            showToast(R.string.no_configured_wifi, Toast.LENGTH_SHORT, Gravity.CENTER);
                        }
                        Log.i(TAG, "------- mBtnScan::onClick() - No configured Wi-Fi");
                    } else {
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.smoothScrollToPosition(0);
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
            if (null != mWiFiManager && null != mRecyclerView && null != mAdapter && null != intent
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
                        Log.d(TAG, "------- mBroadcastReceiver#onReceive(): Security = " + result.capabilities);
                        mAccessPoints.add(new AccessPoint(result));
                    }

                    if (mAccessPoints.isEmpty()) {
                        /*ToastUtil.showText(getActivity(),
                                R.string.no_scan_result, Toast.LENGTH_SHORT, Gravity.CENTER);*/
                        showToast(R.string.no_scan_result, Toast.LENGTH_SHORT, Gravity.CENTER);
                    } else {
                        if (1 < mAccessPoints.size())
                            Collections.sort(mAccessPoints, new SortByRSSI());
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                }

                if (null != mBtnScan)
                    mBtnScan.setEnabled(true);

                if (null != getActivity())
                    ((MainActivity) getActivity()).showOrHideProgressBarScanning(View.GONE);
            }
        }
    };

    private View.OnClickListener mOnItemClickListener = v -> {
        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
        int position = viewHolder.getAdapterPosition();
        AccessPoint accessPoint = mAccessPoints.get(position);
        /*ToastUtil.showText(getActivity(),
                accessPoint.ssid + " at position " + position + " is clicked.",
                Toast.LENGTH_SHORT);*/
        showToast(accessPoint.ssid + " at position " + position + " is clicked.",
                Toast.LENGTH_SHORT);
        // TODO: Add a pop-up dialog that displays the details of the selected access point.
    };
}
