package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;

public class TerminosCondicionesyAviso extends DialogFragment {
    private Button btnCerrarTerminosPrivacidad;
    public PDFView pdfViewAvisosYTerminos;
    private String mostrar;

    public TerminosCondicionesyAviso() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewDialog = inflater.inflate(R.layout.dialog_terminos_condiciones_privacidad, container);
        pdfViewAvisosYTerminos = viewDialog.findViewById(R.id.pdf_viewer_terminos_aviso);
        btnCerrarTerminosPrivacidad = viewDialog.findViewById(R.id.btn_cerrar_aviso_terminos);
        mostrar = getArguments().getString("paraelchoro");

        pdfViewAvisosYTerminos.fromAsset("pdfs/" + mostrar + ".pdf").load();
        btnCerrarTerminosPrivacidad.setText(getResources().getString(R.string.cerrar));

        btnCerrarTerminosPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return viewDialog;
    }
}
