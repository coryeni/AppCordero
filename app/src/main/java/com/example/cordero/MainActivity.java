package com.example.cordero;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Agregar animaciones
        Animation animacion1 = AnimationUtils .loadAnimation(this, R.anim.dezplazamiento_arriba);
        Animation animacion2 = AnimationUtils .loadAnimation(this, R.anim.dezplazamiento_abajo);

        TextView Medical = findViewById(R.id.Medical);
        TextView Project = findViewById(R.id.Project);
        ImageView logoNex = findViewById(R.id.logoNex);

        Medical.setAnimation(animacion2);
        Project.setAnimation(animacion2);
        logoNex.setAnimation(animacion1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(logoNex, "logoImageTrans");
                pairs[1] = new Pair<View, String>(Project, "textTrans");

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
                else{
                    startActivity(intent);
                    finish();
                }
            }
        }, 4000);
    }
}