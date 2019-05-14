package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.ItemSlideMainMenu;

public class SlidingMenuMainAdapter extends BaseAdapter {

    private Context context;
    private List<ItemSlideMainMenu> lstItem;
    private Typeface amorSansProBold;

    public SlidingMenuMainAdapter(Context context, List<ItemSlideMainMenu> lstItem) {
        this.context = context;
        this.lstItem = lstItem;
        amorSansProBold  = Typeface.createFromAsset(context.getAssets(), "fonts/AmorSansProBold.otf");
    }

    @Override
    public int getCount() {
        return lstItem.size();
    }

    @Override
    public Object getItem(int position) {
        return lstItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_sliding_main_menu, null);
        ImageView img =(ImageView)v.findViewById(R.id.item_img);
        TextView tv =(TextView)v.findViewById(R.id.item_title);
        tv.setTypeface(amorSansProBold);
        //tv.startAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.slide_in_left));
        //img.startAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.slide_in_left));
        ItemSlideMainMenu item = lstItem.get(position);
        img.setImageResource(item.getImgId());
        tv.setText(item.getTitle());
        return v;
    }

}
