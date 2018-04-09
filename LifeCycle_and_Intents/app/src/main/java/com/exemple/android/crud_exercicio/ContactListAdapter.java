package com.exemple.android.crud_exercicio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    public ContactListAdapter(Contact[] contacts) {
    }

    /**
     * TODO 24: Criei uma interface chamada OnClickListenerHandler
     * TODO 25: Defina um método onClick, que recebe como parâmetro um objeto do tipo Contact, na interface criada no passo 24
     */
    public interface OnClickListenerHandler {
        void onClick(Contact contact);
    }

    Contact[] contacts;

    /**
     * TODO 26: Defina um campo do tipo OnClickListenerHandler para armazenar a instância que irá receber as notificações de click.
     */
    OnClickListenerHandler clickHandler;

    /**
     * TODO 27: Modifique o construtor para que ele receba como parâmetro, além do array de Contatos,
     * uma instância de OnClickListenerHandler. Armazene essa instância no campo que foi criado
     * no passo 26.
     */
    public ContactListAdapter(Contact[] contacts, OnClickListenerHandler clickHandler) {
        this.contacts = contacts;
        this.clickHandler = clickHandler;
    }

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

    /**
     * TODO 28: Faça a classe ViewHolder implementar a interface View.OnClickListener
     * OBS: O método necessário para implementar essa interface já foi criado.
     */
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView tvFirstName, tvLastName, tvPhone, tvEmail;


        /**
         * TODO 29: Crie um atributo do tipo Contact.
         */
        Contact contact;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFirstName = (TextView) itemView.findViewById(R.id.tvFirstName);
            tvLastName = (TextView) itemView.findViewById(R.id.tvLastName);
            tvPhone = (TextView) itemView.findViewById(R.id.tvPhone);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            /**
             * TODO 30: chame o método setOnClickListener do objeto itemView, setando a própria instância de ViewHolder como a listener.
             */
            itemView.setOnClickListener(this);

        }

        public void setContact(Contact c) {
            tvFirstName.setText(c.getFirst_name());
            tvLastName.setText(c.getLast_name());
            tvEmail.setText(c.getEmail());
            tvPhone.setText(c.getPhone());

            /**
             * TODO 31: armazene o contato recebido como parâmetro no atributo criado no passo 29.
             */
            contact = c;
        }

        /**
         * TODO 32: Remove o comentário da tag @Override
         */

        @Override
        public void onClick(View v) {
            /**
             * TODO 33: Chame o método onClick da instância de OnClickListenerHandler armazenado no
             * atributo definido no passo 26, passando como parâmetro a instância de Contact do atributo
             * definido no passo 29.
             */
            clickHandler.onClick(contact);
        }

    }
}
