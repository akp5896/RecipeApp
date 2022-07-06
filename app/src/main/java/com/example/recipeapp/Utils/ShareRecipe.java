package com.example.recipeapp.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.recipeapp.R;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

public class ShareRecipe {

    private static final String TAG = "Share class";

    public static void startAdvertising(Context context, String username) {
        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build();
        Nearby.getConnectionsClient(context)
                .startAdvertising(
                        username, context.getString(R.string.app_name), getConnectionLifecycleCallback(context), advertisingOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Log.i(TAG, "Advertisement started");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Log.i(TAG, "Advertisement error: " + e);
                        });
    }

    public static void startDiscovery(Context context, String username) {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build();
        Nearby.getConnectionsClient(context)
                .startDiscovery(context.getString(R.string.app_name), getEndpointDiscoveryCallback(context, username), discoveryOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Log.i(TAG, "Discovering started");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Log.i(TAG, "Discovering error: " + e);
                        });
    }

    public static EndpointDiscoveryCallback getEndpointDiscoveryCallback(Context context, String username) {
        return new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                // An endpoint was found. We request a connection to it.
                Nearby.getConnectionsClient(context)
                        .requestConnection(username, endpointId, getConnectionLifecycleCallback(context))
                        .addOnSuccessListener(
                                (Void unused) -> {
                                    // We successfully requested a connection. Now both sides
                                    // must accept before the connection is established.
                                    Log.i(TAG, "Connection requested");
                                })
                        .addOnFailureListener(
                                (Exception e) -> {
                                    // Nearby Connections failed to request the connection.
                                    Log.e(TAG, "Request failed" + e);
                                });
            }

            @Override
            public void onEndpointLost(String endpointId) {
                // A previously discovered endpoint has gone away.
                Log.i(TAG, "Endpoint lost");
            }
        };
    }

    public static ConnectionLifecycleCallback getConnectionLifecycleCallback(Context context) {
        return new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                // Automatically accept the connection on both sides.
                new AlertDialog.Builder(context)
                        .setTitle("Accept connection to " + connectionInfo.getEndpointName())
                        .setMessage("Confirm the code matches on both devices: " + connectionInfo.getAuthenticationDigits())
                        .setPositiveButton(
                                "Accept",
                                (DialogInterface dialog, int which) ->
                                        // The user confirmed, so we can accept the connection.
                                        Nearby.getConnectionsClient(context)
                                                .acceptConnection(endpointId, getPayloadCallback()))
                        .setNegativeButton(
                                android.R.string.cancel,
                                (DialogInterface dialog, int which) ->
                                        // The user canceled, so we should reject the connection.
                                        Nearby.getConnectionsClient(context).rejectConnection(endpointId))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }

            @Override
            public void onConnectionResult(String endpointId, ConnectionResolution result) {
                switch (result.getStatus().getStatusCode()) {
                    case ConnectionsStatusCodes.STATUS_OK:
                        // We're connected! Can now start sending and receiving data.
                        break;
                    case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                        getErrorAlertDialog(context, context.getString(R.string.connection_rejected));
                        break;
                    case ConnectionsStatusCodes.STATUS_ERROR:
                        getErrorAlertDialog(context, context.getString(R.string.connection_broken));
                        break;
                    default:
                        getErrorAlertDialog(context, context.getString(R.string.unknow_error));
                }
            }

            @Override
            public void onDisconnected(String endpointId) {
                Log.i(TAG, "Disconnected from: " + endpointId);
            }
        };
    }

    private static void getErrorAlertDialog(Context context, String s) {
        new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(s)
                .setPositiveButton(
                        "Ok", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @NonNull
    private static PayloadCallback getPayloadCallback() {
        return new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {

            }

            @Override
            public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

            }
        };
    }
}
