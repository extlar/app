package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogInformativo;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.ErrorConexion;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class LoginUsuariosExistentes extends AppCompatActivity implements ConActionbarView {
    private final String PARA_ENTRAR = "yastas/authenticate";
    private String userData, userDataEnBase, nameParameters, paraQueNoExploten, versionName;
    private static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    private TextView tvVersionApp, tvOlvidoPass;
    private EditText etNumUsuarioExistente, etNumTarjetaExistente, etPassExistente;
    private Button btnIniciarSesion;
    private ProgressDialog pgPorSiSeTarda;
    private byte[] valuesEncode;
    private Boolean bienvenidaVista;
    private RelativeLayout lyLoading;
    private ErrorConexion dialogInformativoConeccion;

    private RequestQueue requestQueueLogin;
    private JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login_usuarios_existentes);

        iniGuiLUE();
        iniListenersLUE();
    }

    public void iniGuiLUE(){
        traerVersion();
        ConActionbarView conActionbarView = this;
        conActionbarView.setToolbarValues();

        requestQueueLogin = Volley.newRequestQueue(LoginUsuariosExistentes.this);

        etNumUsuarioExistente = (EditText)findViewById(R.id.et_numero_usuario_login_existente);
        etNumTarjetaExistente = (EditText)findViewById(R.id.et_numero_tarjeta_login_existente);
        etPassExistente = (EditText)findViewById(R.id.et_contrasenna_login_existente);
        btnIniciarSesion = (Button)findViewById(R.id.btn_iniciar_sesion_usuario_existente);
        tvVersionApp = (TextView)findViewById(R.id.tv_version_bienvenida);
        tvOlvidoPass = (TextView)findViewById(R.id.tv_olvide_pass);
        lyLoading = (RelativeLayout)findViewById(R.id.loading);

        tvOlvidoPass.setText(Html.fromHtml("<b><u>" + getResources().getString(R.string.olvide_pass) + "</u></b>"));

        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansPro.otf", false);

        Typeface amorBold  = Typeface.createFromAsset(getAssets(), "fonts/AmorSansProBold.otf");
        btnIniciarSesion.setTypeface(amorBold);

    }

    public void iniListenersLUE(){
        tvVersionApp.setText("V " + versionName);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNumUsuarioExistente.getText().toString().length() <1 || etNumTarjetaExistente.getText().toString().length() <1 || etPassExistente.getText().toString().length() <1 ){
                    Toast.makeText(LoginUsuariosExistentes.this, "Por favor introduce todos los datos", Toast.LENGTH_SHORT).show();

                } else {
                    nameParameters = "dXNlciZwYXNzd29yZCZjYXJk";
                    userData = etNumUsuarioExistente.getText().toString() + "&" + etPassExistente.getText().toString() + "&" + etNumTarjetaExistente.getText().toString();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        userDataEnBase = Base64.getEncoder().encodeToString(userData.getBytes());
                        login(userDataEnBase);
                    } else {
                        int flas = android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE;
                        byte[] bites = userData.getBytes();
                        String valuesEncode2 = android.util.Base64.encodeToString(bites, flas);
                        login(valuesEncode2);
                    }

                }
            }
        });

        //tvOlvidoPass.setVisibility(View.GONE);
        tvOlvidoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OlvidoPass.class));
            }
        });

    }

    public void login(String parametros){
        String stringUrl = getResources().getString(R.string.base_api_lab) + PARA_ENTRAR;
        paraQueNoExploten = parametros;
        JSONObject postParams = new JSONObject();
        try {
            postParams.put("grant_type", "password");
            postParams.put("userData", parametros);
            postParams.put("nameParameters", nameParameters);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("login", stringUrl + "=" + postParams);
        if(isOnlineNet()){
            lyLoading.setVisibility(View.VISIBLE);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, stringUrl, postParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String nombreUsuario = response.getString("userName");
                        String numeroDeUsuario =  response.getString("userId");
                        String roll = response.getString("roleId");
                        String token = response.getString("access_token");

                        Intent accesoAppIntent = new Intent(getApplicationContext(), MainActivity.class);

                        sesionUsuario(nombreUsuario, numeroDeUsuario, roll, token, true, paraQueNoExploten);

                        SharedPreferences wtVisto = getSharedPreferences("prefsPagosYastas", MODE_PRIVATE);
                        SharedPreferences.Editor editor = wtVisto.edit();
                        editor.putBoolean("welcome_tour_visto", true);

                        startActivity(accesoAppIntent);
                        finish();

                    } catch (JSONException e) {
                        try {
                            String error = response.getString("responseMessage");
                            lyLoading.setVisibility(View.GONE);
                            Toast.makeText(LoginUsuariosExistentes.this, error, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(!isOnlineNet()){
                        lyLoading.setVisibility(View.GONE);
                        abirDialogoConexion();

                    }


                }
            })
            {
                @Override
                public Map getHeaders() throws AuthFailureError{
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
            requestQueueLogin.add(jsonObjectRequest);

        } else {
            abirDialogoConexion();
        }
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
    public void setToolbarValues() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                startActivity(new Intent(LoginUsuariosExistentes.this, Bienvenida.class));
                finish();
                break;
        }
        return true;
    }

    public void sesionUsuario(String nombreUsuario,String numeroDeUsuario, String roll, String token, boolean bienvenidaVista, String usTemp){
        SharedPreferences preferences = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombreUsuarioSession", nombreUsuario);
        editor.putString("numeroDeUsuarioSession", numeroDeUsuario);
        editor.putString("rollSession", roll);
        //editor.putString("rollSession", "2");
        editor.putString("yaConTokenSession", token);
        editor.putBoolean("enSession", bienvenidaVista);
        editor.putString("paraQueNoExploten", usTemp);

        editor.apply();
    }

    public Boolean isOnlineNet() {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");
            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void abirDialogoConexion(){
        dialogInformativoConeccion = new ErrorConexion();
        dialogInformativoConeccion.show(getSupportFragmentManager(), "dialog_error_conexion");
        dialogInformativoConeccion.setCancelable(false);
        android.app.Fragment frag = getFragmentManager().findFragmentByTag("dialog_error_conexion");
        if(frag != null){
            getFragmentManager().beginTransaction().remove(frag).commit();
        }
    }

}