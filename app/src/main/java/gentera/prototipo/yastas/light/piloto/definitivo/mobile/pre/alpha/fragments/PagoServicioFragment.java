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
public class PagoServicioFragment extends Fragment {
    PieChart pieChartServicios;

    public PagoServicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View reportesRepoView = inflater.inflate(R.layout.fragment_servicios_reportes, container, false);
        pieChartServicios = (PieChart)reportesRepoView.findViewById(R.id.pie_prueba_servicios);

        pieChartServicios.setUsePercentValues(true);
        pieChartServicios.getDescription().setEnabled(false);
        pieChartServicios.setExtraOffsets(5, 10, 5, 5);
        pieChartServicios.setDragDecelerationFrictionCoef(0.95f);
        pieChartServicios.setDrawHoleEnabled(true);
        pieChartServicios.setHoleRadius(21);
        pieChartServicios.setHoleColor(getResources().getColor(R.color.transparente));
        pieChartServicios.setTransparentCircleRadius(21f);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(46f, "CFE"));
        yValues.add(new PieEntry(25f, "Gas Natural"));
        yValues.add(new PieEntry(10f, "Telmex"));
        yValues.add(new PieEntry(34f, "Izzi"));
        yValues.add(new PieEntry(4f, "Infonavit"));
        yValues.add(new PieEntry(140f, "Pase"));
        yValues.add(new PieEntry(35f, "Avon"));
        yValues.add(new PieEntry(45f, "Siapa"));
        yValues.add(new PieEntry(31f, "Televia"));

        pieChartServicios.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet pieDataSet = new PieDataSet(yValues, "Telefonia movil");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data  = new PieData(pieDataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(getResources().getColor(R.color.morado_yastas));

        pieChartServicios.setData(data);

        return reportesRepoView;
    }

}
