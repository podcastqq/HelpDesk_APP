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
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class crtNewUserActivity extends Activity implements InformationalDialogFragment.Communicator{

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputSurname;
    EditText inputUsername;
    EditText inputPass;
    EditText inputRetypePass;

    // url to create new product
    private static String url_create_user = "http://10.0.2.2/helpdesk/create_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputSurname = (EditText) findViewById(R.id.inputSurname);
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        inputPass = (EditText) findViewById(R.id.inputPass);
        inputRetypePass = (EditText) findViewById(R.id.inputRetypePass);

        // Create button
        Button btnCreateUser = (Button) findViewById(R.id.btnCreateUser);

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
            pDialog = new ProgressDialog(crtNewUserActivity.this);
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
            String retypePass = inputRetypePass.getText().toString();

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
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created user
                    Intent i = new Intent(getApplicationContext(), AllUsersActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create user
                    Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
