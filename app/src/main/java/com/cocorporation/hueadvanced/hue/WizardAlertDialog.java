package com.cocorporation.hueadvanced.hue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.WindowManager;

import com.cocorporation.hueadvanced.R;

/**
 * Generic class for Alert and Progress dialogs wizard
 *
 *
 */

public final class WizardAlertDialog {

    public static final String TAG_WIZARD_ALERT_DIALOG = "WizardAlertDialog";

    private ProgressDialog pdialog;
    private static WizardAlertDialog dialogs;

    private WizardAlertDialog() {

    }

    public static synchronized WizardAlertDialog getInstance() {
        if (dialogs == null) {
            dialogs = new WizardAlertDialog();
        }
        return dialogs;
    }

    /**
     *
     * @param activityContext   Context of the activity
     * @param msg               Message to show
     * @param btnNameResId      String resource id for button name
     */
    public static void showErrorDialog(Context activityContext, String msg, int btnNameResId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle(R.string.title_error).setMessage(msg).setPositiveButton(btnNameResId, null);
        AlertDialog alert = builder.create();
        alert.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        if (! ((Activity) activityContext).isFinishing()) {
            alert.show();
        }

    }

    /**
     * Stops running progress-bar
     */
    public void closeProgressDialog() {

        if (pdialog != null) {
            pdialog.dismiss();
            pdialog = null;
        }
    }

    /**
     * Shows progress-bar
     *
     * @param resID ID
     * @param ctx   Context
     */
    public void showProgressDialog(int resID, Context ctx) {
        Log.w(TAG_WIZARD_ALERT_DIALOG, "showProgressDialog");
        String message = ctx.getString(resID);
        pdialog = ProgressDialog.show(ctx, null, message, true, true);
        pdialog.setCancelable(false);

    }

    public void showProgressDialogWithBar(int resID, Context ctx, int maxTime) {
        Log.w(TAG_WIZARD_ALERT_DIALOG, "showProgressDialogWithBar");
        String message = ctx.getString(resID);
        pdialog = new ProgressDialog(ctx);
        pdialog.setTitle(null);
        pdialog.setMessage(message);
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.setMax(maxTime);
        pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pdialog.setProgressNumberFormat(null);
        pdialog.setProgressPercentFormat(null);
        pdialog.show();
    }

    public void incrementProgressDialogWithBar(int increment) {
        pdialog.incrementProgressBy(increment);
    }

    /**
     *
     * @param activityContext   Activity context
     * @param msg               Message to show
     * @param btnNameResId      Button Id
     */
    public static void showAuthenticationErrorDialog(
            final Activity activityContext, String msg, int btnNameResId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle(R.string.title_error).setMessage(msg)
                .setPositiveButton(btnNameResId, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activityContext.finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

}
