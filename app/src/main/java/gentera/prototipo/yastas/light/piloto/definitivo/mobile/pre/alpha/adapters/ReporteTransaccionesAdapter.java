package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Reportes;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.BuscarDispositivos;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogCorreo;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogCorreoRecargas;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogReSms;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.DialogReenvioCorreo;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.TransaccionReg;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.Reimpresion;

/**
 * Created by jsanchezg on 22/10/2018.
 */

public class ReporteTransaccionesAdapter extends RecyclerView.Adapter<ReporteTransaccionesAdapter.ViewHolder> {

    private final static String TAGrta = "ReporteTransaccionesAdapter";
    private List<TransaccionReg> _transacciones;
    private Context _context;
    private Reimpresion reimpresion;
    private DialogReSms dialogReSms;
    private FragmentManager fmReenvio;
    public static String reenvioInfo;
    private String folioAvonReenvio, storeId, nombreComercio, companniaReimpresion, autorizationCodeReimpresion, importeReimpresion;
    private SharedPreferences preferences;
    private BluetoothAdapter paraPrenderBluetooth;
    private Activity activityImpresion;
    private BluetoothAdapter mBluetoothAdapter = null;
    private DialogReenvioCorreo dialogReenvioCorreo;


    public ReporteTransaccionesAdapter(Context context, List<TransaccionReg> items, FragmentManager fm, Activity activityReimpresion) {
        _transacciones = items;
        fmReenvio = fm;
        this._context = context;
        activityImpresion = activityReimpresion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaccion_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        preferences = _context.getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        storeId = preferences.getString("storeId", "");
        nombreComercio = preferences.getString("StoreName", "");

        paraPrenderBluetooth = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        holder.mItem = _transacciones.get(position);
        holder.monto.setText(String.valueOf(holder.mItem.getMonto()));
        holder.referencia.setText(String.valueOf(holder.mItem.getReferencia()));
        holder.autorizacion.setText(String.valueOf(holder.mItem.getAutorizacion()));
        holder.hora.setText(String.valueOf(holder.mItem.getHora()));

        if(holder.mItem.getDescripcion().equals("Tiempo Aire Iusacell")){
            companniaReimpresion = "Tiempo Aire At&t";
        } else {
            companniaReimpresion = holder.mItem.getDescripcion();
        }

        autorizationCodeReimpresion = String.valueOf(holder.mItem.getAutorizacion());
        importeReimpresion = String.valueOf(holder.mItem.getMonto());

        holder.btnSubMenuDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenuRenotificacion = new PopupMenu(_context, holder.btnSubMenuDetalle);
                popupMenuRenotificacion.getMenuInflater().inflate(R.menu.menu_popup_renotificar, popupMenuRenotificacion.getMenu());
                reenvioInfo = "\nPago: ";
                if(holder.mItem.getDescripcion().equals("Tiempo Aire Iusacell")){
                    reenvioInfo += "Tiempo Aire At&t";
                } else {
                    reenvioInfo += holder.mItem.getDescripcion();
                }
                reenvioInfo += "\nFecha: " + String.valueOf(holder.mItem.getFecha() + "  "+ holder.mItem.getHora());
                reenvioInfo += "\nComercio: " + nombreComercio;
                reenvioInfo += "\nId: " + storeId;
                reenvioInfo += "\n" + String.valueOf(holder.mItem.getReferencia());
                reenvioInfo += "\n" + String.valueOf(holder.mItem.getAutorizacion());
                reenvioInfo += "\nImporte: " + String.valueOf(holder.mItem.getMonto());
                if(holder.mItem.getServiceId()==33){
                    reenvioInfo += "\n" + folioAvonReenvio;
                }
                popupMenuRenotificacion.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getOrder()){
                            case 7:
                                abriDialogReSms("reenvio sms");
                                break;
                            case 8:
                                abriDialogCorreo();
                                break;
                            case 9:
                                if(paraPrenderBluetooth.isEnabled()){
                                    Intent dispositivosIntent = new Intent(_context, BuscarDispositivos.class);
                                    activityImpresion.startActivityForResult(dispositivosIntent, Reportes.REQUEST_CONNECT_DEVICE);

                                } else {
                                    AlertDialog.Builder builderBluetooth = new AlertDialog.Builder(_context);
                                    builderBluetooth.setTitle(_context.getResources().getString(R.string.encender_bluetooth));
                                    builderBluetooth.setMessage(_context.getResources().getString(R.string.expliacion_bluetooth));
                                    builderBluetooth.setIcon(_context.getResources().getDrawable(R.drawable.ic_bluetooth));
                                    builderBluetooth.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            paraPrenderBluetooth.enable();
                                        }
                                    });
                                    builderBluetooth.show();
                                }
                                break;
                        }

                        return true;
                    }
                });
                popupMenuRenotificacion.show();
            }
        });
        if(holder.mItem.getDescripcion().equals("Tiempo Aire Iusacell")){
            holder.descripcion.setText("Tiempo Aire At&t");
        } else {
            holder.descripcion.setText(holder.mItem.getDescripcion());
        }
        if(holder.mItem.getServiceId()==33)
        {
            String folioAvonString = holder.mItem.getAutorizacion().replace("Aut. ", "Folio Avon: ") +
                    holder.mItem.getMonto().replace("$ ", "") + "00" +
                    holder.mItem.getFecha().replace("-" , "") +
                    holder.mItem.getHora().replace(":", "");
            holder.folioAvon.setText(folioAvonString);
            folioAvonReenvio = folioAvonString;
        } else {
            holder.folioAvon.setText("");
        }

        if(holder.mItem.getStatus().equals("C"))
        {
            holder.status.setVisibility(View.VISIBLE);
        }else
        {
            holder.status.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        public final TextView folioAvon;
        public final ImageButton btnSubMenuDetalle;


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
            folioAvon = view.findViewById(R.id.tv_folio_avon);
            btnSubMenuDetalle = view.findViewById(R.id.actions);

            btnSubMenuDetalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }

    public void abriDialogReSms(String masChoros){
        dialogReSms = DialogReSms.newInstance(_context.getResources().getString(R.string.enviar_comprobante), _context.getResources().getString(R.string.ingresa_phone));
        dialogReSms.show(fmReenvio, "dialogSms");

        Fragment frag = fmReenvio.findFragmentByTag("dialogSms");

        if(frag != null){
            fmReenvio.beginTransaction().remove(frag).commit();
        }

    }

    public void abriDialogCorreo(){
        dialogReenvioCorreo = DialogReenvioCorreo.newInstance("param0", "param1", "param2");
        dialogReenvioCorreo.show(fmReenvio, "dialogReenvio");

        Fragment fragCorreo = fmReenvio.findFragmentByTag("dialogReenvio");

        if (fragCorreo !=null){
            fmReenvio.beginTransaction().remove(fragCorreo).commit();
        }

    }



}
