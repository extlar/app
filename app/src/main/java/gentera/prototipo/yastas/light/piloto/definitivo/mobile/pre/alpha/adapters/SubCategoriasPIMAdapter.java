package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.PagoConPIM;
import com.gentera.sdk.modules.pim.model.Category;


public class SubCategoriasPIMAdapter extends BaseAdapter {
    private Context context;
    private List<Category> listCategory;

    public <Category> SubCategoriasPIMAdapter(Context context, List<Category> listaCategory) {
        this.context = context;
        this.listCategory= ((List<com.gentera.sdk.modules.pim.model.Category>) listaCategory).get(0).getChildren();
        //Category reportes = (Category) new gentera.sdk.modules.pim.model.Category("REPORTES", "REPORTES", gentera.sdk.modules.pim.model.Category, null, null);
        //listaCategory.add(reportes);
    }

    /*public <Category> OperacionesPIMGridAdapter(MainActivity context, List<Category> lis) {
        this.context=context;
        this.listCategory= ((List<gentera.sdk.modules.pim.model.Category>) lis).get(0).getChildren();
    }
*/
    @Override
    public int getCount() {
        return listCategory.size();
    }

    @Override
    public Object getItem(int position) {
        return listCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View gridView = convertView;

        if(convertView == null){
            LayoutInflater inflaterGridCategorias = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflaterGridCategorias.inflate(R.layout.grid_operaciones_items_main, null);
        }

        ImageView logoOperaciones = (ImageView) gridView.findViewById(R.id.ib_grid_icono_operaciones);
        TextView tituloOperaciones = (TextView)gridView.findViewById(R.id.tv_grid_titulo_operaciones);

        tituloOperaciones.setText(listCategory.get(position).getTitle());
        if(listCategory.get(position).getTitle().equals("REPORTES")){
            logoOperaciones.setImageDrawable(context.getResources().getDrawable(R.drawable.reportes));
        }
        Glide.with(context).load(listCategory.get(position).getLogoUrl()).into(logoOperaciones);

        logoOperaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPagoPim = new Intent(context, PagoConPIM.class);
                final Bundle bundleTipoServicio = new Bundle();
                bundleTipoServicio.putString("paraTitulo", listCategory.get(position).getTitle());
                bundleTipoServicio.putString("hijos", listCategory.get(position).getChildren().toString());
                intentPagoPim.putExtras(bundleTipoServicio);
                context.startActivity(intentPagoPim);

            }
        });

        return gridView;
    }

}