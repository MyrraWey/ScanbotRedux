package com.muravyovdmitr.scanbot;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.util.Log;

public class LockProgressDialog {
	private Dialog dialog;

	public void start(Activity activity, String message) {
		showProgressDialog(activity, message);
	}

	private void showProgressDialog(Activity activity, String message) {
		if (isShowing()) {
			stop();
		}

		if (!activity.isFinishing() && !activity.isDestroyed()) {
			this.dialog = ProgressDialog.show(activity, "", message, true, false);
		}
	}

	public boolean isShowing() {
		return this.dialog != null && this.dialog.isShowing();
	}

	public void stop() {
		if (this.dialog != null) {
			try {
				this.dialog.dismiss();
			} catch (Exception e) {
				Log.e(LockProgressDialog.class.getName(), e.getMessage());
			}
		}
	}
}
