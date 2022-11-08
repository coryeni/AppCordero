package com.example.cordero;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cordero.archivosguason.Digest;
import com.example.cordero.archivosguason.MyInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    public List <MyInfo> list = null;
    String json = null;
    public static String TAG = "mensaje";
    TextView bienvenidoLabel, continuarLabel, olvidastecontra, nuevoUsuario;
    EditText contrasenaTextField;
    ImageView loginImageMedical;
    Button InicioSesion;



    public static final String archivo = "registro.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEditText usuario = findViewById(R.id.usuarioulti);
        TextInputEditText contra = findViewById(R.id.contraulti);

        TextView registro =findViewById(R.id.nuevoUsuario);
        Button button = findViewById(R.id.InicioSesion);
        TextView olvide = findViewById(R.id.olvidasteContra);

        loginImageMedical = findViewById(R.id.loginImageMedical);
        InicioSesion = findViewById(R.id.InicioSesion);
        bienvenidoLabel = findViewById(R.id.bienvenidolabel);
        continuarLabel = findViewById(R.id.continuarlabel);
        olvidastecontra = findViewById(R.id.olvidasteContra);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);

        Read();
        json2List(json);

        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Login.this, Registro.class);

                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair(loginImageMedical, "logoImageTrans");
                pairs[1] = new Pair(bienvenidoLabel, "textTrans");
                pairs[2] = new Pair(continuarLabel, "iniciaSesionTextTrans");
                pairs[3] = new Pair(nuevoUsuario, "newUserTrans");

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                    startActivity(intent, options.toBundle());
                }
                else{
                    startActivity(intent);
                    finish();
                }
            }
        });

        olvide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Olvide.class);
                startActivity(i);
            }
        });

        InicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usr = String.valueOf(usuario.getText());
                String psw = String.valueOf(contra.getText());

                Digest sha1 = new Digest();

                int i = 0;
                for (MyInfo myInfo : list) {
                    if (myInfo.getUsuario().equals(usr) && myInfo.getContra().equals(psw)) {
                        Intent intent = new Intent(Login.this, Jungle.class);
                        intent.putExtra("Objeto", myInfo);
                        startActivity(intent);
                        i = 1;

                    }
                }
                if (i == 0) {
                    Toast.makeText(getApplicationContext(), "El usuario o contraseña son incorrectos ", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(!isFileExits()){
            button.setEnabled(false);
            olvide.setEnabled(false);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////METODOS A PARTE//////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////


    private boolean isFileExits() {
        File file = getFile();
        if( file == null ) {
            return false;
        }
        return file.isFile() && file.exists();
    }

    private File getFile() {
        return new File( getDataDir() , archivo );
    }

    public void json2List(String json){
        Gson gson = null;
        String mensaje = null;
        if(json == null || json.length() == 0){
        }
        gson = new Gson();
        Type listType = new TypeToken<ArrayList<MyInfo>>(){}.getType();
        list = gson.fromJson(json, listType);
        if(list == null || list.size() == 0){
        }
    }
    public boolean Read(){
        if(!isFileExits()){
            return false;
        }
        File file = getFile();
        FileInputStream fileInputStream = null;
        byte[] bytes = null;
        bytes = new byte[(int)file.length()];
        try{
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            json = new String(bytes);
            Log.d(TAG, json);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
}