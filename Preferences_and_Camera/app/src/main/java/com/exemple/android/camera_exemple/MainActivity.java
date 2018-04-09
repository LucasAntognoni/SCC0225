package com.exemple.android.camera_exemple;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class MainActivity extends AppCompatActivity {

    ImageView imgProfile;
    EditText txtUserName;
    Bitmap imageBitmap;

    private static final int REQUEST_IMAGE_CAPTURE = 55;

    /**
     * TODO 01: Crie constantes String que serão utilizadas como chaves para obter os valores da Shared Preference.
     */
    private static final String EXTRA_USER_NAME = "EXTRA_USER_NAME";
    private static final String EXTRA_FILE_PATH = "EXTRA_FILE_PATH";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        txtUserName = (EditText) findViewById(R.id.txtUserName);

        loadFromSharedPreferences();
    }

    private void loadFromSharedPreferences() {
        String filePath = null;
        String userName = "";

        /**
         * TODO 02: Recupere das Shared Preferences o userName e o filePath da imagem de perfil. Sete o txtUserName e o imgProfile caso haja dados no Shared Preferenes.
         */

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        filePath = sharedPref.getString(EXTRA_FILE_PATH, null);
        userName = sharedPref.getString(EXTRA_USER_NAME, "");

        txtUserName.setText(userName);
        //Você pode utilizar o método laodImageFromFile para setar o contéudo da imgProfile.
        if (filePath != null){
            loadImageFromFile(filePath);
        }

    }

    private void loadImageFromFile(String filePath) {
        File imgFile = new  File(filePath);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgProfile.setImageBitmap(myBitmap);
        }
    }

    public void onBtnTakePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imgProfile.setImageBitmap(imageBitmap);
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        File directory = cw.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE);
        File path = new File(directory, "profile.png");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(path);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path.getAbsolutePath();
    }

    public void onBtnSave(View view) {
        String userName = txtUserName.getText().toString();

        if (!userName.isEmpty() && imageBitmap != null) {
            String filePath = saveImageToInternalStorage(imageBitmap);
            /**
             * TODO 03: Armazene os valores de userName e filePath no the Shared Preferences
             */
            Toast.makeText(this, "User informations saved", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(EXTRA_FILE_PATH, filePath);
            editor.putString(EXTRA_USER_NAME, userName);
            editor.commit();
        }

    }

    public void onBtnClear(View view) {
        txtUserName.setText("");
        imgProfile.setImageResource(R.drawable.profile_image);
        imageBitmap = null;

        /**
         * TODO 04: Remova username e profile picture das shared preferences.
         */
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(EXTRA_FILE_PATH);
        editor.remove(EXTRA_USER_NAME);
        editor.commit();
    }
}
