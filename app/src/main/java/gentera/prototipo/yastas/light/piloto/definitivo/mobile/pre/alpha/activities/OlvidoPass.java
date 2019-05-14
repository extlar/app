package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class OlvidoPass extends AppCompatActivity implements ConActionbarView {
    private final String RESETEAR = "yastas_/setNewPassword";
    private static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    private TextView tvVersionApp, tvYaTengoCodigo;
    private EditText etId, etTarjeta, etPreExp, etNuPass, etConfirmaNuPass;
    private String versionName, nameParameters, userData, userDataEnBase;
    private Button btnLLamarReseteo, btnMeterCodigo, btnResetear;
    private RelativeLayout rlExplicacionReseto, rlLoading;
    private LinearLayout rlMeterCodigoTemporal, rlResetear;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueueReestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_pass);

        iniGuiOlvidoPass();
        iniListenersOlvidoPass();

    }

    public void iniGuiOlvidoPass(){
        traerVersion();
        ConActionbarView conActionbarView = this;
        conActionbarView.setToolbarValues();

        requestQueueReestart = Volley.newRequestQueue(OlvidoPass.this);

        tvVersionApp = (TextView)findViewById(R.id.tv_version_reseteo);
        tvYaTengoCodigo = (TextView)findViewById(R.id.tv_ya_tengo_codigo);
        btnLLamarReseteo = (Button)findViewById(R.id.btn_llamar_reseteo);
        btnMeterCodigo = (Button)findViewById(R.id.btn_meter_codigo);
        btnResetear = (Button)findViewById(R.id.btn_resetear);
        rlExplicacionReseto = (RelativeLayout)findViewById(R.id.rl_contenedor_instrucciones_reseteo);
        rlMeterCodigoTemporal = (LinearLayout)findViewById(R.id.ll_contenedor_codigo_temporal_reseteo);
        rlResetear = (LinearLayout) findViewById(R.id.ll_contenedor_reseteo);
        etId = (EditText)findViewById(R.id.et_numero_usuario_olvido_pass);
        etTarjeta = (EditText)findViewById(R.id.et_numero_tarjeta_olvido_pass);
        etPreExp = (EditText)findViewById(R.id.et_meter_codigo_temporal);
        etNuPass = (EditText)findViewById(R.id.et_meter_nuevo_paas);
        etConfirmaNuPass = (EditText)findViewById(R.id.et_confirma_nuevo_paas);
        rlLoading = (RelativeLayout)findViewById(R.id.rl_loading_reseteo);

        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansPro.otf", false);

        Typeface amorBold  = Typeface.createFromAsset(getAssets(), "fonts/AmorSansProBold.otf");
        btnLLamarReseteo.setTypeface(amorBold);
        btnMeterCodigo.setTypeface(amorBold);
        btnResetear.setTypeface(amorBold);

        //etId.setText("032612");
        //etTarjeta.setText("9999990014410985");
    }

    public void iniListenersOlvidoPass(){
        tvVersionApp.setText("V " + versionName);
        tvYaTengoCodigo.setText(Html.fromHtml("<b><u>" + getResources().getString(R.string.ya_tengo_codigo) +"</u></b>"));

        btnLLamarReseteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLamadaReseteo = new Intent(Intent.ACTION_DIAL);
                intentLamadaReseteo.setData(Uri.parse(getResources().getString(R.string.numero_reseteo_pass)));
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    startActivity(intentLamadaReseteo);
                } else {
                    ActivityCompat.requestPermissions(OlvidoPass.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL);
                }

            }
        });

        tvYaTengoCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlExplicacionReseto.setVisibility(View.GONE);
                rlMeterCodigoTemporal.setVisibility(View.VISIBLE);

            }
        });

        btnMeterCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etId.getText().toString().length() <1 || etTarjeta.getText().toString().length() <1 || etPreExp.getText().toString().length() <1 ){
                    Toast.makeText(OlvidoPass.this, "Por favor introduce todos los datos", Toast.LENGTH_SHORT).show();

                } else {
                    btnMeterCodigo.setVisibility(View.GONE);
                    rlMeterCodigoTemporal.setVisibility(View.GONE);
                    rlResetear.setVisibility(View.VISIBLE);

                }


            }
        });

        btnResetear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNuPass.getText().toString().length() <1 || etConfirmaNuPass.getText().toString().length() <1){
                    Toast.makeText(OlvidoPass.this, "Por favor introduce todos los datos", Toast.LENGTH_SHORT).show();

                } else if(etNuPass.getText().toString().length() <6 || etConfirmaNuPass.getText().toString().length() <6){
                    Toast.makeText(OlvidoPass.this, "La contraseña debe ser de 6 digitos", Toast.LENGTH_SHORT).show();

                } else if(etNuPass.getText().toString().equals(etConfirmaNuPass.getText().toString())){
                    nameParameters = "dXNlciZwYXNzd29yZCZuZXdwYXNzd29yZCZjYXJk";
                    userData = etId.getText().toString() + "&" + etPreExp.getText().toString() + "&" + etConfirmaNuPass.getText().toString() + "&" + etTarjeta.getText().toString();

                    login(userData);
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        userDataEnBase = Base64.getEncoder().encodeToString(userData.getBytes());
                        login(userData);
                    } else {
                        int flas = android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE;
                        byte[] bites = userData.getBytes();
                        String valuesEncode2 = android.util.Base64.encodeToString(bites, flas);
                        login(valuesEncode2);

                        btnMeterCodigo.setVisibility(View.GONE);
                        rlMeterCodigoTemporal.setVisibility(View.GONE);
                        rlResetear.setVisibility(View.VISIBLE);

                    }*/

                } else {
                    Toast.makeText(OlvidoPass.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void login(String parametros){
        String stringUrl = getResources().getString(R.string.base_api_lab) + RESETEAR;
        JSONObject postParams = new JSONObject();
        nameParameters = "user&password&newpassword&card";
        try {
            postParams.put("grant_type", "password");
            postParams.put("userData", parametros);
            postParams.put("nameParameters", nameParameters);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("reseteo", stringUrl + "=" + postParams);
        rlLoading.setVisibility(View.VISIBLE);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, stringUrl, postParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String codigoResponse = "";
                //String descResponse = "";
                JSONObject respuestaSwip = null;
                try {
                    respuestaSwip = response.getJSONObject("responseMessage");
                    codigoResponse = response.getString("responseMessage");
                    //codigoResponse = respuestaSwip.getString("responseMessage");
                    //descResponse = respuestaSwip.getString("AuthorizationDescription");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(!codigoResponse.contains("no")){
                    Intent accesoAppIntent = new Intent(getApplicationContext(), LoginUsuariosExistentes.class);

                    Toast.makeText(getApplicationContext(), "Cambio de contraseña exitoso", Toast.LENGTH_SHORT).show();

                    startActivity(accesoAppIntent);
                    finish();
                } else {
                    rlLoading.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "No se realizo el cambio de contraseña", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //String error = error.getString("responseMessage");
                rlLoading.setVisibility(View.GONE);
                Toast.makeText(OlvidoPass.this, "Verificar datos ingresados", Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");

                //desarrollo
                //headers.put("Authorization", "Basic eHhzQ3ltSUhNcVNZb2dHV2VlbE1PaDBDZFRYRzJpeHI6RVR2eVFPY1FZQWpiYVBGZg==");
                //produccion
                headers.put("Authorization", "Basic OG5kMzlnTHdDamt6VUpLbFA4SDd4NkNOeFU0MGl0WWI6T2RHcTFQMElMUEVLSWpaMg==");
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueueReestart.add(jsonObjectRequest);

    }

    public void traerVersion(){
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            //versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
                Intent intentLamadaAtencion = new Intent(Intent.ACTION_DIAL);
                intentLamadaAtencion.setData(Uri.parse(getResources().getString(R.string.numero_atencion_clientes)));
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    startActivity(intentLamadaAtencion);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL);
                }
                break;
            case android.R.id.home:
                startActivity(new Intent(OlvidoPass.this, LoginUsuariosExistentes.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    public void setToolbarValues() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.olvide_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
