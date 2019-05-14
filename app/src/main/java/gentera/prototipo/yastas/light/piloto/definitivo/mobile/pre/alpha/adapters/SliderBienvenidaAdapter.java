package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Bienvenida;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.LoginUsuariosExistentes;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.TerminosCondicionesyAviso;

public class SliderBienvenidaAdapter extends PagerAdapter{
    public static Context context;
    LayoutInflater layoutInflater;
    private FragmentManager mfragmentManager;
    public String quePinto;
    public Bundle bundleChoro;
    private Boolean bienvenidaYaLaVi;
    public String[] slideEncabezados ={"Bienvenido a ", "Recarga de tiempo aire", "Pago de servicios", "Compra de C\u00F3digos", ""},
    slideTextos ={"Conoce todos los servicios que puedes ofrecer desde tu aplicaci\u00F3n <i><b>Pagos Yast\u00E1s</b><i> <b>¡gana una comisi\u00F3n por cada operaci\u00F3n que realices!</b>",
            "Vende tiempo aire de las compa\u00F1ias participantes y <b>¡gana una comisi\u00F3n por cada recarga!</b>",
            "Acepta pago de servicios de tus clientes como agua, luz, gas, internet, tv de paga e internet y <b>¡gana una comisi\u00F3n por cada pago</b>!",
            "Vende c\u00F3digos/pin para suscripci\u00F3n a Blim, Klic, saldo para Nintendo, Xbox, boletos de Cin\u00E9polis, etc. y <b>¡gana una comisi\u00F3n por cada venta</b>!",
            "Al continuar confirmo que he le\u00EDdo y acepto"};
    public int[] slideImagenes ={R.mipmap.ic_launcher, R.drawable.tiempo_aire_morado, R.drawable.servicios_morado, R.drawable.apps_morado, R.drawable.wt5_ii};

    public SliderBienvenidaAdapter(Context context, FragmentManager mfragmentManager) {
        this.context = context;
        this.mfragmentManager = mfragmentManager;
    }


    @Override
    public int getCount() {
        return     slideTextos.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout) object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.layout_bienvenida_slide, container, false);

        ImageView ivBienvenida = view.findViewById(R.id.iv_bienvenida);
        TextView tvEncabezadoBienvenida = view.findViewById(R.id.tv_encabezado_bienvenida);
        TextView tvEncabezadoSegundoBienvenida = view.findViewById(R.id.tv_encabezado_segunda_parte_bienvenida);
        TextView tvDescripcionBienvenida = view.findViewById(R.id.tv_descripcion_bienvenida);
        Button btnTerminaBienvenida = view.findViewById(R.id.btn_terminar_bienvenida);
        TextView tvDescripcionBienvenida2 = view.findViewById(R.id.tv_descripcion_2_4_bienvenida);
        TextView tvDescripcionBienvenida3 = view.findViewById(R.id.tv_descripcion_3_4_bienvenida);
        TextView tvDescripcionBienvenida4 = view.findViewById(R.id.tv_descripcion_4_4_bienvenida);
        final CheckBox cbValidarVistaTerminoCondiciones = view.findViewById(R.id.cb_validar_aviso_terminos);

        ((Bienvenida) context).getFragmentManager();
        Typeface amorSansPro = Typeface.createFromAsset(((Bienvenida) context).getAssets(), "fonts/AmorSansPro.otf");
        Typeface amorSansProBold = Typeface.createFromAsset(((Bienvenida) context).getAssets(), "fonts/AmorSansProBold.otf");

        btnTerminaBienvenida.setTypeface(amorSansProBold);

        tvEncabezadoBienvenida.setTypeface(amorSansProBold);
        tvDescripcionBienvenida.setTypeface(amorSansPro);
        tvDescripcionBienvenida2.setTypeface(amorSansPro);
        tvDescripcionBienvenida3.setTypeface(amorSansPro);
        tvDescripcionBienvenida4.setTypeface(amorSansPro);

        ivBienvenida.setImageResource(slideImagenes[position]);
        tvEncabezadoBienvenida.setText(slideEncabezados[position].toUpperCase());
        tvDescripcionBienvenida.setText(Html.fromHtml(slideTextos[position]));
        btnTerminaBienvenida.setVisibility(View.GONE);

        switch (position){
            case 0:
                tvEncabezadoSegundoBienvenida.setText(R.string.app_name);
                break;
            case 4:
                tvEncabezadoBienvenida.setVisibility(View.GONE);
                tvEncabezadoSegundoBienvenida.setVisibility(View.GONE);
                btnTerminaBienvenida.setVisibility(View.VISIBLE);
                tvDescripcionBienvenida2.setVisibility(View.VISIBLE);
                tvDescripcionBienvenida3.setVisibility(View.VISIBLE);
                tvDescripcionBienvenida4.setVisibility(View.VISIBLE);
                cbValidarVistaTerminoCondiciones.setSelected(false);
                cbValidarVistaTerminoCondiciones.setVisibility(View.VISIBLE);

                tvDescripcionBienvenida2.setText(Html.fromHtml("los <b><u>t\u00E9rminos y condiciones</u></b>"));
                tvDescripcionBienvenida3.setText(Html.fromHtml("y el <b><u>aviso de privacidad</u></b>"));
                tvDescripcionBienvenida4.setText(Html.fromHtml("de uso de la apliacaci\u00F3n"));

                tvDescripcionBienvenida2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quePinto = "terminosycondiciones";
                        bundleChoro = new Bundle();
                        bundleChoro.putString("paraelchoro",quePinto);
                        ComoDeQueNO dijeQueSi = new ComoDeQueNO();
                        dijeQueSi.abrirDialogAvisoYTerminos(context, mfragmentManager);

                    }
                });

                tvDescripcionBienvenida3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quePinto = "avisodeprivacidad";
                        bundleChoro = new Bundle();
                        bundleChoro.putString("paraelchoro",quePinto);
                        ComoDeQueNO dijeQueSi = new ComoDeQueNO();
                        dijeQueSi.abrirDialogAvisoYTerminos(context, mfragmentManager);

                    }
                });

                btnTerminaBienvenida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cbValidarVistaTerminoCondiciones.isChecked()){
                            limpiarUsuario("", "", "", "");
                            context.startActivity(new Intent(context, LoginUsuariosExistentes.class));
                            ((Activity)context).finish();

                        } else {
                            Toast.makeText(context, "Debes aceptar los 'Terminos y Condiciones' y el 'Aviso de Privacidad'", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }

    @SuppressLint("ValidFragment")
    public class ComoDeQueNO extends android.support.v4.app.DialogFragment {

        public void abrirDialogAvisoYTerminos(Context context, FragmentManager fragmentManager) {
            TerminosCondicionesyAviso terminosCondicionesyAvisoFragment = new TerminosCondicionesyAviso();
            terminosCondicionesyAvisoFragment.show(fragmentManager, "avisos");
            Fragment frag = fragmentManager.findFragmentByTag("avisos");
            terminosCondicionesyAvisoFragment.setArguments(bundleChoro);
            if (frag != null) {
                getFragmentManager().beginTransaction().remove(frag).commit();
            }

        }
    }

    public void limpiarUsuario(String nombreUsuario,String numeroDeUsuario, String roll, String token){
        SharedPreferences preferences = context.getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombreUsuarioSession", nombreUsuario);
        editor.putString("numeroDeUsuarioSession", numeroDeUsuario);
        editor.putString("rollSession", roll);
        editor.putString("yaConTokenSession", token);

        editor.putBoolean("conLosUltimosSkus", true);
        editor.apply();
    }
}
