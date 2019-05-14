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

public class ServiciosGridAdapter extends BaseAdapter {
    private Context context;
    private int iconosOperaciones[];
    private String titulosOperaciones[];
    private LayoutInflater inflaterGridTelefonicas;

    public ServiciosGridAdapter(Context context, int iconosOperaciones[], String titulosOperaciones[]) {
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
                final Intent intentProcesoPago = new Intent (context, ProcesoDePago.class);
                final Bundle bundleTipoServicio = new Bundle();
                switch (position){
                    case 0:
                        bundleTipoServicio.putString("tipoDeServicio", "TelefoniaEInternet");
                        intentProcesoPago.putExtras(bundleTipoServicio);
                        context.startActivity(intentProcesoPago);
                        //Toast.makeText(context, "Servicio disponible proximamente", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(context, "Servicio disponible proximamente", Toast.LENGTH_SHORT).show();
                        /*
                        bundleTipoServicio.putString("tipoDeServicio", "Agua");
                        intentProcesoPago.putExtras(bundleTipoServicio);
                        context.startActivity(intentProcesoPago);*/
                        break;
                    case 2:
                        bundleTipoServicio.putString("tipoDeServicio", "Luz");
                        intentProcesoPago.putExtras(bundleTipoServicio);
                        context.startActivity(intentProcesoPago);
                        //Toast.makeText(context, "Servicio disponible proximamente", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(context, "Servicio disponible proximamente", Toast.LENGTH_SHORT).show();
                        /*
                        bundleTipoServicio.putString("tipoDeServicio", "Gas");
                        intentProcesoPago.putExtras(bundleTipoServicio);
                        context.startActivity(intentProcesoPago);*/
                        break;
                    case 4:
                        Toast.makeText(context, "Servicio disponible proximamente", Toast.LENGTH_SHORT).show();
                        /*
                        bundleTipoServicio.putString("tipoDeServicio", "TesoreriaYGobierno");
                        intentProcesoPago.putExtras(bundleTipoServicio);
                        context.startActivity(intentProcesoPago);*/
                        break;
                    case 5:
                        bundleTipoServicio.putString("tipoDeServicio", "VentaPorCatalogo");
                        intentProcesoPago.putExtras(bundleTipoServicio);
                        ((Activity)context).finish();
                        context.startActivity(intentProcesoPago);
                        break;
                }
                //Intent intent = new Intent(context, CantidadRecarga.class);
                //intent.putExtra(Recargas.PARAM_LOGO_COMPANNIAS_CELULARE_ID_RESOURCE, id);
                //ActivityOptionsCompat options = ActivityOptionsCompat.
                  //      makeSceneTransitionAnimation((Activity) context,(ImageView)v, ViewCompat.getTransitionName((ImageView)v));
                //context.startActivity(intent, options.toBundle());
            }
        });

        return gridView;
    }

}