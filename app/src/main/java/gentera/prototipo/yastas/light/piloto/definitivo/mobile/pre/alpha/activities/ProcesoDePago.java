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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.ProcesoPagoAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.dbs.DataBaseInfoServicios;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.BuscarDispositivos;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogBluetooth;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogCorreo;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogSms;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.BluetoothService;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.Command;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.PrintPicture;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.PrinterCommand;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ProcesoDePago extends AppCompatActivity implements ConActionbarView {
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
    /*******************************************************************************************************/

    private static final int MY_PERMISSIONS_REQUEST_CALL = 10;
    public static final int PERMISO_CAMERA = 20;


    private final String PARA_ENVIAR_PAGO = "paymenttransactions/dopayment";
    private String autorizationCode;
    private int[] iconosTelefoniaEInternet, iconosAgua, iconosLuz, iconosGas, iconosTesoreriaGob, iconosCatalogos;
    public static String[] nombreCompannias;
    public static TextView tvNombreCosmetico;
    private GridView gvCompannias;
    public static RelativeLayout rlContenedorPaso1, rlContenedorPaso2;
    public static String compannia, choroGlobal, choroSms;
    public static boolean mailEnviado = false;
    public static String montoPago="389", comisionPago= "5.00", totalPago="394.00";
    private EditText etMontoCosmeticos;
    public static EditText etReferenciaCosmeticos;
    private Button btnMeterDatosCosmeticos, btnCancelarPago, btnFinalizarPago, btnImprimir, btnEMail, btnSms;
    public static Button btnTerminar;
    public static ImageButton iBtnActivaEscanner;
    private RelativeLayout rlContenedorPaso3, rlContenedorPaso4;
    private LinearLayout llContenedorBtnsCancelarAceptar;
    private TextView tvFechaOperacion, tvHoraOperacion, tvRolloOperacion, tvMontoOperacion, tvIvaOperacion, tvComisionOperacion, tvTotalOperacion, tvTituloPaso, tvTituloNumeroOperacion, tvNumeroOperacion;
    private DialogCorreo dialogCorreo;
    private DialogSms dialogSms;
    private SurfaceView sfvEscanner;
    private double iva, montoParcial, montoTotal, comision;
    private Bundle bundleServicio;
    private RequestQueue requestQueuePago;
    private JsonObjectRequest jsonObjectRequest;
    private SharedPreferences preferences;
    private String token, cantidadProcesada, homologarFecha;
    private Date tomarFechaParaRegistro;
    private RelativeLayout lyLoadingPago, rlContenedorScanner;
    private DialogBluetooth dialogBluetooth;
    private BluetoothAdapter paraPrenderBluetooth;
    private DataBaseInfoServicios dataBaseInfoServicios;
    private String storeId;
    private ImageView ivScanearManual;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_proceso_de_pago);

        iniGuiProcesoDePago();
        iniListenersProcesoDePago();
    }

    public void iniGuiProcesoDePago(){
        bundleServicio = this.getIntent().getExtras();
        bundleServicio.getString("tipoDeServicio");

        ConActionbarView conActionbarView = this;
        conActionbarView.setToolbarValues();

        requestQueuePago = Volley.newRequestQueue(ProcesoDePago.this);
        preferences = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        dataBaseInfoServicios = new DataBaseInfoServicios(getApplicationContext());


        //iconosTelefoniaEInternet = new int[] {R.drawable.axtel, R.drawable.cablemas, R.drawable.dish, R.drawable.izzi, R.drawable.maxcom, R.drawable.megacable, R.drawable. multimedios, R.drawable.sky, R.drawable.telmex, R.drawable.telnor, R.drawable.totalplay, R.drawable.vetv};
        iconosTelefoniaEInternet = new int[] {R.drawable.dish, R.drawable.izzi, R.drawable.megacable, R.drawable.sky, R.drawable.telmex, R.drawable.vetv};
        //iconosAgua  = new int[] {R.drawable.aguakan, R.drawable.amd, R.drawable.aydm, R.drawable.cmapas, R.drawable.comapa, R.drawable.interapas, R.drawable.jad, R.drawable.jumapa, R.drawable.oapas, R.drawable.saltillo, R.drawable.seapal, R.drawable.siapa};
        iconosLuz = new int[] {R.drawable.cfe};
        //iconosGas = new int[] {R.drawable.naturgy};
        //iconosTesoreriaGob = new int[] {R.drawable.consar, R.drawable.infonavit, R.drawable.pase, R.drawable.televia};
        //iconosCatalogos = new int[] {R.drawable.arabela, R.drawable.avon, R.drawable.fuller};
        iconosCatalogos = new int[] {R.drawable.arabela, R.drawable.avon};

        rlContenedorPaso1 = (RelativeLayout)findViewById(R.id.contenedor_paso1_proceso_pago);
        rlContenedorPaso2 = (RelativeLayout)findViewById(R.id.contenedor_paso2_proceso_pago);
        gvCompannias = (GridView)findViewById(R.id.gv_proceso_pago);
        tvNombreCosmetico = (TextView)findViewById(R.id.tv_nombre_servicio_cosmetico);

        etReferenciaCosmeticos = (EditText)findViewById(R.id.et_referencia_proceso_pago);
        etMontoCosmeticos = (EditText)findViewById(R.id.et_monto_proceso_pago);
        iBtnActivaEscanner = (ImageButton)findViewById(R.id.ib_activa_escanner);
        sfvEscanner = (SurfaceView)findViewById(R.id.scanner_view);
        btnMeterDatosCosmeticos = (Button)findViewById(R.id.btn_paso2_paso3);
        rlContenedorPaso3 = (RelativeLayout)findViewById(R.id.contenedor_paso3_proceso_pago);
        tvFechaOperacion = (TextView)findViewById(R.id.tv_fecha_operacion_proceso_pago);
        tvHoraOperacion = (TextView)findViewById(R.id.tv_hora_operacion_proceso_pago);
        tvRolloOperacion = (TextView)findViewById(R.id.tv_rollo_operacion_proceso_pago);
        tvMontoOperacion = (TextView)findViewById(R.id.tv_monto_operacion_proceso_pago);
        tvIvaOperacion = (TextView)findViewById(R.id.tv_monto_iva_operacion_proceso_pago);
        tvComisionOperacion = (TextView)findViewById(R.id.tv_monto_comision_operacion_proceso_pago);
        tvTotalOperacion = (TextView)findViewById(R.id.tv_monto_total_operacion_proceso_pago);
        tvTituloNumeroOperacion = (TextView)findViewById(R.id.tv_titulo_numero_transaccion);
        tvNumeroOperacion = (TextView)findViewById(R.id.tv_numero_transaccion);
        btnFinalizarPago = (Button)findViewById(R.id.btn_finalizar_pago_proceso_pago);
        btnCancelarPago = (Button)findViewById(R.id.btn_cancelar_pago_proceso_pago);
        rlContenedorPaso4 = (RelativeLayout)findViewById(R.id.rl_contenedor_paso4_proceso_pago);
        llContenedorBtnsCancelarAceptar = (LinearLayout)findViewById(R.id.ll_contenedor_btns_cancelar_aceptar);
        tvTituloPaso = (TextView)findViewById(R.id.tv_numero_proceso_pago);
        btnImprimir = (Button)findViewById(R.id.btn_imprimir_pago_proceso_pago);
        btnEMail = (Button)findViewById(R.id.btn_mail_pago_proceso_pago);
        btnSms = (Button)findViewById(R.id.btn_sms_proceso_pago);
        lyLoadingPago = (RelativeLayout)findViewById(R.id.loading_pago);
        rlContenedorScanner = (RelativeLayout)findViewById(R.id.contenedor_scanner);
        ivScanearManual = (ImageView)findViewById(R.id.iv_btn_scannear);

        paraPrenderBluetooth = BluetoothAdapter.getDefaultAdapter();

        btnTerminar = (Button)findViewById(R.id.btn_sin_notificacion_pago_proceso_pago);
        btnTerminar.setEnabled(false);

        sfvEscanner.setZOrderMediaOverlay(true);

        llenarAdapter(bundleServicio);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        mService = new BluetoothService(this, mHandler);

        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansProBold.otf", false);
    }

    public void iniListenersProcesoDePago(){
        btnMeterDatosCosmeticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int refLenght = ProcesoPagoAdapter.longitudReferencia;
                if(etReferenciaCosmeticos.getText().toString().length()>0 && etMontoCosmeticos.getText().toString().length()>0){
                    if(dataBaseInfoServicios.getMultipleReferencia(compannia)){
                        if(Float.parseFloat(etMontoCosmeticos.getText().toString()) > 0){
                            if (etReferenciaCosmeticos.length() == refLenght || etReferenciaCosmeticos.length() == dataBaseInfoServicios.getTopeReferencia(compannia)) {
                                pasarDatosParaPago();

                            } else {
                                Toast.makeText(ProcesoDePago.this, "La referencia debe ser de "+ refLenght + " o " + String.valueOf(dataBaseInfoServicios.getTopeReferencia(compannia)) + " digitos", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if(Float.parseFloat(etMontoCosmeticos.getText().toString()) < dataBaseInfoServicios.getMontoMinimo(compannia)) {
                                Toast.makeText(getApplicationContext(), "Introducir monto minimo", Toast.LENGTH_SHORT).show();
                            } else if (Float.parseFloat(etMontoCosmeticos.getText().toString()) < dataBaseInfoServicios.getMontoMaximo(compannia)){
                                Toast.makeText(getApplicationContext(), "El monto excede el limite permitido", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        if(Float.parseFloat(etMontoCosmeticos.getText().toString()) > 0){
                            if (etReferenciaCosmeticos.length() == refLenght) {
                                pasarDatosParaPago();

                            } else {
                                Toast.makeText(ProcesoDePago.this, "La referencia debe ser de "+ refLenght +" digitos", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if(Float.parseFloat(etMontoCosmeticos.getText().toString()) < dataBaseInfoServicios.getMontoMinimo(compannia)) {
                                Toast.makeText(getApplicationContext(), "Introducir monto minimo", Toast.LENGTH_SHORT).show();
                            } else if (Float.parseFloat(etMontoCosmeticos.getText().toString()) < dataBaseInfoServicios.getMontoMaximo(compannia)){
                                Toast.makeText(getApplicationContext(), "El monto excede el limite permitido", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Por favor introduce referencia y monto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iBtnActivaEscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisosEscanner();
            }
        });

        ivScanearManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paraElEscanner();
            }
        });

        btnCancelarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProcesoDePago.this);
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
                        Toast.makeText(ProcesoDePago.this, "Transaccion cancelada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProcesoDePago.this, PagoDeServicios.class));
                        finish();
                    }
                });
                builder.show();

            }
        });

        btnFinalizarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyLoadingPago.setVisibility(View.VISIBLE);
                float enLaBolsa = preferences.getFloat("enLaBolsa", 0);
                if(enLaBolsa - Float.valueOf(etMontoCosmeticos.getText().toString()) > 0){
                    btnCancelarPago.setVisibility(View.GONE);
                    btnFinalizarPago.setVisibility(View.GONE);
                    procesarPagoServicios();
                } else {
                    lyLoadingPago.setVisibility(View.GONE);
                    Toast.makeText(ProcesoDePago.this, "No cuentas con saldo suficiente para realizar esta operación", Toast.LENGTH_LONG).show();
                }


            }
        });

        btnImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paraPrenderBluetooth.isEnabled()){
                    Intent dispositivosIntent = new Intent(ProcesoDePago.this, BuscarDispositivos.class);
                    startActivityForResult(dispositivosIntent, REQUEST_CONNECT_DEVICE);

                } else {
                    AlertDialog.Builder builderBluetooth = new AlertDialog.Builder(ProcesoDePago.this);
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

        btnEMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abriDialogCorreo();
            }
        });

        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Servicio temporalmente no disponible", Toast.LENGTH_LONG).show();
                //abriDialogSms(choroGlobal);

            }
        });

        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailEnviado = false;
                startActivity(new Intent(ProcesoDePago.this, MainActivity.class));
                finish();
            }
        });

        cargarSesion();

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
                startActivity(new Intent(ProcesoDePago.this, PagoDeServicios.class));
                break;
        }
        return true;
    }

    @Override
    public void setToolbarValues() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        switch (bundleServicio.getString("tipoDeServicio")){
            case "TelefoniaEInternet":
                getSupportActionBar().setTitle(R.string.telefonia_internet);
                break;
            case "Agua":
                getSupportActionBar().setTitle(R.string.agua);
                break;
            case "Luz":
                getSupportActionBar().setTitle(R.string.luz);
                break;
            case "Gas":
                getSupportActionBar().setTitle(R.string.gas);
                break;
            case "TesoreriaYGobierno":
                getSupportActionBar().setTitle(R.string.tesoreria_gobierno);
                break;
            case "VentaPorCatalogo":
                getSupportActionBar().setTitle(R.string.venta_catalogo);
                break;

        }
    }

    public void permisosEscanner(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            rlContenedorScanner.setVisibility(View.VISIBLE);
            sfvEscanner.setVisibility(View.VISIBLE);
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


        sfvEscanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ContextCompat.checkSelfPermission(ProcesoDePago.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(sfvEscanner.getHolder());

                    } else {
                        sfvEscanner.setVisibility(View.GONE);
                        rlContenedorScanner.setVisibility(View.GONE);
                        ActivityCompat.requestPermissions(ProcesoDePago.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISO_CAMERA);
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
                    etReferenciaCosmeticos.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }

            }
        });

    }

    public void paraElEscanner(){
/*
        if(!barcodeDetector.isOperational()){
            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
            this.finish();
        }*/


        sfvEscanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ContextCompat.checkSelfPermission(ProcesoDePago.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(sfvEscanner.getHolder());

                    } else {
                        sfvEscanner.setVisibility(View.GONE);
                        rlContenedorScanner.setVisibility(View.GONE);
                        ActivityCompat.requestPermissions(ProcesoDePago.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISO_CAMERA);
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
                    etReferenciaCosmeticos.post(new Runnable() {
                        @Override
                        public void run() {
                            etReferenciaCosmeticos.setText(barcodes.valueAt(0).displayValue);
                            sfvEscanner.setVisibility(View.GONE);
                            rlContenedorScanner.setVisibility(View.GONE);
                            cameraSource.stop();

                        }
                    });

                }

            }
        });
    }

    public void llenarAdapter(Bundle bundle) {
        switch (bundle.getString("tipoDeServicio")){
            case "TelefoniaEInternet":
                nombreCompannias = new String[] {"Dish", "Izzi", "Mega cable", "Sky", "Telmex", "Vetv"};
                ProcesoPagoAdapter serviciosGridAdapterI = new ProcesoPagoAdapter(this, iconosTelefoniaEInternet);
                gvCompannias.setAdapter(serviciosGridAdapterI);
                break;
            case "Agua":
                nombreCompannias = new String[] {"Aguakan", "Amd", "Aydm", "Cmapas", "Comapa", "Interapas", "Jad", "Jumapa", "Oapas", "Saltillo", "Seapal", "Siapa"};
                if(nombreCompannias.length == 1){
                    gvCompannias.setNumColumns(1);
                }
                ProcesoPagoAdapter serviciosGridAdapterII = new ProcesoPagoAdapter(this, iconosAgua);
                gvCompannias.setAdapter(serviciosGridAdapterII);
                break;
            case "Luz":
                nombreCompannias = new String[] {"Cfe"};
                if(nombreCompannias.length == 1){
                    gvCompannias.setNumColumns(1);
                }
                ProcesoPagoAdapter serviciosGridAdapterIII = new ProcesoPagoAdapter(this, iconosLuz);
                gvCompannias.setAdapter(serviciosGridAdapterIII);
                break;
            case "Gas":
                nombreCompannias = new String[] {"Gas Natural"};
                if(nombreCompannias.length == 1){
                    gvCompannias.setNumColumns(1);
                }
                ProcesoPagoAdapter serviciosGridAdapterIV = new ProcesoPagoAdapter(this, iconosGas);
                gvCompannias.setAdapter(serviciosGridAdapterIV);
                break;
            case "TesoreriaYGobierno":
                nombreCompannias = new String[] {"Consar", "Infonavit", "Pase", "Televia"};
                ProcesoPagoAdapter serviciosGridAdapterV = new ProcesoPagoAdapter(this, iconosTesoreriaGob);
                gvCompannias.setAdapter(serviciosGridAdapterV);
                break;
            case "VentaPorCatalogo":
                //nombreCompannias = new String[] {"Arabela", "Avon", "Fuller"};
                nombreCompannias = new String[] {"Arabela", "Avon"};

                if(nombreCompannias.length == 1){
                    gvCompannias.setNumColumns(1);
                }
                ProcesoPagoAdapter serviciosGridAdapterVI = new ProcesoPagoAdapter(this, iconosCatalogos);
                gvCompannias.setAdapter(serviciosGridAdapterVI);
                iBtnActivaEscanner.setVisibility(View.GONE);
                break;

        }

    }

    public void abriDialogCorreo(){
        dialogCorreo = DialogCorreo.newInstance(String.valueOf(dataBaseInfoServicios.getServiceID(compannia)), this.getResources().getString(R.string.enviar_comprobante), this.getResources().getString(R.string.ingresa_correo), getApplicationContext());
        dialogCorreo.show(getSupportFragmentManager(), "dialogCorreo");

        android.app.Fragment frag = getFragmentManager().findFragmentByTag("dialogCorreo");

        if(frag != null){
            getFragmentManager().beginTransaction().remove(frag).commit();
        }

    }

    public void abriDialogSms(String masChoros){
        dialogSms = DialogSms.newInstance(this.getResources().getString(R.string.enviar_comprobante), this.getResources().getString(R.string.ingresa_phone));
        dialogSms.show(getSupportFragmentManager(), "dialogSms");

        android.app.Fragment frag = getFragmentManager().findFragmentByTag("dialogSms");

        if(frag != null){
            getFragmentManager().beginTransaction().remove(frag).commit();
        }

    }

    public void ocultarTeclado(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

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
            postService.put("serviceId", dataBaseInfoServicios.getServiceID(compannia));
            postService.put("sku", dataBaseInfoServicios.getSku(compannia));
            postParamsPago.put("service" ,postService);

            JSONObject paymentDetail = new JSONObject();
            paymentDetail.put("paymentAmount", cantidadProcesada);
            paymentDetail.put("reference", etReferenciaCosmeticos.getText().toString());
            postParamsPago.put("paymentDetail", paymentDetail);

            JSONObject commissionData = new JSONObject();
            commissionData.put("commissionUserAmount", dataBaseInfoServicios.getTotalComision(compannia));
            commissionData.put("taxAmount", dataBaseInfoServicios.getIvaComision(compannia));
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
                    String descripcionRespuesta =  respuestaTransaccion.getString("responseDescription");

                    JSONObject respuestaPayment = response.getJSONObject("paymentResponseData");
                    String idTransaccion = respuestaPayment.getString("transactionId");
                    String codigoAutorizacion = respuestaPayment.getString("authorizationCode");
                    String tiempoEjecucion = respuestaPayment.getString("executionDateTime");
                    String idUnico = respuestaPayment.getString("uniqueId");
                    String estatusTransaccion = respuestaPayment.getString("transactionStatus");
                    autorizationCode = codigoAutorizacion;
                    tvNumeroOperacion.setText(codigoAutorizacion);
                    homologarFecha = tiempoEjecucion;
                    if(homologarFecha.length() != 0){
                    fechaRecibo = homologarFecha.substring(0,10) + "   " + homologarFecha.substring(11, 19);

                    }

                    if(codigoRespuesta == 0){
                        yaParaAcabar();
                    } else {
                        lyLoadingPago.setVisibility(View.GONE);
                        Toast.makeText(ProcesoDePago.this, "La operación no pudo ser procesada.\nFavor de validar el monto y la referencia", Toast.LENGTH_LONG).show();
                        btnCancelarPago.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lyLoadingPago.setVisibility(View.GONE);
                btnCancelarPago.setVisibility(View.VISIBLE);
                Toast.makeText(ProcesoDePago.this, "Transacción rechazada", Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Accept", "application/json");
                //headers.put("Content-Type", "application/json");
                //headers.put("Authorization", "Basic cm9sZTE6dG9tY2F0");
                headers.put("Authorization", "Bearer "+ token);
                return headers;


            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueuePago.add(jsonObjectRequest);
    }

    public void yaParaAcabar(){
        lyLoadingPago.setVisibility(View.GONE);
        llContenedorBtnsCancelarAceptar.setVisibility(View.GONE);
        tvTituloPaso.setText(getResources().getString(R.string.transaccion_exitosa));

        choroGlobal = tvRolloOperacion.getText().toString() + "\n" + tvMontoOperacion.getText().toString()
                + "\n" + "IVA: $ " + String.valueOf(iva) + "\n" + "Comisión: $ 0.00" + "\n" + "Total: $ " +
                String.valueOf(montoTotal) + "\n" + tvFechaOperacion.getText().toString() + " " + tvHoraOperacion.getText().toString();

        final SimpleDateFormat ddMMyyHHmmss = new SimpleDateFormat("ddMMyy");
        final String paraSms = ddMMyyHHmmss.format(tomarFechaParaRegistro);

        choroSms = "Comercio: " + storeId + "\nPago: " + compannia + " $ " + cantidadProcesada + "\n";
                if(comision > 0){
            choroSms += "Comisiòn: $ " + String.valueOf(comision)
            +"\nIva: $ "+ String.valueOf(iva);
                }
                choroSms += "\nTotal: $ " + String.valueOf(montoTotal)
                + "\nRef: " + etReferenciaCosmeticos.getText().toString() + "\n"
                + "Aut: " + autorizationCode;
        if(compannia.equals("Avon")){
            choroSms +="\nFol: " + autorizationCode + cantidadProcesada.replace(".", "") + paraSms + homologarFecha.substring(11, 19).replace(":", "");
        }

        /*if(btnTerminar.isEnabled()){
            choroGlobal += "\nCOPIA";
        }*/

        folioAvonRecibo = autorizationCode + cantidadProcesada.replace(".", "") + paraSms + homologarFecha.substring(11, 19).replace(":", "");
        referenciaRecibo = etReferenciaCosmeticos.getText().toString();
        montoRecibo = "$ " + cantidadProcesada;

        rlContenedorPaso4.setVisibility(View.VISIBLE);
        tvTituloNumeroOperacion.setVisibility(View.VISIBLE);
        tvNumeroOperacion.setVisibility(View.VISIBLE);

    }

    public void pasarDatosParaPago(){
        rlContenedorPaso2.setVisibility(View.GONE);
        btnMeterDatosCosmeticos.setVisibility(View.GONE);
        rlContenedorPaso3.setVisibility(View.VISIBLE);

        cantidadProcesada = CantidadParaTaradosString(etMontoCosmeticos.getText().toString());
        Log.i("parkinson", cantidadProcesada);

        final SimpleDateFormat yyyymmddFormat = new SimpleDateFormat("dd MM yyyy");
        final SimpleDateFormat reciboFormat = new SimpleDateFormat("dd MMM yyyy");
        final SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
        tomarFechaParaRegistro = Calendar.getInstance().getTime();
        final String fechaRegistro = yyyymmddFormat.format(tomarFechaParaRegistro);
        final String horaRegistro = horaFormat.format(tomarFechaParaRegistro);
        tvFechaOperacion.setText(fechaRegistro);
        tvHoraOperacion.setText(horaRegistro);
        tvRolloOperacion.setText(compannia + "\n"
                + "No. Referencia " + etReferenciaCosmeticos.getText().toString());
        tvMontoOperacion.setText("$ "+ cantidadProcesada);

        montoParcial = Float.parseFloat(etMontoCosmeticos.getText().toString());
        DecimalFormat df = new DecimalFormat("#.00");
        iva = Double.parseDouble(df.format(dataBaseInfoServicios.getIvaComision(compannia)));
        comision = Double.parseDouble(df.format(dataBaseInfoServicios.getComision(compannia)));
        //String strMontoParcial = String.valueOf(montoParcial);
        montoTotal = (iva + comision + Float.parseFloat(etMontoCosmeticos.getText().toString()));
        String strMontoTotal = String.valueOf(montoTotal);
        String strIva = String.valueOf(iva/100*16);
        tvIvaOperacion.setText("$ " + iva);

        tvComisionOperacion.setText("$ " + comision);
        tvTotalOperacion.setText("$ " + montoTotal);

        ocultarTeclado(btnMeterDatosCosmeticos);

    }

    public void cargarSesion(){
        preferences = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        token = preferences.getString("yaConTokenSession", "");
        comercioRecibo = preferences.getString("StoreName", "");

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

        SendDataString("\nPago " + compannia +"\n");
        SendDataString(fechaRecibo);
        SendDataString("\n\nComercio: " + comercioRecibo);
        SendDataString("\nId: " + storeId);
        if(compannia.equals("Avon")){
        SendDataString("\nFol Avon:" + folioAvonRecibo);
        }
        SendDataString("\nAut: " + autorizationCode);
        SendDataString("\nRef: " + referenciaRecibo);
        SendDataString("\nServicio:     " +  montoRecibo);
        if(comision > 0.00){
            SendDataString("\nComision:     $ " +  comision);
            SendDataString("\nIVA comision: $ " +  iva);
        }
        SendDataString("\nTotal:        $ "   +  montoTotal);
        SendDataString("\n\n" + dataBaseInfoServicios.getPieTicket(compannia));
        if(btnImprimir.getText().toString().equals(getResources().getString(R.string.reimprimir))){
            SendDataString("\n\n****COPIA****");
        }
        SendDataString("\n\nVisitanos en www.yastas.com\n\n\n");

        btnTerminar.setBackgroundColor(getResources().getColor(R.color.verde_yastas));
        btnTerminar.setEnabled(true);
        btnImprimir.setText(getResources().getString(R.string.reimprimir));
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
