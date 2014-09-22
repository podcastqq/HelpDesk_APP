package app.hd.helpdesk_app;

/**
 * Created by Martynas Smilgevicius on 2014-09-07.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ActivityMain extends Activity {

    Button btnViewUsers;
    Button btnCreateUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //buttons
        btnViewUsers = (Button) findViewById(R.id.btnViewUsers);
        btnCreateUser = (Button) findViewById(R.id.btnCreateUser);

        //view users click event
        btnViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lauch all users activity
                Intent i = new Intent(getApplicationContext(), AllUsersActivity.class);
                startActivity(i);
            }
        });

        //create new user click event
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch create new user activity
                Intent i = new Intent(getApplicationContext(), crtNewUserActivity.class);
                startActivity(i);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
