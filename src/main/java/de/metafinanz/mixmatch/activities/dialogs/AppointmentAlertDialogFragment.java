package de.metafinanz.mixmatch.activities.dialogs;

import de.metafinanz.mixmatch.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AppointmentAlertDialogFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.messageLocationDetailAlertDialog)
		       .setTitle(R.string.titleLocationDetailAlertDialog);
		builder.setPositiveButton(R.string.buttonOk, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	           }
	       });
		builder.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });

		return builder.create();
	}

}
