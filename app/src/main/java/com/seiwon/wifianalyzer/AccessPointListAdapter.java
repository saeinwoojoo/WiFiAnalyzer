package com.seiwon.wifianalyzer;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import static com.seiwon.wifianalyzer.AccessPoint.NUMBER_OF_LEVELS;

public class AccessPointListAdapter extends BaseAdapter {

    private static final String TAG = "AccessPointListAdapter";

    private List<AccessPoint> mData;
    private int mResource;

    public AccessPointListAdapter(List<AccessPoint> data, @LayoutRes int resource) {
        mData = data;
        mResource = resource;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        if (null != mData)
            return mData.size();

        return 0;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        if (null != mData)
            return mData.get(position);

        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView)
            convertView = LayoutInflater.from(parent.getContext()).inflate(mResource, parent, false);

        ((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        bindView(position, convertView);

        return convertView;
    }

    private void bindView(int position, View view) {
        if (null == mData) {
            Log.e(TAG, "------- bindView(): data is null.");
            return;
        }

        final AccessPoint accessPoint = mData.get(position);
        if (null == accessPoint) {
            Log.e(TAG, "------- bindView(): data set at position " + position + " is null.");
            return;
        }

        CharSequence text;

        accessPoint.setSignalImage(view.findViewById(R.id.iv_signal_strength));

        TextView tvSecurityAuthType = view.findViewById(R.id.tv_security_auth_type);
        if (null != tvSecurityAuthType) {
            text = accessPoint.getSecurityString(view.getContext(), true);
            if (!TextUtils.isEmpty(text))
                tvSecurityAuthType.setText(text);
            else
                tvSecurityAuthType.setText("");
        }

        TextView tvSSID = view.findViewById(R.id.tv_ssid);
        if (null != tvSSID) {
            if (!TextUtils.isEmpty(accessPoint.ssid))
                tvSSID.setText(accessPoint.ssid);
            else
                tvSSID.setText("");
        }

        TextView tvChannel = view.findViewById(R.id.tv_channel);
        text = Integer.toString(AccessPoint.convertFrequencyToChannel(accessPoint.frequency));
        if (!TextUtils.isEmpty(text))
            tvChannel.setText(tvChannel.getContext().getString(R.string.wifi_channel, text));
        else
            tvChannel.setText("");

        TextView tvBSSID = view.findViewById(R.id.tv_bssid);
        if (null != tvBSSID) {
            if (!TextUtils.isEmpty(accessPoint.bssid))
                tvBSSID.setText(accessPoint.bssid);
            else
                tvBSSID.setText("");
        }

        TextView tvGHz = view.findViewById(R.id.tv_ghz);
        if (null != tvGHz) {
            String strGHz = AccessPoint.getGHzFromFrequency(accessPoint.frequency);
            if (TextUtils.isEmpty(strGHz)) {
                tvGHz.setVisibility(View.GONE);
            } else {
                tvGHz.setText(strGHz);
                tvGHz.setVisibility(View.VISIBLE);
            }
        }

        int progress = AccessPoint.getLevel(accessPoint.getRSSI(), NUMBER_OF_LEVELS);
        ProgressBar pbSignalLevel = view.findViewById(R.id.pb_signal_level);
        pbSignalLevel.setProgress(progress);
        TextView tvSignalLevelPercent = view.findViewById(R.id.tv_signal_level_percent);
        tvSignalLevelPercent.setText(tvSignalLevelPercent.getContext()
                .getString(R.string.wifi_signal_level_percent, progress));
        int rssi = accessPoint.getRSSI();
        TextView tvSignalStrength = view.findViewById(R.id.tv_rssi);
        if (null != tvSignalStrength) {
            if (Integer.MAX_VALUE != rssi) {
                text = Integer.toString(rssi);
                if (!TextUtils.isEmpty(text))
                    tvSignalStrength.setText(view.getContext().getString(R.string.signal_strength, text));
                else
                    tvSignalStrength.setText("");
            } else {
                tvSignalStrength.setText("");
            }
        }
    }
}
