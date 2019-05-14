package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

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
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.DetalleSeccion;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.TransaccionReg;

/**
 * Created by jsanchezg on 22/10/2018.
 */

public class DetailSectionAdapter extends RecyclerView.Adapter<DetailSectionAdapter.ViewHolder> {

    private final static String TAG = "ReporteTransaccionesAdapter";
    private List<DetalleSeccion> _transacciones;
    private Context _context;

    public DetailSectionAdapter(Context context, List<DetalleSeccion> items) {
        _transacciones = items;
        this._context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItem = _transacciones.get(position);
        holder.descripcion.setText(holder.mItem.getDescripcion());
        holder.monto.setText(String.valueOf(holder.mItem.getMonto()));
        holder.dia.setText(String.valueOf(holder.mItem.getDia()));

    }

    @Override
    public int getItemCount() {
        return _transacciones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView descripcion;
        public final TextView monto;
        public final TextView dia;



        public DetalleSeccion mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            descripcion =  view.findViewById(R.id.tv_desc);
            monto =  view.findViewById(R.id.tv_monto);
            dia =  view.findViewById(R.id.tv_dia);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
