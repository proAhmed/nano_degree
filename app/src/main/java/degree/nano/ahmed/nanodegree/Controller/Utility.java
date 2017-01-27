package degree.nano.ahmed.nanodegree.Controller;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by ahmed on 1/27/2017.
 */
public class Utility {

    public static void email(Activity activity,String email){
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "market");


        emailIntent.setType("message/rfc822");

        try {
            activity.startActivity(Intent.createChooser(emailIntent,
                    "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity,
                    "No email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }

    }


}
