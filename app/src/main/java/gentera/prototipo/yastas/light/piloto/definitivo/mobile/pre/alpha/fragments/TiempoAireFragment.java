package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TiempoAireFragment extends Fragment {
    PieChart pieChartTiempoAire;


    public TiempoAireFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View reportesTAView = inflater.inflate(R.layout.fragment_tiempo_aire_reportes, container, false);
        pieChartTiempoAire = (PieChart)reportesTAView.findViewById(R.id.pie_prueba_tiempo_aire);

        pieChartTiempoAire.setUsePercentValues(true);
        pieChartTiempoAire.getDescription().setEnabled(false);
        pieChartTiempoAire.setExtraOffsets(5, 10, 5, 5);
        pieChartTiempoAire.setDragDecelerationFrictionCoef(0.95f);
        pieChartTiempoAire.setDrawHoleEnabled(true);
        pieChartTiempoAire.setHoleRadius(40);
        pieChartTiempoAire.setHoleColor(getResources().getColor(R.color.transparente));
        pieChartTiempoAire.setTransparentCircleRadius(21f);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(46f, "Telcel"));
        yValues.add(new PieEntry(25f, "at&t"));
        yValues.add(new PieEntry(10f, "Movistar"));
        yValues.add(new PieEntry(34f, "Unefon"));

        pieChartTiempoAire.animateY(1000, Easing.EasingOption.EaseInElastic);

        PieDataSet pieDataSet = new PieDataSet(yValues, "Telefonia movil");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data  = new PieData(pieDataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(getResources().getColor(R.color.morado_yastas));

        pieChartTiempoAire.setData(data);

        return reportesTAView;


    }

}
