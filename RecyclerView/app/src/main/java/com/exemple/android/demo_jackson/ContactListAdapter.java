package com.exemple.android.demo_jackson;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Caio on 14/03/2018.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    Contact[] contacts;

    public ContactListAdapter(Contact[] contacts) {
        this.contacts = contacts;
    }

    //TODO 06: Implemente o método onCreateViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.contact_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContact(contacts[position]);
    }

    @Override
    public int getItemCount() {
        return contacts.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //TODO 03: Crie os atributos que irão referênciar as TextView do Layout contact_item.xml

        TextView firstName;
        TextView lastName;
        TextView email;
        TextView phone;

        public ViewHolder(View itemView) {
            super(itemView);
            //TODO 04: Associe os objetos criados pelo contact_item.xml com os atributos criados no passo 03

            firstName = (TextView) itemView.findViewById(R.id.firstName);
            lastName = (TextView) itemView.findViewById(R.id.lastName);
            email = (TextView) itemView.findViewById(R.id.email);
            phone = (TextView) itemView.findViewById(R.id.phone);
        }

        public void setContact(Contact c) {
            //TODO 05: Faça o bind do dados do Contact c passado como paramêtro os atributos TextView

            firstName.setText(String.valueOf(c.first_name));
            lastName.setText(String.valueOf(c.last_name));
            email.setText(String.valueOf(c.email));
            phone.setText(String.valueOf(c.phone));
        }

    }
}
