package de.metafinanz.mixmatch.activities.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import de.metafinanz.mixmatch.R;

public class QuitApplicationAlertDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.messageQuitAlertDialog)
		       .setTitle(R.string.titleQuitAlertDialog);
		builder.setPositiveButton(R.string.buttonOk, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               getActivity().finish();
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
