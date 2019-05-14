package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.ProcesoDePago;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.dbs.DataBaseInfoServicios;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.Servicios;

import static gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.ProcesoDePago.etReferenciaCosmeticos;
import static gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.ProcesoDePago.iBtnActivaEscanner;
import static gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.ProcesoDePago.rlContenedorPaso2;

/**
 * Created by El Tona on 18/12/2017.
 */

public class ProcesoPagoAdapter extends BaseAdapter{
    private Context context;
    private int logosServicios[];
    private LayoutInflater inflaterGridTelefonicas;
    private String[] compannias;
    private String companniaRecarga;
    public static String commpanniaBusqueda;
    private DataBaseInfoServicios dataBaseInfoServicios;
    public static int longitudReferencia, conLectorInt, topeLongitudReferencia;

    public ProcesoPagoAdapter(Context context, int logosTelefonicas[]) {
        this.context = context;
        this.logosServicios = logosTelefonicas;
    }

    @Override
    public int getCount() {
        return logosServicios.length;
    }

    @Override
    public Object getItem(int position) {
        return logosServicios[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View gridView = convertView;


        if(convertView == null){
            inflaterGridTelefonicas = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflaterGridTelefonicas.inflate(R.layout.grid_pago_items, null);
        }

        ImageView logoCells = (ImageView) gridView.findViewById(R.id.ib_grid_compannias);
        logoCells.setImageResource(logosServicios[position]);
        logoCells.setTag(logosServicios[position]);

        logoCells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        cambio(position);

            }
        });

        return gridView;
    }

    public void cambio(int pos){
        ProcesoDePago.tvNombreCosmetico.setText(ProcesoDePago.nombreCompannias[pos]);
        ProcesoDePago.compannia = ProcesoDePago.nombreCompannias[pos];

        dataBaseInfoServicios = new DataBaseInfoServicios(context);

        longitudReferencia = dataBaseInfoServicios.getLongitudReferencia(ProcesoDePago.nombreCompannias[pos]);
        if(dataBaseInfoServicios.getConLector(ProcesoDePago.nombreCompannias[pos])){
            iBtnActivaEscanner.setVisibility(View.VISIBLE);
        } else {
            iBtnActivaEscanner.setVisibility(View.GONE);
        }

        ProcesoDePago.rlContenedorPaso1.setVisibility(View.GONE);
        if(dataBaseInfoServicios.getMultipleReferencia(ProcesoDePago.nombreCompannias[pos])){
            topeLongitudReferencia = dataBaseInfoServicios.getTopeReferencia(ProcesoDePago.nombreCompannias[pos]);
            etReferenciaCosmeticos.setHint("Introduce referencia de " + longitudReferencia + " o " + topeLongitudReferencia + " digitos");
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(topeLongitudReferencia);
            etReferenciaCosmeticos.setFilters(fArray);

        } else {
            etReferenciaCosmeticos.setHint("Introduce referencia de " + longitudReferencia + " digitos");
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(longitudReferencia);
            etReferenciaCosmeticos.setFilters(fArray);

        }

        rlContenedorPaso2.setVisibility(View.VISIBLE);

    }

}
