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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.firestore.model.value.StringValue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.OperacionesPIMGridAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.ProductosPIMAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.BuscarDispositivos;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogCorreo;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogSms;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.BluetoothService;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.Command;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.PrintPicture;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.PrinterCommand;
import me.anwarshahriar.calligrapher.Calligrapher;

import com.gentera.sdk.GenteraSDK;
import com.gentera.sdk.helpers.commons.Transaction;
import com.gentera.sdk.helpers.commons.TransactionError;
import com.gentera.sdk.modules.pim.model.Category;
import com.gentera.sdk.modules.pim.model.Product;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.VISIBLE;
import static gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.ProcesoDePago.etReferenciaCosmeticos;

public class PagoConPIM extends AppCompatActivity implements ConActionbarView {
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
    /*******************************************************************************************************/
    private final String PARA_ENVIAR_PAGO = "paymenttransactions/dopayment";
    private static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    public static final int PERMISO_CAMERA = 2;
    private Bundle bundleServicio;
    private String encabezado, hijos, code, storeId, token, comercioRecibo, autorizationCode, fechaRecibo, homologarFecha, cantidadProcesada, folioAvonRecibo, referenciaRecibo, montoRecibo, storeName;
    private JSONObject hijosJson;
    public static GridView gvSubCategoriasPim;
    private List<Category> listaHijos;
    public static RelativeLayout rlContenedorPrincipalPagoPIM, rlContenedorPaso1PIM;
    private RelativeLayout rlContenedorScannerPIM, rlContenedorPaso2PIM, rlLoadingPIM, rlContenedorUltimoPasoPIM;
    public static TextView tvCompanniaPagoPim;
    public static ImageButton ibActivaEscanerPIM;
    public static boolean tieneScannerPIM;

    public static EditText etReferenciaPagoPIM, etMontoPagoPIM;
    private ImageView btnEscanearPIM;
    private Button btnCancelarPagoPIM, btnFinalizarPagoPIM, btnSMSPIM, btnMailPIM, btnPrintPIM;
    public static Button btnPaso12PIM;
    private SharedPreferences preferences;
    public static String externalSku, bussinesServiceId, mascaraReferencia, companniaPIM, mensajeGlobalPIM, mensajeSmsPIM, footerRecibo, leyendaRecibo, leyenda1, leyenda2, leyenda3, footerReciboPIM, headerReciboPIM, leyendaDinamica;
    public static int leyendaDinamicaNum;
    public static float comisionstr, comisioinTax;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueuePago;
    private TextView tvNumeroTransaccionPIM, tvFechaOperacionPIM, tvHoraOperacionPIM, tvRolloOperacionPIM, tvMontoOperacionPIM, tvIvaOperacionPIM, tvComisionOperacionPIM, tvTotalOperacionPIM, tvTituloProcesoPIM;
    public static int longitudReferenciaPIM;
    public static float montoMinimoPIM, montoMaximoPIM;
    private Date tomarFechaParaRegistro;
    public static float iva, montoTotal, comision;
    private LinearLayout llContenedorBotonesCancelAcept;
    private DialogCorreo dialogCorreo;
    public static Button btnTerminaPagoPIM;
    private BluetoothAdapter paraPrenderBluetooth;
    private DialogSms dialogSms;
    public static boolean mailEnviadoPIM = false;

