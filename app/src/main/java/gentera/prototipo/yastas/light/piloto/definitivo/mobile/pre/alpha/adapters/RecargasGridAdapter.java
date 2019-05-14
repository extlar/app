package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Recargas;

/**
 * Created by El Tona on 18/12/2017.
 */

public class RecargasGridAdapter extends BaseAdapter{
    private Context context;
    private int logosTelefonicas[];
    private LayoutInflater inflaterGridTelefonicas;
    private String [] nombresCompannias = new String[] {"Telcel", "Movistar", "Nextel", "Att&t", "Unefon"};
    private String[] compannias, montosAtt, montosMovistar, montosUnefon, montosTelcel, montosNextel, internetAmigo, amigoSinLimite;
    private int paraRB, contadorRb;
    private SharedPreferences preferencesRecargas;
    //private String cantidadRecarga;

    public RecargasGridAdapter(Context context, int logosTelefonicas[]) {
        this.context = context;
        this.logosTelefonicas = logosTelefonicas;

        montosAtt = new String[] {"30", "50", "100", "200", "300", "500"};
        montosMovistar = new String[] {"20", "30", "50", "60", "100", "120", "150", "200", "300", "500"};
        montosNextel = new String[] {"30", "50", "100", "200", "500"};
        montosTelcel = new String[] {"20", "30", "50", "100", "150", "200", "300", "500"};
        montosUnefon = new String[] {"30", "50", "150", "200", "300", "500"};
        internetAmigo = new String[] {"20", "30", "50", "100", "150", "200", "300", "500"};
        amigoSinLimite = new String[] {"20", "30", "50", "100", "150", "200", "300", "500"};
    }

    @Override
    public int getCount() {
        return logosTelefonicas.length;
    }

