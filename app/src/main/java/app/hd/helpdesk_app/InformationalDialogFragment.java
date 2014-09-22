package app.hd.helpdesk_app;

/**
 * Created by Martynas Smilgevicius on 2014-09-19.
 */

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class InformationalDialogFragment extends DialogFragment implements View.OnClickListener {
    Button Ok;
    Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator= (Communicator) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_dialog_frag, null);
        Ok = (Button) view.findViewById(R.id.ok);
        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View view) {
        communicator.onDialogMessage("OK was clicked");
        dismiss();
    }

    interface Communicator
    {
        public void onDialogMessage(String message);
    }
}
