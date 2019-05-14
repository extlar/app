package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.PagoConPIM;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gentera.sdk.modules.pim.model.Product;


public class ProductosPIMAdapter extends BaseAdapter {
    private Context context;
    private List<Product> listProductos;
    private String code;

    public <Product> ProductosPIMAdapter(Context context, List<Product> listProductos, String code) {
        this.context = context;
        this.listProductos = (List<com.gentera.sdk.modules.pim.model.Product>) listProductos;
        this.code = code;
    }

    @Override
    public int getCount() {
        return listProductos.size();
    }

    @Override
    public Object getItem(int position) {
        return listProductos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View gridView = convertView;


        if (convertView == null) {
            LayoutInflater inflaterGridCategorias = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflaterGridCategorias.inflate(R.layout.grid_operaciones_items_main, null);
            //gridView.setBackgroundColor(context.getResources().getColor(R.color.verde_yastas));
      }
            //final ProgressBar loadinImage = (ProgressBar) gridView.findViewById(R.id.loading_image);

        final ImageView logoOperaciones = (ImageView) gridView.findViewById(R.id.ib_grid_icono_operaciones);
        TextView tituloOperaciones = (TextView) gridView.findViewById(R.id.tv_grid_titulo_operaciones);

        //loadinImage.setVisibility(View.VISIBLE);

        Glide.with(context).load(listProductos.get(position).getImageProductAtt()).into(logoOperaciones);
        tituloOperaciones.setText(listProductos.get(position).getProductNameAtt());
        logoOperaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagoConPIM.gvSubCategoriasPim.setVisibility(View.GONE);
                PagoConPIM.rlContenedorPrincipalPagoPIM.setVisibility(View.VISIBLE);
                PagoConPIM.rlContenedorPaso1PIM.setVisibility(View.VISIBLE);
                PagoConPIM.companniaPIM = listProductos.get(position).getCompanyNameAtt();
                PagoConPIM.footerRecibo = listProductos.get(position).getFooterAtt();
                PagoConPIM.tvCompanniaPagoPim.setText(listProductos.get(position).getProductNameAtt());
                int scannerAAr = listProductos.get(position).getEnableScannerAtt();
                if (scannerAAr == 1) {
                    PagoConPIM.ibActivaEscanerPIM.setVisibility(View.VISIBLE);
                }
                if (listProductos.get(position).getOverduePaymentAtt() != 1) {
                    PagoConPIM.mascaraReferencia = listProductos.get(position).getReferenceMaskAtt();
                    System.out.print(PagoConPIM.mascaraReferencia);
                }
                PagoConPIM.longitudReferenciaPIM = listProductos.get(position).getReferenceLengthAtt();
                PagoConPIM.etReferenciaPagoPIM.setHint("Introduce referencia de " + String.valueOf(PagoConPIM.longitudReferenciaPIM) + " digitos");
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(PagoConPIM.longitudReferenciaPIM);
                PagoConPIM.etReferenciaPagoPIM.setFilters(fArray);

                PagoConPIM.montoMinimoPIM = listProductos.get(position).getMinLimitAmountAtt();
                PagoConPIM.montoMaximoPIM = listProductos.get(position).getMaxLimitAmountAtt();
                PagoConPIM.externalSku = listProductos.get(position).getExternalSkuAtt();
                PagoConPIM.bussinesServiceId = String.valueOf(listProductos.get(position).getBusinessServiceIdAtt());
                PagoConPIM.comisionstr = listProductos.get(position).getUserFeeAtt();
                PagoConPIM.comisioinTax = listProductos.get(position).getUserFeeVatAtt();
                int referenciaAlpha = listProductos.get(position).getReferenceTypeAtt();
                if (referenciaAlpha == 0) {
                    PagoConPIM.etReferenciaPagoPIM.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (referenciaAlpha == 1) {
                    PagoConPIM.etReferenciaPagoPIM.setInputType(InputType.TYPE_CLASS_NUMBER);
                }

                PagoConPIM.btnPaso12PIM.setVisibility(View.VISIBLE);
                PagoConPIM.etReferenciaPagoPIM.setVisibility(View.VISIBLE);
                PagoConPIM.etMontoPagoPIM.setVisibility(View.VISIBLE);

                PagoConPIM.btnPaso12PIM.setEnabled(true);
                PagoConPIM.etReferenciaPagoPIM.setEnabled(true);
                PagoConPIM.etMontoPagoPIM.setEnabled(true);
                PagoConPIM.leyendaRecibo = listProductos.get(position).getLeyend1Att();
                PagoConPIM.footerReciboPIM = listProductos.get(position).getFooterAtt();

                PagoConPIM.leyendaDinamicaNum = listProductos.get(position).getDynamicLegendAtt();
                PagoConPIM.leyendaDinamica = "";
                switch (listProductos.get(position).getDynamicLegendAtt()){
                    case 0:
                        if(listProductos.get(position).getLeyend1Att().length() > 0){
                            PagoConPIM.leyenda1 = listProductos.get(position).getLeyend1Att();
                            PagoConPIM.leyenda2 = listProductos.get(position).getLeyend2Att();
                            PagoConPIM.leyenda3 = listProductos.get(position).getLeyend3Att();
                        }
                        break;
                    case 1:
                        PagoConPIM.leyenda1 = "";
                        PagoConPIM.leyenda2 = listProductos.get(position).getLeyend2Att();
                        PagoConPIM.leyenda3 = listProductos.get(position).getLeyend3Att();
                        break;
                    case 2:
                        PagoConPIM.leyenda1 = listProductos.get(position).getLeyend1Att();
                        PagoConPIM.leyenda2 = "";
                        PagoConPIM.leyenda3 = listProductos.get(position).getLeyend3Att();
                        break;
                    case 3:
                        PagoConPIM.leyenda1 = listProductos.get(position).getLeyend1Att();
                        PagoConPIM.leyenda2 = listProductos.get(position).getLeyend2Att();
                        PagoConPIM.leyenda3 = "";
                        break;
                        default:
                            break;
                }

                if(code.equals("TelcelTopUp") || code.equals("MovistarTopUp") || code.equals("UnefonTopUp") || code.equals("IusacellTopUp") || code.equals("TelcelPacks") || code.equals("NextelTopUp")){
                    String cantidadRecargas = listProductos.get(position).getProductNameAtt();
                    //cantidadRecargas.lastIndexOf(" ");

                    PagoConPIM.etMontoPagoPIM.setText(cantidadRecargas.substring(cantidadRecargas.lastIndexOf(" "), cantidadRecargas.length()));
                    PagoConPIM.etMontoPagoPIM.setEnabled(false);
                    //PagoConPIM.cantidadProcesada = cantidadRecargas.substring(cantidadRecargas.lastIndexOf(" "), cantidadRecargas.length());
                }
            }
        });

        return gridView;
    }


}