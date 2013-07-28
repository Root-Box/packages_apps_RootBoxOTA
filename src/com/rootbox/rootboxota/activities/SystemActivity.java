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

package com.rootbox.rootboxota.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.rootbox.rootboxota.R;
import com.rootbox.rootboxota.updater.GappsUpdater;
import com.rootbox.rootboxota.updater.RomUpdater;
import com.rootbox.rootboxota.updater.Updater.PackageInfo;
import com.rootbox.rootboxota.updater.Updater.UpdaterListener;

public class SystemActivity extends Activity implements UpdaterListener {

    private RomUpdater mRomUpdater;
    private GappsUpdater mGappsUpdater;

    private PackageInfo mRom;
    private PackageInfo mGapps;

    private TextView mTitle;
    private TextView mMessage;
    private Button mButton, mDownloadInstall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_system);

        mTitle = (TextView) findViewById(R.id.title);
        mMessage = (TextView) findViewById(R.id.message);
        mButton = (Button) findViewById(R.id.button);
        mButton.setVisibility(View.GONE);
        mDownloadInstall = (Button) findViewById(R.id.button_install_download);
        mDownloadInstall.setVisibility(View.GONE);

        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mRomUpdater.check();
                mGappsUpdater.check();
            }

        });

        mDownloadInstall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.rootbox.rootboxota", "com.rootbox.rootboxota.MainActivity"));
                startActivity(intent);
            }

        });

        mRom = null;
        mGapps = null;

        mRomUpdater = new RomUpdater(this, false);
        mRomUpdater.addUpdaterListener(this);
        mGappsUpdater = new GappsUpdater(this, false);
        mGappsUpdater.addUpdaterListener(this);

        mRomUpdater.check();
        mGappsUpdater.check();
    }

    @Override
    public void startChecking(boolean isRom) {
        setMessages(null, isRom);
    }

    @Override
    public void versionFound(PackageInfo[] info, boolean isRom) {
        setMessages(info, isRom);
    }

    private void setMessages(PackageInfo[] info, boolean isRom) {
        if (info != null && info.length > 0) {
            if (isRom) {
                mRom = info != null && info.length > 0 ? info[0] : null;
            } else {
                mGapps = info != null && info.length > 0 ? info[0] : null;
            }
        }
        Resources res = getResources();
        boolean checking = mRomUpdater.isScanning() || mGappsUpdater.isScanning();
        if (checking) {
            mTitle.setText(R.string.all_up_to_date);
            mMessage.setText(R.string.rom_scanning);
            mButton.setVisibility(View.GONE);
        } else {
            mDownloadInstall.setVisibility(View.VISIBLE);
            if (mRom != null && mGapps != null) {
                mTitle.setText(R.string.rom_gapps_new_version);
                mMessage.setText(res.getString(R.string.system_update));
            } else if (mRom != null) {
                mTitle.setText(R.string.rom_new_version);
                mMessage.setText(res.getString(R.string.system_update));
            } else if (mGapps != null) {
                mTitle.setText(R.string.gapps_new_version);
                mMessage.setText(res.getString(R.string.system_update));
            } else {
                mDownloadInstall.setVisibility(View.GONE);
                mButton.setVisibility(View.VISIBLE);
                mTitle.setText(R.string.all_up_to_date);
                mMessage.setText(R.string.no_updates);
            }
        }
    }
}
