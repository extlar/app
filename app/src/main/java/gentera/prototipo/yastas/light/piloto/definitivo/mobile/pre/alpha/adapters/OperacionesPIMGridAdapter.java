package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.PagoConPIM;
import com.gentera.sdk.modules.pim.model.Category;
import com.gentera.sdk.modules.pim.model.Product;

import static android.support.constraint.Constraints.TAG;


public class OperacionesPIMGridAdapter extends BaseAdapter {
    private Context context;
    private List<Category> listCategory;
    private List<Product> listProductos;

    public <Category> OperacionesPIMGridAdapter(Context context, List<Category> listaCategory, boolean initial) {
        this.context = context;
        this.listCategory = initial? ((List<com.gentera.sdk.modules.pim.model.Category>) listaCategory).get(0).getChildren(): (List<com.gentera.sdk.modules.pim.model.Category>) listaCategory;
        //this.listCategory= ((List<com.gentera.sdk.modules.pim.model.Category>) listaCategory).get(0).getChildren();

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

                SharedPreferences preferences = context.getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("encabezadoPagoPIM", listCategory.get(position).getTitle());

                //bundleTipoServicio.putString("code", listCategory.get(position).getCode());
                JSONArray jsArray = new JSONArray(listCategory);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Category>>(){}.getType();
                String hijos = gson.toJson(listCategory.get(position).getChildren(), type);

                //bundleTipoServicio.putString("hijos", hijos);
                intentPagoPim.putExtras(bundleTipoServicio);
                editor.putString("hijos", hijos);
                editor.putString("code", listCategory.get(position).getCode());
                editor.apply();
                context.startActivity(intentPagoPim);

            }
        });

        return gridView;
    }

}