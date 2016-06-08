package com.cocorporation.hueadvanced.core.lightgroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.cocorporation.hueadvanced.R;
import com.cocorporation.hueadvanced.model.Light;
import com.cocorporation.hueadvanced.events.LightsLoadedEvent;
import com.cocorporation.hueadvanced.service.LightService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Corentin on 1/12/2016.
 */
public class LightGroupsListFragment extends Fragment {
    public static final String TAG = "LightGroupsListFragment";

    private EventBus bus;

    private RecyclerView mLightsRecyclerView;
    private LightsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light_groups_list, container, false);

        mLightsRecyclerView = (RecyclerView) view.findViewById(R.id.light_list_recycler_view);
        mLightsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getBus().register(this);

        LightService.getLights(getBus());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(LightsLoadedEvent event) {
        Log.d(TAG, "updateUI");
        //LightManager lightManager = LightManager.get(getActivity());
        //List<Light> lights = new ArrayList<Light>(); //lightManager.getAllLights();//TODO: here, will need to get only the lights of the group

        List<Light> lights = event.getLights();
        if (mAdapter == null) {
            mAdapter = new LightsAdapter(lights);
            mLightsRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class LightHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mNameTextView;
        private TextView mDescriptionTextView;
        private Switch mOnOffSwitch;

        private Light mLight;

        public LightHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_light_name_text_view);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.list_item_light_description_text_view);
            mOnOffSwitch = (Switch) itemView.findViewById(R.id.list_item_light_on_switch);

            mOnOffSwitch.setOnClickListener(this);
        }

        public void bindLight(Light light) {
            mLight = light;
            mNameTextView.setText(mLight.getName());
            mDescriptionTextView.setText(mLight.getManufacturername());
            mOnOffSwitch.setChecked(light.getState().getOn());
        }

        @Override
        public void onClick(View v) {
            if (v == mOnOffSwitch) {
                Switch sw = (Switch)v;
                LightService.turnOnOffLight(mLight.getLightId(), sw.isChecked());

                /*
                if (sw.isChecked()) {
                    //LightManager.get().switchOnMax(mLight);
                } else {
                    //LightManager.get().switchOff(mLight);
                }
                */
            }
        }
    }

    private class LightsAdapter extends RecyclerView.Adapter<LightHolder> {

        private List<Light> mLights;

        public LightsAdapter(List<Light> lights) {
            mLights = lights;
        }

        @Override
        public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.light_groups_item, parent, false);
            return new LightHolder(view);
        }

        @Override
        public void onBindViewHolder(LightHolder holder, int position) {
            Light light = mLights.get(position);
            holder.bindLight(light);
        }

        @Override
        public int getItemCount() {
            return mLights.size();
        }
    }

    private EventBus getBus() {
        if (bus == null) {
            bus =  EventBus.getDefault();
        }
        return bus;
    }
}
