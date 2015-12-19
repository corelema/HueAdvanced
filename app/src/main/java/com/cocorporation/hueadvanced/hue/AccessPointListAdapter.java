package com.cocorporation.hueadvanced.hue;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cocorporation.hueadvanced.R;
import com.philips.lighting.hue.sdk.PHAccessPoint;

import java.util.List;

/**
 * Created by Corentin on 12/12/2015.
 */
public class AccessPointListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<PHAccessPoint> accessPoints;

    class BridgeListItem {
        private TextView bridgeIp;
        private TextView bridgeMac;
    }

    /**
     * creates instance of {@link AccessPointListAdapter} class.
     *
     * @param context           the Context object.
     * @param accessPoints      an array list of {@link PHAccessPoint} object to display.
     */
    public AccessPointListAdapter(Context context, List<PHAccessPoint> accessPoints) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        this.accessPoints = accessPoints;
    }

    @Override
    public int getCount() {
        return accessPoints.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BridgeListItem item;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.select_bridge_item, null);

            item = new BridgeListItem();
            item.bridgeMac = (TextView) convertView.findViewById(R.id.bridge_mac);
            item.bridgeIp = (TextView) convertView.findViewById(R.id.bridge_ip);

            convertView.setTag(item);
        } else {
            item = (BridgeListItem) convertView.getTag();
        }
        PHAccessPoint accessPoint = accessPoints.get(position);
        item.bridgeIp.setTextColor(Color.BLACK);
        item.bridgeIp.setText(accessPoint.getIpAddress());
        item.bridgeMac.setTextColor(Color.DKGRAY);
        item.bridgeMac.setText(accessPoint.getMacAddress());

        return convertView;
    }

    /**
     * Update date of the list view and refresh listview.
     *
     * @param accessPoints      An array list of {@link PHAccessPoint} objects.
     */
    public void updateData(List<PHAccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
        notifyDataSetChanged();
    }
}
