package lukes.pokemonapp;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A fragment for displaying Pokémon data and moves. It contains an image and stats about the Pokémon
 * that are displayed in a list view. You can also toggle between different Pokémon using the buttons.
 * This class is likely complete, except for the minor details.
 * @author Luke Schoeberle 7/16/2016.
 */
public class PokemonFragment extends Fragment { //Fragment code 1
    /**
     * The Pokémon provided in the constructor. This Pokémon is displayed on this Fragment.
     */
    private Pokemon poke;

    /**
     * This Fragment's main activity. This is essentially just a shortcut to avoid calling
     * (MainActivity) getActivity() to change fragments each time.
     */
    private MainActivity ma;

    /**
     * Determines if you are at the end of a team. This is necessary to know when to
     * behave different because the team ending message has already appeared.
     */
    private boolean atEndOfTeam;

    /**
     * The default constructor, which is the only one allowed for Fragment. This accesses its arguments
     * in the Bundle to find the Pokémon's name (which was previously done with an argument in the constructor).
     */
    public PokemonFragment() {
        super();
    }

    /**
     * Creates this view based on the parameters. Essentially, it initializes the buttons, the text boxes,
     * and the listView. It also causes the buttons to change the screen to other Fragments.
     * @param inflater Creates this view from the layout file.
     * @param container Puts this view into the specified container.
     * @param savedInstance The bundle that contains the current Pokémon argument.
     * @return The view created by the inflater.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        // This Fragment's view, which is described by the XML layout file
        View myView = inflater.inflate(R.layout.pokemon_screen, container, false);

        // The text box for the Pokémon's name, which is initialized to the String provided in the constructor
        TextView pokeName = myView.findViewById(R.id.pokemonName);

        // An image of the Pokémon, which is initialized to a resource based on the Pokémon's name
        ImageView pokePic = myView.findViewById(R.id.pokemonPic);

        // The button used to move to the next Pokémon on the current team. If you are already on the last Pokémon,
        // Clicking the button instead shows a message about confirming your team selection
        Button forwardButton = myView.findViewById(R.id.forwardButton);

        // The button used to move to the last Pokémon on the current team. If you are already on the last Pokémon,
        // Clicking the button instead returns to the team selection screen
        Button backButton = myView.findViewById(R.id.backButton);

        ma = (MainActivity) getActivity();

        Bundle bundle = getArguments();
        if(bundle != null) {
            poke = bundle.getParcelable("key", Pokemon.class);
        }

        atEndOfTeam = false;

        // Use Bulbasaur as the default Pokémon for now if the bundle fails to provide the parameter
        if(poke == null) {
            poke = new Pokemon("Bulbasaur");
        }

        // Sets the text to the Pokémon's name.
        String pokemonName = poke.getName();

        pokeName.setText(pokemonName);

        //initializes the image
        switch(pokemonName) {
            case "Bulbasaur":
                pokePic.setImageResource(R.drawable.bulbasaur);
                break;
            case "Charmander":
                pokePic.setImageResource(R.drawable.charmander);
                break;
            case "Squirtle":
                pokePic.setImageResource(R.drawable.squirtle);
                break;
            case "Chikorita":
                pokePic.setImageResource(R.drawable.chikorita);
                break;
            case "Cyndaquil":
                pokePic.setImageResource(R.drawable.cyndaquil);
                break;
            case "Totodile":
                pokePic.setImageResource(R.drawable.totodile);
                break;
            case "Wooper":
                pokePic.setImageResource(R.drawable.wooper);
                break;
            case "Voltorb":
                pokePic.setImageResource(R.drawable.voltorb);
                break;
            case "Snubbull":
                pokePic.setImageResource(R.drawable.snubbull);
                break;
            case "Treecko":
                pokePic.setImageResource(R.drawable.treecko);
                break;
            case "Torchic":
                pokePic.setImageResource(R.drawable.torchic);
                break;
            case "Mudkip":
                pokePic.setImageResource(R.drawable.mudkip);
                break;
            default:
                throw new IllegalArgumentException(pokemonName + " has not been defined yet!");
        }
        //initializes the list array
        String[] listPokeStats = {"TYPE:          " + poke.getType(), "ABILITY:     " + poke.getAbility(),
                                  "LEVEL:         " + poke.getLevel(), "HP:              " + poke.getInitStats()[0],
                                  "ATTACK:     " + poke.getInitStats()[1], "DEFENSE:   " + poke.getInitStats()[2],
                                  "SP. ATK:      " + poke.getInitStats()[3], "SP. DEF:      " + poke.getInitStats()[4],
                                  "SPEED:       " + poke.getInitStats()[5],  "MOVES:", poke.getMoves().get(0).toString(),
                                  poke.getMoves().get(1).toString(), poke.getMoves().get(2).toString(),
                                  poke.getMoves().get(3).toString()};

        // Creates the adapter for putting the list array into the list view
        ArrayAdapter<String> adapter = new ArrayAdapter<>(myView.getContext(), R.layout.text_for_stats_listview, listPokeStats);

        // The displayed list of Pokémon stats, which shows the moves and stats of the Pokémon named in the constructor
        ListView pokeInfo = myView.findViewById(R.id.statsListView);
        pokeInfo.setAdapter(adapter);

        //determines what happens when items in the list are clicked
        pokeInfo.setOnItemClickListener((parent, viewClicked, position, id) -> {
                TextView textView = (TextView) viewClicked;
                String text = textView.getText().toString();
                switch (position) { //position is like index in the array
                    case 0:
                        makeBoxWithOk(text.substring(15) + " Type", getTypeDescription(text.substring(15)));
                        break;
                    case 1:
                        String s = text.substring(13);
                        switch (s) { //shows box with a description based on the ability name.
                            case "Blaze":
                                makeBoxWithOk(s + " Ability", "This ability boosts the power of Fire type " +
                                                              "moves when the user's health is low.");
                                break;
                            case "Intimidate":
                                makeBoxWithOk(s + " Ability", "This ability lowers the Attack of the opponent by " +
                                                              "one stage when the user comes into battle.");
                                break;
                            case "Overgrow":
                                makeBoxWithOk(s + " Ability", "This ability boosts the power of Grass type " +
                                                              "moves when the user's health is low.");
                                break;
                            case "Static":
                                makeBoxWithOk(s + " Ability", "Whenever an opponent's move makes contact with the " +
                                                              "Static user, the opponent has a 30% of getting paralyzed.");
                                break;
                            case "Torrent":
                                makeBoxWithOk(s + " Ability", "This ability boosts the power of Water type " +
                                                              "moves when the user's health is low.");
                                break;
                            case "Water Absorb":
                                makeBoxWithOk(s + " Ability", "Whenever the user is hit by a Water type move, the " +
                                                              "user heals 25% of its health instead of taking damage.");
                                break;
                            default:
                                throw new IllegalArgumentException(s + " is not an ability that is currently supported by the simulator!");
                        }
                        break;
                    case 10: case 11: case 12: case 13: //all the move slots act the same
                        Move move = new StatusMove(text);
                        if(!move.getName().isEmpty()) {
                            makeBoxWithOk(text, getMoveDescription(move));
                            break;
                        }
                        move = new PhysicalMove(text);
                        if(!move.getName().isEmpty()) {
                            makeBoxWithOk(text, getMoveDescription(move));
                            break;
                        }
                        move = new SpecialMove(text);
                        if(!move.getName().isEmpty()) {
                            makeBoxWithOk(text, getMoveDescription(move));
                            break;
                        }
                    break;
                }
        });
        //sets the back button to change screens correctly.
        backButton.setOnClickListener((v) -> {
            switch(pokemonName) {
                case "Bulbasaur": case "Chikorita": case "Treecko":
                    ma.displayView(2, "");
                    break;
                case "Charmander":
                    ma.displayView(1, "Bulbasaur");
                    break;
                case "Squirtle":
                    if(atEndOfTeam) {
                        ma.displayView(2, "");
                    }
                    else {
                        ma.displayView(1, "Charmander");
                    }
                    break;
                case "Cyndaquil":
                    ma.displayView(1, "Chikorita");
                    break;
                case "Totodile":
                    if(atEndOfTeam) {
                        ma.displayView(2, "");
                    }
                    else {
                        ma.displayView(1, "Cyndaquil");
                    }
                    break;
                case "Torchic":
                    ma.displayView(1, "Treecko");
                    break;
                case "Mudkip":
                    if(atEndOfTeam) {
                        ma.displayView(2, "");
                    }
                    else {
                        ma.displayView(1, "Torchic");
                    }
                    break;
                default:
                    throw new IllegalArgumentException(pokemonName + " cannot possibly be used at this point in the simulator!");
            }
        });
        //sets the forward button to change screens correctly
        forwardButton.setOnClickListener((v) -> {
            switch(pokemonName) {
                case "Bulbasaur":
                    ma.displayView(1, "Charmander");
                    break;
                case "Charmander":
                    ma.displayView(1, "Squirtle");
                    break;
                case "Squirtle":
                    if(atEndOfTeam) {
                        ma.displayView(3, "Bulbasaur");
                    }
                    else {
                        prepareForBattleStage();
                    }
                    break;
                case "Chikorita":
                    ma.displayView(1, "Cyndaquil");
                    break;
                case "Cyndaquil":
                    ma.displayView(1, "Totodile");
                    break;
                case "Totodile":
                    if(atEndOfTeam) {
                        ma.displayView(3, "Chikorita");
                    }
                    else {
                        prepareForBattleStage();
                    }
                    break;
                case "Treecko":
                    ma.displayView(1, "Torchic");
                    break;
                case "Torchic":
                    ma.displayView(1, "Mudkip");
                    break;
                case "Mudkip":
                    if(atEndOfTeam) {
                        ma.displayView(3, "Treecko");
                    }
                    else {
                        prepareForBattleStage();
                    }
                    break;
                default:
                    throw new IllegalArgumentException(pokemonName + " cannot possibly be used currently on this screen in the simulator!");
            }
        });
        return myView;
    }

    /**
     * A private helper method that creates a box with ok with a title and a message. This is here to
     * shorten the code above.
     * @param title The title of the box.
     * @param message The message inside the box.
     */
    private void makeBoxWithOk(String title, String message)  {
        new AlertDialog.Builder(getContext()) //this is how to create a box
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok",(dialog, id) -> {/*do nothing, just close the box*/})
                .setCancelable(false)
                .show();
    }

