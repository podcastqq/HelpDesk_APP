package app.hd.helpdesk_app;

/**
 * Created by Martynas Smilgevicius on 2014-09-07.
 */

        import java.util.ArrayList;
        import java.util.List;

        import org.apache.http.NameValuePair;
        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONArray;
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

public class EditUserActivity extends Activity{

    EditText txtName;
    EditText txtSurname;
    EditText txtUsername;
    EditText txtPass;
    EditText txtRetypePass;
    Button btnSave;
    Button btnDelete;

    String pid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single user details url
    private static final String url_user_details = "http://10.0.2.2/helpdesk/get_user_details.php";

    // url to update user
    private static final String url_update_user = "http://10.0.2.2/helpdesk/update_user.php";

    // url to delete user
    private static final String url_delete_user = "http://10.0.2.2/helpdesk/delete_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "user";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_SURNAME = "surname";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);

        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        // getting user details from intent
        Intent i = getIntent();

        // getting user id (pid) from intent
        pid = i.getStringExtra(TAG_PID);

        // Getting complete user details in background thread
        new GetUserDetails().execute();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new SaveUserDetails().execute();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                new DeleteUser().execute();
            }
        });

    }

    /**
     * Background Async Task to Get complete user details
     * */
    class GetUserDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditUserActivity.this);
            pDialog.setMessage(getString(R.string.loading_user_details));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", pid));

                        // getting user details by making HTTP request
                        // Note that user details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_user_details, "GET", params);

                        // check your log for json response
                        Log.d("Single User Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received user details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_USER); // JSON Array

                            // get first user object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // user with this pid found
                            // Edit Text
                            txtName = (EditText) findViewById(R.id.inputName);
                            txtSurname = (EditText) findViewById(R.id.inputSurname);
                            txtUsername = (EditText) findViewById(R.id.inputUsername);
                            txtPass = (EditText) findViewById(R.id.inputPass);
                            txtRetypePass = (EditText) findViewById(R.id.inputRetypePass);

                            // display user data in EditText
                            txtName.setText(product.getString(TAG_NAME));
                            txtSurname.setText(product.getString(TAG_SURNAME));
                            txtUsername.setText(product.getString(TAG_USERNAME));
                            txtUsername.setText(product.getString(TAG_PASSWORD));

                        }else{
                            // user with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save user Details
     * */
    class SaveUserDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditUserActivity.this);
            pDialog.setMessage(getString(R.string.updating_user_details));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Updating user
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String name = txtName.getText().toString();
            String username = txtUsername.getText().toString();
            String surname = txtSurname.getText().toString();
            String pass = txtPass.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_SURNAME, surname));
            params.add(new BasicNameValuePair(TAG_USERNAME, username));
            params.add(new BasicNameValuePair(TAG_PASSWORD, pass));

            // sending modified data through http request
            // Notice that update user url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_user,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about user update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update user
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
            // dismiss the dialog once user updated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete User
     * */
    class DeleteUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditUserActivity.this);
            pDialog.setMessage(getString(R.string.deleting_user));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting user
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid", pid));

                // getting user details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_user, "POST", params);

                // check your log for json response
                Log.d("Delete user", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // user successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about user deletion
                    setResult(100, i);
                    finish();
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
            // dismiss the dialog once user deleted
            pDialog.dismiss();

        }

    }
}

