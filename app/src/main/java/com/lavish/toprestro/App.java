package com.lavish.toprestro;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.ViewGroup;
import android.view.Window;

import com.lavish.toprestro.dialogs.ErrorDialog;
import com.lavish.toprestro.models.Profile;
import com.lavish.toprestro.other.Prefs;

public class App extends Application {

    public Profile profile;
    private Dialog dialog;

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        profile = new Prefs(this).getProfile();
    }

    public void showLoadingDialog(Context context){
        if(dialog != null && dialog.isShowing())
            return;

        dialog = new Dialog(context, R.style.LoaderStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loader);
        dialog.setCancelable(false);

        if(dialog.getWindow() == null)
            return;

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void hideLoadingDialog(){
        if(dialog != null)
            dialog.dismiss();
    }

    public boolean isLoggedIn(){
        return profile != null;
    }

    public boolean isOffline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return !(wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected());
    }

}
