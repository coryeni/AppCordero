package com.example.cordero;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cordero.archivosguason.Digest;
import com.example.cordero.archivosguason.MyInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Registro extends AppCompatActivity {

    TextView nuevoUsuario, bienvenidoLabel, continuarLabel;
    ImageView logoRegistroMedical;
    RadioButton Masculino, Femenino;

    Button registrocuenta;

    TextInputEditText nombreedit,usuarioedit,contrasenaedit,correoedit,fechaedit;

    public static final String archivo ="registro.json";
    private String json2;

    private File getFile(){
        return new File(getDataDir(), archivo);
    }

    private boolean writeFile(String text){
        File file = null;
        FileOutputStream fileOutputStream = null;
        try {
            file = getFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(text.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        List<MyInfo> list = new ArrayList<MyInfo>();

        logoRegistroMedical = findViewById(R.id.logoRegistroMedical);
        bienvenidoLabel = findViewById(R.id.bienvenidolabel);
        continuarLabel = findViewById(R.id.continuarlabel);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);
        Masculino = findViewById(R.id.Masculino);
        Femenino = findViewById(R.id.Femenino);

        nombreedit = findViewById(R.id.nombreEditText);
        usuarioedit = findViewById(R.id.usuarioEditText);
        contrasenaedit = findViewById(R.id.contraEditText);
        correoedit = findViewById(R.id.emailEditText);
        fechaedit = findViewById(R.id.fechaEditText);
        Femenino = findViewById(R.id.Femenino);
        Masculino = findViewById(R.id.Masculino);

        registrocuenta = findViewById(R.id.Registrarte);

        registrocuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nombreedit.getText().length()==0||usuarioedit.getText().length()==0 || contrasenaedit.getText().length()==0){
                    Toast.makeText(getApplicationContext(), "Rellena los primeros 3 campos", Toast.LENGTH_LONG).show();
                    return;
                }

                String Nombre = nombreedit.getText().toString();
                String Usuario = usuarioedit.getText().toString();
                String Contra = contrasenaedit.getText().toString();
                String Correo = correoedit.getText().toString();
                String Fecha = fechaedit.getText().toString();
                String SexoI = "";

                if(Femenino.isChecked()){
                    SexoI += "Femenino";
                }
                if(Masculino.isChecked()){
                    SexoI += "Masculino";
                }
                if(!Femenino.isChecked() && !Masculino.isChecked()){
                    SexoI = "No especificado";
                }

                Digest sha1 = new Digest();
                byte[] txtByte = sha1.createSha1(Nombre + Contra);
                String pswCifr = sha1.bytesToHex(txtByte);

                json2 = lista2Json(Nombre, Usuario,Contra,  pswCifr, Correo, Fecha, SexoI);

                try {
                    if (writeFile(json2)) {
                        vaciar();
                        Toast.makeText(getApplicationContext(), "Usuario Registrado", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
            }

        });

        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionBack();
            }
        });
    }

    private void vaciar(){
        nombreedit.setText("");
        usuarioedit.setText("");
        contrasenaedit.setText("");
        correoedit.setText("");
        fechaedit.setText("");
        Femenino.setSelected(false);
        Masculino.setSelected(false);
    }

    public String lista2Json(String nom, String us, String contra,String cifra, String cor, String fecha, String sex) {
        MyInfo myInfo = null;
        Gson gson = null;
        String json = null;
        String mnsj = null;
        ArrayList list;

        myInfo = new MyInfo();
        myInfo.setNombre(nom);
        myInfo.setUsuario(us);
        myInfo.setContra(contra);
        myInfo.setCifra(cifra);
        myInfo.setCorreo(cor);
        myInfo.setFecha(fecha);
        myInfo.setSexo(sex);

        Log.d(TAG, "test");

        gson = new Gson();
        list = new ArrayList<MyInfo>();
        list.add(myInfo);
        json = gson.toJson(list, ArrayList.class);

        if (json != null) {
            Log.d(TAG, json);
            mnsj = "Archivo Creado";
        } else {
            mnsj = "Usuario no Registrado";
        }
        Toast.makeText(getApplicationContext(), mnsj, Toast.LENGTH_LONG).show();
        return json;
    }


    @Override
    public void onBackPressed(){
        transitionBack();
    }

    public void transitionBack(){
        Intent intent = new Intent(Registro.this, Login.class);
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair(logoRegistroMedical, "logoImageTrans");
        pairs[1] = new Pair(bienvenidoLabel, "textTrans");
        pairs[2] = new Pair(continuarLabel, "iniciaSesionTextTrans");
        pairs[3] = new Pair(nuevoUsuario, "newUserTrans");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Registro.this, pairs);
            startActivity(intent, options.toBundle());
        }
        else{
            startActivity(intent);
            finish();
        }
    }
}