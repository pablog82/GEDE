package com.babelgroup.gede.util.afirma;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

/**
 * The Class ClientPasswordCallback.
 */
public class ClientPasswordCallback implements CallbackHandler {

	/** The username. */
	private final String username;

	/** The password. */
	private final String password;

	/**
	 * Instantiates a new client password callback.
	 *
	 * @param username the username
	 * @param password the password
	 */
	public ClientPasswordCallback(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.
	 * callback.Callback[])
	 */
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

		for (Callback callback : callbacks) {

			WSPasswordCallback pc = (WSPasswordCallback) callback;

			if (pc.getUsage() == WSPasswordCallback.USERNAME_TOKEN) {

				// you can source the username and password from
				// other sources like login context, LDAP, DB etc

				pc.setIdentifier(username);
				pc.setPassword(password);
			}
		}
	}
}
