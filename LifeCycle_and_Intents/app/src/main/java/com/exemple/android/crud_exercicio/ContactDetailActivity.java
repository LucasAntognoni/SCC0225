package com.exemple.android.crud_exercicio;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
 * TODO 73: Faça ContactDetailActivity Extender LoaderManager.LoaderCallbacks<Contact>.
 * Os métodos desta interface já foram implementados, apenas descomente suas tags @Override
 */
public class ContactDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Contact> {

    EditText txtFirstName, txtLastName, txtEmail, txtPhone;
    Button btnSave, btnCancel, btnEdit, btnDelete, btnSendEmail, btnCall;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * TODO 67: Criei um atributo para armazenar o contato atual.
     */
    Contact contact;

    /**
     * TODO 74: Crie uma chave String para representar o Contact que será passado para os Loaders via Bundle.
     */
    private static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    /**
     * TODO 75: Crie identificadores inteiros para dois Loaders diferentes, um para a operação de Update, e outro para a operação de Delete.
     */
    private static final int UPDATE_LOADER = 20;
    private static final int DELETE_LOADER = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPhone = (EditText) findViewById(R.id.txtPhone);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSendEmail = (Button) findViewById(R.id.btnSendEmail);
        btnCall = (Button) findViewById(R.id.btnCall);

        /**
         * TODO 68: Recupere o Contact passado como parâmetro pela Intent pai e salve no atributo criado no passo 67
         */
        contact = getIntent().getParcelableExtra(EXTRA_CONTACT);