    /**
     * A helper method that displays the message after clicking next after the last Pokémon. It sets
     * atEndOfTeam to true as well.
     */
    private void prepareForBattleStage() {
        makeBoxWithOk("Last Pokémon!", "This is the last Pokémon on the team. Choose this team " +
                "if you wish by pressing Next again, or press Back to return to the team selection screen. ");
        atEndOfTeam = true;
    }

    /**
     * A helper method that produces a String that describes type weaknesses and strengths. This is
     * used mainly for description on this page (not in the battle stage).
     * @param type The type to be described.
     * @return A String that describes all the type's weaknesses and strengths.
     */
    private String getTypeDescription(String type) {
        String[] formatting = new String[] {" type moves are super-effective against ",
                                           " types but are ineffective or useless against ",
                                           " types. ", " types are weak to ",
                                           " type moves and are resistant or immune to ", " type moves."};
        switch (type) {
            case "Electric":
                return "Electric" + formatting[0] + "Water and Flying" +
                        formatting[1] + "Grass, Electric, Dragon, and Ground" + formatting[2] +
                        "Electric" + formatting[3] + "Ground" + formatting[4] +
                        "Electric, Flying, and Steel" + formatting[5];
            case "Fairy":
                return "Fairy" + formatting[0] + "Dragon, Dark, and Fighting" +
                        formatting[1] + "Fire, Steel, and Poison" + formatting[2] +
                        "Fairy" + formatting[3] + "Steel and Poison" + formatting[4] +
                        "Dragon, Dark, Fighting, and Bug" + formatting[5];
            case "Fire":
                return "Fire" + formatting[0] + "Steel, Grass, Bug, and Ice" +
                        formatting[1] + "Fire, Water, Dragon, and Rock" + formatting[2] +
                        "Fire" + formatting[3] + "Ground, Rock, and Water" + formatting[4] +
                        "Steel, Fairy, Grass, Bug, Ice, and Fire" + formatting[5];
            case "Grass":
                return  "Grass" + formatting[0] + "Water, Ground, and Rock" +
                        formatting[1] + "Grass, Fire, Dragon, Steel, Flying, Poison, and Bug" + formatting[2]
                        + "Grass" + formatting[3] + "Fire, Ice, Flying, Bug, and Poison" + formatting[4] +
                        "Ground, Water, Grass, and Electric" + formatting[5];
            case "Grass/Poison":
                return "Grass" + formatting[0] + "Water, Ground, and Rock" +
                        formatting[1] + "Grass, Fire, Dragon, Steel, Flying, Poison, and Bug" + formatting[2] +
                        "Poison" + formatting[0] + "Grass and Fairy" +
                        formatting[1] + "Ground, Poison, Rock, Ghost, and Steel" + formatting[2] +
                        "Grass/Poison" + formatting[3] + "Psychic, Fire, Ice, and Flying" + formatting[4] +
                        "Grass, Fairy, Water, Electric, and Fighting" + formatting[5];
            case "Water":
                return  "Water" + formatting[0] + "Fire, Ground, and Rock" +
                        formatting[1] + "Grass, Water, and Dragon" + formatting[2]
                        + "Water" + formatting[3] + "Electric and Grass" + formatting[4] +
                        "Steel, Fire, Ice, and Water" + formatting[5];
            case "Water/Ground":
                return  "Water" + formatting[0] + "Fire, Ground, and Rock" +
                        formatting[1] + "Grass, Water, and Dragon" + formatting[2] +
                        "Ground" + formatting[0] + "Fire, Rock, Steel, Electric, and Poison" +
                        formatting[1] + "Flying, Grass, and Bug" + formatting[2] +
                        "Water/Ground" + formatting[3] + "Grass" + formatting[4] +
                        "Steel, Electric, Fire, Poison, and Rock" + formatting[5];
            default:
                return "";
        }
    }

    /**
     * A helper method that generates a description of each move. It describes each move's basic feature;
     * and its exact implementation depends on the type of move.
     * @param move1 The move to be described.
     * @return A String that describes the basic features of the move.
     */
    private String getMoveDescription(Move move1) {
        if (move1.isAttackingMove()) {
            AttackingMove move = (AttackingMove) move1;
            return move.getName() + " is a " + move.getType() + " type move. Its PP is " +
                    move.getPP() + ", its BP is " + move.getBP() + ", and its accuracy is " +
                    move.getAccuracy() + "%. " + move.getAdditionalEffects();
        }
        StatusMove move = (StatusMove) move1;
        return move.getName() + " is a " + move.getType() +
                " type move. Its PP is " + move.getPP() + ". " + move.getEffects();
    }
}