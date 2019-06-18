package com.saeinwoojoo.android.wifianalyzer;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import static com.saeinwoojoo.android.wifianalyzer.AccessPoint.NUMBER_OF_LEVELS;

public class AccessPointAdapter extends RecyclerView.Adapter<AccessPointAdapter.ViewHolder> {

    private static final String TAG = "AccessPointAdapter";

    private List<AccessPoint> mData;
    private int mResource;
    private View.OnClickListener mOnItemClickListener;

    public AccessPointAdapter(List<AccessPoint> data, @LayoutRes int resource) {
        mData = data;
        mResource = resource;
    }

    @Override
    public int getItemCount() {
        if (null != mData)
            return mData.size();

        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(mResource, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (null == mData) {
            Log.e(TAG, "------- onBindViewHolder(): data is null.");
            return;
        }

        final AccessPoint accessPoint = mData.get(position);
        if (null == accessPoint) {
            Log.e(TAG, "------- onBindViewHolder(): dataSet at position " + position + " is null.");
            return;
        }

        accessPoint.setSignalImage(viewHolder.ivSignal);

        CharSequence text = accessPoint.getSecurityString(viewHolder.itemView.getContext(), true);
        if (!TextUtils.isEmpty(text))
            viewHolder.tvSecurityAuthType.setText(text);
        else
            viewHolder.tvSecurityAuthType.setText("");

        if (!TextUtils.isEmpty(accessPoint.ssid))
            viewHolder.tvSSID.setText(accessPoint.ssid);
        else
            viewHolder.tvSSID.setText("");

        text = Integer.toString(AccessPoint.convertFrequencyToChannel(accessPoint.frequency));
        if (!TextUtils.isEmpty(text))
            viewHolder.tvChannel.setText(
                    viewHolder.itemView.getContext().getString(R.string.wifi_channel, text));
        else
            viewHolder.tvChannel.setText("");

        if (!TextUtils.isEmpty(accessPoint.bssid))
            viewHolder.tvBSSID.setText(accessPoint.bssid);
        else
            viewHolder.tvBSSID.setText("");

        String strGHz = AccessPoint.getGHzFromFrequency(accessPoint.frequency);
        if (TextUtils.isEmpty(strGHz)) {
            viewHolder.tvGHz.setVisibility(View.GONE);
        } else {
            viewHolder.tvGHz.setText(strGHz);
            viewHolder.tvGHz.setVisibility(View.VISIBLE);
        }

        int progress = AccessPoint.getLevel(accessPoint.getRSSI(), NUMBER_OF_LEVELS);
        viewHolder.pbSignalLevel.setProgress(progress);
        text = viewHolder.itemView.getContext().getString(R.string.wifi_signal_level_percent, progress);
        viewHolder.tvSignalLevelPercent.setText(text);
        int rssi = accessPoint.getRSSI();
        if (Integer.MAX_VALUE != rssi) {
            text = Integer.toString(rssi);
            if (!TextUtils.isEmpty(text))
                viewHolder.tvRSSI.setText(
                        viewHolder.itemView.getContext().getString(R.string.signal_strength, text));
            else
                viewHolder.tvRSSI.setText("");
        } else {
            viewHolder.tvRSSI.setText("");
        }

        Log.d(TAG, "------- onBindViewHolder() position = " + position + ", level = " + text);
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivSignal;
        final TextView tvSecurityAuthType;

        final TextView tvSSID;
        final TextView tvChannel;

        final TextView tvBSSID;
        final TextView tvGHz;

        final ProgressBar pbSignalLevel;
        final TextView tvSignalLevelPercent;
        final TextView tvRSSI;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);

            ivSignal = itemView.findViewById(R.id.iv_signal_strength);
            tvSecurityAuthType = itemView.findViewById(R.id.tv_security_auth_type);

            tvSSID = itemView.findViewById(R.id.tv_ssid);
            tvSSID.setSelected(true);
            tvChannel = itemView.findViewById(R.id.tv_channel);

            tvBSSID = itemView.findViewById(R.id.tv_bssid);
            tvGHz = itemView.findViewById(R.id.tv_ghz);

            pbSignalLevel = itemView.findViewById(R.id.pb_signal_level);
            tvSignalLevelPercent = itemView.findViewById(R.id.tv_signal_level_percent);
            tvRSSI = itemView.findViewById(R.id.tv_rssi);
        }
    }
}
