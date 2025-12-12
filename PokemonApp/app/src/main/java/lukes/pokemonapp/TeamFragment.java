package lukes.pokemonapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * The fragment that drives the team selection screen. Essentially, it just tells the buttons to
 * change the screen to the PokemonFragments when they are clicked.
 * @author Luke Schoeberle 7/21/2016.
 */
public class TeamFragment extends Fragment { //fragment code 2

    /**
     * This Fragment's main activity. This is essentially just a shortcut so I don't have to say
     * (MainActivity) getActivity() for changing fragments every time.
     */
    private MainActivity ma;

    /**
     * Creates this view based on the parameters. Essentially, it initializes the buttons and causes them
     * to change the screen to the PokemonFragments.
     * @param inflater Creates this view from the layout file.
     * @param container Puts this view into the specified container.
     * @param savedInstance Not used in this case.
     * @return The view created by the inflater.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        // The view that this Fragment represents, which is described by the XML layout file and
        // Initialized in the onCreateView method
        View myView = inflater.inflate(R.layout.team_selection_screen, container, false);

        Button team1Button = myView.findViewById(R.id.team1Button);
        Button team2Button = myView.findViewById(R.id.team2Button);
        Button team3Button = myView.findViewById(R.id.team3Button);

        ma = (MainActivity) getActivity();

        // Changes screens
        team1Button.setOnClickListener((v) -> ma.displayView(1, "Bulbasaur"));
        team2Button.setOnClickListener((v) -> ma.displayView(1,"Chikorita"));
        team3Button.setOnClickListener((v) -> ma.displayView(1, "Treecko"));

        return myView;
    }
}