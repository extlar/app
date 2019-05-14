package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.CuentaClabeSTP;
import me.anwarshahriar.calligrapher.Calligrapher;

public class CuentaClabe extends AppCompatActivity implements ConActionbarView {
    private static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    private TextView tvCuentaClabe;
    private Button btncompartir;
    private Bundle bundle;
    private String clabe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_clabe);

        iniGuiClabe();
        iniListenersClabe();
    }

    public void iniGuiClabe(){
        ConActionbarView actionbarView = this;
        actionbarView.setToolbarValues();

        bundle = this.getIntent().getExtras();

        tvCuentaClabe = (TextView)findViewById(R.id.tv_clabe_opciones_fondeo);
        btncompartir = (Button)findViewById(R.id.btn_compartir_clabe);

        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansProBold.otf", true);

    }

    public void iniListenersClabe(){
        try {
            clabe = CuentaClabeSTP.generarCuentaClabeSTP(bundle.getString("idCob"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        btncompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringShare;
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                stringShare = clabe;
                intentShare.putExtra(Intent.EXTRA_TEXT, stringShare);
                startActivity(Intent.createChooser(intentShare, "Share using"));

            }
        });

        tvCuentaClabe.setText(clabe);

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
                startActivity(new Intent(CuentaClabe.this, MainActivity.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    public void setToolbarValues() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.recarga_saldo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
