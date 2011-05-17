package de.metafinanz.mixnmatch.frontend.android.utils;

/**
 * Quelle: http://www.javapractices.com/topic/TopicAction.do?Id=180 
 */

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public final class MailUtil {

	/**
	 * Validate the form of an email address.
	 * 
	 * <P>
	 * Return <tt>true</tt> only if
	 * <ul>
	 * <li> <tt>aEmailAddress</tt> can successfully construct an
	 * {@link javax.mail.internet.InternetAddress}
	 * <li>when parsed with "@" as delimiter, <tt>aEmailAddress</tt> contains
	 * two tokens which satisfy
	 * {@link hirondelle.web4j.util.Util#textHasContent}.
	 * </ul>
	 * 
	 * <P>
	 * The second condition arises since local email addresses, simply of the
	 * form "<tt>albert</tt>", for example, are valid for
	 * {@link javax.mail.internet.InternetAddress}, but almost always undesired.
	 */
	public static boolean isValidEmailAddress(String aEmailAddress) {
		if (aEmailAddress == null)
			return false;
		boolean result = true;
		try {
			@SuppressWarnings("unused")
			InternetAddress emailAddr = new InternetAddress(aEmailAddress);
			
			if (!hasNameAndDomain(aEmailAddress)) {
				result = false;
			}
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	private static boolean hasNameAndDomain(String aEmailAddress) {
		String[] tokens = aEmailAddress.split("@");
		return tokens.length == 2 
			&& tokens[0].trim().length() > 0
			&& tokens[1].trim().length() > 0;
	}

	// ..elided
}