    private SurfaceView sfvEscannerPIM;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pago_con_pim);

        iniGuiPagoPim();
        iniListenersPim();
    }

    public void iniGuiPagoPim(){
        //bundleServicio = this.getIntent().getExtras();
        //encabezado = bundleServicio.getString("paraTitulo");
        cargarSesion();
        preferences = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);

        requestQueuePago = Volley.newRequestQueue(PagoConPIM.this);

        //hijos = bundleServicio.getString("hijos");
        //code = bundleServicio.getString("code");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Category>>(){}.getType();
        listaHijos = gson.fromJson(hijos, type);
        gvSubCategoriasPim = (GridView)findViewById(R.id.gv_sub_categorias_pim);
        rlContenedorPrincipalPagoPIM = (RelativeLayout)findViewById(R.id.rl_contenedor_pricinpal_pago_pim);
        rlContenedorPaso1PIM = (RelativeLayout)findViewById(R.id.contenedor_paso1_proceso_pago_pim);
        tvCompanniaPagoPim = (TextView)findViewById(R.id.tv_nombre_servicio_pago_pim);
        ibActivaEscanerPIM = (ImageButton)findViewById(R.id.ib_activa_escanner_pim);
        rlContenedorScannerPIM = (RelativeLayout)findViewById(R.id.rl_contenedor_scanner_pim);
        sfvEscannerPIM = (SurfaceView)findViewById(R.id.scanner_view_pim);
        //btnEscanearPIM = (ImageView) findViewById(R.id.btn_scannear_pim);
        etMontoPagoPIM = (EditText)findViewById(R.id.et_monto_proceso_pago_pim);
        btnPaso12PIM= (Button)findViewById(R.id.btn_paso1_paso2_pim);
        rlContenedorPaso2PIM = (RelativeLayout)findViewById(R.id.rl_contenedor_paso2_proceso_pago_pim);
        btnCancelarPagoPIM = (Button)findViewById(R.id.btn_cancelar_pago_pim);
        rlLoadingPIM = (RelativeLayout)findViewById(R.id.loading_pago_pim);
        tvNumeroTransaccionPIM = (TextView)findViewById(R.id.tv_numero_transaccion_pim);
        tvFechaOperacionPIM = (TextView)findViewById(R.id.tv_fecha_operacion_proceso_pago_pim);
        tvHoraOperacionPIM = (TextView)findViewById(R.id.tv_hora_operacion_proceso_pago_pim);
        tvRolloOperacionPIM = (TextView)findViewById(R.id.tv_rollo_operacion_proceso_pago_pim);
        tvMontoOperacionPIM = (TextView)findViewById(R.id.tv_monto_operacion_proceso_pago_pim);
        tvIvaOperacionPIM = (TextView)findViewById(R.id.tv_monto_iva_operacion_proceso_pago_pim);
        tvComisionOperacionPIM = (TextView)findViewById(R.id.tv_monto_comision_operacion_proceso_pago_pim);
        tvTotalOperacionPIM = (TextView)findViewById(R.id.tv_monto_total_operacion_proceso_pago_pim);
        tvTituloProcesoPIM = (TextView)findViewById(R.id.tv_numero_proceso_pago_pim);
        llContenedorBotonesCancelAcept = (LinearLayout)findViewById(R.id.ll_contenedor_btns_cancelar_aceptar_pim);
        rlContenedorUltimoPasoPIM = (RelativeLayout)findViewById(R.id.rl_contenedor_ultimo_paso_pago_pim);
        btnSMSPIM = (Button)findViewById(R.id.btn_sms_proceso_pago_pim);
        btnMailPIM = (Button)findViewById(R.id.btn_mail_pago_proceso_pago_pim);
        btnPrintPIM = (Button)findViewById(R.id.btn_imprimir_pago_proceso_pago_pim);
        btnFinalizarPagoPIM = (Button)findViewById(R.id.btn_finalizar_pago_pim);
        btnTerminaPagoPIM = (Button)findViewById(R.id.btn_termina_pago_pim);
        btnTerminaPagoPIM.setEnabled(false);

        sfvEscannerPIM.setZOrderMediaOverlay(true);

        etReferenciaPagoPIM = (EditText)findViewById(R.id.et_referencia_proceso_pago_pim);

        paraPrenderBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (listaHijos.size() <= 0) {
            try {
                runOnUiThread(new Thread(new Runnable() {
                    public void run() {
                        try {
                            GenteraSDK.getInstance().Pim().getProductsOnCategory(code, new Transaction() {
                                @Override
                                public <Product> void onTransactionSuccess(List<Product> list) {
                                    Log.i(TAG, "Exito en la consulta de productos: >> " + list.toString());
                                    ProductosPIMAdapter productosPIMAdapter = new ProductosPIMAdapter(PagoConPIM.this, list, code);
                                    gvSubCategoriasPim.setAdapter(productosPIMAdapter);
                                }

                                @Override
                                public void onTransactionFailed(TransactionError error) {
                                    Log.e(TAG, "ERROR en la consulta de oriductos: >> " + error.getMessage());
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
        } else {
            try {
//            runOnUiThread (new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        GenteraSDK.getInstance().Pim().getCategoriesCatalog(new Transaction() {
//                            @Override
//                            public <Category> void onTransactionSuccess(List<Category> list) {
//                                Log.i(TAG, "Exito en la consulta de categorias: >> " + list.toString());
//                                List<Category> lis= (List<Category>) list;
                OperacionesPIMGridAdapter operacionesPIMGridAdapter = new OperacionesPIMGridAdapter(PagoConPIM.this, listaHijos,false);
                gvSubCategoriasPim.setAdapter(operacionesPIMGridAdapter);
//                            }
//
//                            @Override
//                            public void onTransactionFailed(TransactionError error) {
//                                Log.e(TAG, "ERROR en la consulta de categorias: >> " + error.getMessage());
//                            }
//                        });
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }));

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }


    }

    public void iniListenersPim(){
        ConActionbarView conActionbarView = this;
        conActionbarView.setToolbarValues();

        ibActivaEscanerPIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    etReferenciaPagoPIM.setEnabled(false);
                    etReferenciaPagoPIM.setVisibility(View.GONE);
                    etMontoPagoPIM.setEnabled(false);
                    etMontoPagoPIM.setVisibility(View.GONE);
                    btnPaso12PIM.setEnabled(false);
                    btnPaso12PIM.setVisibility(View.GONE);
                /*if(rlContenedorScannerPIM.getVisibility() == VISIBLE){

                }*/

                activaEscannerPIM();
                etReferenciaPagoPIM.setEnabled(true);
                etReferenciaPagoPIM.setVisibility(View.VISIBLE);
                etMontoPagoPIM.setEnabled(true);
                etMontoPagoPIM.setVisibility(View.VISIBLE);
                btnPaso12PIM.setEnabled(true);
                btnPaso12PIM.setVisibility(View.VISIBLE);
                //escannerManual();
            }
        });

      /*  btnEscanearPIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escannerManual();
                etReferenciaPagoPIM.setEnabled(true);
                etReferenciaPagoPIM.setVisibility(View.VISIBLE);
                etMontoPagoPIM.setEnabled(true);
                etMontoPagoPIM.setVisibility(View.VISIBLE);
                btnPaso12PIM.setEnabled(true);
                btnPaso12PIM.setVisibility(View.VISIBLE);
                /*if(rlContenedorScannerPIM.getVisibility() != VISIBLE){

                }*/

/*

            }
        });*/

        btnPaso12PIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etReferenciaPagoPIM.getText().toString().length() > 0 && etMontoPagoPIM.getText().toString().length() > 0){
                    if(Float.parseFloat(etMontoPagoPIM.getText().toString()) > 0){
                        if(etReferenciaPagoPIM.getText().toString().length() == longitudReferenciaPIM){
                            pasarDatosParaPago();
                        } else {
                            Toast.makeText(PagoConPIM.this, "La referencia debe ser de "+ longitudReferenciaPIM + " digitos", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if(Float.parseFloat(etMontoPagoPIM.getText().toString()) < montoMinimoPIM) {
                            Toast.makeText(getApplicationContext(), "Introducir monto minimo", Toast.LENGTH_SHORT).show();
                        } else if (Float.parseFloat(etMontoPagoPIM.getText().toString()) < montoMaximoPIM){
                            Toast.makeText(getApplicationContext(), "El monto excede el limite permitido", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Por favor introduce referencia y monto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelarPagoPIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PagoConPIM.this);
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
                        Toast.makeText(getApplicationContext(), "Transaccion cancelada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PagoConPIM.this, PagoConPIM.class));
                        finish();
                    }
                });
                builder.show();

            }

        });

        btnFinalizarPagoPIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadingPIM.setVisibility(View.VISIBLE);
                float enLaBolsa = preferences.getFloat("enLaBolsa", 0);
                if(enLaBolsa - Float.valueOf(etMontoPagoPIM.getText().toString()) > 0){
                    btnCancelarPagoPIM.setVisibility(View.GONE);
                    btnFinalizarPagoPIM.setVisibility(View.GONE);
                    procesarPagoServicios();
                } else {
                    rlLoadingPIM.setVisibility(View.GONE);
                    Toast.makeText(PagoConPIM.this, "No cuentas con saldo suficiente para realizar esta operación", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnSMSPIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abriDialogSms(mensajeGlobalPIM);
            }
        });

        btnMailPIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abriDialogCorreo();

            }
        });

        btnPrintPIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paraPrenderBluetooth.isEnabled()){
                    Intent dispositivosIntent = new Intent(PagoConPIM.this, BuscarDispositivos.class);
                    startActivityForResult(dispositivosIntent, REQUEST_CONNECT_DEVICE);

                } else {
                    AlertDialog.Builder builderBluetooth = new AlertDialog.Builder(PagoConPIM.this);
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

        btnTerminaPagoPIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailEnviadoPIM = false;
                leyendaDinamica = "";
                leyenda1 = "";
                leyenda2 = "";
                leyenda3 = "";
                startActivity(new Intent(PagoConPIM.this, MainActivity.class));
                finish();

            }
        });

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        mService = new BluetoothService(this, mHandler);


        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansProBold.otf", true);

    }

    public void abriDialogSms(String masChoros){
        dialogSms = DialogSms.newInstance(this.getResources().getString(R.string.enviar_comprobante), this.getResources().getString(R.string.ingresa_phone));
        dialogSms.show(getSupportFragmentManager(), "dialogSms");

        android.app.Fragment frag = getFragmentManager().findFragmentByTag("dialogSms");

        if(frag != null){
            getFragmentManager().beginTransaction().remove(frag).commit();
        }

    }

    public void abriDialogCorreo(){
        dialogCorreo = DialogCorreo.newInstance(bussinesServiceId, this.getResources().getString(R.string.enviar_comprobante), this.getResources().getString(R.string.ingresa_correo), getApplicationContext());
        dialogCorreo.show(getSupportFragmentManager(), "dialogCorreo");

        android.app.Fragment frag = getFragmentManager().findFragmentByTag("dialogCorreo");

        if(frag != null){
            getFragmentManager().beginTransaction().remove(frag).commit();
        }

    }

    public void pasarDatosParaPago(){
        rlContenedorPaso1PIM.setVisibility(View.GONE);
        btnPaso12PIM.setVisibility(View.GONE);
        rlContenedorPaso2PIM.setVisibility(View.VISIBLE);

        cantidadProcesada = CantidadParaTaradosString(etMontoPagoPIM.getText().toString());
        Log.i("parkinson", cantidadProcesada);

        final SimpleDateFormat yyyymmddFormat = new SimpleDateFormat("dd MM yyyy");
        final SimpleDateFormat reciboFormat = new SimpleDateFormat("dd MMM yyyy");
        final SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
        tomarFechaParaRegistro = Calendar.getInstance().getTime();
        final String fechaRegistro = yyyymmddFormat.format(tomarFechaParaRegistro);
        final String horaRegistro = horaFormat.format(tomarFechaParaRegistro);
        tvFechaOperacionPIM.setText(fechaRegistro);
        tvHoraOperacionPIM.setText(horaRegistro);
        tvRolloOperacionPIM.setText(tvCompanniaPagoPim.getText().toString() + "\n"
                + "No. Referencia " + etReferenciaPagoPIM.getText().toString());
        tvMontoOperacionPIM.setText("$ "+ cantidadProcesada);

        //montoParcial = Float.parseFloat(etMontoCosmeticos.getText().toString());
        DecimalFormat df = new DecimalFormat("#0.00");
        iva = comisioinTax;
        comision = comisionstr;
        //String strMontoParcial = String.valueOf(montoParcial);
        montoTotal = (comisionstr + comisioinTax + Float.parseFloat(etMontoPagoPIM.getText().toString()));
        String strMontoTotal = String.valueOf(montoTotal);
        String strIva = String.valueOf(iva/100*16);

        tvIvaOperacionPIM.setText("$ " + df.format(iva));
        tvComisionOperacionPIM.setText("$ " + df.format(comisionstr));

        tvTotalOperacionPIM.setText("$ " + String.format("%.2f", montoTotal));
        tvTituloProcesoPIM.setText("Confirma operación");

        ocultarTeclado(btnPaso12PIM);

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

    public void cargarSesion(){
        preferences = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        token = preferences.getString("yaConTokenSession", "");
        comercioRecibo = preferences.getString("StoreName", "");
        encabezado = preferences.getString("encabezadoPagoPIM", "");
        hijos = preferences.getString("hijos", "");
        code = preferences.getString("code", "");
        storeName = preferences.getString("StoreName", "");

    }

    public void procesarPagoServicios(){
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
            postService.put("serviceId", bussinesServiceId);
            postService.put("sku", externalSku);
            postParamsPago.put("service" ,postService);

            JSONObject paymentDetail = new JSONObject();
            if(code.equals("TelcelTopUp") || code.equals("MovistarTopUp") || code.equals("UnefonTopUp") || code.equals("IusacellTopUp") || code.equals("TelcelPacks") || code.equals("NextelTopUp")){
                paymentDetail.put("paymentAmount", etMontoPagoPIM.getText().toString().replace(" ", ""));
            } else {
                paymentDetail.put("paymentAmount", cantidadProcesada);
            }
            paymentDetail.put("reference", etReferenciaPagoPIM.getText().toString());
            postParamsPago.put("paymentDetail", paymentDetail);

            JSONObject commissionData = new JSONObject();
            commissionData.put("commissionUserAmount", String.valueOf(comisionstr));
            commissionData.put("taxAmount", String.valueOf(comisioinTax));
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
                Log.i("respuesta pago", response.toString());
                try {
                    JSONObject respuestaTransaccion = response.getJSONObject("transactionResponse");
                    int codigoRespuesta = respuestaTransaccion.getInt("responseCode");
                    leyendaDinamica = respuestaTransaccion.getString("responseDescription");
                    String descripcionRespuesta =  respuestaTransaccion.getString("responseDescription");

                    JSONObject respuestaPayment = response.getJSONObject("paymentResponseData");
                    String idTransaccion = respuestaPayment.getString("transactionId");
                    String codigoAutorizacion = respuestaPayment.getString("authorizationCode");
                    String tiempoEjecucion = respuestaPayment.getString("executionDateTime");
                    String idUnico = respuestaPayment.getString("uniqueId");
                    String estatusTransaccion = respuestaPayment.getString("transactionStatus");
                    autorizationCode = codigoAutorizacion;
                    tvNumeroTransaccionPIM.setText(codigoAutorizacion);
                    homologarFecha = tiempoEjecucion;
                    if(homologarFecha.length() != 0){
                        fechaRecibo = homologarFecha.substring(0,10) + "   " + homologarFecha.substring(11, 19);
                    }

                    if(codigoRespuesta == 0){
                        yaParaAcabar();
                    } else {
                        rlLoadingPIM.setVisibility(View.GONE);
                        Toast.makeText(PagoConPIM.this, "La operación no pudo ser procesada.\nFavor de validar el monto y la referencia", Toast.LENGTH_LONG).show();
                        btnCancelarPagoPIM.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Log.e(TAG,error ==null?"error desconocido":error.getMessage());
                rlLoadingPIM.setVisibility(View.GONE);
                btnCancelarPagoPIM.setVisibility(View.VISIBLE);
                Toast.makeText(PagoConPIM.this, "Transacción rechazada", Toast.LENGTH_SHORT).show();

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

    public void yaParaAcabar(){
        rlLoadingPIM.setVisibility(View.GONE);
        llContenedorBotonesCancelAcept.setVisibility(View.GONE);
        tvTituloProcesoPIM.setText(getResources().getString(R.string.transaccion_exitosa));

        final SimpleDateFormat ddMMyyHHmmss = new SimpleDateFormat("ddMMyy");
        final String paraSms = ddMMyyHHmmss.format(tomarFechaParaRegistro);
        tvHoraOperacionPIM.setText(homologarFecha.substring(11, 19));

        mensajeGlobalPIM = companniaPIM + "\nFecha: " + homologarFecha.substring(0,10) + "    Hora: " + homologarFecha.substring(11, 19) +  "\n" + "Id comercio: " + storeId +
                "\nComercio: " + storeName + "\nRef. " + etReferenciaPagoPIM.getText().toString()+
        "\nAut. " + autorizationCode + "\nImporte: " + cantidadProcesada;
        if(comision > 0){
            mensajeGlobalPIM += "\nComisión: $ " + String.valueOf(comision)
                    +"\nIva: $ "+ String.valueOf(iva);
            //String totalStr = String.valueOf(comision + iva + cantidadProcesada);
            //float total = comision + iva + Float.parseFloat(cantidadProcesada);

            mensajeGlobalPIM += "\nTotal: $ " + String.format("%.2f", montoTotal);
        }
        if(companniaPIM.equals("Pago AVON") || companniaPIM.equals("PAGO AVON") || companniaPIM.equals("AVON") || companniaPIM.equals("Avon")){
            mensajeGlobalPIM +="\nFol: " + autorizationCode + cantidadProcesada.replace(".", "") + paraSms + homologarFecha.substring(11, 19).replace(":", "");
        }

        switch (leyendaDinamicaNum){
            case 0:
                if(leyenda1 != null){
                    if(leyenda1.length() > 0){
                        mensajeGlobalPIM += "\n\n"+ leyenda1 + "\n";

                    }
                }
                if(leyenda2 != null){
                    if(leyenda2.length() > 0){
                        mensajeGlobalPIM += "\n" + leyenda2 + "\n";

                    }
                }
                if(leyenda3 != null){
                    if(leyenda3.length() > 0){
                        mensajeGlobalPIM += "\n" + leyenda3 + "\n";

                    }
                }
                break;
            case 1:

                mensajeGlobalPIM += "\n"+ leyenda1 + "\n";
                if(leyenda2 != null){
                    if(leyenda2.length() > 0){
                        mensajeGlobalPIM +="\n"+ leyenda2 + "\n";

                    }
                }
                if(/*leyenda3.length() > 0 && */leyenda3 != null){
                    if(leyenda3.length() > 0){
                        mensajeGlobalPIM +="\n"+ leyenda3 + "\n";

                    }
                }
                break;
            case 2:
                if(leyenda1 != null){
                    if(leyenda1.length() > 0){
                        mensajeGlobalPIM += "\n"+leyenda1 + "\n";

                    }
                }
                //SendDataString("\n\n" + leyendaDinamica + "\n");
                mensajeGlobalPIM += "\n"+ leyenda2 + "\n";
                if(leyenda3 != null){
                    if(leyenda3.length() > 0){
                        mensajeGlobalPIM +="\n"+ leyenda3 + "\n";

                    }
                }
                break;
            case 3:
                if(leyenda1 != null){
                    if(leyenda1.length() > 0){
                        mensajeGlobalPIM += "\n"+leyenda1 + "\n";

                    }
                }
                if(leyenda2 != null){
                    if(leyenda2.length() > 0){
                        mensajeGlobalPIM += "\n"+ leyenda2 + "\n";

                    }
                }
                mensajeGlobalPIM += "\n"+ leyenda3;
                //SendDataString("\n\n" + leyendaDinamica + "\n");
                break;

        }

        mensajeSmsPIM = companniaPIM + "\nComercio: " + storeId + "\n" + "Monto: $ " + cantidadProcesada + "\n";
        if(comision > 0){
            mensajeSmsPIM += "Comisión: $ " + String.valueOf(comision)
                    +"\nIva: $ "+ String.valueOf(iva);
        }
        mensajeSmsPIM += "\nTotal: $ " + String.format("%.2f", montoTotal)
                + "\nRef: " + etReferenciaPagoPIM.getText().toString() + "\n"
                + "Aut: " + autorizationCode;
        if(companniaPIM.equals("Pago AVON") || companniaPIM.equals("PAGO AVON") || companniaPIM.equals("AVON") || companniaPIM.equals("Avon")){
            mensajeSmsPIM +="\nFol: " + autorizationCode + cantidadProcesada.replace(".", "") + paraSms + homologarFecha.substring(11, 19).replace(":", "");
        }

        /*if(btnTerminar.isEnabled()){
            choroGlobal += "\nCOPIA";
        }*/

        folioAvonRecibo = autorizationCode + cantidadProcesada.replace(".", "") + paraSms + homologarFecha.substring(11, 19).replace(":", "");
        referenciaRecibo = etReferenciaPagoPIM.getText().toString();
        montoRecibo = "$ " + cantidadProcesada;

        rlContenedorUltimoPasoPIM.setVisibility(View.VISIBLE);
        //tvTituloNumeroOperacion.setVisibility(View.VISIBLE);
        //tvNumeroOperacion.setVisibility(View.VISIBLE);

    }

    @Override
    public void setToolbarValues() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(encabezado);
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
                if(rlContenedorPrincipalPagoPIM != null && rlContenedorPrincipalPagoPIM.getVisibility() == View.VISIBLE){
                    rlContenedorPrincipalPagoPIM.setVisibility(View.GONE);
                    gvSubCategoriasPim.setVisibility(View.VISIBLE);
                    rlContenedorUltimoPasoPIM.setVisibility(View.GONE);
                    rlContenedorPaso2PIM.setVisibility(View.GONE);
                    etMontoPagoPIM.setText("");
                    etReferenciaPagoPIM.setText("");
                } else {
                    //rlContenedorPaso1PIM.setVisibility(View.GONE);
                    super.onBackPressed();
                }
                if(rlContenedorScannerPIM.getVisibility() == VISIBLE || sfvEscannerPIM.getVisibility() == VISIBLE){
                    rlContenedorScannerPIM.setVisibility(View.GONE);
                    sfvEscannerPIM.setVisibility(View.GONE);
                    ocultarTeclado(btnPaso12PIM);
                }
                break;
        }
        return true;
    }

    public void activaEscannerPIM(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            rlContenedorScannerPIM.setVisibility(View.VISIBLE);
            sfvEscannerPIM.setVisibility(View.VISIBLE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISO_CAMERA);
        }

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(500, 800)
                .build();
/*
        if(!barcodeDetector.isOperational()){
            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
            this.finish();
        }*/

        sfvEscannerPIM.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ContextCompat.checkSelfPermission(PagoConPIM.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(sfvEscannerPIM.getHolder());

                    } else {
                        /*sfvEscannerPIM.setVisibility(View.GONE);
                        rlContenedorScannerPIM.setVisibility(View.GONE);
                        ActivityCompat.requestPermissions(PagoConPIM.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISO_CAMERA);*/
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {


            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() > 0){
                    etReferenciaPagoPIM.post(new Runnable() {
                        @Override
                        public void run() {
                            etReferenciaPagoPIM.setText(barcodes.valueAt(0).displayValue.replace(" ", ""));
                            sfvEscannerPIM.setVisibility(View.GONE);
                            rlContenedorScannerPIM.setVisibility(View.GONE);
                            cameraSource.stop();

                        }
                    });

                }

            }
        });


    }

    public void escannerManual(){
        sfvEscannerPIM.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {


            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() > 0){
                    etReferenciaPagoPIM.post(new Runnable() {
                        @Override
                        public void run() {
                            etReferenciaPagoPIM.setText(barcodes.valueAt(0).displayValue);
                            sfvEscannerPIM.setVisibility(View.GONE);
                            rlContenedorScannerPIM.setVisibility(View.GONE);
                            cameraSource.stop();

                        }
                    });

                }

            }
        });

    }

    public void ocultarTeclado(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

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

        SendDataString("\nPago " + companniaPIM +"\n");
        SendDataString(homologarFecha.substring(0,10) + "  Hora: " + homologarFecha.substring(11, 19));
        SendDataString("\n\nComercio: " + comercioRecibo);
        SendDataString("\nId: " + storeId);
        if(companniaPIM.equals("Pago AVON") || companniaPIM.equals("PAGO AVON") || companniaPIM.equals("AVON") || companniaPIM.equals("Avon")){
            SendDataString("\nFol Avon:" + folioAvonRecibo);
        }
        SendDataString("\nAut: " + autorizationCode);
        SendDataString("\nRef: " + referenciaRecibo);
        SendDataString("\nServicio:     " +  montoRecibo);
        if(comision > 0.00){
            SendDataString("\nComision:     $ " +  comision);
            SendDataString("\nIVA comision: $ " +  iva);
        }
        SendDataString("\nTotal:        $ "   +  String.format("%.2f", montoTotal));
        if(btnPrintPIM.getText().toString().equals(getResources().getString(R.string.reimprimir))){
            SendDataString("\n\n****COPIA****");
        }

        switch (leyendaDinamicaNum){
            case 0:
                if(/*leyenda1.length() >= 0 && */leyenda1 != null){
                    if(leyenda1.length() > 0){
                    SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyenda1));

                    }
                }
                if(/*leyenda2.length() >= 0 && */leyenda2 != null){
                    if(leyenda2.length() > 0){
                    SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyenda2));

                    }
                }
                if(/*leyenda3.length() >= 0 && */leyenda3 != null){
                    if(leyenda3.length() > 0){
                    SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyenda3));

                    }
                }
                break;
            case 1:
                //leyenda2 = "Camión, mamón, teléfono, día";
                SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyendaDinamica));
                if(/*leyenda2.length() >= 0 && */leyenda2 != null){
                    if(leyenda2.length() > 0){
                    SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyenda2));

                    }
                }
                if(/*leyenda3.length() >= 0 && */leyenda3 != null){
                    if(leyenda3.length() > 0){
                    SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyenda3));

                    }
                }
                break;
            case 2:
                if(/*leyenda1.length() >= 0 && */leyenda1 != null){
                    if(leyenda1.length() > 0){
                    SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyenda1));

                    }
                }
                    SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyendaDinamica));
                if(/*leyenda3.length() >= 0 && */leyenda3 != null){
                    if(leyenda3.length() > 0){
                    SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyenda3));

                    }
                }
                break;
            case 3:
                if(/*leyenda1.length() >= 0 && */leyenda1 != null){
                    if(leyenda1.length() > 0){
                    SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyenda1));

                    }
                }
                if(/*leyenda2.length() >= 0 && */leyenda2 != null){
                    if(leyenda2.length() > 0){
                    SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyenda2));

                    }
                }
                SendDataString("\n\n" + limpiarLeyendaProvisionalmente(leyendaDinamica));
                break;
                /*default:
                    leyenda1 = "";
                    leyenda2 = "";
                    leyenda3 = "";
                    leyendaDinamica = "";
                    break;*/

        }
        //SendDataString("\n\n" + leyendaRecibo);

        SendDataString("\n\n" + limpiarLeyendaProvisionalmente(footerReciboPIM) + "\n\n\n");

        btnTerminaPagoPIM.setBackgroundColor(getResources().getColor(R.color.verde_yastas));
        btnTerminaPagoPIM.setEnabled(true);
        btnPrintPIM.setText(getResources().getString(R.string.reimprimir));
        mService.stop();

    }

    private void SendDataString(String data) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, "No conectado", Toast.LENGTH_SHORT).show();
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

    public String limpiarLeyendaProvisionalmente(String string){
        String acentos = "ÁÉÍÓÚÑñáéíóú";
        String sinAcentos = "AEIOUNnaeiou";
        String choroLimpio = string;
        int tildes = acentos.length();
        for(int i =0 ; i< tildes; i++){
            choroLimpio = choroLimpio.replace(acentos.charAt(i), sinAcentos.charAt(i));
        }
        return choroLimpio;
    }

}
