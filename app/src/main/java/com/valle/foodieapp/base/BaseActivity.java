package com.valle.foodieapp.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.valle.foodieapp.R;
import com.valle.foodieapp.network.APIClient;
import com.valle.foodieapp.network.APIInterface;
import com.valle.foodieapp.network.NetworkManager;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseActivity extends AppCompatActivity {
    public ProgressDialog progressDialog;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void bindView(Activity activity) {
        unbinder = ButterKnife.bind(activity);
    }


    public void unBindView() {
        unbinder.unbind();
    }

    public void showProgressDialog(Context activity) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("por favor espera...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void hideProgressDialog(Context context) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void makeHttpCall(NetworkResponceListener context, String url, Object o) {
        new NetworkManager(context).getNetworkResponce(url, o);
    }

    public APIInterface getRetrofitInterface() {
        return APIClient.getClient().create(APIInterface.class);
    }

    public void showSnakBar(View viewLayout) {
        Snackbar snackbar = Snackbar
                .make(viewLayout, getResources().getString(R.string.internet_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.restry), view -> {
                    if (!CommonUtils.isNetworkAvailable(BaseActivity.this)) {
                        showSnakBar(viewLayout);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void exitFromApp(Activity activity) {
        new AlertDialog.Builder(activity).setTitle(getString(R.string.alert)).setMessage(getString(R.string.r_u_sure_u_want_to_exit)).setPositiveButton(getString(R.string.yes), (dialog, which) -> {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();
            dialog.dismiss();
        }).setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss()).create().show();
    }
}
