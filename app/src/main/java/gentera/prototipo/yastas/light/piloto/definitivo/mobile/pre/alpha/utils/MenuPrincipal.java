package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gentera.sdk.GenteraSDK;
import com.gentera.sdk.helpers.commons.TransactionError;
import com.gentera.sdk.modules.messagecenter.android_module.ui.surveys.SurveysActivity;
import com.gentera.sdk.modules.messagecenter.android_module.util.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.MainActivity;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.Splash;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.SlidingMenuMainAdapter;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.ItemSlideMainMenu;

public class MenuPrincipal extends AppCompatActivity {

    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private List<ItemSlideMainMenu> listSliding;
    private SlidingMenuMainAdapter adapter;
    private Toolbar toolbarParaLogo;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Context context;
    private String TAG;
    private String nombreOperador;


    public Toolbar crearMenu(final Activity activity, final Context context, String nombreOperador) {
        listViewSliding = (ListView) activity.findViewById(R.id.lv_sliding_main_menu);
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout_main);
        listSliding = new ArrayList<>();
        this.context = context;
        this.nombreOperador = nombreOperador;
        View view;

        //Add item for sliding list
        //listSliding.add(new ItemSlideMainMenu(R.mipmap.ic_launcher_round, context.getResources().getString(R.string.inicio)));
        //listSliding.add(new ItemSlideMainMenu(R.drawable.ic_perfil, context.getResources().getString(R.string.mi_perfil)));
        //listSliding.add(new ItemSlideMainMenu(R.drawable.ic_mensajes, context.getResources().getString(R.string.mensajes)));
        listSliding.add(new ItemSlideMainMenu(R.drawable.ic_surveys, "Sruveyes"));
        listSliding.add(new ItemSlideMainMenu(R.drawable.ic_share, context.getResources().getString(R.string.compartir_app)));
        listSliding.add(new ItemSlideMainMenu(R.drawable.ic_cerrar_sesion, context.getResources().getString(R.string.cerrar_sesion)));
        adapter = new SlidingMenuMainAdapter(activity, listSliding);
        listViewSliding.setAdapter(adapter);

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        GenteraSDK.initialize("admin", "admin", context);
                        try {
                            GenteraSDK.getInstance().MessageCenter(nombreOperador).syncCertifications(new Transaction() {
                                @Override
                                public <T> void onTransactionSuccess(T t) {
                                    Log.i(TAG, "onTransactionSuccess: " + t.toString());
                                    context.startActivity(new Intent(context, SurveysActivity.class));
                                }

                                @Override
                                public void onTransactionFailed(TransactionError transactionError) {
                                    Log.d(TAG, "onTransactionFailed: ");
                                }
                            });
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        Intent intentCompartirApp = new Intent(Intent.ACTION_SEND);
                        intentCompartirApp.setType("text/plain");
                        intentCompartirApp.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
                        intentCompartirApp.putExtra(Intent.EXTRA_TEXT, "Prueba nuestra nueva app:\n"+ context.getResources().getString(R.string.app_name ) + "\n" + context.getResources().getString(R.string.link_play_store));
                        activity.startActivity(Intent.createChooser(intentCompartirApp, "Share Via"));

                    break;
                    case 2:
                        borrarSesion();
                        ((Activity)context).finish();
                        activity.startActivity(new Intent(activity, Splash.class));
                    break;
                }
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbarParaLogo, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                activity.invalidateOptionsMenu();
                syncState();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        return toolbarParaLogo;

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (drawerLayout.isDrawerOpen(listViewSliding)) {
                    drawerLayout.closeDrawer(listViewSliding);
                } else {
                    drawerLayout.openDrawer(listViewSliding);
                }
                return true;
            }
            /*case  {
                startActivity(new Intent(getApplicationContext(), Login.class));
                return true;
            }*/
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void borrarSesion(){
        SharedPreferences preferences = context.getSharedPreferences("prefsPagosYastas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("nombreUsuarioSession");
        editor.remove("numeroDeUsuarioSession");
        editor.remove("rollSession");
        editor.remove("yaConTokenSession");
        editor.remove("enSession");
        editor.remove("storeId");
        editor.remove("enLaBolsa");
        editor.remove("paraQueNoExploten");

        editor.apply();
    }


}
