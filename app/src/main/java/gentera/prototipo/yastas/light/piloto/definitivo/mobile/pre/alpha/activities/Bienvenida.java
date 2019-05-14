package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.SliderBienvenidaAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.dbs.DataBaseInfoServicios;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Bienvenida extends AppCompatActivity {
    private ViewPager vpBienvenida;
    private LinearLayout llBolitasBienvenida;
    private Button btnAnteriorBienvenida, btnSiguienteBienvenida;
    private SliderBienvenidaAdapter sliderBienvenidaAdapter;
    private TextView[] mDots;
    private int paginaActual;
    private DataBaseInfoServicios dataBaseInfoServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_bienvenida);

        iniGuiBienvenida();
        iniListenersBienvenida();
    }

    public void iniGuiBienvenida(){
        dataBaseInfoServicios = new DataBaseInfoServicios(getApplicationContext());

        vpBienvenida = (ViewPager)findViewById(R.id.vp_bienvenida);
        llBolitasBienvenida = (LinearLayout)findViewById(R.id.ll_bolitas_bienvenida);
        btnAnteriorBienvenida = (Button)findViewById(R.id.btn_anterior_bienvenida);
        btnSiguienteBienvenida = (Button)findViewById(R.id.btn_siguiente_bienvenida);
        sliderBienvenidaAdapter = new SliderBienvenidaAdapter(this, getSupportFragmentManager());
        btnSiguienteBienvenida.setVisibility(View.VISIBLE);
        btnSiguienteBienvenida.setText(getResources().getString(R.string.siguiente));

        Calligrapher calligrapherRL = new Calligrapher(getApplicationContext());
        calligrapherRL.setFont(this, "fonts/AmorSansPro.otf", false);

        Typeface amorBold  = Typeface.createFromAsset(getAssets(), "fonts/AmorSansProBold.otf");
        btnSiguienteBienvenida.setTypeface(amorBold);
        btnAnteriorBienvenida.setTypeface(amorBold);
    }

    public void iniListenersBienvenida(){
        //insertServicios();

        vpBienvenida.setAdapter(sliderBienvenidaAdapter);
        addDots(0);
        vpBienvenida.addOnPageChangeListener(viewListener);

        btnSiguienteBienvenida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paginaActual == mDots.length -1){

                } else {
                    vpBienvenida.setCurrentItem(paginaActual +1);

                }
            }
        });

        btnAnteriorBienvenida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpBienvenida.setCurrentItem(paginaActual -1);
            }
        });

    }

    public void addDots(int position){
        mDots = new TextView[5];
        llBolitasBienvenida.removeAllViews();
        for(int i=0; i<mDots.length; i++){
            mDots[i] =new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(45);
            mDots[i].setTextColor(getResources().getColor(R.color.morado_yastas));
            llBolitasBienvenida.addView(mDots[i]);
        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.verde_yastas));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            paginaActual = position;

            if(position == 0){
                btnAnteriorBienvenida.setEnabled(false);
                btnAnteriorBienvenida.setVisibility(View.INVISIBLE);
                btnSiguienteBienvenida.setEnabled(true);
                btnSiguienteBienvenida.setVisibility(View.VISIBLE);
                btnSiguienteBienvenida.setText(getResources().getString(R.string.siguiente));
            } else if(position == mDots.length -1){
                btnAnteriorBienvenida.setEnabled(true);
                btnAnteriorBienvenida.setVisibility(View.VISIBLE);
                btnAnteriorBienvenida.setText(getResources().getString(R.string.anterior));
                btnSiguienteBienvenida.setEnabled(false);
                btnSiguienteBienvenida.setVisibility(View.INVISIBLE);
            } else {
                btnAnteriorBienvenida.setEnabled(true);
                btnAnteriorBienvenida.setVisibility(View.VISIBLE);
                btnAnteriorBienvenida.setText(getResources().getString(R.string.anterior));
                btnSiguienteBienvenida.setEnabled(true);
                btnSiguienteBienvenida.setVisibility(View.VISIBLE);
                btnSiguienteBienvenida.setText(getResources().getString(R.string.siguiente));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void insertServicios(){
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Telcel 20", "8469766750482", "201", "20",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Telcel 30", "8469760100306", "201", "20",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Telcel 50", "8469760100504", "201", "20",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Telcel 100", "8469760101006", "201", "20",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Telcel 150", "8469760101509", "201", "20",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Telcel 200", "8469760102003", "201", "20",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Telcel 300", "8469760103000", "201", "20",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Telcel 500", "8469760105004", "201", "20",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Movistar 20", "7779860100207", "202", "21",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Dudas tiempo aire *611 o 01800 8888366");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Movistar 30", "7779860100306", "202", "21",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Dudas tiempo aire *611 o 01800 8888366");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Movistar 50", "7779860100504", "202", "21",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Dudas tiempo aire *611 o 01800 8888366");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Movistar 60", "7779860100607", "202", "21",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Dudas tiempo aire *611 o 01800 8888366");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Movistar 100", "7779860101006", "202", "21",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Dudas tiempo aire *611 o 01800 8888366");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Movistar 120", "7779860101205", "202", "21",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Dudas tiempo aire *611 o 01800 8888366");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Movistar 150", "7779860101501", "202", "21",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Dudas tiempo aire *611 o 01800 8888366");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Movistar 200", "7779860102003", "202", "21",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Dudas tiempo aire *611 o 01800 8888366");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Movistar 300", "7779860103000", "202", "21",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Dudas tiempo aire *611 o 01800 8888366");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Movistar 500", "7779860105004", "202", "21",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Dudas tiempo aire *611 o 01800 8888366");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Iusacell 30","7385830100306", "203", "22",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Envía un SMS con la palabra: saldo al 1111 o marca *611 desde tu celular");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Iusacell 50", "7385830100504", "203", "22",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f,"Envía un SMS con la palabra: saldo al 1111 o marca *611 desde tu celular");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Iusacell 100", "7385830101008", "203", "22",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f,"Envía un SMS con la palabra: saldo al 1111 o marca *611 desde tu celular");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Iusacell 200", "7385830102005", "203", "22",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f,"Envía un SMS con la palabra: saldo al 1111 o marca *611 desde tu celular");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Iusacell 300", "7385830103002", "203", "22",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f,"Envía un SMS con la palabra: saldo al 1111 o marca *611 desde tu celular");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Iusacell 500","7385830105006", "203", "22",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f,"Envía un SMS con la palabra: saldo al 1111 o marca *611 desde tu celular");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Unefon 30", "8578690100307", "205", "23",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Marca desde tu celular *611 o envía SMS al 1111 con la palabra saldo desde tu unefon");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Unefon 50", "8578690100505", "205", "23",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Marca desde tu celular *611 o envía SMS al 1111 con la palabra saldo desde tu unefon");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Unefon 150", "8578690101502", "205", "23",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Marca desde tu celular *611 o envía SMS al 1111 con la palabra saldo desde tu unefon");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Unefon 200","8578690102004", "205", "23",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Marca desde tu celular *611 o envía SMS al 1111 con la palabra saldo desde tu unefon");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Unefon 300", "8578690103001", "205", "23",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Marca desde tu celular *611 o envía SMS al 1111 con la palabra saldo desde tu unefon");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Unefon 500", "8578690105005", "205", "23",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Marca desde tu celular *611 o envía SMS al 1111 con la palabra saldo desde tu unefon");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Cfe", "1841", "200", "25", 0, 1, 10, 100000, 1, 1, "Cuenta",0, 30, 30,5.60f, 0.90f, 6.50f, "EL PAGO SE APLICARA AL DIA SIGUIENTE HABIL PARA CUALQUIER ACLARACION FAVOR DE COMUNICARSE A CFE AL 071");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Telmex", "12520", "207", "26", 1, 1, 10, 100000, 1, 1,"Numero de telefono", 0,20, 20, 6.03f, 0.97f, 7.00f, "EL PAGO SE APLICARA EL DIA SIGUIENTE HABIL PARA CUALQUIER DUDA O ACLARACION FAVOR DE COMUNICARSE A TELMEX AL 018001230000");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Nextel 30", "7503004884001", "204", "27",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Para consultar tu saldo marca 40 o *611");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Nextel 50", "7503004884018", "204", "27",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Para consultar tu saldo marca 40 o *611");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Nextel 100", "7503004884025", "204", "27",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Para consultar tu saldo marca 40 o *611");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Nextel 200", "7503004884032", "204", "27",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Para consultar tu saldo marca 40 o *611");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Nextel 500", "7503004884049", "204", "27",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Para consultar tu saldo marca 40 o *611");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Mega cable", "41", "208", "28", 1, 1, 1, 28500, 1, 1, "Cuenta", 0, 26, 26, 8.62f, 1.38f, 10.00f, "EL PAGO SE APLICA EL MISMO DIA DE LA OPERACION. PARA CUALQUIER ACLARACION FAVOR DE COMUNICARSE A SERVICIO A CLIENTES DE MEGACABLE DE SU LOCALIDAD");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Sky", "40", "210", "30", 1, 1, 10, 100000, 0, 1, "Cuenta", 0, 12, 12, 6.03f, 0.97f, 7.00f, "EL PAGO SE APLICA EL MISMO DIA DE LA OPERACION PARA CUALQUIER ACLARACION FAVOR DE COMUNICARSE A SKY AL 5169-0000 Y 01800-4759-759");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Vetv", "91", "211", "31", 1, 1, 10, 100000, 0, 1, "Cuenta", 0, 12, 12,6.03f, 0.97f, 7.00f, "EL PAGO SE APLICA EL MISMO DIA DE LA OPERACION PARA CUALQUIER ACLARACION FAVOR DE COMUNICARSE A VETV AL 5169-0000 Y 01800-4759-759");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Avon", "0", "213", "33", 0, 1, 1, 9999, 0, 1, "Referencia",0, 20, 20, 0.00f, 0.00f, 0.00f, "EL PAGO SE APLICA AL SIGUIENTE DIA HABIL PARA CUALQUIER ACLARACION LLAMA A LINEA DE COSMETICOS SIN COSTO AL 018000064000");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Dish", "38", "218", "38", 1, 1, 1, 100000, 1, 1, "Numero de cliente", 0, 14, 14,7.33f, 1.17f, 8.50f, "EL PAGO SE APLICA EL MISMO DIA DE LA OPERACION PARA CUALQUIER ACLARACION FAVOR DE COMUNICARSE A DISH EN EL DF AL 96283450 O AL 018009001111 SIN COSTO");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Arabela", "0", "219", "39", 1, 0, 1, 1100, 0, 1, "Cuenta", 0, 8, 8, 0.00f, 0.00f, 0.00f, "EL PAGO SE APLICARA AL SIGUIENTE DIA HABIL");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "Izzi", "6900", "224", "47", 1, 1, 1, 100000, 1, 1,"Referencia y Numero de telefono", 1, 8, 10, 0.00f, 0.00f, 0.00f, "EL PAGO SE APLICA EL MISMO DIA DE LA OPERACION. PARA CUALQUIER ACLARACION FAVOR DE COMUNICARSE A IZZI AL 01-800-120-5000");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "INTERNET AMIGO 20", "7378840100202", "227", "54",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "INTERNET AMIGO 30", "7378840100309", "227", "54",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "INTERNET AMIGO 50", "7378840100503", "227", "54",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "INTERNET AMIGO 100", "7378840101007", "227", "54",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "INTERNET AMIGO 150", "7378840101502", "227", "54",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "INTERNET AMIGO 200", "7378840102006", "227", "54",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "INTERNET AMIGO 300", "7378840103005", "227", "54",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "INTERNET AMIGO 500", "7378840105003", "227", "54",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "AMIGO SIN LIMITE 20", "6583760100206", "228", "55",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "AMIGO SIN LIMITE 30", "6583760100309", "228", "55",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "AMIGO SIN LIMITE 50", "6583760100507", "228", "55",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "AMIGO SIN LIMITE 100", "6583760101001", "228", "55",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "AMIGO SIN LIMITE 150", "6583760101506", "228", "55",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "AMIGO SIN LIMITE 200", "6583760102000", "228", "55",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "AMIGO SIN LIMITE 300", "6583760103009", "228", "55",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
        dataBaseInfoServicios.insertar_nuevo_servicio(null, "AMIGO SIN LIMITE 500", "6583760105007", "228", "55",0,0,0,100000, 0,1, "Numero celular", 0, 10, 10, 0.0f, 0.0f, 0.0f, "Llamanos sin costo desde tu Amigo Telcel al *264 o marca *111 si tienes plan de renta");
    }
}
