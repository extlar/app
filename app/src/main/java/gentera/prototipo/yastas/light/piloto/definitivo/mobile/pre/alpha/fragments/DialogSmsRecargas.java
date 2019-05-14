package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.ProcesoDePago;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Recargas;

public class DialogSmsRecargas extends DialogFragment {
    private TextView tvTituloDialog, tvCuerpoDialog;
    private Button btnAceptarCorreo, btnCerrarSms;
    public Context context;
    private EditText etsmsConfirmacion;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int MI_PERMISO_SMS = 3;

    private String mParam1;
    private String mParam2;

    public DialogSmsRecargas() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_sms, container);
        this.context = view.getContext();

        tvTituloDialog = (TextView) view.findViewById(R.id.tv_encabezado_en_dialogo);
        tvCuerpoDialog = (TextView) view.findViewById(R.id.tv_cuerpo_en_dialogo);
        etsmsConfirmacion = (EditText) view.findViewById(R.id.et_confirmacion_via_sms);
        btnAceptarCorreo = (Button) view.findViewById(R.id.btn_dialog_sms);
        btnCerrarSms = (Button) view.findViewById(R.id.btn_cancela_sms);

        tvTituloDialog.setText(getResources().getString(R.string.enviar_comprobante));
        tvCuerpoDialog.setText(getResources().getString(R.string.ingresa_phone));

        Typeface amorType = Typeface.createFromAsset(context.getAssets(), "fonts/AmorSansProBold.otf");
        tvTituloDialog.setTypeface(amorType);
        tvCuerpoDialog.setTypeface(amorType);
        btnAceptarCorreo.setTypeface(amorType);
        btnCerrarSms.setTypeface(amorType);

        btnCerrarSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAceptarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Servicio temporalmente no disponible", Toast.LENGTH_LONG).show();
                /*
                if(ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED  &&
                        (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
                    String celularParaAbono = etsmsConfirmacion.getText().toString();
                    if(celularParaAbono.length() == 10){
                        SmsManager smsManager = SmsManager.getDefault();
                        String sms = Recargas.choroSms;
                        smsManager.sendTextMessage(celularParaAbono, null, sms, null, null);
                        Recargas.btnTerminaRecarga.setBackgroundColor(getResources().getColor(R.color.verde_yastas));
                        Recargas.btnTerminaRecarga.setEnabled(true);
                        dismiss();
                    } else {
                        Toast.makeText(context, "El número debe ser de diez digitos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ic_mensajes);
                    builder.setTitle(getResources().getString(R.string.permiso_mensajes));
                    builder.setMessage(getResources().getString(R.string.expliacion_permiso_mensajes));
                    builder.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                                    MI_PERMISO_SMS);

                        }
                    });
                    builder.show();
                }
*/
            }
        });
        return view;

    }

    public static DialogSmsRecargas newInstance(String param1, String param2) {
        DialogSmsRecargas fragment = new DialogSmsRecargas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

}