package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.ServiciosGridAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class PagoDeServicios extends AppCompatActivity implements ConActionbarView {
    private static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    private GridView gvServicios;
    private int[] iconosServicios;
    private String[] titulosServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pago_de_servicios);

        iniGuiPagoServicios();
        iniListenersPagoServicios();
    }

    public void iniGuiPagoServicios(){
        ConActionbarView conActionbarView = this;
        conActionbarView.setToolbarValues();

        iconosServicios = new int[]{R.drawable.serv_telefonia_internet_morado, R.drawable.serv_agua_gris, R.drawable.serv_luz_morado, R.drawable.serv_gas_gris, R.drawable.serv_gobierno_gris, R.drawable.serv_belleza_morado};
        titulosServicios = new String[]{this.getResources().getString(R.string.telefonia_internet), this.getResources().getString(R.string.agua), this.getResources().getString(R.string.luz), this.getResources().getString(R.string.gas), this.getResources().getString(R.string.tesoreria_gobierno), this.getResources().getString(R.string.venta_catalogo)};

        gvServicios = (GridView)findViewById(R.id.gv_servicios);

        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansProBold.otf", false);

    }

    public void iniListenersPagoServicios(){
        ServiciosGridAdapter operacionesGridAdapter = new ServiciosGridAdapter(this, iconosServicios, titulosServicios);
        gvServicios.setAdapter(operacionesGridAdapter);
        setSingleEvent(gvServicios);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_generico, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idMenu = item.getItemId();

        switch (idMenu){
            case R.id.menu_llamada_servicio:
                Intent intentLamada = new Intent(Intent.ACTION_DIAL);
                intentLamada.setData(Uri.parse(getResources().getString(R.string.numero_atencion_clientes)));
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    startActivity(intentLamada);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL);
                }
                break;
            case android.R.id.home:
                finish();
                startActivity(new Intent(PagoDeServicios.this, MainActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void setToolbarValues() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.pago_servicios);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setSingleEvent(GridView mainGrid){
        for(int i=0; i<mainGrid.getChildCount(); i++){
            CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int finalI = i;
            final Intent intentProcesoPago = new Intent (PagoDeServicios.this, ProcesoDePago.class);
            final Bundle bundleTipoServicio = new Bundle();
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (finalI){
                        case 0:
                            bundleTipoServicio.putString("tipoDeServicio", "TelefoniaEInternet");
                            intentProcesoPago.putExtras(bundleTipoServicio);
                            finish();
                            startActivity(intentProcesoPago);
                            break;
                        case 1:
                            bundleTipoServicio.putString("tipoDeServicio", "Agua");
                            intentProcesoPago.putExtras(bundleTipoServicio);
                            finish();
                            startActivity(intentProcesoPago);
                            break;
                        case 2:
                            bundleTipoServicio.putString("tipoDeServicio", "Luz");
                            intentProcesoPago.putExtras(bundleTipoServicio);
                            finish();
                            startActivity(intentProcesoPago);
                            break;
                        case 3:
                            bundleTipoServicio.putString("tipoDeServicio", "Gas");
                            intentProcesoPago.putExtras(bundleTipoServicio);
                            finish();
                            startActivity(intentProcesoPago);
                            break;
                        case 4:
                            bundleTipoServicio.putString("tipoDeServicio", "TesoreriaYGobierno");
                            intentProcesoPago.putExtras(bundleTipoServicio);
                            finish();
                            startActivity(intentProcesoPago);
                            break;
                        case 5:
                            bundleTipoServicio.putString("tipoDeServicio", "VentaPorCatalogo");
                            intentProcesoPago.putExtras(bundleTipoServicio);
                            finish();
                            startActivity(intentProcesoPago);
                            break;
                    }
                }
            });
        }
    }
}
