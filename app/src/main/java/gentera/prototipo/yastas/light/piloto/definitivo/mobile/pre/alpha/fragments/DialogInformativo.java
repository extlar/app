package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;

public class DialogInformativo extends DialogFragment {
    private Context context;
    private TextView tvEncabezadoDialog, tvCuerpoDialog;
    private Button btnOkDialog;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DialogInformativo() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogInformativo.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogInformativo newInstance(String param1, String param2) {
        DialogInformativo fragment = new DialogInformativo();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_informativo, container, false);
        this.context = view.getContext();
        tvEncabezadoDialog = (TextView)view.findViewById(R.id.tv_encabezado_en_dialogo);
        tvCuerpoDialog = (TextView)view.findViewById(R.id.tv_cuerpo_en_dialogo);
        btnOkDialog = (Button)view.findViewById(R.id.btn_ok_dialogo);
        tvEncabezadoDialog.setText(mParam1);
        tvCuerpoDialog.setText(mParam2);

        Typeface amorSansProBold  = Typeface.createFromAsset(context.getAssets(), "fonts/AmorSansProBold.otf");
        Typeface amorSansPro  = Typeface.createFromAsset(context.getAssets(), "fonts/AmorSansPro.otf");
        tvEncabezadoDialog.setTypeface(amorSansProBold);
        btnOkDialog.setTypeface(amorSansProBold);
        tvCuerpoDialog.setTypeface(amorSansPro);

        btnOkDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
