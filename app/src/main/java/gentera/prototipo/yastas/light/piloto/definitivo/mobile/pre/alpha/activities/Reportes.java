package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.BuscarDispositivos;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.GananciasSeccionFragment;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.ReporteTransaccionesAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.ViewPagerAdapterReportes;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DatePickerFragment;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.interfaces.ConActionbarView;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.CustomMarkerView;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.TransaccionReg;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.BluetoothService;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.Command;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.PrintPicture;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.PrinterCommand;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Reportes extends AppCompatActivity implements ConActionbarView {
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
    public static final int REQUEST_CONNECT_DEVICE = 1;
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

    private final String PARA_REPORTE_GANANCIAS = "earnings", SERVICE_ID = "33";
    private static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    private String[] itemsTipoReporte, itemsMesReporte, itemsAnnoReporte;
    private Spinner spTipoReportes, spMesAnno;
    private LinearLayout llContenedorFecha;
    private TabLayout tabLayoutReportes;
    private ViewPager viewPagerReportes;
    private ViewPagerAdapterReportes viewPagerAdapterReportes;

    private LineChart lcReporteGanancias;
    private PieChart pieMeses;

    private String _comisionistaId;
    private String _comercioId;
    private String _fechaConsulta;
    private String _token;
    private String _comisionistaAgentId;
    JSONObject _data;
    private Button btnGenerarReporte;
    private LinearLayout lySeleccionSeccion;
    private RelativeLayout lyReporteSeccion;
    private RelativeLayout lyReporteTransSeccion;
    private RelativeLayout lyNoTransactions;
    private RelativeLayout lyLoading;
    private TextView montoTitle, fechaTitle, fechaSelectionDescription;
    private EditText transDayValue;
    private TextView transRepoTitle;
    RecyclerView rvListaTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_reportes);
        initViews();
