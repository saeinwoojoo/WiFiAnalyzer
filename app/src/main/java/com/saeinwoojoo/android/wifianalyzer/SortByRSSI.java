package com.saeinwoojoo.android.wifianalyzer;

import java.util.Comparator;

import static com.saeinwoojoo.android.wifianalyzer.AccessPoint.NUMBER_OF_LEVELS;

public class SortByRSSI implements Comparator<AccessPoint> {
    public int compare(AccessPoint ap1, AccessPoint ap2) {
        return AccessPoint.getLevel(ap2.getRSSI(), NUMBER_OF_LEVELS)
                - AccessPoint.getLevel(ap1.getRSSI(), NUMBER_OF_LEVELS);
    }
}
