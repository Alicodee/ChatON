package com.chaton.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chaton.Classes.FriendlyMessage;
import com.chaton.Classes.GlobalClass;
import com.chaton.R;
import com.firebase.client.collection.LLRBNode;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
   private  List<FriendlyMessage> objects;
   private Context context;
    public MessageAdapter(List<FriendlyMessage> objects,Context context) {
      this.objects = objects;
      this.context = context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FriendlyMessage message = (FriendlyMessage) objects.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }


    @Override
    public int getItemViewType(int position) {
        FriendlyMessage message = (FriendlyMessage) objects.get(position);

        if (message.getEmailNode().equals(GlobalClass.emailNode)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }




    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MessageView extends  RecyclerView.ViewHolder {
        ImageView photoImageView ;
        TextView messageTextView ;
        TextView authorTextView;
        public MessageView(View itemView) {
            super(itemView);
            photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
             authorTextView = (TextView) itemView.findViewById(R.id.nameTextView);
        }

    }
    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView messagePhoto;

        SentMessageHolder(View itemView) {
            super(itemView);
            messagePhoto = (ImageView) itemView.findViewById(R.id.photoImageView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            messageText.setTypeface(GlobalClass.getMed(context));
        }

        void bind(FriendlyMessage message) {

            boolean isPhoto = message.getPhotoUrl() != null;

            if (isPhoto) {
                messageText.setVisibility(View.GONE);
                messagePhoto.setVisibility(View.VISIBLE);
                Glide.with(messagePhoto.getContext())
                        .load(message.getPhotoUrl())
                        .into(messagePhoto);
            } else {
                messageText.setVisibility(View.VISIBLE);
                messagePhoto.setVisibility(View.GONE);
                messageText.setText(message.getText());
            }
            // Format the stored timestamp into a readable String using method.
            if(message.getTime() != null){
                timeText.setText(message.getTime());
            }

        }
    }
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage,messagePhoto;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messagePhoto = (ImageView) itemView.findViewById(R.id.photoImageView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(FriendlyMessage message) {
            messageText.setTypeface(GlobalClass.getMed(context));
            nameText.setTypeface(GlobalClass.getMed(context));
            Boolean isDP = message.getDp() != null;
            boolean isPhoto = message.getPhotoUrl() != null;
            if (isPhoto) {
                messageText.setVisibility(View.GONE);
                messagePhoto.setVisibility(View.VISIBLE);
                Glide.with(messagePhoto.getContext())
                        .load(message.getPhotoUrl())
                        .into(messagePhoto);
            } else {
                messageText.setVisibility(View.VISIBLE);
                messagePhoto.setVisibility(View.GONE);
                messageText.setText(message.getText());
            }
          if(isDP){
              Glide.with(profileImage.getContext())
                      .load(message.getDp())
                      .into(profileImage);
          }
            nameText.setText(message.getName());

            // Format the stored timestamp into a readable String using method.
            if(message.getTime() != null){
                timeText.setText(message.getTime());
            }

            // Insert the profile image from the URL into the ImageView.
            //  Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    public void add(FriendlyMessage obj){
        objects.add(obj);
        notifyDataSetChanged();


    }
}
