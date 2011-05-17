package de.metafinanz.mixnmatch.frontend.android.utils;

import android.util.Log;
import android.widget.EditText;

public class TextTestUtils {
	private static final String TAG = "TextTestUtils";

	public static final class Type {
		public static final int TEXT = 0;
		public static final int EMAIL = 1;
	}


	public static boolean testText(int type, EditText editText) {
		boolean result = false;
		
		switch (type) {
		case Type.TEXT:
			if (editText.getText().length() > 0) 
				result = true;
			break;
			
		case Type.EMAIL:
			if (editText.getText().length() > 0) 
				result = MailUtil.isValidEmailAddress(editText.getText().toString());
			break;
			
		default:
			result = false;
		}
		
		Log.d(TAG, "Validating text for type '"+type+"' '"+editText.getText().toString()+"' with result: " + result);
		
		return result;
		
	}
}
