package com.exemple.android.crud_exercicio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SyncRequest;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.os.AsyncTask;;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * TODO 01: Faça a MainActivity implementar a interface LoaderManager.LoaderCallbacks<Contact[]>
 * OBS: Os métodos necessários para implementar está interface já foram criados.
 */

/**
 * TODO 34: Faça a MainActivity implementar a interface ContactListAdapter.OnClickListenerHandler
 * OBS: O método necessário para implementar está inferface já foi criado.
 */
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Contact[]>,
        ContactListAdapter.OnClickListenerHandler {

    RecyclerView rvContants;
    OkHttpClient client;
    ObjectMapper mapper;

    /**
     * TODO 02: Crie uma constante estática do tipo inteiro para ser o ID do Loader
     */
    private static final int CONTACTS_LOADER = 25;

    /**
     * TODO 03: Crie uma constante estática do tipo String para ser chave do objeto Contact que
     * será passado via Bundle para o Loader.
     */
    public static final String EXTRA_URL = "EXTRA_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvContants = (RecyclerView) findViewById(R.id.rvContants);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvContants.setLayoutManager(layoutManager);
        rvContants.setHasFixedSize(true);

        client = new OkHttpClient();
        mapper = new ObjectMapper();

        /**
         * TODO 04: Inicie o LoadManager com o Bundle sendo inicialmente null.
         */
        getSupportLoaderManager().initLoader(MainActivity.CONTACTS_LOADER, null, this);
        fetchContacts();
    }

    /**
     * TODO 05: Remova os comentário das tags @Override dos métodos onCreateLoader, onLoadFinished e onLoaderReset
     * TODO 06: Faça o paramêtro Bundle Args final, para que ele possa ser acessado por inner-classes.
     */
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Contact[]> onCreateLoader(int id, final Bundle args) {
        /**
         * TODO 07: Crie e retorne uma instância da classe anônima AsyncTaskLoader<Contact[]>
         *     Dica: Lembre-se do ";" no final da declaração da classe
         *     Dica: Recorte e cole a declaração de ContactsFetcherTask aqui.
         */
        return new AsyncTaskLoader<Contact[]>(this) {
            /**
             * TODO 08: Crie o atributo cachedContacts, do tipo Contact[]
             */
            Contact[] cachedContacts;
            /**
             *  TODO 09: Crie o atributo cacheDate, do tipo Date.
             */
            Date cacheDate;

            /**
             * TODO 10: Implemente o método onStartLoading()
             */
            @Override
            protected void onStartLoading() {

                /**
                 * TODO 11: Em onStartLoading: Se o parâmetro args for nulo, apenas retorne.
                 */
                if (args == null) {
                    return;
                }

                /**
                 * TODO 12: Em onStartLoading: Verifique se os atributos cachedContacts e cacheDate são nulos.
                 *      Se não forem, verifique se a diferença entre a data atual e a data armazenada em
                 *      cacheDate são muito diferentes (5000 milisegundos)
                 *      Se não forem, chame deliveryResults, passando os dados dos contatos armazenados em
                 *      cache e retorne.
                 */
                if (cachedContacts != null && cacheDate != null) {

                    Date currentDate = new Date();
                    long diff = currentDate.getTime() - cacheDate.getTime();

                    if (diff < 5000) {
                        deliverResult(cachedContacts);
                        return;
                    }
                }

                /**
                 * TODO 13: Em onStartLoading: chame o método forceLoad()
                 */
                forceLoad();
            }

            /**
             * TODO 14: Transforme a assinatura do método doInBackground para transformá-lo em loadinBackground()
             */
            @Override
            public Contact[] loadInBackground() {
                /**
                 * TODO 15: Remova a linha abaixo em que era buscado a URL por meio do parâmetro params
                 * (que não existe mais)
                 */
                //URL searchURL = params[0];

                /**
                 * TODO 16: Obtenha a "URL" por meio do Bundle args e a armazene em uma variável do tipo String.
                 */
                String searchURL = args.getString(EXTRA_URL);
                Request request = new Request.Builder().url(searchURL).build();

                try {
                    Response response = client.newCall(request).execute();
                    String jsonString = response.body().string();

                    Contact[] contacts = mapper.readValue(jsonString, Contact[].class);

                    /**
                     * TODO 17: Armazene a data atual no atributo cacheDate, e o contatos obtidos no atributo cahedContacts
                     */
                    cachedContacts = contacts;
                    cacheDate = new Date();

                    return contacts;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Contact[]> loader, Contact[] contacts) {
        /**
         * TODO 18: Copie o contéudo do método onPostExecute aqui e em seguida apague onPostExecute.
         */
        if (contacts != null) {
            /**
             * TODO 36: Ao instanciar o ContactListAdapter, passe como parâmetro uma referência
             * para um OnClickListenerHandler (no caso, a própria activity).
             */
            ContactListAdapter adapter = new ContactListAdapter(contacts, this);
            rvContants.setAdapter(adapter);
        }

    }

    @Override
    public void onLoaderReset(Loader<Contact[]> loader) {
        /*
         * We aren't using this method in our example application, but we are required to Override
         * it to implement the LoaderCallbacks<Contact> interface
         */
    }

    public class ContactsFetcherTask extends AsyncTask<URL, Void, Contact[]> {

        /**
         * TODO 08: Crie o atributo cachedContacts, do tipo Contact[]
         */

        /**
         *  TODO 09: Crie o atributo cacheDate, do tipo Date.
         */

        /**
         * TODO 10: Implemente o método onStartLoading()
         */

        /**
         * TODO 11: Em onStartLoading: Se o parâmetro args for nulo, apenas retorne.
         */

        /**
         * TODO 12: Em onStartLoading: Verifique se os atributos cachedContacts e cacheDate são nulos.
         *      Se não forem, verifique se a diferença entre a data atual e a data armazenada em
         *      cacheDate são muito diferentes (5000 milisegundos)
         *      Se não forem, chame deliveryResults, passando os dados dos contatos armazenados em
         *      cache e retorne.
         */

        /**
         * TODO 13: Em onStartLoading: chame o método forceLoad()
         */

        /**
         * TODO 14: Transforme a assinatura do método doInBackground para transformá-lo em loadinBackground()
         */
        @Override
        protected Contact[] doInBackground(URL... params) {
            /**
             * TODO 15: Remova a linha abaixo em que era buscado a URL por meio do parâmetro params
             * (que não existe mais)
             */
            URL searchURL = params[0];

            /**
             * TODO 16: Obtenha a "URL" por meio do Bundle args e a armazene em uma variável do tipo String.
             */

            Request request = new Request.Builder().url(searchURL).build();

            try {
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();

                Contact[] contacts = mapper.readValue(jsonString, Contact[].class);

                /**
                 * TODO 17: Armazene a data atual no atributo cacheDate, e o contatos obtidos no atributo cahedContacts
                 */

                return contacts;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Contact[] contacts) {
            if (contacts != null) {
                /**
                 * TODO 36: Ao instanciar o ContactListAdapter, passe como parâmetro uma referência
                 * para um OnClickListenerHandler (no caso, a própria activity).
                 */
                ContactListAdapter adapter = new ContactListAdapter(contacts);
                rvContants.setAdapter(adapter);
            }
        }
    }


    public void onClickFloatingButton(View view) {
        Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void fetchContacts() {
        /*
         * Ao invés de fazer o parse da URL aqui, foi criando uma nossa classe, WebServiceUtils, que
         * ajuda a gerar URL compatíveis com o webservice.
         */
        URL url = WebServiceUtil.WEBSERVICE_URL;

        /**
         * TODO 19: Remova as duas linhas abaixo.
         */
        //ContactsFetcherTask task = new ContactsFetcherTask();
        //task.execute(url);

        /**
         * TODO 20: Criei um objeto do tipo Bundle
         */
        Bundle fetcherBundle = new Bundle();

        /**
         * TODO 21: Coloque o valor da variável url (transforamda em string) no bundle via putString
         */
        fetcherBundle.putString(EXTRA_URL, url.toString());

        /**
         * TODO 22: Obtenha uma referêcia para o LoaderManager e a armazene em uma variável.
         */
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Contact[]> contactsLoader = loaderManager.getLoader(MainActivity.CONTACTS_LOADER);

        /**
         * TODO 23: Verifique se o Loader com o ID definido no passo 2 é nulo.
         *  Se for, inicie-o através do método initLoader do LoaderManager
         *  Se não, reinicie-o através do méotdo restartLoader do LoaderManager.
         */
        if (contactsLoader == null) {
            loaderManager.initLoader(CONTACTS_LOADER, fetcherBundle, this);
        } else {
            loaderManager.restartLoader(CONTACTS_LOADER, fetcherBundle, this);
        }

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

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchContacts();
    }

    /**
     * TODO 35: Remova o comentário da tag  @Override do método onClick
     */
    @Override
    public void onClick(Contact contact) {
        /**
         * TODO 66: Remova o Toast e criei um Intent para iniciar a ContactDetailActivity, passando contact via putExtra.
         */

        Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
        intent.putExtra("EXTRA_CONTACT", contact);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
