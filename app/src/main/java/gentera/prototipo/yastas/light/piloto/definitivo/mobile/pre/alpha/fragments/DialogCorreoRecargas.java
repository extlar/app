package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.BuildConfig;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.ProcesoDePago;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Recargas;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils.MailSender;

public class DialogCorreoRecargas extends DialogFragment {
    private TextView tvTituloDialog, tvCuerpoDialog;
    private Button btnEnviaCorreo, btnCancelaCorreo;
    public Context context;
    private EditText etCorreoEnvio;
    private static final String ARG_PARAM0 = "param0";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String remitenteList, detalle;

    private String mParam0;
    private String mParam1;
    private String mParam2;

    public DialogCorreoRecargas() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_ingresa_correo, container);
        this.context = view.getContext();

        tvTituloDialog = (TextView) view.findViewById(R.id.tv_encabezado_en_dialogo);
        tvCuerpoDialog = (TextView) view.findViewById(R.id.tv_cuerpo_en_dialogo);
        etCorreoEnvio = (EditText) view.findViewById(R.id.et_correo_comprobante_cosmeticos);
        btnEnviaCorreo = (Button) view.findViewById(R.id.btn_envia_correo);
        btnCancelaCorreo = (Button) view.findViewById(R.id.btn_cancela_correo);

        tvTituloDialog.setText(getResources().getString(R.string.enviar_comprobante));
        tvCuerpoDialog.setText(getResources().getString(R.string.ingresa_correo));

        Typeface amorType = Typeface.createFromAsset(context.getAssets(), "fonts/AmorSansProBold.otf");
        tvTituloDialog.setTypeface(amorType);
        tvCuerpoDialog.setTypeface(amorType);
        btnEnviaCorreo.setTypeface(amorType);
        btnCancelaCorreo.setTypeface(amorType);

        btnCancelaCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnEnviaCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCorreoEnvio.length() > 0){
                    detalle = Recargas.choroGlobalRecargas;
                    if(Recargas.mailEnviadoRecargas){
                        detalle += "\n\nCOPIA";
                    }

                    remitenteList = etCorreoEnvio.getText().toString();
                    new SendEmailAsyncTask().execute();

                /*String titulo;
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("message/rfc822");
                titulo = "Comprobante de pago Yastás";
                String[] remitentes = remitenteList.split(",");
                intentShare.putExtra(Intent.EXTRA_SUBJECT, titulo);
                intentShare.putExtra(Intent.EXTRA_EMAIL, remitentes);
                intentShare.putExtra(Intent.EXTRA_TEXT, detalle);
                startActivity(Intent.createChooser(intentShare, "Enviar comprobante"));*/
                    Toast.makeText(context, "Email enviado", Toast.LENGTH_SHORT);
                    Recargas.btnTerminaRecarga.setBackgroundColor(getResources().getColor(R.color.verde_yastas));
                Recargas.btnTerminaRecarga.setEnabled(true);
                Recargas.mailEnviadoRecargas = true;
                dismiss();

                } else {
                    Toast.makeText(context, "Introduce al menos una dirección de correo", Toast.LENGTH_SHORT);
                }

            }
        });
        return view;

    }

/*
    public void verificarBlueTooth() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                    PERMISO_BLUETOOTH);
            paraPrenderBluetooth.enable();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_bluetooth);
            builder.setTitle(context.getResources().getString(R.string.encender_bluetooth));
            builder.setMessage(context.getResources().getString(R.string.expliacion_bluetooth));
            builder.setPositiveButton(context.getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    paraPrenderBluetooth.enable();

                    Toast.makeText(context, "buscando conexión con impresora", Toast.LENGTH_SHORT).show();

                }
            });
            builder.show();
        }
    }*/

    public static DialogCorreoRecargas newInstance(String param0, String param1, String param2) {
        DialogCorreoRecargas fragment = new DialogCorreoRecargas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM0, param0);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam0 = getArguments().getString(ARG_PARAM0);
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        //MailSender sender = new MailSender("apiadmin@compartamos.com", "Gentera1458");
        //MailSender sender = new MailSender("aclaraciones@yastas.com", "Soporte22#");
        //MailSender sender = new MailSender("laura.leon@yastas.com", "Soporte31#", context);
        MailSender sender = new MailSender("informacion@yastas.com", "Seguridad2019#", context);


        public SendEmailAsyncTask() {
            if (BuildConfig.DEBUG)
                Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
            try {
                sender.sendMail(remitenteList, detalle);

                //sender.sendMail("aclaraciones@yastas.com" + ", " + remitenteList,detalle);
                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
                return false;
            } catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "failed");
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}