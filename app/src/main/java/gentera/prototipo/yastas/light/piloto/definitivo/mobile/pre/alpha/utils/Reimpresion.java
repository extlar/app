package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Reportes;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.fragments.BuscarDispositivos;

public class Reimpresion extends AppCompatActivity {
    private static BluetoothAdapter paraPrenderBluetooth = BluetoothAdapter.getDefaultAdapter();
    /******************************************************************************************************/
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CHOSE_BMP = 3;
    private static final int REQUEST_CAMER = 4;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mService = null;
    private static final boolean DEBUG = true;
    private static final String TAG = "maldito bluetooth";
    private String mConnectedDeviceName = null;
    private PrintPicture ponElLogo;
    private String fechaRecibo, comercioRecibo, folioAvonRecibo, referenciaRecibo, montoRecibo;
    private Context _context;
    /*******************************************************************************************************/

    public Reimpresion(Context context) {
        this._context = context;
    }

    public void verficarConexion(){
        if(paraPrenderBluetooth.isEnabled()){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            // If the adapter is null, then Bluetooth is not supported
            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth is not available",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            mService = new BluetoothService(this, mHandler);

            if(_context instanceof Reportes) {
                Toast.makeText(_context, "a huevo", Toast.LENGTH_SHORT).show();
                Intent reimpresionIntent= new Intent(_context, BuscarDispositivos.class);
                startActivityForResult(reimpresionIntent, REQUEST_CONNECT_DEVICE);
                //startActivityForResult(paraContexto.intentReimpresion(), REQUEST_CONNECT_DEVICE);
            } else {
                Toast.makeText(_context, "valio madres", Toast.LENGTH_SHORT).show();
            }

        } else {
            AlertDialog.Builder builderBluetooth = new AlertDialog.Builder(_context);
            builderBluetooth.setTitle(_context.getResources().getString(R.string.encender_bluetooth));
            builderBluetooth.setMessage(_context.getResources().getString(R.string.expliacion_bluetooth));
            builderBluetooth.setIcon(_context.getResources().getDrawable(R.drawable.ic_bluetooth));
            builderBluetooth.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    paraPrenderBluetooth.enable();
                }
            });
            builderBluetooth.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE: {
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            BuscarDispositivos.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    if (BluetoothAdapter.checkBluetoothAddress(address)) {
                        BluetoothDevice device = mBluetoothAdapter
                                .getRemoteDevice(address);
                        // Attempt to connect to the device
                        mService.connect(device);
                    }
                }
                break;
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (DEBUG)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            //Print_Recibo_Avon();//

                            break;
                        case BluetoothService.STATE_CONNECTING:

                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:

                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
