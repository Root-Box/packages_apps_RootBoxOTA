/*
 * Copyright (C) 2013 ParanoidAndroid Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use mContext file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rootbox.rootboxota.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rootbox.rootboxota.R;

public class ChangelogFragment extends Fragment {

    private static final String CHANGELOG_URL
            = "https://plus.google.com/app/basic/107979589566958860409/posts";
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_changelog,
                container, false);
        WebView webView = ((WebView) rootView.findViewById(R.id.changelog));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progress_bar_loop);
                mProgressBar.setIndeterminate(true);
                mProgressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Activity act = getActivity();
                if (act == null) {
                    return;
                }
                mProgressBar = (ProgressBar) act.findViewById(R.id.progress_bar_loop);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progress_bar_loop);
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), R.string.changelog_error, Toast.LENGTH_SHORT).show();
            }
        });
        if (savedInstanceState == null) {
            webView.loadUrl(CHANGELOG_URL);
        }
        return rootView;
    }

}