        /**
         * TODO 70: Chame o método criado no passo 69 para setar os valores dos EditText
         */
        setEditTexts(contact);
        /**
         * TODO 76: Inicie os dois Loaders, com os IDS defininos no passo 75.
         */
        getSupportLoaderManager().initLoader(UPDATE_LOADER, null, this);
        getSupportLoaderManager().initLoader(DELETE_LOADER, null, this);
    }

    /**
     * TODO 69: Crie um método que irá setar os EditText de acordo com os valores de um objeto Contact passado como parâmetro
     */
    public void setEditTexts(Contact contact) {
        txtFirstName.setText(contact.first_name);
        txtLastName.setText(contact.last_name);
        txtEmail.setText(contact.email);
        txtPhone.setText(contact.phone);
    }

    /**
     * Método usado para "travar" os campos, de modo a não permitir edição em momentos não apropriados
     */
    public void lockEditTexts() {
        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPhone.setEnabled(false);

        btnSendEmail.setVisibility(View.VISIBLE);
        btnCall.setVisibility(View.VISIBLE);
    }

    /**
     * Método usado para "liberar" os campos, de modo a permitir edição
     */
    public void unlockEditTexts() {
        txtFirstName.setEnabled(true);
        txtLastName.setEnabled(true);
        txtEmail.setEnabled(true);
        txtPhone.setEnabled(true);

        btnSendEmail.setVisibility(View.INVISIBLE);
        btnCall.setVisibility(View.INVISIBLE);
    }

    public void onClickBtnSendEmail(View view) {
        /**
         * TODO 71: Crei uma intent para enviar um e-mail para o Contact atual, armazenado na instância criada no passo 67.
         * DICA: Use a ação ACTION_SEND, descrita na documentação:
         * https://developer.android.com/guide/components/intents-common.html
         */

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, contact.email);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onClickBtnCall(View view) {
        /**
         * TODO 72: Crei uma intent para ligar para o Contact atual, armazenado na instância criada no passo 67.
         * DICA: Use a ação ACTION_DIAL, descrita na documentação:
         * https://developer.android.com/guide/components/intents-common.html
         */

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contact.phone));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    public void onClickbtnSave(View view) {
        lockEditTexts();

        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);

        /**
         * TODO 77: Altere os valores do objeto Contact definido no passo 67 para refletir os EditTexts atuais.
         */
        contact.first_name = txtFirstName.getText().toString();
        contact.last_name = txtLastName.getText().toString();
        contact.email = txtEmail.getText().toString();
        contact.phone = txtPhone.getText().toString();

        /**
         * TODO 78: Crie um objeto Bundle e coloque nele o contato defenido no passo 67.
         */
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONTACT, contact);


        /**
         * TODO 79: Inicie, ou reinicie, o Loader referente a operação de Update com o ID definido no passo 75, passando o Bundle criado no passo 78
         */

        if (bundle == null) {
            getSupportLoaderManager().initLoader(UPDATE_LOADER, bundle, this);
        } else {
            getSupportLoaderManager().restartLoader(UPDATE_LOADER, bundle, this);
        }
    }

    public void onClickBtnDelete(View view) {
        btnDelete.setEnabled(false);
        btnEdit.setEnabled(false);

        /**
         * TODO 80: Crie um objeto Bundle e coloque nele o contato defenido no passo 67.
         */
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONTACT, contact);

        /**
         * TODO 81: Inicie, ou reinicie, o Loader referente a operação de Deleção com o ID definido no passo 75, passando o Bundle criado no passo 80.
         */
        if (bundle == null) {
            getSupportLoaderManager().initLoader(DELETE_LOADER, bundle, this);
        } else {
            getSupportLoaderManager().restartLoader(DELETE_LOADER, bundle, this);
        }

    }

    public void onClickBtnCancel(View view) {
        lockEditTexts();
        btnEdit.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);

        btnSave.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
    }

    public void onClickBtnEdit(View view) {
        unlockEditTexts();
        btnEdit.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);

        btnSave.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Contact> onCreateLoader(int id, final Bundle args) {
        /**
         * TODO 82: Faça um IF para diferenciar qual dos dois Loaders está sendo criado. Compare o atributo ID com os identificadores criados no passo 75.
         */

        if (id == UPDATE_LOADER) {
            /**
             * TODO 83: Crie uma AsyncTaskLoader para realizar a operação de Update, implementando os métodos onStartLoading e loadInBackground.
             * Não daremos detalhes de como devem ser implementado esses métodos, mas é bem parecido com o que já fizemos anteriormente, só
             * muda a chamada feita com a biblioteca OkHttp, já que devemos utilizar put ao invés de post
             * DICA: Use o método estático WebServiceUtil.getContactURL(Contact contact) para gerar a URL da requisição.
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
                        Request request = new Request.Builder().url(WebServiceUtil.getContactURL(contact)).put(body).build();

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

        if (id == DELETE_LOADER) {
            /**
             * TODO 84: Crie uma AsyncTaskLoader para realizar a operação de deleção, implementando os métodos onStartLoading e loadInBackground.
             * Não daremos detalhes de como devem ser implementado esses métodos, mas é bem parecido com o que já fizemos anteriormente, só
             * muda a chamada feita com a biblioteca OkHttp, já que devemos utilizar delete ao invés de post
             * DICA: Use o método estático WebServiceUtil.getContactURL(Contact contact) para gerar a URL da requisição.
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
                        Request request = new Request.Builder().url(WebServiceUtil.getContactURL(contact)).delete(body).build();

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
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Contact> loader, Contact contact) {
        /**
         * TODO 85: Use loader.getId() para diferenciar qual loader terminou de executar. Exiba mensagens apropriadas para cada um dos Loaders via Toast.
         */
        if (loader.getId() == UPDATE_LOADER) {
            Toast.makeText(
                    getApplicationContext(),
                    "Contact updated",
                    Toast.LENGTH_LONG
            ).show();
        }
        if (loader.getId() == DELETE_LOADER) {
            Toast.makeText(
                    getApplicationContext(),
                    "Contact deleted",
                    Toast.LENGTH_LONG
            ).show();
        }
        /**
         * TODO 86: Caso o Loader para deleteção tenha terminado, feche a activity atual e volte para a MainActivity.
         */
        finish();
    }

    @Override
    public void onLoaderReset(Loader<Contact> loader) {
        /*
         * We aren't using this method in our example application, but we are required to Override
         * it to implement the LoaderCallbacks<Contact> interface
         */
    }
}