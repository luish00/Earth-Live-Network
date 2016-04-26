package com.hector.luis.spaceapp2016.earthlivelh.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Created by Hector Arredondo on 17/04/2016.
 */
public class CustomRequest extends Request<JSONObject> {

    private final String TAG = CustomRequest.class.getSimpleName();

    public Context context;
    private static ProgressDialog dialog = null;

    private Listener<JSONObject> jsonObjectListener;
    private Map<String, String> params;

    private boolean isCancelable = false;
    private String mensaje = "Cargando...";

    public CustomRequest(Context context, String url, Map<String, String> params,
                         Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.jsonObjectListener = reponseListener;
        this.params = params;
        this.context = context;
        this.dialog = new ProgressDialog(context);
    }

    public CustomRequest(Context context, int method, String url, Map<String, String> params,
                         Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.jsonObjectListener = reponseListener;
        this.params = params;
        this.context = context;
        this.dialog = new ProgressDialog(context);
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            closeDialogProgress();

            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            closeDialogProgress();

            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        jsonObjectListener.onResponse(response);

        closeDialogProgress();
    }

    public void startProgress() {
        startDialog(this.mensaje);
    }

    public void startProgress(String msj) {
        startDialog(msj);
    }

    public void startProgress(int msj) {
        startDialog(context.getResources().getString(msj));
    }

    private void startDialog(String prMsj) {
        if (!CustomRequest.dialogIsShowing()) {
            dialog.setMessage(prMsj);
            dialog.setCancelable(isCancelable);
            dialog.show();
        }
    }

    public static void closeDialogProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog.cancel();
            dialog = null;
        }
    }

    private static boolean dialogIsShowing() {
        if (dialog != null && dialog.isShowing()) {
            return true;
        }

        return false;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isCancelable() {
        return isCancelable;
    }

    public void setIsCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }
}