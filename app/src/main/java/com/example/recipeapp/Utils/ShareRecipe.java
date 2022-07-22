package com.example.recipeapp.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.recipeapp.Activities.DetailsActivity;
import com.example.recipeapp.Models.Recipe;
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
import com.google.gson.Gson;

import org.parceler.Parcels;

import java.nio.charset.StandardCharsets;

public class ShareRecipe {

    private static final String TAG = "Share class";

    public static void startAdvertising(Context context, String username, Recipe recipe) {
        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build();
        Nearby.getConnectionsClient(context)
                .startAdvertising(
                        username, context.getString(R.string.app_name), getConnectionLifecycleCallback(context, Role.SENDER, recipe), advertisingOptions)
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
                .startDiscovery(context.getString(R.string.app_name), getEndpointDiscoveryCallback(context, username, Role.RECEIVER, null), discoveryOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Log.i(TAG, "Discovering started");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Log.i(TAG, "Discovering error: " + e);
                        });
    }

    public static EndpointDiscoveryCallback getEndpointDiscoveryCallback(Context context, String username, Role role, Recipe recipe) {
        return new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                // An endpoint was found. We request a connection to it.
                Nearby.getConnectionsClient(context)
                        .requestConnection(username, endpointId, getConnectionLifecycleCallback(context, role, recipe))
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

    public static ConnectionLifecycleCallback getConnectionLifecycleCallback(Context context, Role role, Recipe recipe) {
        return new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                new AlertDialog.Builder(context)
                        .setTitle("Sharing recipe with " + connectionInfo.getEndpointName())
                        .setMessage("Confirm the code matches on both devices: " + connectionInfo.getAuthenticationDigits())
                        .setPositiveButton(
                                "Accept",
                                (DialogInterface dialog, int which) ->
                                        // The user confirmed, so we can accept the connection.
                                        Nearby.getConnectionsClient(context)
                                                .acceptConnection(endpointId, getPayloadCallback(context)))
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
                        // Here we are sending recipe
                        if(role == Role.SENDER) {
                            byte[] recipeBytes = new Gson().toJson(recipe).getBytes(StandardCharsets.UTF_8);
                            Nearby.getConnectionsClient(context).sendPayload(endpointId, Payload.fromBytes(recipeBytes));
                            Nearby.getConnectionsClient(context).stopAdvertising();
                        }
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
    private static PayloadCallback getPayloadCallback(Context context) {
        return new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
                // This is called when the first byte of payload is received
                // For the bytes payload, called at the same time as onPayloadTransferUpdate
                Log.i(TAG, "Transfer started");
                if(payload.getType() == Payload.Type.BYTES) {
                    Recipe recipe = new Gson().fromJson(new String(payload.asBytes(), StandardCharsets.UTF_8), Recipe.class);
                    Intent i = new Intent(context, DetailsActivity.class);
                    i.putExtra(DetailsActivity.RECIPE, Parcels.wrap(recipe));
                    context.startActivity(i);
                }
            }

            @Override
            public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
                if(payloadTransferUpdate.getStatus() != PayloadTransferUpdate.Status.SUCCESS) {
                    Log.i(TAG, "Something went wrong: " + payloadTransferUpdate.getStatus());
                    Nearby.getConnectionsClient(context).stopDiscovery();
                    return;
                }
                Log.i(TAG, "Payload transferred");
            }
        };
    }

    enum Role {
        SENDER,
        RECEIVER
    }
}
