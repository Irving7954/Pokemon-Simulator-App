package lukes.pokemonapp;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//try to use toast for something

/**
 * A first Fragment that is shown in the app. Currently, it just has an introductory text and a start
 * button that takes you to the team selection screen, but it may have more features later.
 * @author Luke Schoeberle 7/13/2016.
 */
public class StartFragment extends Fragment { //Fragment Code 0

    /**
     * The view that this Fragment has. From what I can tell, this essentially represents the overall
     * screen (the xml layout file) that is initialized in the onCreateView method.
     */
    private View myView;

    /**
     * The start button of this screen. Currently, its only purpose is to take you to the TeamFragment.
     */
    private Button startButton;

    /**
     * This Fragment's main activity. This is essentially just a shortcut so I don't have to say
     * (MainActivity) getActivity() for changing fragments every time.
     */
    private MainActivity ma;

    /**
     * Creates this view based on the parameters. Essentially, it initializes the button and causes it
     * to change the screen to the TeamFragment. Currently, all of these variables could simply be local
     * variables because they are only used in this method, but I am leaving them as instance variables
     * for when this fragment has other purposes.
     * @param inflater Creates this view from the layout file.
     * @param container Puts this view into the specified container.
     * @param savedInstance Not used in this case.
     * @return The view created by the inflater.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        //initializes variables
        myView = inflater.inflate(R.layout.activity_main, container, false);
        startButton = myView.findViewById(R.id.startButton);
        ma = (MainActivity) getActivity();
        //changes screen
        startButton.setOnClickListener((v) -> ma.displayView(2, ""));

        return myView;
    }
}
