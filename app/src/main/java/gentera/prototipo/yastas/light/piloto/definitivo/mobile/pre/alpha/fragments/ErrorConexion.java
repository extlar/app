package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorConexion extends DialogFragment {
    private Button btnCerrar;


    public ErrorConexion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_error_conexion, container, false);
        btnCerrar = (Button)view.findViewById(R.id.btn_ok_dialogo);

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;

    }

}
