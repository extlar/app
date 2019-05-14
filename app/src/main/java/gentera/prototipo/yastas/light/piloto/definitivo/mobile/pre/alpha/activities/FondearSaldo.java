package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogInformativo;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.CuentaClabeSTP;
import me.anwarshahriar.calligrapher.Calligrapher;

public class FondearSaldo extends AppCompatActivity implements ConActionbarView {
    private static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    private Spinner spFondearSaldo;
    private String[] itemsOpcionesFondeo;
    private RelativeLayout rlContendorConfirmacionFondeo;
    private TextView tvTuNumeroEs, tvInstuccionesFondeo, tvRecordatorioFondeo, tvReferenciaClabeFondeo;
    private LinearLayout llContenedorBotonesFondeo;
    private ImageView ivCodigoBarrasFondeo;
    private Button btnGuardarFondeo, btnCompartirFondeo;
    private DialogInformativo dialogInformativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_fondear_saldo);

        iniGuiFondearSaldo();
        iniListenersFondearSaldo();
    }

    public void iniGuiFondearSaldo(){

        spFondearSaldo = (Spinner)findViewById(R.id.sp_opciones_fondeo);
        rlContendorConfirmacionFondeo = (RelativeLayout)findViewById(R.id.rl_contenedor_confirmacion_opciones_fondeo);
        tvTuNumeroEs = (TextView)findViewById(R.id.tv_tu_numero_opciones_fondeo);
        tvReferenciaClabeFondeo = (TextView)findViewById(R.id.tv_referencia_clabe_opciones_fondeo);
        tvInstuccionesFondeo = (TextView)findViewById(R.id.tv_instrucciones_opciones_fondeo);
        tvRecordatorioFondeo = (TextView)findViewById(R.id.tv_recordatorio_opciones_fondeo);
        llContenedorBotonesFondeo = (LinearLayout)findViewById(R.id.ll_contenedor_botones_opciones_fondeo);
        ivCodigoBarrasFondeo = (ImageView)findViewById(R.id.iv_codigo_barras_fondeo);
        btnGuardarFondeo = (Button)findViewById(R.id.btn_guardar_opciones_fondeo);
        btnCompartirFondeo = (Button)findViewById(R.id.btn_compartir_opciones_fondeo);

        itemsOpcionesFondeo = getResources().getStringArray(R.array.metodos_recarga_saldo);

        ArrayAdapter<String> adapadorSpinnerOpcionesFondeo = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, itemsOpcionesFondeo);
        adapadorSpinnerOpcionesFondeo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFondearSaldo.setAdapter(adapadorSpinnerOpcionesFondeo);

        ConActionbarView actionbarView = this;
        actionbarView.setToolbarValues();

        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansProBold.otf", true);

        Typeface amorType  = Typeface.createFromAsset(getAssets(), "fonts/AmorSansPro.otf");
        tvInstuccionesFondeo.setTypeface(amorType);

    }

    public void iniListenersFondearSaldo(){
        spFondearSaldo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        rlContendorConfirmacionFondeo.setVisibility(View.GONE);
                        break;
                    case 1:
                        rlContendorConfirmacionFondeo.setVisibility(View.VISIBLE);
                        tvTuNumeroEs.setText(R.string.tu_numero_spei);
                        ivCodigoBarrasFondeo.setImageDrawable(null);
                        tvReferenciaClabeFondeo.setText("130999000118359719");
                        tvInstuccionesFondeo.setText(R.string.instrucciones_spei);
                        tvRecordatorioFondeo.setText(R.string.recordatorio_spei);
                        break;
                    case 2:
                        rlContendorConfirmacionFondeo.setVisibility(View.VISIBLE);
                        tvTuNumeroEs.setText(R.string.tu_numero_compartamos_banco);
                        ivCodigoBarrasFondeo.setImageResource(R.drawable.codigo_de_barras_temp);
                        tvReferenciaClabeFondeo.setText("130999000118359719");
                        tvInstuccionesFondeo.setText(R.string.instrucciones_compartamos_banco);
                        tvRecordatorioFondeo.setText(R.string.recordatorio_compartamos_banco);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGuardarFondeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInformativo = DialogInformativo.newInstance(getResources().getString(R.string.archivo_guardado), getResources().getString(R.string.explicacion_guardado));
                dialogInformativo.show(getSupportFragmentManager(), "dialog_bienvenida");
                android.app.Fragment frag = getFragmentManager().findFragmentByTag("dialog_bienvenida");
                if(frag != null){
                    getFragmentManager().beginTransaction().remove(frag).commit();
                }

            }
        });

        btnCompartirFondeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringShare;
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                stringShare = tvReferenciaClabeFondeo.getText().toString();
                intentShare.putExtra(Intent.EXTRA_TEXT, stringShare);
                startActivity(Intent.createChooser(intentShare, "Share using"));
            }
        });

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
                startActivity(new Intent(FondearSaldo.this, MainActivity.class));
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
