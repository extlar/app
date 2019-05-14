package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Bienvenida;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.CuentaClabe;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.OperacionesGridAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.OperacionesPIMGridAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogInformativo;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.TerminosCondicionesyAviso;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.MenuPrincipal;
import com.gentera.sdk.GenteraSDK;
import com.gentera.sdk.helpers.commons.Transaction;
import com.gentera.sdk.helpers.commons.TransactionError;
import com.gentera.sdk.modules.pim.model.Category;
import com.gentera.sdk.modules.pim.model.Product;
import com.google.firebase.FirebaseApp;

import me.anwarshahriar.calligrapher.Calligrapher;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends AppCompatActivity implements ConActionbarView {
    private final String PARA_CONSULTAR_STORE_ID = "usermanagement/userbyid/", PARA_CONSULTAR_SALDO = "saldo_yastas/balance?";
    private static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    private DrawerLayout drawerLayoutMain;
    private RelativeLayout rlIndicadorSemaforoSaldo;
    private DialogInformativo dialogInformativo;
    private Boolean dejarDialogEnVisto, conLosUltimosSkus;
    private GridView gvOperacionesMain, gvOperacionesPIMMain;
    private TextView tvCantidadSaldo, tvNombreOperador, tvNombreComercio, tvNumeroOperador, tvVersionApp, tvAviso, tvTerminos;
    private Button btnRecargaMain;
    private int[] iconosOperacionesMain;
    private String[] titulosOperacionesMain;
    private String nombreOperador, numeroOperador, storeId, token, roll, commisionAgentId,  versionName, quePinto, paraQueNoExploten;
    private Bundle bundleDatosUsuario;
    private JsonObjectRequest jsonObjectRequestStoreId, jsonObjectRequestConsultaSaldo, jsonObjectRequestRefreshToken;
    private RequestQueue storeIdQueque, requestQueueRefreshToken;;
    private SharedPreferences preferences;
    public Bundle bundleChoro;
    private LinearLayout llContenedorSaldo;
    private ProgressBar pbMientrasPintaSaldo;
    protected FragmentManager mfragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        iniGuiMain();
        iniListenersMain();
        abrirDialogBienvenida();
        buscarUltimosSkus();
    }

    public void iniGuiMain(){
        //FirebaseApp.initializeApp(this);

        requestQueueRefreshToken = Volley.newRequestQueue(MainActivity.this);
        storeIdQueque = Volley.newRequestQueue(MainActivity.this);

        ConActionbarView conActionbarView = this;
        conActionbarView.setToolbarValues();

        preferences = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);

        mfragmentManager = getSupportFragmentManager();

        cargarSesion();
        MenuPrincipal mMenuPrincipal = new MenuPrincipal();
        mMenuPrincipal.crearMenu(this, this, nombreOperador);
        traerVersion();
        storeId();

        if(roll.equals("2")){
            iconosOperacionesMain = new  int[]{/*R.drawable.tiempo_aire_morado, R.drawable.servicios_morado, R.drawable.paquetes, R.drawable.apps_gris, */R.drawable.reportes_verde, R.drawable.admin_gris};
        } else {
            iconosOperacionesMain = new  int[]{/*R.drawable.tiempo_aire_morado, R.drawable.servicios_morado, R.drawable.paquetes, R.drawable.apps_gris, */R.drawable.reportes_gris, R.drawable.admin_gris};
        }

        titulosOperacionesMain = new String[]{/*this.getResources().getString(R.string.tiempo_aire), this.getResources().getString(R.string.pago_servicios),  this.getResources().getString(R.string.paquetes_datos), this.getResources().getString(R.string.recarga_apps_juegos), */this.getResources().getString(R.string.reportes).toUpperCase(), this.getResources().getString(R.string.administracion).toUpperCase()};
        drawerLayoutMain = (DrawerLayout)findViewById(R.id.drawer_layout_main);

        btnRecargaMain = (Button) findViewById(R.id.btn_recargar_main);
        rlIndicadorSemaforoSaldo = (RelativeLayout) findViewById(R.id.rl_indicador_semaforo_saldo);
        tvCantidadSaldo = (TextView)findViewById(R.id.tv_cantidad_saldo_main);
        tvNombreOperador = (TextView)findViewById(R.id.tv_nombre_usuario);
        tvNombreComercio = (TextView)findViewById(R.id.tv_nombre_comercio);
        tvNumeroOperador = (TextView)findViewById(R.id.tv_numero_operador);
        tvVersionApp = (TextView)findViewById(R.id.tv_version_app);
        tvAviso = (TextView)findViewById(R.id.tv_aviso);
        tvTerminos = (TextView)findViewById(R.id.tv_terminos);
        llContenedorSaldo = (LinearLayout)findViewById(R.id.ll_contenedor_saldo);
        pbMientrasPintaSaldo = (ProgressBar)findViewById(R.id.pb_mientras_carga_saldo);

        tvTerminos.setText(Html.fromHtml("<b><u>T\u00E9rminos y Condiciones</u></b>"));
        tvAviso.setText(Html.fromHtml("<b><u>Aviso de Privacidad</u></b>"));
        tvNombreOperador.setText("Nombre: " + nombreOperador);
        tvNumeroOperador.setText("Operador: " + numeroOperador);

        gvOperacionesMain = (GridView)findViewById(R.id.gv_operaciones);
        gvOperacionesPIMMain = (GridView)findViewById(R.id.gv_operaciones_pim);
        tvVersionApp.setText("V " + versionName);

        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansProBold.otf", true);

    }

    public void iniListenersMain(){
        //traerCategoriasPIM();

        OperacionesGridAdapter operacionesGridAdapter = new OperacionesGridAdapter(this, iconosOperacionesMain, titulosOperacionesMain);
        gvOperacionesMain.setAdapter(operacionesGridAdapter);

        FirebaseApp.initializeApp(this);

        GenteraSDK.initialize("admin", "admin", MainActivity.this);
        try {
            runOnUiThread (new Thread(new Runnable() {
                public void run() {
                    try {
                        GenteraSDK.getInstance().Pim().getCategoriesCatalog(new Transaction() {
                            @Override
                            public <Category> void onTransactionSuccess(List<Category> list) {
                                Log.i(TAG, "Exito en la consulta de categorias: >> " + list.toString());
                                List<Category> lis= (List<Category>) list;
                                OperacionesPIMGridAdapter operacionesPIMGridAdapter = new OperacionesPIMGridAdapter(MainActivity.this, lis,true);
                                gvOperacionesPIMMain.setAdapter(operacionesPIMGridAdapter);
                            }

                            @Override
                            public void onTransactionFailed(TransactionError error) {
                                Log.e(TAG, "ERROR en la consulta de categorias: >> " + error.getMessage());
                            }
                        });
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        btnRecargaMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFondeo = new Intent(MainActivity.this, CuentaClabe.class);
                Bundle b = new Bundle();
                b.putString("idCob", commisionAgentId);
                intentFondeo.putExtras(b);
                startActivity(intentFondeo);
            }
        });

        tvAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quePinto = "avisodeprivacidad";
                bundleChoro = new Bundle();
                bundleChoro.putString("paraelchoro",quePinto);
                ComoDeQueNO dijeQueSi = new ComoDeQueNO();
                dijeQueSi.abrirDialogAvisoYTerminos(MainActivity.this, mfragmentManager);

            }
        });

        tvTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quePinto = "terminosycondiciones";
                bundleChoro = new Bundle();
                bundleChoro.putString("paraelchoro",quePinto);
                ComoDeQueNO dijeQueSi = new ComoDeQueNO();
                dijeQueSi.abrirDialogAvisoYTerminos(MainActivity.this, mfragmentManager);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!drawerLayoutMain.isDrawerOpen(GravityCompat.START)) {
                    drawerLayoutMain.openDrawer(GravityCompat.START);
                    return true;

                } else {
                    drawerLayoutMain.closeDrawer(GravityCompat.START);
                    return false;
                }
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirDialogBienvenida(){
        preferences = getSharedPreferences("prefsPagosYastas", MODE_PRIVATE);
        dejarDialogEnVisto = preferences.getBoolean("first_time_start_dialog", true);

        if(dejarDialogEnVisto){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("first_time_start_dialog", false);
            editor.apply();

            dialogInformativo = DialogInformativo.newInstance(getResources().getString(R.string.bienvenido), getResources().getString(R.string.explora_tu_nueva_app));
            dialogInformativo.show(getSupportFragmentManager(), "dialog_bienvenida");
            android.app.Fragment frag = getFragmentManager().findFragmentByTag("dialog_bienvenida");
            if(frag != null){
                getFragmentManager().beginTransaction().remove(frag).commit();
            }
        }
    }

    public void buscarUltimosSkus(){
        conLosUltimosSkus = preferences.getBoolean("conLosUltimosSkus", true);
        SharedPreferences.Editor editorSkus = preferences.edit();
        editorSkus.putBoolean("conLosUltimosSkus", true);
        editorSkus.apply();

        if(conLosUltimosSkus){

        } else {
            editorSkus.remove("nombreUsuarioSession");
            editorSkus.remove("numeroDeUsuarioSession");
            editorSkus.remove("rollSession");
            editorSkus.remove("yaConTokenSession");
            editorSkus.remove("enSession");
            editorSkus.remove("storeId");
            editorSkus.remove("enLaBolsa");
            editorSkus.remove("paraQueNoExploten");

            editorSkus.apply();

            finish();
            startActivity(new Intent(getApplicationContext(), Bienvenida.class));
        }
    }

    @Override
    public void setToolbarValues() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void storeId(){
        String stringUrlStoreId = getResources().getString(R.string.base_api_lab) + PARA_CONSULTAR_STORE_ID + numeroOperador;
        Log.i("in storeID peticion:",stringUrlStoreId);
        jsonObjectRequestStoreId = new JsonObjectRequest(Request.Method.GET, stringUrlStoreId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    storeId = response.getString("storeId");
                    commisionAgentId = response.getString("commisionAgentId");

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("storeId", storeId);
                    editor.putString("commisionAgentId", commisionAgentId);
                    editor.apply();

                    consultaDeSaldo(storeId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                rlIndicadorSemaforoSaldo.setBackground(getResources().getDrawable(R.drawable.shape_semaforo_saldo_gris));
                tvCantidadSaldo.setText("No disponible");
                Toast.makeText(MainActivity.this, "No pudimos obtener tu saldo", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                Log.i("token", token);
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer "+ token);
                return headers;
            }
        }
        ;
        jsonObjectRequestStoreId.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        storeIdQueque.add(jsonObjectRequestStoreId);

    }

    public void consultaDeSaldo(String storeId){
        String stringUrlBalance = getResources().getString(R.string.base_api_lab) + PARA_CONSULTAR_SALDO + "StoreID=" + storeId + "&UserID="+ numeroOperador;

        jsonObjectRequestConsultaSaldo = new JsonObjectRequest(Request.Method.GET, stringUrlBalance, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tvCantidadSaldo.setText("MXN $ " + response.getString("StoreBalance"));
                    tvNombreComercio.setText("Comercio: "+ response.getString("StoreName"));
                    String nombreComercio = response.getString("StoreName");
                    double enLaBolsa = Double.parseDouble(response.getString("StoreBalance"));

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putFloat("enLaBolsa", (float) enLaBolsa);
                    editor.putString("StoreName", nombreComercio);
                    editor.apply();

                    if(enLaBolsa < 500){
                        rlIndicadorSemaforoSaldo.setBackground(getResources().getDrawable(R.drawable.shape_semaforo_saldo_naranja));
                    } else if (enLaBolsa < 1000){
                        rlIndicadorSemaforoSaldo.setBackground(getResources().getDrawable(R.drawable.shape_semaforo_saldo_amarillo));
                    } else {
                        rlIndicadorSemaforoSaldo.setBackground(getResources().getDrawable(R.drawable.shape_semaforo_saldo_verde));
                    }
                    llContenedorSaldo.setVisibility(View.VISIBLE);
                    pbMientrasPintaSaldo.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, "No pudimos obtener tu saldo", Toast.LENGTH_SHORT).show();
                String stringUrl = getResources().getString(R.string.base_api_lab) + "yastas/authenticate";
                JSONObject postParams = new JSONObject();
                try {
                    postParams.put("grant_type", "password");
                    postParams.put("userData", paraQueNoExploten);
                    postParams.put("nameParameters", "dXNlciZwYXNzd29yZCZjYXJk");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonObjectRequestRefreshToken = new JsonObjectRequest(Request.Method.POST, stringUrl, postParams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("access_token");

                            SharedPreferences preferences = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("yaConTokenSession", token);
                            editor.apply();
                            llContenedorSaldo.setVisibility(View.VISIBLE);
                            pbMientrasPintaSaldo.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            try {
                                String error = response.getString("responseMessage");
                                //lyLoading.setVisibility(View.GONE);
                                //Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                })
                {
                    @Override
                    public Map getHeaders() throws AuthFailureError{
                        HashMap headers = new HashMap();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Basic OG5kMzlnTHdDamt6VUpLbFA4SDd4NkNOeFU0MGl0WWI6T2RHcTFQMElMUEVLSWpaMg==");
                        return headers;
                    }
                };


                jsonObjectRequestRefreshToken.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueueRefreshToken.add(jsonObjectRequestRefreshToken);
            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer "+ token);
                return headers;
            }
        };

        jsonObjectRequestConsultaSaldo.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        storeIdQueque.add(jsonObjectRequestConsultaSaldo);

    }

    public void cargarSesion(){
        numeroOperador = preferences.getString("numeroDeUsuarioSession", "");
        token = preferences.getString("yaConTokenSession", "");
        nombreOperador = preferences.getString("nombreUsuarioSession", "");
        roll = preferences.getString("rollSession", "");
        paraQueNoExploten = preferences.getString("paraQueNoExploten", "");
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

    @SuppressLint("ValidFragment")
    public class ComoDeQueNO extends android.support.v4.app.DialogFragment {

        public void abrirDialogAvisoYTerminos(Context context, FragmentManager fragmentManager) {
            TerminosCondicionesyAviso terminosCondicionesyAvisoFragment = new TerminosCondicionesyAviso();
            terminosCondicionesyAvisoFragment.show(fragmentManager, "avisos");
            Fragment frag = fragmentManager.findFragmentByTag("avisos");
            terminosCondicionesyAvisoFragment.setArguments(bundleChoro);
            if (frag != null) {
                getFragmentManager().beginTransaction().remove(frag).commit();
            }

        }
    }

/*    public static List<Category> traerCategoriasPIM(){
        GenteraSDK.initialize("admin", "admin");
        GenteraSDK.getInstance().Pim().getCategoriesCatalog(new Transaction() {
            @Override
            public <T> void onTransactionSuccess(List<T> list) {
                Log.i(TAG, "onTransactionSuccess:\n " + list.toString());
                List<Category> listaCategorias = new ArrayList<Category>();
                for (Category c: listaCategorias){
                    System.out.print(c);
                }

        //        return

            }

            @Override
            public void onTransactionFailed(TransactionError transactionError) {
                Log.i(TAG, "onTransactionFail:\n " + transactionError.getMessage().toString());
            }
        });

    }*/

}