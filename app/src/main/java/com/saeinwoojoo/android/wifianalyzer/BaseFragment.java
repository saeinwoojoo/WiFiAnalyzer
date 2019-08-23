package com.saeinwoojoo.android.wifianalyzer;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.saeinwoojoo.android.thememanager.library.ThemeManager;

import java.util.ArrayList;
import java.util.List;

abstract public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    abstract public int getLayoutId();

    protected WifiManager mWiFiManager;
    protected List<AccessPoint> mAccessPoints = new ArrayList<>();
    protected Button mBtnScan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "------- onCreateView()...");
        View view = inflater.inflate(getLayoutId(), container, false);
        ThemeManager.getInstance().applyTheme(view, getLayoutId());
        return view;
    }

    public void showToast(int resId) {
        if (null != getActivity())
            ((BaseActivity) getActivity()).showToast(resId);
    }

    public void showToast(int resId, int duration) {
        if (null != getActivity())
            ((BaseActivity) getActivity()).showToast(resId, duration);
    }

    public void showToast(int resId, int duration, int gravity) {
        if (null != getActivity())
            ((BaseActivity) getActivity()).showToast(resId, duration, gravity);
    }

    public void showToast(CharSequence text) {
        if (null != getActivity())
            ((BaseActivity) getActivity()).showToast(text);
    }

    public void showToast(CharSequence text, int duration) {
        if (null != getActivity())
            ((BaseActivity) getActivity()).showToast(text, duration);
    }

    public void showToast(CharSequence text, int duration, int gravity) {
        if (null != getActivity())
            ((BaseActivity) getActivity()).showToast(text, duration, gravity);
    }

    public boolean containFlags(@NonNull Intent intent, int flags) {
        return 0 != (intent.getFlags() & flags);
    }
}
