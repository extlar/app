package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.TransaccionReg;

/**
 * Created by jsanchezg on 22/10/2018.
 */

public class ReporteTransaccionesAdapter extends RecyclerView.Adapter<ReporteTransaccionesAdapter.ViewHolder> {

    private final static String TAG = "ReporteTransaccionesAdapter";
    private List<TransaccionReg> _transacciones;
    private Context _context;

    public ReporteTransaccionesAdapter(Context context, List<TransaccionReg> items) {
        _transacciones = items;
        this._context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaccion_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItem = _transacciones.get(position);
        holder.descripcion.setText(holder.mItem.getDescripcion());
        holder.monto.setText(String.valueOf(holder.mItem.getMonto()));
        holder.referencia.setText(String.valueOf(holder.mItem.getReferencia()));
        holder.autorizacion.setText(String.valueOf(holder.mItem.getAutorizacion()));
        holder.hora.setText(String.valueOf(holder.mItem.getHora()));
        if(holder.mItem.getStatus().equals("C"))
        {
            holder.status.setVisibility(View.VISIBLE);
        }else
        {
            holder.status.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return _transacciones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView descripcion;
        public final TextView monto;
        public final TextView referencia;
        public final TextView autorizacion;
        public final TextView hora;
        public final ImageView status;


        public TransaccionReg mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            descripcion =  view.findViewById(R.id.trans_descripcion2);
            monto =  view.findViewById(R.id.trans_monto);
            referencia =  view.findViewById(R.id.tran_ref);
            autorizacion =  view.findViewById(R.id.trans_autorizacion);
            hora =  view.findViewById(R.id.trans_hora);
            status =  view.findViewById(R.id.status);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
