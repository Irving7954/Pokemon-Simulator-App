package lukes.pokemonapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

/**
 * The main driver of the app. It is responsible for changing between fragments (essentially screens)
 * and creating the initial instance state.
 * @author Luke Schoeberle 7/12/2016 (roughly)
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Creates the instance for this app and sets the view to StartFragment on the main screen. The
     * main screen is just a blank background.
     * @param savedInstanceState The Bundle to be set to.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        displayView(0,"");
    }

    /**
     * Sets the fragment to the specified code and pokemon name, if applicable. Each code represents
     * a different screen, and this method is used to toggle between them. For certain ones, it uses
     * the same screen but displays different data based on the second parameter (the name). This uses
     * FragmentManager and FragmentTransaction for this, which I don't entirely understand.
     * @param position The fragment that you wish to move to (based on its code).
     * @param pokeName The Pokemon that you wish to display (meaningless except for certain Fragments).
     */
    public void displayView(int position, String pokeName) {
        Fragment fragment;
        Bundle bund;
        Pokemon poke;
        switch(position) { //chooses a fragment based on the code
            case 0:
                fragment = new StartFragment();
                break;
            case 1:
                fragment = new PokemonFragment();
                bund = new Bundle();
                poke = new Pokemon(pokeName);
                bund.putParcelable("key", poke);
                fragment.setArguments(bund);
                break;
            case 2:
                fragment = new TeamFragment();
                break;
            case 3:
                fragment = new BattleFragment();
                bund = new Bundle();
                poke = new Pokemon(pokeName);
                bund.putParcelable("key", poke);
                fragment.setArguments(bund);
                break;
            default:
                throw new IllegalArgumentException(position + " is not a valid fragment at this time.");
        }
        //changes the current fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.backgroundLayout, fragment);
        fragmentTransaction.commit();
    }
}