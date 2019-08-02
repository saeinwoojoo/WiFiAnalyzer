package com.saeinwoojoo.android.wifianalyzer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saeinwoojoo.android.thememanager.library.ThemeManager;

abstract public class BaseFragment extends Fragment {

    abstract public int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
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
}
