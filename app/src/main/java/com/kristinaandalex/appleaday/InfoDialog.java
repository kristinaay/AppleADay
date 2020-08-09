package com.kristinaandalex.appleaday;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;


public class InfoDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Need help getting started?")
                .setMessage("Click on a day to start tracking your symptoms or click the button below the calendar " +
                "for COVID-19 related resources.")
                .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //system will just dismiss and close dialog
                    }
                });
        return builder.create();
    }
}
