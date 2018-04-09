package com.exemple.android.crud_exercicio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * TODO 47: Faça a activity implementar LoaderManager.LoaderCallbacks<Contact>;
 * OBS: Os métodos necessários já estão criados.
 */

public class AddContactActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Contact> {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * TODO 36: Crie atributos para armazenar referências para os views do Layout.
     */
    EditText txtFirstName, txtLastName, txtEmail, txtPhone;
    Button btnAdd;

    /**
     * TODO 38: Crei um identificador para o load manager
     */
    private static final int POST_LOADER = 22;

    /**
     * TODO 38: Crei uma chave para o objeto Contact que será passado via bundle para o Load Manager.
     */
    private static final  String EXTRA_CONTACT = "EXTRA_CONTACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        /**
         * TODO 37: Obetenha referências para os objetos view do Layout e as armazen nos campos criados no passo 36
         */
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPhone = (EditText) findViewById(R.id.txtPhone);

        btnAdd = (Button) findViewById(R.id.btnAdd);

        /**
         * TODO 39: Inicie o LoaderManager com um bundle nulo.
         */
        getSupportLoaderManager().initLoader(POST_LOADER, null, this);
    }

    public void onClickBtnAdd(View view) {

        /**
         * TODO 40: Desabilite o botão usado para iniciar a ação de adicionar um novo contato.
         */
        btnAdd.setEnabled(false);
        /**
         * TODO 41: instancie um novo objeto do tipo Contact, e sete first_name, last_name, email
         * e phone de acordo com o informado pelo usuário nas EditTexts.
         */
        Contact c = new Contact();
        c.setFirst_name(txtFirstName.getText().toString());
        c.setLast_name(txtLastName.getText().toString());
        c.setEmail(txtEmail.getText().toString());
        c.setPhone(txtPhone.getText().toString());


        /**
         * TODO 44: Instancie um objeto do tipo Bundle.
         */
        Bundle postBundle = new Bundle();

        /**
         * TODO 45: Coloque no Bundle o Contact instanciado no passo 44.
         */
        postBundle.putParcelable(EXTRA_CONTACT, c);

        /**
         * TODO 46: Obtenha a instância para o LoaderManager e para o Loader, e inicie (ou reiniciei)
         * o Loader passando o bundle criado, e a própria activity como listener.
         */
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Contact> postLoader = loaderManager.getLoader(POST_LOADER);

        if (postBundle == null) {
            loaderManager.initLoader(POST_LOADER, postBundle, this);
        } else {
            loaderManager.restartLoader(POST_LOADER, postBundle, this);
        }
    }

    /**
     * TODO 48: Remove a comentário das tags @Override dos métodos onCreateLoader, onLoadFinished e onLoaderReset
     * TODO 49: Faça o paramêtro Bundle args do método onCreateLoder final.
     */

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Contact> onCreateLoader(int id, final Bundle args) {
        /**
         * TODO 50: Instancie e retorne um objeto de uma classe anônima que implemente a inferface AsyncTaskLoader<Contact>.
         * TODO 51: Implemente o mêtodo onStartLoading, que chama forceLoad caso o parâmetro args não seja null.
         * TODO 52: Implemente o mêtodo loadInBackground.
         * TODO 52: Em loadInBackground: Obtenha o objeto Contact atravez do Bundle args
         * TODO 53: Convertá-lo para uma string Json através de uma instância de ObjectMapper
         * TODO 54: Crie um RequestBody, informando o String JSON e o campo JSON da classe AddContactActivity como MINEType.
         * TODO 55: Crie uma Request do tipo post, passando o RequestBody criado.
         * TODO 56: Instancia um objeto do tipo OkHttpClient e execute a Request criada.
         * TODO 57: Verifique se a Response produzida foi bem sucedida (método isSuccessful()).
         * TODO 58: Se for bem sucedida, utilize a instância de ObjectMapper para transformar o corpo da resposta em um objeto Contact.
         * TODO 59: Se tudo deu certo, retorne a instância de Contact obtida no passo 58
         * TODO 60: Caso contrário, retorne null.
         */
        return new AsyncTaskLoader<Contact>(this) {

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                forceLoad();
            }

            @Override
            public Contact loadInBackground() {

                Contact contact = args.getParcelable(EXTRA_CONTACT);

                if (contact == null) {
                    return null;
                }

                ObjectMapper mapper = new ObjectMapper();

                try {
                    String jsonString = mapper.writeValueAsString(contact);
                    OkHttpClient client = new OkHttpClient();

                    RequestBody body = RequestBody.create(JSON, jsonString);
                    Request request = new Request.Builder().url(WebServiceUtil.WEBSERVICE_URL).post(body).build();

                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        Contact newContact = mapper.readValue(
                                response.body().string(),
                                Contact.class);

                        return contact;
                    }

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Contact> loader, Contact contact) {
        /**
         * TODO 64: Verifique se contact é nulo (o que sinaliza um erro) ou não e emita mensagens apropriadas através de Toats.
         */
        if (contact == null) {
            Toast.makeText(
                    getApplicationContext(),
                    "Error adding contact",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Contact added",
                    Toast.LENGTH_LONG
            ).show();
        }
        /**
         * TODO 65: Feche a Activity atual, voltando para a Activity anterior pelo método finish().
         */
        finish();

    }

    @Override
    public void onLoaderReset(Loader<Contact> loader) {
        /*
         * Este método não será usado mais precisamos implementá-lo
         */
    }
}
