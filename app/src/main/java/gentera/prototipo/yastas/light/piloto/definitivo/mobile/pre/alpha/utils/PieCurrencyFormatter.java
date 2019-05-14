package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class PieCurrencyFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public  PieCurrencyFormatter() {
        mFormat = new DecimalFormat("###,###,##0.00"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return "$ "+mFormat.format(value) ; // e.g. append a dollar-sign
    }
}