//        SeleccionaTipoReporte();

    }

    public void initViews() {
        ConActionbarView conActionbarView = this;
        conActionbarView.setToolbarValues();

        //paraPrenderBluetooth = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mService = new BluetoothService(this, mHandler);

        _data = null;
        getUserData();
        lyNoTransactions = (RelativeLayout) findViewById(R.id.no_transactions);
        lyReporteSeccion = (RelativeLayout) findViewById(R.id.contenedor_graficos);
        lyReporteTransSeccion = (RelativeLayout) findViewById(R.id.ly_reporte_transacciones);
        lyLoading = (RelativeLayout) findViewById(R.id.loading);
        lySeleccionSeccion = findViewById(R.id.contenedor_seleccion_fecha);

        setSeleccionScreen();

        // tipo general
        fechaSelectionDescription = findViewById(R.id.titulo_selec_fecha);
        spTipoReportes = (Spinner) findViewById(R.id.sp_tipo_reporte);
        btnGenerarReporte = findViewById(R.id.btn_continuar_ganancias_reportes);
        ArrayAdapter<String> spinnerTipoReportesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.tipo_reportes));
        spTipoReportes.setAdapter(spinnerTipoReportesArrayAdapter);
        initSpinnerTipo();
        btnGenerarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spTipoReportes.getSelectedItemId() == 0) {
                    Toast.makeText(Reportes.this, getString(R.string.tipo_reporte_necesario), Toast.LENGTH_LONG).show();
                }
                if (spTipoReportes.getSelectedItemId() == 2) {
                    String fechTrans = transDayValue.getText().toString();
                    fechTrans = fechTrans.trim();
                    generaReporteTransacciones(fechTrans);
                }
                if (spTipoReportes.getSelectedItemId() == 1) {
                    _fechaConsulta = formaFechaFromSpinner();
                    generaReporteGanancias();
                }
            }
        });


        lcReporteGanancias = (LineChart) findViewById(R.id.lChart_ganancias);
        tabLayoutReportes = (TabLayout) findViewById(R.id.tabLayout_reportes);
        viewPagerReportes = (ViewPager) findViewById(R.id.view_pager_reportes);


        llContenedorFecha = (LinearLayout) findViewById(R.id.contenedor_seleccion_fecha);


    }

    public void getUserData() {
        SharedPreferences wtVisto = getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        if (wtVisto.getBoolean("enSession", false)) {
            _comercioId = wtVisto.getString("storeId", "5230");
            _comisionistaId = wtVisto.getString("numeroDeUsuarioSession", null);
            _comisionistaAgentId = wtVisto.getString("commisionAgentId", null);
            _token = wtVisto.getString("yaConTokenSession", null);
        } else {
            finish();
        }
    }

    public void iniGuiReportes() {
        itemsTipoReporte = getResources().getStringArray(R.array.tipo_reportes);
        lcReporteGanancias.setNoDataText(getString(R.string.no_transacciones));
        intitReporteDeGanancias();

        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansProBold.otf", true);

    }

    public void initSpMesesGanancias() {
        ArrayList<String> meses = new ArrayList<String>();
        int mesActual = Calendar.getInstance().get(Calendar.MONTH)+5;
        int annoActual = Calendar.getInstance().get(Calendar.YEAR)-1;
        itemsMesReporte = getResources().getStringArray(R.array.meses);
        itemsAnnoReporte = getResources().getStringArray(R.array.annos_dummy);

        for (int i = 0; i <= 7; i++) {
            meses.add(itemsMesReporte[mesActual] + " " + Integer.toString(annoActual));
            mesActual++;
            if (mesActual == 12) {
                mesActual = 0;
                annoActual++;
            }
        }
            Collections.reverse(meses);

        ArrayAdapter<String> adapadorSpinnerMesAnno = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, meses);
        adapadorSpinnerMesAnno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMesAnno.setAdapter(adapadorSpinnerMesAnno);


    }


    void generaReporteGanancias() {
        // ocultar elementos de seleecion de reporte y mostrar graficos
        setLoadingScreen();
        montoTitle = findViewById(R.id.plusvalia);
        fechaTitle = findViewById(R.id.tv_mes_reportado);
        iniGuiReportes();

    }


    void generaReporteTransacciones(String pFechayyyy_MM_dd) {
        // ocultar elementos de seleecion de reporte y mostrar graficos
        setLoadingScreen();
        getTransationsData(pFechayyyy_MM_dd, _comisionistaAgentId, _comercioId, SERVICE_ID);
    }

    void getTransationsData(final String pFechaConsulta, String pcomisionAgentId, String pComercioId, String pServiceId) {
        String[] fechItems = pFechaConsulta.split("-");
        String startDate = fechItems[2] + "-" + fechItems[1] + "-" + fechItems[0] + "T" + "00:00:00";
        String endDate = fechItems[2] + "-" + fechItems[1] + "-" + fechItems[0] + "T" + "23:59:59";
        startDate = startDate.replace(" ", "");
        endDate = endDate.replace(" ", "");
        //hardcode
      // pcomisionAgentId = "2090";
       // pComercioId = "2169";

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.base_api_lab) + "yastastransactionsinquiry/transactionsbydatetime?" +
                "commisionAgentId=" + pcomisionAgentId +
                "&storeId=" + pComercioId +
                "&startDateTime=" + startDate +
                "&endDateTime=" + endDate;
          //      "&serviceId=" + pServiceId;
        //+
          //      "&serviceId=" + pServiceId;

        Log.i("PETION", "URL:" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject pResponse) {
                        Log.i("PETICION", "response:" + pResponse);
                        _data = pResponse;
                        JSONObject transData;
                        setTransactionsRepoScreen();
                        transRepoTitle = findViewById(R.id.titulo_rep_trans);
                        transRepoTitle.setText(getString(R.string.titulo_repo_trans).replace("%", pFechaConsulta));
                        if (_data != null) {
                            try {
                                transData = _data.getJSONObject("MT_GetTransactionsByDateTimeResp_sync").
                                        getJSONObject("GetTransactionsByDateTimeResponseData");
                                if (transData.getJSONObject("ProcessingConditions").getInt("TotalOfRegisters") > 0) {
                                    try
                                    {
                                        llenarReporteOperaciones(transData.getJSONArray("TransactionDataItem"), pFechaConsulta);
                                    }catch (JSONException er)
                                    {
                                        JSONArray newArray= new JSONArray();
                                        newArray.put(transData.getJSONObject("TransactionDataItem"));
                                        llenarReporteOperaciones(newArray, pFechaConsulta);
                                    }

                                    setTransactionsRepoScreen();
                                } else {
                                    setNoTransactionsScreen();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Reportes.this, "Ocurrio un error con la peticion.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PETICION", "Error response:" + error);

                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + _token);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }

    void llenarReporteOperaciones(JSONArray pTransDataList, String fecha) throws JSONException {

        List<TransaccionReg> transacciones = new ArrayList<>();
        for (int i = 0; i < pTransDataList.length(); i++) {
            TransaccionReg tran = new TransaccionReg(pTransDataList.getJSONObject(i));
            tran.setFecha(fecha);
            transacciones.add(tran);
        }
        ReporteTransaccionesAdapter transAdap = new ReporteTransaccionesAdapter(Reportes.this, transacciones, getSupportFragmentManager(), this);
        rvListaTrans.setAdapter(transAdap);
    }

    void initSectionsViews() {

        try {
            viewPagerAdapterReportes = new ViewPagerAdapterReportes(getSupportFragmentManager());
            JSONArray _secciones = _data.getJSONArray("sectionsAmounts");
            for (int i = 0; i < _secciones.length(); i++) {
                String title = _secciones.getJSONObject(i).getString("concept");
                title = title.replace("Recargas de", "");
                title = title.toUpperCase();
                String secciotrS = _secciones.getJSONObject(i).toString();
                viewPagerAdapterReportes.addFragments(GananciasSeccionFragment.newInstance(secciotrS,_comisionistaId,_comercioId,_fechaConsulta,_token),
                        title, getResources().getDrawable(R.drawable.coin_tres));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        viewPagerReportes.setAdapter(viewPagerAdapterReportes);
        tabLayoutReportes.setupWithViewPager(viewPagerReportes);

    }

    public void initSpinnerTipo() {
        spTipoReportes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (spMesAnno == null) {
                            spMesAnno = (Spinner) findViewById(R.id.sp_mes_anno_reporte);
                            initSpMesesGanancias();
                        }
                        if (transDayValue == null) {
                            transDayValue = findViewById(R.id.trans_day_value);
                        }
                        fechaSelectionDescription.setText("");
                        spMesAnno.setVisibility(View.GONE);
                        transDayValue.setVisibility(View.GONE);
                        break;
                    case 1:
                        fechaSelectionDescription.setText(getString(R.string.selecciona_un_mes_para));
                        if (transDayValue == null) {
                            transDayValue = findViewById(R.id.trans_day_value);
                        }
                        transDayValue.setVisibility(View.GONE);
                        if (spMesAnno == null) {
                            spMesAnno = (Spinner) findViewById(R.id.sp_mes_anno_reporte);
                            initSpMesesGanancias();
                        }
                        spMesAnno.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        if (transDayValue == null) {
                            transDayValue = findViewById(R.id.trans_day_value);
                        }
                        transDayValue.setVisibility(View.VISIBLE);
                        transDayValue.setText(getDateInString());

                        if (spMesAnno != null) spMesAnno.setVisibility(View.GONE);

                        fechaSelectionDescription.setText(getString(R.string.day_picker_reporte));
                        transDayValue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        // +1 because january is zero
                                        final Calendar c = Calendar.getInstance();
                                        int diia = c.get(Calendar.MONTH);
                                        int mes = (month + 1);

                                        String daystr = day < 10 ? "0" + day : String.valueOf(day);
                                        String messrt = mes < 10 ? "0" + mes : String.valueOf(mes);
                                        final String selectedDate = daystr + "-" + messrt + "-" + year;
                                        transDayValue.setText(selectedDate);

                                    }
                                });
                                newFragment.show(Reportes.this.getSupportFragmentManager(), "datePicker");
                            }
                        });
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @SuppressLint("ResourceAsColor")
    public void intitReporteDeGanancias() {

        Legend lGanancias = lcReporteGanancias.getLegend();
        lGanancias.setDrawInside(true);
        lGanancias.setEnabled(false);

        getAndSetEarnings(_comisionistaAgentId, _comercioId, _fechaConsulta);

    }


    public void getAndSetEarnings(String pComisionistaId, String pComercioId, String pFechaConsulta) {
        // Instantiate the RequestQueue.
        //hardcode
        //pComisionistaId = "4993";
        //pComercioId = "5230";
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.base_api_lab) + "earnings?" +
                "comisionistaId=" + pComisionistaId +
                "&comercioId=" + pComercioId +
                "&fechaConsulta=" + pFechaConsulta;

        Log.i("PETION", "URL:" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject pResponse) {
                        Log.i("PETICION", "response:" + pResponse);
                        _data = pResponse;
                        if (_data != null) {
                            try {
                                llenarReporteGanancias(_data.getJSONArray("amountByDays"));
                                String monto = getString(R.string.has_ganado);
                                String montoReal = _data.getString("totalAmount");
                                float montoFloat = Float.parseFloat(montoReal);
                                if (montoFloat <= 0)// no hay ganancias, engo no hay transacciones
                                {
                                    setNoTransactionsScreen();
                                } else {
                                    setReporteScreen();
                                    montoTitle.setText(monto.replace("$", "$ " + montoReal));
                                    fechaTitle.setText(_data.getString("readableDate"));
                                    initSectionsViews();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PETICION", "Error response:" + error);

                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + _token);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    public void setNoTransactionsScreen() {
        lyNoTransactions.setVisibility(View.VISIBLE);
        lyReporteSeccion.setVisibility(View.GONE);
        lySeleccionSeccion.setVisibility(View.GONE);
        lyLoading.setVisibility(View.GONE);
        lyReporteTransSeccion.setVisibility(View.GONE);

    }

    public void setLoadingScreen() {
        lyNoTransactions.setVisibility(View.GONE);
        lyReporteSeccion.setVisibility(View.GONE);
        lySeleccionSeccion.setVisibility(View.GONE);
        lyLoading.setVisibility(View.VISIBLE);
        lyReporteTransSeccion.setVisibility(View.GONE);

    }

    public void setReporteScreen() {
        getSupportActionBar().setTitle("Reporte de ganancias");
        lyNoTransactions.setVisibility(View.GONE);
        lyReporteSeccion.setVisibility(View.VISIBLE);
        lySeleccionSeccion.setVisibility(View.GONE);
        lyLoading.setVisibility(View.GONE);
        lyReporteTransSeccion.setVisibility(View.GONE);

    }

    public void setSeleccionScreen() {
        getSupportActionBar().setTitle(R.string.reportes);
        lyNoTransactions.setVisibility(View.GONE);
        lyReporteSeccion.setVisibility(View.GONE);
        lySeleccionSeccion.setVisibility(View.VISIBLE);
        lyLoading.setVisibility(View.GONE);
        lyReporteTransSeccion.setVisibility(View.GONE);

    }

    public void setTransactionsRepoScreen() {
        getSupportActionBar().setTitle("Reporte de operaciones");
        lyNoTransactions.setVisibility(View.GONE);
        lyReporteSeccion.setVisibility(View.GONE);
        lySeleccionSeccion.setVisibility(View.GONE);
        lyLoading.setVisibility(View.GONE);
        lyReporteTransSeccion.setVisibility(View.VISIBLE);
        if (rvListaTrans == null) {
            rvListaTrans = findViewById(R.id.rv_repo_trans);
            LinearLayoutManager llm = new LinearLayoutManager(Reportes.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rvListaTrans.setLayoutManager(llm);
            DividerItemDecoration itemDecor = new DividerItemDecoration(Reportes.this, DividerItemDecoration.VERTICAL);
            rvListaTrans.addItemDecoration(itemDecor);

        }


    }

    public void llenarReporteGanancias(JSONArray pDaysData) throws JSONException {
        ArrayList<Entry> yVals = new ArrayList<>();

        for (int i = 0; i < pDaysData.length(); i++) {
            float val = (float) pDaysData.getJSONObject(i).getDouble("totalAmount");
            yVals.add(new Entry(i, val));
        }

        LineDataSet set1;
        set1 = new LineDataSet(yVals, "Ganancias");
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(getResources().getColor(android.R.color.white));
        set1.setCircleColor(getResources().getColor(android.R.color.white));
        set1.setLineWidth(2f);
        set1.setFillAlpha(100);
        set1.setDrawFilled(true);
        set1.setDrawCircles(true);
        set1.setCircleRadius(3f);
        set1.setCubicIntensity(.25f);
        set1.setFillColor(Color.WHITE);
        LineData ld = new LineData(set1);
        ld.setDrawValues(false);


        lcReporteGanancias.setDrawGridBackground(true);
        lcReporteGanancias.setGridBackgroundColor(getResources().getColor(R.color.colorPrimary));
        lcReporteGanancias.getAxisLeft().setDrawGridLines(false);
        lcReporteGanancias.getXAxis().setDrawGridLines(false);
        lcReporteGanancias.getAxisRight().setDrawGridLines(false);
        lcReporteGanancias.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        lcReporteGanancias.setDescription(null);
        lcReporteGanancias.setDrawBorders(false);
        lcReporteGanancias.getAxisLeft().setDrawLabels(false);
        lcReporteGanancias.getAxisRight().setDrawLabels(false);
        lcReporteGanancias.getLegend().setEnabled(false);
        lcReporteGanancias.setAutoScaleMinMaxEnabled(true);
        lcReporteGanancias.setPinchZoom(false);
        lcReporteGanancias.setDoubleTapToZoomEnabled(false);



        XAxis xl = lcReporteGanancias.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        lcReporteGanancias.setViewPortOffsets(0, 0, 0, 0);
        lcReporteGanancias.setData(ld);
        lcReporteGanancias.setDrawMarkers(true);
//        lcReporteGanancias.setMarker(new MarkerView(Reportes.this, R.layout.activity_splash));
        CustomMarkerView  marker=new CustomMarkerView(Reportes.this, R.layout.value_maker_linechart);
        marker.setOffset(0,-100);
        lcReporteGanancias.setMarker(marker);
    }

    //    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_generico, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idMenu = item.getItemId();

        switch (idMenu) {
            case R.id.menu_llamada_servicio:
                Intent intentLamada = new Intent(Intent.ACTION_DIAL);
                intentLamada.setData(Uri.parse(getResources().getString(R.string.numero_atencion_clientes)));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intentLamada);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL);
                }
                break;
            case android.R.id.home:
                startActivity(new Intent(Reportes.this, MainActivity.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    public void setToolbarValues() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.reportes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
    }

    public String formaFechaFromSpinner() {
        String fecha = "";
        String numeroMes = "";
        String mesSeleccionado = spMesAnno.getSelectedItem().toString().split(" ")[0];
        String mesanio = spMesAnno.getSelectedItem().toString().split(" ")[1];
        if (mesSeleccionado.equals("Enero")) {
            numeroMes = "01";
        }
        if (mesSeleccionado.equals("Febrero")) {
            numeroMes = "02";
        }
        if (mesSeleccionado.equals("Marzo")) {
            numeroMes = "03";
        }
        if (mesSeleccionado.equals("Abril")) {
            numeroMes = "04";
        }
        if (mesSeleccionado.equals("Mayo")) {
            numeroMes = "05";
        }
        if (mesSeleccionado.equals("Junio")) {
            numeroMes = "06";
        }
        if (mesSeleccionado.equals("Julio")) {
            numeroMes = "07";
        }
        if (mesSeleccionado.equals("Agosto")) {
            numeroMes = "08";
        }
        if (mesSeleccionado.equals("Septiembre")) {
            numeroMes = "09";
        }
        if (mesSeleccionado.equals("Octubre")) {
            numeroMes = "10";
        }
        if (mesSeleccionado.equals("Noviembre")) {
            numeroMes = "11";
        }
        if (mesSeleccionado.equals("Diciembre")) {
            numeroMes = "12";
        }
        fecha = "01-" + numeroMes + "-" + mesanio;
        return fecha;
    }

    public String getDateInString() {
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, 1);
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String inActiveDate = null;
        try {
            inActiveDate = format1.format(date);
            System.out.println(inActiveDate);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return inActiveDate;
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
                    Toast.makeText(Reportes.this,
                            "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(Reportes.this,
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(Reportes.this, "Device connection was lost",
                            Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(Reportes.this, "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void Print_Recibo_Avon(){
        Print_BMP();

        SendDataString("\n\n");

        SendDataString(ReporteTransaccionesAdapter.reenvioInfo);

        SendDataString("\n\nREIMPRESION");

        SendDataString("\n\nVisitanos en www.yastas.com\n\n\n");

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
        Bitmap mBitmap = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.logo_ticket)).getBitmap();
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

}
