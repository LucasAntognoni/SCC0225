package com.exemple.android.demo_jackson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //TODO 07: Remova o atributo tvDisplay e adicione um atributo para referênciar a RecyclerView criada
    RecyclerView clientList;
    OkHttpClient client;
    ObjectMapper mapper;

    //TODO 10: Mude o template da classe para retornar Contact[] ao invés de String
    public class ContactsFetcherTask extends AsyncTask<URL, Void, Contact[]> {

        //TODO 11: Mude a assinatura e o retorno do método doInBackground para que ela seja compatível como novo template da classe
        @Override
        protected Contact[] doInBackground(URL... params) {
            URL searchURL = params[0];
            Request request = new Request.Builder().url(searchURL).build();

            try {
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();

                Contact[] contacts = mapper.readValue(jsonString, Contact[].class);

                StringBuilder sb = new StringBuilder();
                for (Contact c : contacts) {
                    sb.append(c.getFirst_name());
                    sb.append(" ");
                    sb.append(c.getLast_name());
                    sb.append("\n");
                    sb.append(c.getPhone());
                    sb.append(" - ");
                    sb.append(c.getEmail());
                    sb.append("\n\n\n");
                }
                return contacts;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        //TODO 12: Mude a assinatura do método onPostExecute para que ela seja compatível como novo template da classe
        @Override
        protected void onPostExecute(Contact[] c) {
            //TODO 13: Remova o código referente ao tratando da String

            //TODO 14: Instancie um ContactListAdapter com o array de contatos recebido
            //TODO 15: Defina o ContactListAdapter instanciado no passo 14 como sendo o adaptador da RecyclerView

            ContactListAdapter cAdapter = new ContactListAdapter(c);
            clientList.setAdapter(cAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Not implemented yet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //TODO 08: Remova a busca pela TextView tvDisplay e adicione o equivalente para a RecyclerView
        clientList = (RecyclerView) findViewById(R.id.recyclerView);

        //TODO 09: Associe um LinearLayoutManager com a RecyclerView e defina outras configurações que julgar necessário, como FixedSize

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        clientList.setLayoutManager(layoutManager);

        client = new OkHttpClient();
        mapper = new ObjectMapper();

        fetchContacts();
    }

    private void fetchContacts() {
        URL url = HttpUrl.parse("https://contacts-test-api.herokuapp.com/contacts/").url();
        ContactsFetcherTask task = new ContactsFetcherTask();
        task.execute(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
