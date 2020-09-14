package com.valle.foodieapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.listeners.WompiSuccessListner;
import com.valle.foodieapp.models.GetPaymentLinkModel;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;


public class WompiPaymentCheckoutFragment extends Fragment {

    private GetPaymentLinkModel getPaymentLinkModel;
    private WompiSuccessListner wompiSuccessListner;
    private WebView webViewForPWA;
    private CircleProgressBar circleProgressBar;
    private String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/72.0.3626.121 Mobile Safari/535.19";

    public WompiPaymentCheckoutFragment(GetPaymentLinkModel getPaymentLinkModel, WompiSuccessListner wompiSuccessListner) {
        this.getPaymentLinkModel = getPaymentLinkModel;
        this.wompiSuccessListner = wompiSuccessListner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wompi_payment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);

        webViewForPWA = view.findViewById(R.id.webViewPwa);
        circleProgressBar = view.findViewById(R.id.progressBar);
        circleProgressBar.setShowArrow(true);
        circleProgressBar.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        circleProgressBar.setCircleBackgroundEnabled(true);
        webViewForPWA.getSettings().setLoadsImagesAutomatically(true);
        webViewForPWA.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webViewForPWA.getSettings().setUserAgentString(USER_AGENT);
        webViewForPWA.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {

                if (url.contains("http://weburlforclients.com/gamma/vallafood/api/v1/PaymentCallback/")) {
                    ((HomeTabActivity) getActivity()).removeFragment();
                    wompiSuccessListner.onPaymentSuccess("");

                }

            }
        });

        webViewForPWA.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {

                if (circleProgressBar.getVisibility() == View.GONE) {
                    circleProgressBar.setVisibility(View.VISIBLE);
                }

                if (progress == 100) {
                    circleProgressBar.setVisibility(View.GONE);
                }
            }
        });

        webViewForPWA.loadUrl("https://checkout.wompi.co/l/" + getPaymentLinkModel.data.id);
    }
}
