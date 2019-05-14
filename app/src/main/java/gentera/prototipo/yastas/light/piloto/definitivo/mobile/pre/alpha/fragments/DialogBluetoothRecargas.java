package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;

public class DialogBluetoothRecargas extends Activity {
    private Button btnCancelarDispositivos;
    private ListView listVinculados, listEncontrados;
    private ArrayAdapter<String> vinculadosAdapter, encontradosAdapter;

    public DialogBluetoothRecargas() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bluetooth, container, false);

        listVinculados = (ListView)view.findViewById(R.id.pairedDeviceList);
        listEncontrados = (ListView)view.findViewById(R.id.discoveredDeviceList);
        btnCancelarDispositivos = (Button)view.findViewById(R.id.btn_cancelar_dispositivos);

        Object[] pairedObjects = BluetoothAdapter.getDefaultAdapter().getBondedDevices().toArray();
        final BluetoothDevice[] pairedDevices = new BluetoothDevice[pairedObjects.length];
        for (int i = 0; i < pairedObjects.length; ++i){
            pairedDevices[i] = (BluetoothDevice)pairedObjects[i];
        }

        vinculadosAdapter = new ArrayAdapter<String>(DialogBluetoothRecargas.this, android.R.layout.simple_list_item_1);
        for (int i = 0; i < pairedDevices.length; ++i) {
            vinculadosAdapter.add(pairedDevices[i].getName());
        }

        listVinculados.setAdapter(vinculadosAdapter);
        listVinculados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //crear conexion
                finish();
            }
        });

        //Object[] discoveredObjects = BluetoothDevice.CON
        listEncontrados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish();
            }
        });

        btnCancelarDispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        encontradosAdapter = new ArrayAdapter<String>(DialogBluetoothRecargas.this, android.R.layout.simple_list_item_1);
        return view;

    }

}
