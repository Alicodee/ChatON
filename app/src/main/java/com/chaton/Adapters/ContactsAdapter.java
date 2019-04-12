package com.chaton.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaton.Classes.GlobalClass;
import com.chaton.R;
import com.chaton.Classes.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsView> {
    private List<Users> objects;
    private Context context;

    public ContactsAdapter(List<Users> objects, Context context){
        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactsView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ContactsView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsView holder, int position) {
        Users user = objects.get(position);
        boolean isPhoto = user.getUrlPhoto() != null;
        String stringToUri;
        Uri uri;
        if(isPhoto){
            stringToUri = user.getUrlPhoto();
            uri = Uri.parse(stringToUri);
            Picasso.get().load(uri).placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_circle)
                    .into(holder.userImageView);
        }
        holder.userEmailTextView.setTypeface(GlobalClass.getThin(context));
        holder.userNameTextView.setTypeface(GlobalClass.getMed(context));
        holder.userEmailTextView.setText(user.getEmail());
        holder.userNameTextView.setText(user.getName());
        }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ContactsView extends RecyclerView.ViewHolder{
        ImageView userImageView ;
        TextView userNameTextView ;
        TextView userEmailTextView;
        public ContactsView(View itemView) {
            super(itemView);
            userImageView = (ImageView) itemView.findViewById(R.id.userImg);
            userNameTextView = (TextView) itemView.findViewById(R.id.userName);
            userEmailTextView = (TextView) itemView.findViewById(R.id.user_email);
        }
    }
    public void add(Users obj){
        objects.add(obj);
        notifyDataSetChanged();


    }
}
