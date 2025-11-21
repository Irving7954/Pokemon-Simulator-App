package lukes.pokemonapp;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A first Fragment that is shown in the app. Currently, it just has an introductory text and a start
 * button that takes you to the team selection screen, but it may have more features later.
 * @author Luke Schoeberle 7/13/2016.
 */
public class StartFragment extends Fragment { //Fragment Code 0

    /**
     * This Fragment's main activity. This is essentially just a shortcut so I don't have to say
     * (MainActivity) getActivity() for changing fragments every time.
     */
    private MainActivity ma;

    /**
     * Creates this view based on the parameters. Essentially, it initializes the button and causes it
     * to change the screen to the TeamFragment.
     * @param inflater Creates this view from the layout file.
     * @param container Puts this view into the specified container.
     * @param savedInstance Not used in this case.
     * @return The view created by the inflater.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        // This Fragment's view, which is described by the XML layout file
        View myView = inflater.inflate(R.layout.activity_main, container, false);

        // The start button of this screen, which only currently exists to take the user to the TeamFragment
        Button startButton = myView.findViewById(R.id.startButton);

        ma = (MainActivity) getActivity();
        //changes screen
        startButton.setOnClickListener((v) -> ma.displayView(2, ""));

        return myView;
    }
}
