package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;

public class RecargaPaqueteDatos extends AppCompatActivity implements ConActionbarView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recarga_paquete_datos);

        iniGuiProcesoRecarga();
        iniListenersProcesoRecarga();
    }

    public void iniGuiProcesoRecarga(){
        ConActionbarView conActionbarView = this;
        conActionbarView.setToolbarValues();
    }

    public void iniListenersProcesoRecarga(){

    }

    @Override
    public void setToolbarValues() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.paquetes_datos);

    }
}