    @Override
    public Object getItem(int position) {
        return logosTelefonicas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View gridView = convertView;


        if(convertView == null){
            inflaterGridTelefonicas = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflaterGridTelefonicas.inflate(R.layout.grid_recargas_items, null);
        }

        ImageView logoCells = (ImageView) gridView.findViewById(R.id.ib_grid_telefonicas);
        logoCells.setImageResource(logosTelefonicas[position]);
        logoCells.setTag(logosTelefonicas[position]);

        logoCells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id= Integer.parseInt(v.getTag().toString() );
                cambio(position);
                //Toast.makeText(context, nombresCompannias[id], Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(context, CantidadRecarga.class);
                //intent.putExtra(Recargas.PARAM_LOGO_COMPANNIAS_CELULARE_ID_RESOURCE, id);
                //ActivityOptionsCompat options = ActivityOptionsCompat.
                  //      makeSceneTransitionAnimation((Activity) context,(ImageView)v, ViewCompat.getTransitionName((ImageView)v));
                //context.startActivity(intent, options.toBundle());
            }
        });

        return gridView;
    }

    public void cambio(int pos){
        preferencesRecargas = context.getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        String trabajando = preferencesRecargas.getString("trabajandoRecargas", "");
        Recargas.rlContenedorPaso2.setVisibility(View.VISIBLE);
        Recargas.rlContenedorPaso1.setVisibility(View.GONE);
        if(trabajando.equals(context.getResources().getString(R.string.tiempo_aire))){
            switch (pos){
                case 0:
                    Recargas.companniaRecarga = "At&t";
                    Recargas.companniaRecarga = "Iusacell";
                    paraRB = montosAtt.length;
                    crearRGConCantidades(montosAtt);
                    break;
                case 1:
                    Recargas.companniaRecarga = "Movistar";
                    paraRB = montosMovistar.length;
                    crearRGConCantidades(montosMovistar);
                    break;
                case 2:
                    Recargas.companniaRecarga = "Nextel";
                    paraRB = montosNextel.length;
                    crearRGConCantidades(montosNextel);
                    break;
                case 3:
                    Recargas.companniaRecarga = "Telcel";
                    paraRB = montosTelcel.length;
                    crearRGConCantidades(montosTelcel);
                    break;
                case 4:
                    Recargas.companniaRecarga = "Unefon";
                    paraRB = montosUnefon.length;
                    crearRGConCantidades(montosUnefon);
                    break;
            }
        } else if(trabajando.equals(context.getResources().getString(R.string.paquetes_datos))){
            switch (pos){
                case 0:
                    Recargas.companniaRecarga = "INTERNET AMIGO";
                    paraRB = montosTelcel.length;
                    crearRGConCantidades(montosTelcel);
                    break;
                case 1:
                    Recargas.companniaRecarga = "AMIGO SIN LIMITE";
                    paraRB = montosTelcel.length;
                    crearRGConCantidades(montosTelcel);
                    break;

            }
        }


    }

    public void crearRGConCantidades(String[] cuantos){
        int iGlogal;
        boolean impar = false;
        //paraRB = montosTelcel.length;

        if(paraRB %2 == 0){
            paraRB = paraRB / 2;
        } else {
            paraRB +=1;
            paraRB = paraRB / 2;
            impar = true;
        }

        final RadioGroup rg_1_50 = new RadioGroup(context);
        final RadioGroup rg_51_100 = new RadioGroup(context);//create the RadioGroup
        final RadioButton[] rbI = new RadioButton[paraRB];
        final RadioButton[] rbII = new RadioButton[paraRB];


        rg_1_50.setOrientation(RadioGroup.VERTICAL);
        rg_51_100.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL

        for(iGlogal=0; iGlogal < paraRB; iGlogal++){
            rbI[iGlogal]  = new RadioButton(context);
            rg_1_50.addView(rbI[iGlogal]); //the RadioButtons are added to the radioGroup instead of the layout
            rbI[iGlogal].setText("$ "+ cuantos[iGlogal]);
            rbI[iGlogal].setId(iGlogal);
            final int finalI = iGlogal;
            rbI[iGlogal].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Recargas.cantidadRecarga =rbI[finalI].getText().toString();
                    rg_51_100.clearCheck();
                    Recargas.btnPaso2Recarga.setEnabled(true);
                    Recargas.btnPaso2Recarga.setBackgroundColor(context.getResources().getColor(R.color.verde_yastas));
                    //btnEnviarRecarga.setVisibility(View.VISIBLE);
                    contadorRb = v.getId();
                }
            });
        }

        if(impar){
            paraRB -=1;
        }
        for(int j=0; j < paraRB; j++){
            rbII[j]  = new RadioButton(context);
            rg_51_100.addView(rbII[j]); //the RadioButtons are added to the radioGroup instead of the layout
            rbII[j].setText("$ "+ cuantos[iGlogal]);
            iGlogal++;
            rbII[j].setId(j);
            final int finalII = j;
            rbII[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Recargas.cantidadRecarga =rbII[finalII].getText().toString();
                    rg_1_50.clearCheck();
                    Recargas.btnPaso2Recarga.setEnabled(true);
                    Recargas.btnPaso2Recarga.setBackgroundColor(context.getResources().getColor(R.color.verde_yastas));
                    //btnEnviarRecarga.setVisibility(View.VISIBLE);
                    contadorRb = v.getId();
                }
            });

        }
        //rlParaMeterCantidadesEnRbPrimerMitad.setHorizontalGravity(View.TEXT_ALIGNMENT_CENTER);
        Recargas.rlParaMeterCantidadesEnRbPrimerMitad.addView(rg_1_50);//you add the whole RadioGroup to the layout
        //rlParaMeterCantidadesEnRbSegundaMitad.setHorizontalGravity(View.TEXT_ALIGNMENT_CENTER);
        Recargas.rlParaMeterCantidadesEnRbSegundaMitad.addView(rg_51_100);//you add the whole RadioGroup to the layout
        //llParaMeterCantidadesEnRb.addView(submit);
    }

}