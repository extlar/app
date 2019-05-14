package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;

public class Splash extends AppCompatActivity {
    private GifImageView givSplashScreen;
    private Boolean enSession, bienvenidaVista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);

        iniGuiSplash();
        iniListenersGuiSplash();
    }

    public void iniGuiSplash(){
        givSplashScreen = findViewById(R.id.giv_splasscreen);

    }

    public void iniListenersGuiSplash(){
        magiaDelGif();

    }

    public void magiaDelGif(){
        try {
            InputStream inputStream = getAssets().open("gif/colibri_splash.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            givSplashScreen.setBytes(bytes);
            givSplashScreen.startAnimation();
        } catch (IOException ex) {

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                verificarSesion();
            }
        }, 1999);
    }

    public void verificarSesion(){
        SharedPreferences wtVisto = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        bienvenidaVista = wtVisto.getBoolean("welcome_tour_visto", true);
        enSession = wtVisto.getBoolean("enSession",false);

        if(bienvenidaVista) {
            SharedPreferences.Editor editor = wtVisto.edit();
            editor.putBoolean("welcome_tour_visto", false);
            editor.commit();

            startActivity(new Intent(Splash.this, Bienvenida.class));
            finish();
        } else if (enSession){
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        } else{
            startActivity(new Intent(Splash.this, LoginUsuariosExistentes.class));
            finish();

        }

    }

}
