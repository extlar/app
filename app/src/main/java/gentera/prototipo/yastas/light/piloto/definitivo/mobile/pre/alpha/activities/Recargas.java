package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.RecargasGridAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.dbs.DataBaseInfoServicios;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.BuscarDispositivos;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogCorreo;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogCorreoRecargas;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogSms;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogSmsRecargas;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.BluetoothService;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.Command;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.PrintPicture;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.PrinterCommand;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Recargas extends AppCompatActivity implements ConActionbarView {
    /******************************************************************************************************/
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CHOSE_BMP = 3;
    private static final int REQUEST_CAMER = 4;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mService = null;
    private static final boolean DEBUG = true;
    private static final String TAG = "maldito bluetooth";
    private String mConnectedDeviceName = null;
    private PrintPicture ponElLogo;
    private String fechaRecibo, comercioRecibo, folioAvonRecibo, referenciaRecibo, montoRecibo;

    //---------------------------------------------------------

    private static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    private static final int MI_PERMISO_SMS = 3;
    private final String PARA_ENVIAR_PAGO = "paymenttransactions/dopayment";
    private GridView gvCompanniasCelulares;
    public static RelativeLayout rlContenedorPaso1, rlContenedorPaso2, rlParaMeterCantidadesEnRbPrimerMitad, rlParaMeterCantidadesEnRbSegundaMitad;
    private RelativeLayout rlContenedorPaso3, rlContenedorPaso4, rlTerminarRecargas;
    private Button btnPaso3Recarga, btnPaso4Recarga, btnPaso4Cancelar, btnImprimirRecarga, btnSmsRecarga, btnEMailRecarga;
    public static Button btnPaso2Recarga, btnTerminaRecarga;
    private EditText etNumeroRecarga, etConfirmaNumeroRecarga;
    public static String companniaRecarga, choroGlobalRecargas, choroSms;
    private TextView tvFechaRecarga, tvHoraRecarga, tvImporteRecarga, tvCompanniaRecarga, tvRolloOperacion, tvTituloRecarga;
    private int[] iconosCelulares;
    private String[] montosTelcel, montosAtt, montosMovistar, montosNextel, montosUnefon, nombresCompannias;
    private String fechaRegistro, horaRegistro, companniaTelefonicaStr, storeId, token, autorizationCode, homologarFecha, continuarTrabajando;
    private SharedPreferences preferences;
    private DataBaseInfoServicios dataBaseInfoServicios;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueuePago;
    private int quePasoVamos;
    public static String cantidadRecarga, SENT_SMS;
    public static final String PARAM_LOGO_COMPANNIAS_CELULARE_ID_RESOURCE="id_resource_logo_compannia_celular";
    private DialogCorreoRecargas dialogCorreoRecargas;
    private DialogSmsRecargas dialogSmsRecargas;
    private RelativeLayout lyLoadingPago;
    private BluetoothAdapter paraPrenderBluetooth;
    private Bundle bundleTipoRecarga;
    private Date tomarFechaParaRegistro;
    public static boolean mailEnviadoRecargas = false;
    //private DataBaseManagerTransaccionesImp managerRegistroTransaccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_recargas);

        iniGuiRecargas();
        iniListenersRecargas();
    }

    public void iniGuiRecargas(){
        bundleTipoRecarga = this.getIntent().getExtras();

        preferences = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        cargarSesion();

        ConActionbarView conActionbarView = this;
        conActionbarView.setToolbarValues();


        dataBaseInfoServicios = new DataBaseInfoServicios(getApplicationContext());

        requestQueuePago = Volley.newRequestQueue(Recargas.this);

        gvCompanniasCelulares = (GridView)findViewById(R.id.gv_telefonicas);
        rlContenedorPaso1 = (RelativeLayout)findViewById(R.id.contenedor_paso1_recargas);
        rlContenedorPaso2 = (RelativeLayout)findViewById(R.id.contenedor_paso2_recargas);
        rlContenedorPaso3 = (RelativeLayout)findViewById(R.id.contenedor_paso3_recargas);
        rlContenedorPaso4 = (RelativeLayout)findViewById(R.id.contenedor_paso4_recargas);
        rlParaMeterCantidadesEnRbPrimerMitad = (RelativeLayout)findViewById(R.id.rl_para_meter_las_cantidades_de_recargas_1_50);
        rlParaMeterCantidadesEnRbSegundaMitad = (RelativeLayout)findViewById(R.id.rl_para_meter_las_cantidades_de_recargas_51_100);
        btnPaso2Recarga = (Button)findViewById(R.id.btn_paso2_paso3);
        btnPaso3Recarga = (Button)findViewById(R.id.btn_paso3_paso4);
        btnPaso4Recarga = (Button)findViewById(R.id.btn_paso4_finaliza);
        btnPaso4Cancelar = (Button)findViewById(R.id.btn_paso4_cancelar);
        btnImprimirRecarga = (Button)findViewById(R.id.btn_imprimir_pago_recarga);
        btnSmsRecarga = (Button)findViewById(R.id.btn_sms_recarga);
        btnEMailRecarga = (Button)findViewById(R.id.btn_mail_pago_recarga);
        btnTerminaRecarga = (Button)findViewById(R.id.btn_sin_notificacion_pago_recarga);
        etNumeroRecarga = (EditText) findViewById(R.id.et_numero_recarga);
        etConfirmaNumeroRecarga = (EditText) findViewById(R.id.et_confirma_numero_recarga);
        tvFechaRecarga = (TextView)findViewById(R.id.tv_fecha_operacion_recarga_telefonica);
        tvHoraRecarga = (TextView)findViewById(R.id.tv_hora_operacion_recarga_telefonica);
        tvCompanniaRecarga = (TextView)findViewById(R.id.tv_compannia_numero_recarga_telefonica);
        tvImporteRecarga = (TextView)findViewById(R.id.tv_importe_operacion_recarga_telefonica);
        rlTerminarRecargas = (RelativeLayout)findViewById(R.id.rl_contenedor_ultimopaso_pago_recarga);
        lyLoadingPago = (RelativeLayout)findViewById(R.id.loading_pago);
        tvRolloOperacion = (TextView)findViewById(R.id.tv_rollo_operacion_recarga);
        tvTituloRecarga = (TextView)findViewById(R.id.tv_titulo_recarga);

        paraPrenderBluetooth = BluetoothAdapter.getDefaultAdapter();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        mService = new BluetoothService(this, mHandler);

        conQueTrabajamos();

        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansProBold.otf", false);

    }

    public void iniListenersRecargas(){
        quePasoVamos = 1;
        RecargasGridAdapter recargasGridAdapter = new RecargasGridAdapter(this, iconosCelulares);
        gvCompanniasCelulares.setAdapter(recargasGridAdapter);

        //crearRGConCantidades(montosMovistar);
        //jalarMontos(companniaRecarga);

        btnPaso2Recarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPaso2Recarga.setVisibility(View.GONE);
                rlContenedorPaso2.setVisibility(View.GONE);
                rlContenedorPaso3.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), companniaRecarga + " " +  cantidadRecarga.substring(2), Toast.LENGTH_SHORT).show();

            }
        });

        btnPaso3Recarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNumeroRecarga.length()!=10 || etConfirmaNumeroRecarga.length()!=10){
                    Toast.makeText(getApplicationContext(), "El número celular debe ser de 10 digitos", Toast.LENGTH_SHORT).show();
                } else
                if(!etNumeroRecarga.getText().toString().equals(etConfirmaNumeroRecarga.getText().toString())){
                    Toast.makeText(getApplicationContext(), "No coincide el número celular", Toast.LENGTH_SHORT).show();
                } else {
                    btnPaso3Recarga.setVisibility(View.GONE);
                    rlContenedorPaso3.setVisibility(View.GONE);
                    rlContenedorPaso4.setVisibility(View.VISIBLE);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    final SimpleDateFormat yyyymmddFormat = new SimpleDateFormat("dd MMM yyyy");
                    final SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
                    Date tomarFechaParaRegistro = Calendar.getInstance().getTime();
                    fechaRegistro = yyyymmddFormat.format(tomarFechaParaRegistro);
                    horaRegistro = horaFormat.format(tomarFechaParaRegistro);
                    tvFechaRecarga.setText(fechaRegistro);
                    tvHoraRecarga.setText(horaRegistro);
                    tvCompanniaRecarga.setText(companniaRecarga + "\n" + etConfirmaNumeroRecarga.getText().toString());
                    cantidadRecarga.substring(2);
                    tvImporteRecarga.setText("$ " + cantidadRecarga.substring(2));

                    ocultarTeclado(btnPaso3Recarga);

                }

            }
        });

        btnPaso4Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Recargas.this);
                //builder.setIcon(R.drawable.ic_mensajes);
                builder.setTitle(getResources().getString(R.string.cancelar));
                builder.setMessage(getResources().getString(R.string.seguro_que_cancelas));
                builder.setCancelable(false);
                builder.setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.si), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Recargas.this, "Transacción cancelada", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(Recargas.this, Recargas.class));
                    }
                });
                builder.show();
            }
        });

        btnPaso4Recarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyLoadingPago.setVisibility(View.VISIBLE);
                float enLaBolsa = preferences.getFloat("enLaBolsa", 0);
                if(enLaBolsa - Float.valueOf(cantidadRecarga.substring(2)) > 0){
                    btnPaso4Cancelar.setVisibility(View.GONE);
                    btnPaso4Recarga.setVisibility(View.GONE);
                    procesarPagoRecarga();
                } else {
                    lyLoadingPago.setVisibility(View.GONE);
                    Toast.makeText(Recargas.this, "No cuentas con saldo suficiente para realizar esta operación", Toast.LENGTH_LONG).show();
                }


                /*if(ActivityCompat.checkSelfPermission(Recargas.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED  &&
                        (ActivityCompat.checkSelfPermission(Recargas.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
                    Intent intentSms = new Intent(Recargas.this, MainActivity.class);
                    SmsManager smsManager = SmsManager.getDefault();
                    String sms = "Tu recarga de " + cantidadRecarga + " se realizo con exito.";
                    String celularParaAbono = etConfirmaNumeroRecarga.getText().toString();
                    smsManager.sendTextMessage(celularParaAbono, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "Recarga exitosa", Toast.LENGTH_SHORT).show();
                    startActivity(intentSms);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Recargas.this);
                    builder.setIcon(R.drawable.ic_mensajes);
                    builder.setTitle(getResources().getString(R.string.permiso_mensajes));
                    builder.setMessage(getResources().getString(R.string.expliacion_permiso_mensajes));
                    builder.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Recargas.this,
                            new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                            MI_PERMISO_SMS);

                        }
                    });
                    builder.show();
                }*/
            }
        });

        btnImprimirRecarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paraPrenderBluetooth.isEnabled()){
                    Intent dispositivosIntent = new Intent(Recargas.this, BuscarDispositivos.class);
                    startActivityForResult(dispositivosIntent, REQUEST_CONNECT_DEVICE);

                } else {
                    AlertDialog.Builder builderBluetooth = new AlertDialog.Builder(Recargas.this);
                    builderBluetooth.setTitle(getResources().getString(R.string.encender_bluetooth));
                    builderBluetooth.setMessage(getResources().getString(R.string.expliacion_bluetooth));
                    builderBluetooth.setIcon(getResources().getDrawable(R.drawable.ic_bluetooth));
                    builderBluetooth.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            paraPrenderBluetooth.enable();
                        }
                    });
                    builderBluetooth.show();
                }
            }
        });

        btnEMailRecarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abriDialogCorreo();
            }
        });

        btnSmsRecarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Servicio temporalmente no disponible", Toast.LENGTH_LONG).show();
                //abriDialogSms(choroSms);

            }
        });

        btnTerminaRecarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailEnviadoRecargas = false;
                startActivity(new Intent(Recargas.this, MainActivity.class));
                finish();
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
                startActivity(new Intent(Recargas.this, MainActivity.class));
                finish();
                break;
        }
        return true;
    }

    public void procesarPagoRecarga(){
        final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date paraPasarFecha = Calendar.getInstance().getTime();
        final String fechaVenta = yyyyMMdd.format(paraPasarFecha);

        String stringUrl = getResources().getString(R.string.base_api_lab) + PARA_ENVIAR_PAGO;
        JSONObject postParamsPago = new JSONObject();
        try {
            storeId = preferences.getString("storeId", "");
            String userId = preferences.getString("numeroDeUsuarioSession", "");

            postParamsPago.put("storeId", storeId);
            postParamsPago.put("userId", userId);

            JSONObject postService = new JSONObject();
            postService.put("serviceId", dataBaseInfoServicios.getServiceID(companniaRecarga + " " + cantidadRecarga.substring(2)));
            postService.put("sku", dataBaseInfoServicios.getSku(companniaRecarga + " " + cantidadRecarga.substring(2)));
            postParamsPago.put("service" ,postService);

            JSONObject paymentDetail = new JSONObject();
            paymentDetail.put("paymentAmount", cantidadRecarga.substring(2));
            paymentDetail.put("reference", etConfirmaNumeroRecarga.getText().toString());
            postParamsPago.put("paymentDetail", paymentDetail);

            JSONObject commissionData = new JSONObject();
            commissionData.put("commissionUserAmount", 0.00);
            commissionData.put("taxAmount", 0.00);
            postParamsPago.put("commissionData", commissionData);

            JSONObject transactionControl = new JSONObject();
            transactionControl.put("requestDateTime", fechaVenta+"Z");
            transactionControl.put("uniqueId", storeId+userId);
            postParamsPago.put("transactionControl", transactionControl);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, stringUrl, postParamsPago, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("respuesta recarga", response.toString());
                try {
                    JSONObject respuestaTransaccion = response.getJSONObject("transactionResponse");
                    int codigoRespuesta = respuestaTransaccion.getInt("responseCode");
                    String descripcionRespuesta =  respuestaTransaccion.getString("responseDescription");

                    JSONObject respuestaPayment = response.getJSONObject("paymentResponseData");
                    String idTransaccion = respuestaPayment.getString("transactionId");
                    String codigoAutorizacion = respuestaPayment.getString("authorizationCode");
                    String tiempoEjecucion = respuestaPayment.getString("executionDateTime");
                    String idUnico = respuestaPayment.getString("uniqueId");
                    String estatusTransaccion = respuestaPayment.getString("transactionStatus");
                    autorizationCode = codigoAutorizacion;
                    //tvNumeroOperacion.setText(codigoAutorizacion);
                    homologarFecha = tiempoEjecucion;
                    if(homologarFecha.length() != 0){
                        fechaRecibo = homologarFecha.substring(0,10) + "   " + homologarFecha.substring(11, 19);

                    }

                    if(codigoRespuesta == 0){
                        yaParaAcabarLaRecarga();
                    } else {
                        lyLoadingPago.setVisibility(View.GONE);
                        Toast.makeText(Recargas.this, "La operación no pudo ser procesada.\nFavor de validar el monto y la referencia", Toast.LENGTH_LONG).show();
                        btnPaso4Cancelar.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("valio madres recarga", error.toString());
                lyLoadingPago.setVisibility(View.GONE);
                btnPaso4Cancelar.setVisibility(View.VISIBLE);
                Toast.makeText(Recargas.this, "Transacción rechazada", Toast.LENGTH_SHORT).show();

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueuePago.add(jsonObjectRequest);
    }

    public void yaParaAcabarLaRecarga(){
        lyLoadingPago.setVisibility(View.GONE);
        rlContenedorPaso4.setVisibility(View.GONE);

        rlTerminarRecargas.setVisibility(View.VISIBLE);

        tvTituloRecarga.setText(getResources().getString(R.string.transaccion_exitosa));

        choroGlobalRecargas = "Comercio: " + storeId +
                "\n" + String.valueOf(dataBaseInfoServicios.getServicio(companniaRecarga + " " + cantidadRecarga.substring(2)))
                + "\n" + "IVA: $ 0.00" + "\n" + "Comisión: $ 0.00" + "\n" + "Total: $ " + String.valueOf(cantidadRecarga.substring(2)) +
                "\nReferencia: " + etConfirmaNumeroRecarga.getText().toString() + "\n" +
                tvFechaRecarga.getText().toString() + " " + tvHoraRecarga.getText().toString()
                + "\nAut: " + autorizationCode;

        final SimpleDateFormat ddMMyyHHmmss = new SimpleDateFormat("ddMMyy");
        //final String paraSms = ddMMyyHHmmss.format(tomarFechaParaRegistro);

        choroSms = "Comercio: " + storeId + "\nPago: " + companniaRecarga + " $ " + cantidadRecarga.substring(2) + "\n";
        /*if(comision > 0){
            choroSms += "Comisiòn: $ " + String.valueOf(comision)
                    +"\nIva: $ "+ String.valueOf(iva);
        }*/
        choroSms += "\nTotal: $ " + String.valueOf(cantidadRecarga.substring(2))
                + "\nRef: " + etConfirmaNumeroRecarga.getText().toString() + "\n"
                + "Aut: " + autorizationCode;

        referenciaRecibo = etConfirmaNumeroRecarga.getText().toString();
        montoRecibo = "$ " + cantidadRecarga.substring(2);

        rlContenedorPaso4.setVisibility(View.VISIBLE);
        //tvTituloNumeroOperacion.setVisibility(View.VISIBLE);
        //tvNumeroOperacion.setVisibility(View.VISIBLE);

    }

    public void cargarSesion(){
        preferences = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        token = preferences.getString("yaConTokenSession", "");
        comercioRecibo = preferences.getString("StoreName", "");
        continuarTrabajando = preferences.getString("trabajandoRecargas", "");

    }

    public void ocultarTeclado(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    @Override
    public void setToolbarValues() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(bundleTipoRecarga != null){
            if(bundleTipoRecarga.getString("queProcede").equals(getResources().getString(R.string.tiempo_aire))){
                getSupportActionBar().setTitle(R.string.tiempo_aire);

            } else if(bundleTipoRecarga.getString("queProcede").equals(getResources().getString(R.string.paquetes_datos))){
                getSupportActionBar().setTitle(R.string.paquetes_datos);
            }
        } else {
            getSupportActionBar().setTitle(continuarTrabajando);
        }


    }

    public void abriDialogSms(String masChoros){
        dialogSmsRecargas = DialogSmsRecargas.newInstance(this.getResources().getString(R.string.enviar_comprobante), this.getResources().getString(R.string.ingresa_phone));
        dialogSmsRecargas.show(getSupportFragmentManager(), "dialogSms");

        android.app.Fragment frag = getFragmentManager().findFragmentByTag("dialogSms");

        if(frag != null){
            getFragmentManager().beginTransaction().remove(frag).commit();
        }

    }

    public void abriDialogCorreo(){
        dialogCorreoRecargas = DialogCorreoRecargas.newInstance(String.valueOf(dataBaseInfoServicios.getServiceID(companniaRecarga)), this.getResources().getString(R.string.enviar_comprobante), this.getResources().getString(R.string.ingresa_correo));
        dialogCorreoRecargas.show(getSupportFragmentManager(), "dialogCorreo");

        android.app.Fragment frag = getFragmentManager().findFragmentByTag("dialogCorreo");

        if(frag != null){
            getFragmentManager().beginTransaction().remove(frag).commit();
        }

    }

    public void conQueTrabajamos(){
        if(bundleTipoRecarga != null) {
            if(bundleTipoRecarga.getString("queProcede").equals(getResources().getString(R.string.tiempo_aire))){
                iconosCelulares = new int[] {R.drawable.att, R.drawable.movistar, R.drawable.nextel, R.drawable.telcel,R.drawable.unefon};

            } else if(bundleTipoRecarga.getString("queProcede").equals(getResources().getString(R.string.paquetes_datos))){
                iconosCelulares = new int[] {R.drawable.internet_amigo, R.drawable.amigo_sin_limite};
            }
        } else {
            if(continuarTrabajando.equals(getResources().getString(R.string.tiempo_aire))){
                iconosCelulares = new int[] {R.drawable.att, R.drawable.movistar, R.drawable.nextel, R.drawable.telcel,R.drawable.unefon};

            } else if(continuarTrabajando.equals(getResources().getString(R.string.paquetes_datos))){
                iconosCelulares = new int[] {R.drawable.internet_amigo, R.drawable.amigo_sin_limite};
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE: {
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            BuscarDispositivos.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    if (BluetoothAdapter.checkBluetoothAddress(address)) {
                        BluetoothDevice device = mBluetoothAdapter
                                .getRemoteDevice(address);
                        // Attempt to connect to the device
                        mService.connect(device);
                    }
                }
                break;
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (DEBUG)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Print_Recibo_Avon();//

                            break;
                        case BluetoothService.STATE_CONNECTING:

                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:

                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void Print_Recibo_Avon(){
        Print_BMP();

        SendDataString("\nPago " + companniaRecarga +"\n");
        SendDataString(fechaRecibo);
        SendDataString("\n\nComercio: " + comercioRecibo);
        SendDataString("\nId: " + storeId);

        SendDataString("\nAut: " + autorizationCode);
        SendDataString("\nRef: " + referenciaRecibo);
        //SendDataString("\nServicio:     " +  montoRecibo);
        /*if(comision > 0.00){
            SendDataString("\nComision:     $ " +  comision);
            SendDataString("\nIVA comision: $ " +  iva);
        }*/
        SendDataString("\nTotal:        "   +  montoRecibo);
        SendDataString("\n\n" + dataBaseInfoServicios.getPieTicket(companniaRecarga + " " + cantidadRecarga.substring(2)));
        if(btnImprimirRecarga.getText().toString().equals(getResources().getString(R.string.reimprimir))){
            SendDataString("\n\n****COPIA****");
        }
        SendDataString("\n\nVisitanos en www.yastas.com\n\n\n");

        btnTerminaRecarga.setBackgroundColor(getResources().getColor(R.color.verde_yastas));
        btnTerminaRecarga.setEnabled(true);
        btnImprimirRecarga.setText(getResources().getString(R.string.reimprimir));
        mService.stop();

    }

    private void SendDataString(String data) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, "No conectado", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (data.length() > 0) {
            try {
                mService.write(data.getBytes("GBK"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void SendDataByte(byte[] data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, "Impresora no conectada", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mService.write(data);
    }

    private void Print_BMP(){
        //	byte[] buffer = PrinterCommand.POS_Set_PrtInit();
        Bitmap mBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.logo_ticket)).getBitmap();
        int nMode = 0;
        int nPaperWidth = 384;
        if(mBitmap != null)
        {
            /**
             * Parameters:
             * mBitmap  要打印的图片
             * nWidth   打印宽度（58和80）
             * nMode    打印模式
             * Returns: byte[]
             */
            byte[] data = PrintPicture.POS_PrintBMP(mBitmap, nPaperWidth, nMode);
            //	SendDataByte(buffer);
            SendDataByte(Command.ESC_Init);
            SendDataByte(Command.LF);
            SendDataByte(data);
            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }
    }

    public String CantidadParaTaradosString(String delEditText){
        String ahoraSi = "";
        String fraccionStr = delEditText.substring(delEditText.indexOf(".")+1, delEditText.length());
        if(delEditText.contains(".")){
            switch (fraccionStr.length()){
                case 0:
                    ahoraSi = delEditText + "00";
                    break;
                case 1:
                    ahoraSi = delEditText + "0";
                    break;
                case 2:
                    ahoraSi = delEditText.substring(0, delEditText.indexOf(".")+3);
                    break;
                default:
                    ahoraSi = delEditText.substring(0, delEditText.indexOf(".")+3);
                    break;
            }

        } else {
            ahoraSi = delEditText + ".00";
        }
        return ahoraSi;
    }
}
