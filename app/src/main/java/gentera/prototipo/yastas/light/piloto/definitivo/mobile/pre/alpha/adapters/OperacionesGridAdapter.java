package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.PagoDeServicios;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.ProcesoDePago;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.RecargaPaqueteDatos;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Recargas;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Reportes;

public class OperacionesGridAdapter extends BaseAdapter {
    private Context context;
    private int iconosOperaciones[];
    private String titulosOperaciones[];
    private LayoutInflater inflaterGridTelefonicas;
    private String roll;

    public OperacionesGridAdapter(Context context, int iconosOperaciones[], String titulosOperaciones[]) {
        this.context = context;
        this.iconosOperaciones = iconosOperaciones;
        this.titulosOperaciones = titulosOperaciones;
    }

    @Override
    public int getCount() {
        return iconosOperaciones.length;
    }

    @Override
    public Object getItem(int position) {
        return iconosOperaciones[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View gridView = convertView;


        if(convertView == null){
            inflaterGridTelefonicas = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflaterGridTelefonicas.inflate(R.layout.grid_operaciones_items_main, null);
        }

        ImageView logoOperaciones = (ImageView) gridView.findViewById(R.id.ib_grid_icono_operaciones);
        logoOperaciones.setImageResource(iconosOperaciones[position]);
        logoOperaciones.setTag(iconosOperaciones[position]);
        TextView tituloOperaciones = (TextView)gridView.findViewById(R.id.tv_grid_titulo_operaciones);
        tituloOperaciones.setText(titulosOperaciones[position]);

        logoOperaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        SharedPreferences preferences = context.getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
                        roll = preferences.getString("rollSession", "");
                        if(roll.equals("2")){
                            context.startActivity(new Intent(context, Reportes.class));
                            ((Activity)context).finish();
                        } else {
                            Toast.makeText(context, "No cuentas con permiso para acceder a los reportes", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        //startActivity(new Intent(MainActivity.this, Administracion.class));
                        Toast.makeText(context, "Servicio disponible proximamente", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return gridView;
    }

}