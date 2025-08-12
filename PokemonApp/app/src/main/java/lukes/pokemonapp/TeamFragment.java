package lukes.pokemonapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * The fragment that drives the team selection screen. Essentially, it just tells the buttons to
 * change the screen to the PokemonFragments when they are clicked. The third button is still
 * in development since the Pokémon in the third team do not have data yet.
 * @author Luke Schoeberle 7/21/2016.
 */
public class TeamFragment extends Fragment { //fragment code 2

    /**
     * The view that this Fragment has. From what I can tell, this essentially represents the overall
     * screen (the xml layout file) that is initialized in the onCreateView method.
     */
    private View myView;

    /**
     * The button for the first team.
     */
    private Button team1Button;

    /**
     * The button for the second team.
     */
    private Button team2Button;

    /**
     * The button for the third team. This is not currently in use because the third team has no data yet.
     */
    private Button team3Button; //get working later //TODO

    /**
     * This Fragment's main activity. This is essentially just a shortcut so I don't have to say
     * (MainActivity) getActivity() for changing fragments every time.
     */
    private MainActivity ma;

    /**
     * Creates this view based on the parameters. Essentially, it initializes the buttons and causes them
     * to change the screen to the PokemonFragments. Currently, all of these variables could simply be local
     * variables because they are only used in this method, but I am leaving them as instance variables in case
     * I need more detail in this fragment.
     * @param inflater Creates this view from the layout file.
     * @param container Puts this view into the specified container.
     * @param savedInstance Not used in this case.
     * @return The view created by the inflater.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        //initializes variables
        myView = inflater.inflate(R.layout.team_selection_screen, container, false);

        team1Button = myView.findViewById(R.id.team1Button);
        team2Button = myView.findViewById(R.id.team2Button);
        team3Button = myView.findViewById(R.id.team3Button);

        ma = (MainActivity) getActivity();

        //changes screens
        team1Button.setOnClickListener((v) -> ma.displayView(1, "Bulbasaur"));
        team2Button.setOnClickListener((v) -> ma.displayView(1,"Chikorita"));
        //team3Button.setOnClickListener((v) -> ma.displayView(1, "Chikorita")); //will come later //TODO

        return myView;
    }
}
