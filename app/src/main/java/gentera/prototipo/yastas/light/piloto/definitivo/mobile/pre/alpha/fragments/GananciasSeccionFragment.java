package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Reportes;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.DetailSectionAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.DetalleSeccion;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.PieCurrencyFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class GananciasSeccionFragment extends Fragment {
    public static final String PARA_DETALLE_GANANCIAS = "earnings/details";
    PieChart pieChartTiempoAire;
    private static final String ARG_PARAM1 = "section";
    private static final String COMIS = "comis";
    private static final String COMER = "comer";
    private static final String FECHA = "fecha";
    private static final String TOKEN = "token";
    private static final String TAG = "GananciaSeccion";
    private JSONObject _sectionJson;
    private JSONArray _sectionDetail;
    private String _sectionStr;
    private String _sectionTitle;
    private String _comisionistaId;
    private String _comercioId;
    private String _token;
    private String _fecha;
    int groupServiceId = 0;
    private RecyclerView rvDetail;
    DetailSectionAdapter detailAdapdter;
    JSONArray _data;
    private SharedPreferences preferences;
    //private Trace trackerPie = FirebasePerformance.getInstance().newTrace("ChecandoElPie");

    public GananciasSeccionFragment() {
        // Required empty public constructor
    }

    public static GananciasSeccionFragment newInstance(String pSectionJson, String comisionistaId, String comercioId, String fechaConsulta, String token) {
        GananciasSeccionFragment fragment = new GananciasSeccionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, pSectionJson);
        args.putString(COMIS, comisionistaId);
        args.putString(COMER, comercioId);
        args.putString(FECHA, fechaConsulta);
        args.putString(TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _sectionStr = getArguments().getString(ARG_PARAM1);
            _comisionistaId = getArguments().getString(COMIS);
            _comercioId = getArguments().getString(COMER);
            _fecha = getArguments().getString(FECHA);
            _token = getArguments().getString(TOKEN);
            try {
                _sectionJson = new JSONObject(_sectionStr);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

        }
        cargarSesion();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View reportesTAView = inflater.inflate(R.layout.fragment_ganancias_section, container, false);
        pieChartTiempoAire = (PieChart) reportesTAView.findViewById(R.id.sectionChart);

        pieChartTiempoAire.setUsePercentValues(false);
        pieChartTiempoAire.getDescription().setEnabled(false);
        pieChartTiempoAire.setExtraOffsets(20, 20, 20, 20);
        pieChartTiempoAire.setDragDecelerationFrictionCoef(0.95f);
        pieChartTiempoAire.setDrawHoleEnabled(false);
        pieChartTiempoAire.setHoleRadius(40);
        pieChartTiempoAire.setHoleColor(getResources().getColor(R.color.transparente));
        pieChartTiempoAire.setTransparentCircleRadius(21f);

//        pieChartTiempoAire.setDrawMarkers(true);
        ArrayList<PieEntry> yValues = new ArrayList<>();
        try {
            _sectionDetail = _sectionJson.getJSONArray("amountsDetail");
            _sectionTitle = _sectionJson.getString("concept");
            groupServiceId = _sectionJson.getInt("sectionId");
            //groupServiceId=getGroupServiceId(_sectionTitle);
            //groupServiceId=_sectionTitle.get
            for (int i = 0; i < _sectionDetail.length(); i++) {
                float value = (float) _sectionDetail.optJSONObject(i).getDouble("amount");
                String concepto = _sectionDetail.optJSONObject(i).getString("concept");
                concepto = concepto.replace("RECARGA TIEMPO AIRE ", "");
                concepto = concepto.toLowerCase();
                value = value < 0 ? value * -1 : value;
                yValues.add(new PieEntry(value, concepto
                ));
            }
        } catch (JSONException e1) {
            Log.e(TAG, e1.getMessage());
        }


        pieChartTiempoAire.animateY(1500, Easing.EasingOption.EaseOutBounce);
        pieChartTiempoAire.animateX(1500, Easing.EasingOption.EaseOutBounce);
//        pieChartTiempoAire.animateY(100, Easing.EasingOption.EaseInElastic);

        PieDataSet pieDataSet = new PieDataSet(yValues, _sectionTitle);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(10f);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueLineWidth(1f);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart2Length(1.2f);
        pieDataSet.setValueFormatter( new PieCurrencyFormatter ());


        PieData data = new PieData(pieDataSet);
        data.setValueTextSize(12f);
        data.setValueTextColor(getResources().getColor(R.color.gris));
        pieChartTiempoAire.setEntryLabelTextSize(12);
        pieChartTiempoAire.setEntryLabelColor(getResources().getColor(R.color.gris));
        pieChartTiempoAire.setData(data);

        rvDetail = reportesTAView.findViewById(R.id.rv_lista_detalle);
        rvDetail.setVisibility(View.GONE);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetail.setLayoutManager(llm);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvDetail.addItemDecoration(itemDecor);

        return reportesTAView;


    }

    //hardcode for detail transactions, remove if dynamic
    public int getGroupServiceId(String pTitle) {
        if(_sectionTitle.equals("Recargas de Tiempo A")
                || _sectionTitle.equals("Recargas de Tiempo Aire"))return 1;
        if(_sectionTitle.equals("Pago de Servicios Bá")
                || _sectionDetail.equals("Pago de Servicios Básicos")) return 2;
        if(_sectionTitle.equals("Pagos Gubernamentale")) return 3;
        if(_sectionTitle.equals("Saldo Plataformas di")) return 4;
        if(_sectionTitle.equals("OTROS CARGOS Y ABONO")) return 6;
        if( _sectionTitle.equals("Servicios Financiero")) return 5;
        if(_sectionTitle.equals("No clasificados") ) return 7;
        return 0;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSectionDetail(groupServiceId);
    }

    void getSectionDetail(int pSection) {

        //trackerPie.start();
        //trackerPie.incrementCounter("CargandoPie");
        //hardcode
        //_comisionistaId = "4993";
        //_comercioId = "5230";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = getString(R.string.base_api_lab) + PARA_DETALLE_GANANCIAS +
                "?comisionistaId=" + _comisionistaId +
                "&comercioId=" + _comercioId +
                "&fechaConsulta=" + _fecha +
                "&sectionId=" + pSection;
        url = url.trim();

        Log.i("PETION", "URL:" + url);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray pResponse) {
                        Log.i("PETICION", "response:" + pResponse);
                        try {
                            List<DetalleSeccion> trans = new ArrayList<>();
//                            _data = new JSONArray(pResponse.toString());
                            _data=pResponse;
                            if (_data != null) {
//
                                for (int i = 0; i < _data.length(); i++) {
                                    JSONObject reg = _data.getJSONObject(i);
                                    trans.add(new DetalleSeccion(reg));
                                }
                                detailAdapdter = new DetailSectionAdapter(getActivity(), trans);
                                rvDetail.setVisibility(View.VISIBLE);
                                rvDetail.setAdapter(detailAdapdter);

                            }
                        } catch (JSONException e) {
//                                e.printStackTrace();
//                                Toast.makeText(getActivity(), "Ocurrio un error con la peticion.", Toast.LENGTH_LONG).show();
//                                getActivity().finish();
                        }
                        //trackerPie.stop();
                    }
                }, new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PETICION", "Error response:" + error);
                        //trackerPie.stop();

                    }
                })

        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + _token);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);

    }

    public void cargarSesion(){
        preferences = getActivity().getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        _comisionistaId = preferences.getString("commisionAgentId", "");
        _comercioId = preferences.getString("storeId", "");
    }

}
