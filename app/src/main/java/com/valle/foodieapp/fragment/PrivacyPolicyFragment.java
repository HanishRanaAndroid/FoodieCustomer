package com.valle.foodieapp.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.valle.foodieapp.R;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class PrivacyPolicyFragment extends Fragment {
    private WebView webViewForPWA;
    private CircleProgressBar circleProgressBar;
    private String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/72.0.3626.121 Mobile Safari/535.19";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_term_and_conditon, container, false);
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

        webViewForPWA.loadUrl("https://vallefood.co/client");

    }
}
