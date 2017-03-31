package com.transerve.testfirebase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.transerve.testfirebase.R;
import com.transerve.testfirebase.activity.ChatActivity;
import com.transerve.testfirebase.model.Chat;

import java.util.List;

/**
 * Created by Sujay on 10-03-2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private String TAG = ChatAdapter.class.getSimpleName();
    private static final int ANIMATED_ITEMS_COUNT = 2;
    private Context context;
    private List<Chat> chatList;
    private int lastPosition = -1;

    public ChatAdapter() {
    }

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    public void refresh(List<Chat> chatList) {
        this.chatList = chatList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        if (chat.getFrom_id() == ChatActivity.MY_ID) {
            holder.layoutOther.setVisibility(View.GONE);
            holder.layoutCurrent.setVisibility(View.VISIBLE);

            holder.tvCurrentUserName.setText(chat.getFrom_name());
            holder.tvCurrentMessage.setText(chat.getMessage());
            holder.tvCurrentDate.setReferenceTime(chat.getTimeLong());

            setAnimation(holder.itemView, position, true);

        } else {
            holder.layoutOther.setVisibility(View.VISIBLE);
            holder.layoutCurrent.setVisibility(View.GONE);

            holder.tvOtherUserName.setText(chat.getFrom_name());
            holder.tvOtherMessage.setText(chat.getMessage());
            holder.tvOtherDate.setReferenceTime(chat.getTimeLong());

            setAnimation(holder.itemView, position, false);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvOtherUserName, tvCurrentUserName, tvOtherMessage, tvCurrentMessage;
        protected RelativeTimeTextView tvOtherDate, tvCurrentDate;
        protected LinearLayout mRootLayout, layoutOther, layoutCurrent;

        public ViewHolder(View itemView) {
            super(itemView);
            mRootLayout = (LinearLayout) itemView.findViewById(R.id.mRootLayout);
            tvOtherMessage = (TextView) itemView.findViewById(R.id.tvOtherMessage);
            tvOtherUserName = (TextView) itemView.findViewById(R.id.tvOtherUserName);
            tvCurrentUserName = (TextView) itemView.findViewById(R.id.tvCurrentUserName);
            tvCurrentMessage = (TextView) itemView.findViewById(R.id.tvCurrentMessage);
            layoutOther = (LinearLayout) itemView.findViewById(R.id.layoutOther);
            layoutCurrent = (LinearLayout) itemView.findViewById(R.id.layoutCurrent);
            tvOtherDate = (RelativeTimeTextView) itemView.findViewById(R.id.tvOtherDate);
            tvCurrentDate = (RelativeTimeTextView) itemView.findViewById(R.id.tvCurrentDate);
        }
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position, boolean chk) {
        if (position > lastPosition) {
            Log.e(TAG, "setAnimation  position : " + position + ", lastPosition : " + lastPosition);
            Animation animation;
            if (chk) {
                animation = AnimationUtils.loadAnimation(context, R.anim.current_chat2);

            } else {
                animation = AnimationUtils.loadAnimation(context, R.anim.other_chat2);
            }
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.mRootLayout.clearAnimation();
        holder.itemView.clearAnimation();
    }
}
