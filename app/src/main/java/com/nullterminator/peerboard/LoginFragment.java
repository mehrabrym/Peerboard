/**
 * Created by Rayun on 2015-03-01.
 */

package com.nullterminator.peerboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nullterminator.peerboard.R;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

public class LoginFragment extends Fragment implements View.OnClickListener, LoaderCallbacks<Cursor> {

    int veriPIN = 0;
	String name = "";
	//private Toolbar toolbar;
	View view;
	//Comment 1 - Feb 18 6:15pm
    //Comment 2 - Feb 18 6:18pm
    //Comment 3 - Feb 18 9:19pm
    //Comment 4 - Feb 22 9:47pm
	//Comment 5 - Feb 22 10:20pm
	//Comment 6 - Mar 10 11:22pm
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
	protected Activity mActivity;

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);
        mActivity = act;
    }

    @Override
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState)
    {
        super.onActivityCreated(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_login, container, false);

		/*toolbar = (Toolbar) view.findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        view.setSupportActionBar(toolbar);*/ //TODO: Make materiYOLO work later

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) view.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
					if (id == R.id.login || id == EditorInfo.IME_NULL) {
						attemptLogin();
						return true;
					}
					return false;
				}
			});

        Button mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					attemptLogin();
				}
			});

		Button mEmailSignUpButton = (Button) view.findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(getActivity(), HomepageActivity.class);
					startActivity(i);
					//attemptSignUp();
				}
			});

        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);
		
        return view;
    }

    @Override
    public void onClick(View v)
    {
        //do whatever you want here
    }
	
	private void populateAutoComplete() {
        //getLoaderManager().initLoader(0, null, getActivity()); //TODO: figure out
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

		if (TextUtils.isEmpty(password)){
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
			try {
				Context context = getActivity();
				CharSequence text = "Will attempt jdbc now";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@mail.utoronto.ca");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
				show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
					}
				});

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
				show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
					}
				});
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
								// Retrieve data rows for the device user's 'profile' contact.
								Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
													 ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

								// Select only email addresses.
								ContactsContract.Contacts.Data.MIMETYPE +
								" = ?", new String[]{ContactsContract.CommonDataKinds.Email
									.CONTENT_ITEM_TYPE},

								// Show primary email addresses first. Note that there won't be
								// a primary email address if the user hasn't specified one.
								ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    } //TODO: Figure this out

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
			ContactsContract.CommonDataKinds.Email.ADDRESS,
			ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
			new ArrayAdapter<>(getActivity(),
									 android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String salt;
            String pword;
            String pwd = "";



            // TODO: attempt authentication against a network service.
            try {
                salt = Sha1Hash.SHA1(mEmail);
                pword = Sha1Hash.SHA1(mPassword);
                pwd = Sha1Hash.SHA1(salt + pword);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.getMessage();
                e.printStackTrace();
            }

            try {
                veriPIN = DBCompare.authenticateLogin(mEmail, pwd);
            } catch (SQLException e) {
                e.printStackTrace();
            }

			try {
				if ((veriPIN == -1)||(veriPIN == -2)){
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

            /*try {
                GMailSender sender = new GMailSender("mehrabrym@gmail.com", "arnobfr2");
                sender.sendMail("Here is your PIN",
                        "This is the veriPIN: Please enter it in the verification page of the app.",
                        "mehrabrym@gmail.com",
                        "mehrabrym@gmail.com");
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }*/

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("mehrabrym@gmail.com", "abcdefthispasswordisnotgoingtowork"); //TODO: Problem - password in plain text
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("mehrabrym@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("mehrabrym@gmail.com"));
                message.setSubject("Your PIN for registering at Peerboard!");
                message.setContent("This is the veriPIN: " + veriPIN + ". Please enter it in the verification page of the app.", "text/html; charset=utf-8");

                Transport.send(message);

            } catch (MessagingException e) {
				Log.e("Sendmail", e.getMessage(), e);
                //throw new RuntimeException(e);
            }

            //Rayun: code for database communication

            /*for (String credential : DUMMY_CREDENTIALS) {
			 String[] pieces = credential.split(":");
			 if (pieces[0].equals(mEmail)) {
			 // Account exists, return true if the password matches.
			 return pieces[1].equals(mPassword);
			 }
			 }*/

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
			//TODO: need to understand this part and write the post execute part.

            if (success&&(getActivity()!=null)) {
				Context context = getActivity();
				CharSequence text = "Successfully logged in, PIN = " + veriPIN;
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
                getActivity().finish();

                Intent i = new Intent(getActivity(), HomepageActivity.class);
                startActivity(i);
            } else {
				Context context = getActivity();
				CharSequence text = "Email invalid/not registered " + veriPIN;
				int duration = Toast.LENGTH_SHORT;
                if(context!=null){
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
