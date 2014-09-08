package app.hd.helpdesk_app;

/**
 * Created by KMTPOFF on 2014-09-07.
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class crtNewUserActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputSurname;
    EditText inputUsername;
    EditText inputPass;
    EditText inputRetypePass;

    // url to create new product
    private static String url_create_user = "http://localhost/helpdesk/create_user.php";

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

                if (inputPass.equals(inputRetypePass)) {
                    new CreateNewUser().execute();
                } else {

                    }
            }
        });
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
            pDialog.setMessage("@string/creating_user");
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

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created user
                    Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create user
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
