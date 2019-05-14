package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GananciasSeccionFragment extends Fragment {
    PieChart pieChartTiempoAire;
    private static final String ARG_PARAM1 = "section";
    private static final String TAG = "GananciaSeccion";
    private JSONObject _sectionJson;
    private JSONArray _sectionDetail;
    private String _sectionStr;
    private String _sectionTitle;

    public GananciasSeccionFragment() {
        // Required empty public constructor
    }

    public static GananciasSeccionFragment newInstance(String pSectionJson) {
        GananciasSeccionFragment fragment = new GananciasSeccionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, pSectionJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _sectionStr = getArguments().getString(ARG_PARAM1);
            try {
                _sectionJson = new JSONObject(_sectionStr);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

        }
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
            for (int i = 0; i < _sectionDetail.length(); i++) {
                float value = (float) _sectionDetail.optJSONObject(i).getDouble("amount");
                String concepto = _sectionDetail.optJSONObject(i).getString("concept");
                concepto = concepto.replace("RECARGA TIEMPO AIRE ", "");
                concepto= concepto.toLowerCase();
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

        PieData data = new PieData(pieDataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(getResources().getColor(R.color.gris));
        pieChartTiempoAire.setEntryLabelTextSize(10);
        pieChartTiempoAire.setEntryLabelColor(getResources().getColor(R.color.gris));
        pieChartTiempoAire.setData(data);


        return reportesTAView;


    }

}
