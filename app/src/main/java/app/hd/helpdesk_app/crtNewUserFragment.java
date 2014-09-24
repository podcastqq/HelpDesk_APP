package app.hd.helpdesk_app;

/**
 * Created by Martynas Smilgevicius on 2014-09-07.
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class crtNewUserFragment extends Fragment implements InformationalDialogFragment.Communicator{

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputSurname;
    EditText inputUsername;
    EditText inputPass;
    EditText inputRetypePass;
    String url_create_user;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // url to create new product
         url_create_user = getString(R.string.url_create_user);
    }
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.add_user_fragment, container, false);

        // Edit Text
        inputName = (EditText) rootView.findViewById(R.id.inputName);
        inputSurname = (EditText) rootView.findViewById(R.id.inputSurname);
        inputUsername = (EditText) rootView.findViewById(R.id.inputUsername);
        inputPass = (EditText) rootView.findViewById(R.id.inputPass);
        inputRetypePass = (EditText) rootView.findViewById(R.id.inputRetypePass);

        // Create button
        Button btnCreateUser = (Button) rootView.findViewById(R.id.btnCreateUser);

        // button click event
        btnCreateUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread

                if (inputPass.getText().toString().equals(inputRetypePass.getText().toString())) {
                    new CreateNewUser().execute();
                } else {
                        FragmentManager manager = getFragmentManager();
                        InformationalDialogFragment OkDialog = new InformationalDialogFragment();
                        OkDialog.show(manager, "OkDialog");
                    }
            }
        });
        return rootView;
    }

    @Override
    public void onDialogMessage(String message) {
        //do smth when wrong pass alert dissmissed
    }

    /**
     * Background Async Task to Create new user
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getString(R.string.creating_user));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating user
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String surname = inputSurname.getText().toString();
            String username = inputUsername.getText().toString();
            String pass = inputPass.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("surname", surname));
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("pass", pass));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                    "POST", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
//            try {
//                int success = json.getInt(TAG_SUCCESS);
//
//                if (success == 1) {
//                    // successfully created user
//                    Intent i = new Intent(getApplicationContext(), AllUsersActivity.class);
//                    startActivity(i);
//
//                    // closing this screen
//                    finish();
//                } else {
//                    // failed to create user
//                    Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
