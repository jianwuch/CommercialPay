package com.gsd.idreamsky.weplay.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.gsd.idreamsky.weplay.utils.MR;

public class LoadingDialog extends Dialog {

	public LoadingDialog(Context context) {
		super(context, android.R.style.Theme_Panel);
		setView(context);
		setProperty(context);
	}

	private void setView(Context context) {

		View layoutBook = LayoutInflater.from(context).inflate(
				MR.getIdByLayoutName(context, "gsd_loading"), null);
		setContentView(layoutBook);
	}

	private void setProperty(Context mContext) {
		setCanceledOnTouchOutside(true);
		setCancelable(true);
	}

}
