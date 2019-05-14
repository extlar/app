package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;

/**
 * Created by jsanchezg on 25/10/2018.
 */

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;

    public CustomMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        float dia = e.getX();
        dia += 1;
        String diaPunto = String.valueOf(dia);
            diaPunto = diaPunto.substring(0, diaPunto.length()-2);
            tvContent.setText("$" + e.getY() + " - " + diaPunto); // set the entry-value as the display text

        super.refreshContent(e,highlight);
    }

//    @Override
//    public int getXOffset(float xpos) {
//        // this will center the marker-view horizontally
//        return -(getWidth() / 2);
//    }
//
//    @Override
//    public int getYOffset(float ypos) {
//        // this will cause the marker-view to be above the selected value
//        return -getHeight();
//    }
}