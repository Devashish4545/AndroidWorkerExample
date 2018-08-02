/*
 * Copyright 2014 Eduardo Barrenechea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xrstudio.rxjava.demoappofworkmanager.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xrstudio.rxjava.demoappofworkmanager.MainActivity;
import com.xrstudio.rxjava.demoappofworkmanager.R;
import com.xrstudio.rxjava.demoappofworkmanager.db.MST_MESSAGE_TABLE;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


/**
 * This class is use for conversation adapter.
 */
public class ConversationAdapter extends RealmRecyclerViewAdapter<MST_MESSAGE_TABLE, RecyclerView.ViewHolder> {


    public static final int MSG_TEXT_SEND = 0;
    public static final int MSG_TEXT_RECV = 1;
    private LayoutInflater mInflater;
    private MainActivity mActivity;

    public ConversationAdapter(@Nullable OrderedRealmCollection<MST_MESSAGE_TABLE> data, boolean autoUpdate, MainActivity activity) {
        super(data, autoUpdate);

        mActivity = activity;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        MST_MESSAGE_TABLE single_chatModel = getItem(position);

        if (single_chatModel.getMessageDirection() == MSG_TEXT_SEND) {
            return Integer.valueOf(MSG_TEXT_SEND);
        } else {
            return Integer.valueOf(MSG_TEXT_RECV);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;
        switch (String.valueOf(viewType)) {
            case MSG_TEXT_SEND + "":
                v = mInflater.inflate(R.layout.chat_send_bubble_text, parent, false);
                return new Text_Msg_Holder(v);
            case MSG_TEXT_RECV + "":
                v = mInflater.inflate(R.layout.chat_rcv_bubble_text, parent, false);
                return new Text_Msg_Holder(v);
        }
        return new Text_Msg_Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final MST_MESSAGE_TABLE single_chatModel = getItem(holder.getAdapterPosition());
        Text_Msg_Holder textMsgHolder = (Text_Msg_Holder) holder;
        textMsgHolder.txt_receive_message.setText(single_chatModel.getMessage());
    }

    class Text_Msg_Holder extends RecyclerView.ViewHolder {
        TextView txt_receive_message;

        public Text_Msg_Holder(View itemView) {
            super(itemView);
            txt_receive_message = itemView.findViewById(R.id.txt_receive_message);
        }
    }

}
