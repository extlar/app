package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.ProcesoDePago;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.ReporteTransaccionesAdapter;

public class DialogReSms extends DialogFragment {
    private String ENVIO_SMS_URL = "v1/yastas/mc//send-smart-notification";
    private TextView tvTituloDialog, tvCuerpoDialog;
    private Button btnAceptarCorreo, btnCerrarSms;
    public Context context;
    private EditText etsmsConfirmacion;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int MI_PERMISO_SMS = 3;
    private JsonObjectRequest jsonObjectRequestSMS;
    private RequestQueue smsQueque;

    private String mParam1;
    private String mParam2;

    public DialogReSms() {


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
                String celularParaAbono = etsmsConfirmacion.getText().toString();
                if(celularParaAbono.length() == 10){
                    String sms = ReporteTransaccionesAdapter.reenvioInfo;
                    enviarSms(celularParaAbono, sms, getToken());


                    dismiss();
                } else {
                    Toast.makeText(context, "El número debe ser de diez digitos", Toast.LENGTH_SHORT).show();
                }


                /*
                if(ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED  &&
                        (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
                    String celularParaAbono = etsmsConfirmacion.getText().toString();
                    if(celularParaAbono.length() == 10){
                        SmsManager smsManager = SmsManager.getDefault();
                        String sms = ReporteTransaccionesAdapter.reenvioInfo;
                        smsManager.sendTextMessage(celularParaAbono, null, sms, null, null);
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

    public static DialogReSms newInstance(String param1, String param2) {
        DialogReSms fragment = new DialogReSms();
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

    public void enviarSms(String numeroDestinatario, String detalle, final String token){
        smsQueque = Volley.newRequestQueue(context);

        String stringUrlSMS = getResources().getString(R.string.base_api_lab) + ENVIO_SMS_URL;
        JSONObject postParams = new JSONObject();
        try {
            JSONObject sender = new JSONObject();
            JSONObject notificationContent = new JSONObject();
            sender.put("senderId", "YASTAS_APP");
            sender.put("description", "servicio sms");
            notificationContent.put("sender" ,sender);

            JSONObject receiver = new JSONObject();
            receiver.put("applicationId", 1);
            receiver.put("cellphone", numeroDestinatario);
            notificationContent.put("receiver", receiver);

            JSONObject message = new JSONObject();
            message.put("subject", "prueba");
            message.put("text", detalle);
            notificationContent.put("message", message);

            postParams.put("notificationContent", notificationContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("enviar sms",postParams.toString());
        jsonObjectRequestSMS = new JsonObjectRequest(Request.Method.POST, stringUrlSMS, postParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "SMS enviado", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                Log.i("token", token);
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer "+ token);
                return headers;
            }
        }
        ;
        jsonObjectRequestSMS.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        smsQueque.add(jsonObjectRequestSMS);

    }

    public String getToken() {
        SharedPreferences sacaToken = context.getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
            String token = sacaToken.getString("yaConTokenSession", null);

            return  token;
    }

}