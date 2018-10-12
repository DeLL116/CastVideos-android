/*
 * Copyright (C) 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.sample.cast.refplayer.browser;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.sample.cast.refplayer.R;

import com.androidquery.AQuery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * An {@link ArrayAdapter} to populate the list of videos.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private static final float ASPECT_RATIO = 9f / 16f;
    private final ItemClickListener mClickListener;
    private final Context mAppContext;
    private List<MediaInfo> videos;

    public VideoListAdapter(ItemClickListener clickListener, Context context) {
        mClickListener = clickListener;
        mAppContext = context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.browse_row, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final MediaInfo castSampleMediaInfo = videos.get(position);
        final MediaInfo overriddenWithLiveMediaInfo;
        if (position == 0) {

            String liveStreamOverrideUri;

            // Stadium Linear LiveStream Sample
            liveStreamOverrideUri = "https://d28avce4cnwu2y.cloudfront.net/v1/master/61a556f78e4547c8ab5c6297ea291d6350767ca2/Mux/hls/live/522512/mux_4/master.m3u8?ads.an=Stadium&ads.rdid=8ff0d128-b056-4b5e-ae7d-83d3200e825a&ads.token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.IntcInVzZXJfaWRcIjowLFwicHJvZmlsZV9pZFwiOjAsXCJkZXZpY2VfaWRcIjowLFwiZXhwaXJlc1wiOjE1NzAxNDQwMDksXCJpYXRcIjoxNTM4NjA4MDA5LFwidW5pcXVlXCI6XCI1YmI1NGI4OWU2MWRkXCIsXCJwcmVmaXhcIjpcIkdUX1wifSI.lLNKe6df386yVIWTGa0uCrebS9O69JHV5o2ki6VxCqA&ads.gdfp_req=1&ads.ad_rule=0&ads.correlator=1538609789&ads.output=vast&ads.cmsid=2455200&ads.unviewed_position_start=1&ads.impl=s&ads.env=vp&ads.vid=70987&ads.idtype=adid&ads.ppid=1e55c2e40caf708799ff26eb975c3ef3512e8dd5&ads.is_lat=0&ads.sz=640x480&ads.msid=com.watchstadium&ads.iu=%2F32984737%2FWatchStadium%2Flive%2Flinear%2Fmid";

            // Rando HLS LiveStream from https://azure.microsoft.com/en-us/blog/live-247-reference-streams-available/
//             liveStreamOverrideUri = "http://b028.wpc.azureedge.net/80B028/Samples/a38e6323-95e9-4f1f-9b38-75eba91704e4/5f2ce531-d508-49fb-8152-647eba422aec.ism/Manifest(format=m3u8-aapl-v3)";

            // Rando HLS LiveStream from https://azure.microsoft.com/en-us/blog/live-247-reference-streams-available/
//            liveStreamOverrideUri = "http://b028.wpc.azureedge.net/80B028/SampleStream/595d6b9a-d98e-4381-86a3-cb93664479c2/b722b983-af65-4bb3-950a-18dded2b7c9b.ism/Manifest(format=m3u8-aapl-v3)";

            overriddenWithLiveMediaInfo = new MediaInfo.Builder(liveStreamOverrideUri)
                    .setContentType("application/x-mpegurl")
                    .setStreamType(MediaInfo.STREAM_TYPE_LIVE)
                    .setStreamDuration(1000 * 60)
                    .setMetadata(castSampleMediaInfo.getMetadata())
                    .setCustomData(castSampleMediaInfo.getCustomData())
                    .build();
        } else {
            overriddenWithLiveMediaInfo = castSampleMediaInfo;
        }

        MediaMetadata mm = overriddenWithLiveMediaInfo.getMetadata();
        viewHolder.setTitle(mm.getString(MediaMetadata.KEY_TITLE));
        viewHolder.setDescription(mm.getString(MediaMetadata.KEY_SUBTITLE));
        viewHolder.setImage(mm.getImages().get(0).getUrl().toString());

        viewHolder.mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.itemClicked(view, overriddenWithLiveMediaInfo, position);
            }
        });
        viewHolder.mImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.itemClicked(view, overriddenWithLiveMediaInfo, position);
            }
        });

        viewHolder.mTextContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.itemClicked(view, overriddenWithLiveMediaInfo, position);
            }
        });
        CastSession castSession = CastContext.getSharedInstance(mAppContext).getSessionManager()
                .getCurrentCastSession();
        viewHolder.mMenu.setVisibility(
                (castSession != null && castSession.isConnected()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 : videos.size();
    }

    /**
     * A {@link android.support.v7.widget.RecyclerView.ViewHolder} that displays a single video in
     * the video list.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View mParent;
        private final View mMenu;
        private final View mTextContainer;
        private AQuery mAquery;
        private TextView mTitleView;
        private TextView mDescriptionView;
        private ImageView mImgView;

        public static ViewHolder newInstance(View parent) {
            ImageView imgView = (ImageView) parent.findViewById(R.id.imageView1);
            TextView titleView = (TextView) parent.findViewById(R.id.textView1);
            TextView descriptionView = (TextView) parent.findViewById(R.id.textView2);
            View menu = parent.findViewById(R.id.menu);
            View textContainer = parent.findViewById(R.id.text_container);
            AQuery aQuery = new AQuery(parent);
            return new ViewHolder(parent, imgView, textContainer, titleView, descriptionView, menu,
                    aQuery);
        }

        private ViewHolder(View parent, ImageView imgView, View textContainer, TextView titleView,
                TextView descriptionView, View menu, AQuery aQuery) {
            super(parent);
            mParent = parent;
            mImgView = imgView;
            mTextContainer = textContainer;
            mMenu = menu;
            mTitleView = titleView;
            mDescriptionView = descriptionView;
            mAquery = aQuery;
        }

        public void setTitle(String title) {
            mTitleView.setText(title);
        }

        public void setDescription(String description) {
            mDescriptionView.setText(description);
        }

        public void setImage(String imgUrl) {
            mAquery.id(mImgView).width(114).image(imgUrl,
                    true, true, 0, R.drawable.default_video, null, 0, ASPECT_RATIO);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mParent.setOnClickListener(listener);
        }

        public ImageView getImageView() {
            return mImgView;
        }
    }

    public void setData(List<MediaInfo> data) {
        videos = data;
        notifyDataSetChanged();
    }

    /**
     * A listener called when an item is clicked in the video list.
     */
    public interface ItemClickListener {

        void itemClicked(View view, MediaInfo item, int position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
