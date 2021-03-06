package edu.temple.voicetest;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import static android.speech.SpeechRecognizer.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displaySpeechRecognizer();
    }
    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            ((TextView)findViewById(R.id.spoken_text)).setText(spokenText);
            processText(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void processText(String spokenText){
        DialogFragment newFragment = ConfirmCommandDialogFragment.newInstance(
                R.string.confirm_command_alert_dialog_title, spokenText.toUpperCase());
        newFragment.show(getFragmentManager(), "dialog");
    }
    public static class ConfirmCommandDialogFragment extends DialogFragment {
        public static ConfirmCommandDialogFragment newInstance(int title, String command) {
            ConfirmCommandDialogFragment frag = new ConfirmCommandDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            args.putString("command", command);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");
            String command = getArguments().getString("command");
            return new AlertDialog.Builder(getActivity())
                    .setMessage(getString(title) + command)
                    .setPositiveButton(R.string.alert_dialog_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }
                    )
                    .setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((MainActivity)getActivity()).displaySpeechRecognizer();
                                }
                            }
                    )
                    .create();
        }
    }
}