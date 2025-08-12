package lukes.pokemonapp;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;

/**
 * A fragment for displaying Pokemon data and moves. It contains an image and stats about the pokemon
 * that are displayed in a list view. You can also toggle between different Pokemon using the buttons.
 * This class is likely complete, except for the minor details.
 * @author Luke Schoeberle 7/16/2016.
 */
public class PokemonFragment extends Fragment { //Fragment code 1

    /**
     * The view that this Fragment has. From what I can tell, this essentially represents the overall
     * screen (the xml layout file) that is initialized in the onCreateView method.
     */
    private View myView;

    /**
     * The text box for the Pokémon's name. This is set to the String provided in the constructor.
     */
    private TextView pokeName;

    /**
     * An image of the Pokémon. This is set to an image found in res/drawable based on the Pokémon's name.
     */
    private ImageView pokePic;

    /**
     * The displayed list of Pokémon stats. This shows the moves and stats of the Pokémon named in the constructor.
     */
    private ListView pokeInfo;

    /**
     * The Pokémon provided in the constructor. This Pokémon is displayed on this Fragment.
     */
    private Pokemon poke;

    /**
     * The button used to move to the next Pokémon. If you are already on the last Pokémon, this
     * shows a message about choosing this team for sure.
     */
    private Button forwardButton;

    /**
     * The button for moving back to the last Pokémon. If you are on the first Pokémon, this goes
     * back to the team selection screen.
     */
    private Button backButton;

    /**
     * This Fragment's main activity. This is essentially just a shortcut so I don't have to say
     * (MainActivity) getActivity() for changing fragments every time.
     */
    private MainActivity ma;

    /**
     * Determines whether or not you are at the end of a team. This is necessary to know when to
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
     * Currently, most of these variables could simply be local variables because they are only used
     * in this method, but I am leaving them as instance variables for later uses.
     * @param inflater Creates this view from the layout file.
     * @param container Puts this view into the specified container.
     * @param savedInstance Not used in this case.
     * @return The view created by the inflater.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        //initializes variables
        myView = inflater.inflate(R.layout.pokemon_screen, container, false);
        pokeName = myView.findViewById(R.id.pokemonName);
        pokePic = myView.findViewById(R.id.pokemonPic);
        forwardButton = myView.findViewById(R.id.forwardButton);
        backButton = myView.findViewById(R.id.backButton);

        ma = (MainActivity) getActivity();

        poke = new Pokemon((String) getArguments().getSerializable("key"));

        atEndOfTeam = false;

        //sets the text to the pokemon's name.
        pokeName.setText(poke.getName());

        //initializes the image
        switch(poke.getName()) {
            case "Bulbasaur":
                pokePic.setImageResource(R.drawable.bulbasaur); break;
            case "Charmander":
                pokePic.setImageResource(R.drawable.charmander); break;
            case "Squirtle":
                pokePic.setImageResource(R.drawable.squirtle); break;
            case "Chikorita":
                pokePic.setImageResource(R.drawable.chikorita); break;
            case "Cyndaquil":
                pokePic.setImageResource(R.drawable.cyndaquil); break;
            case "Totodile":
                pokePic.setImageResource(R.drawable.totodile); break;
            case "Wooper":
                pokePic.setImageResource(R.drawable.wooper); break;
            case "Voltorb":
                pokePic.setImageResource(R.drawable.voltorb); break;
            case "Snubbull":
                pokePic.setImageResource(R.drawable.snubbull); break;
            //more cases will follow in the future //TODO
        }
        //initializes the list array
        String[] listPokeStats = {"TYPE:          " + poke.getType(), "ABILITY:     " + poke.getAbility(),
                                  "LEVEL:         " + poke.getLevel(), "HP:              " + poke.getInitStats()[0],
                                  "ATTACK:     " + poke.getInitStats()[1], "DEFENSE:   " + poke.getInitStats()[2],
                                  "SP. ATK:      " + poke.getInitStats()[3], "SP. DEF:      " + poke.getInitStats()[4],
                                  "SPEED:       " + poke.getInitStats()[5],  "MOVES:", poke.getMoves().get(0).toString(),
                                  poke.getMoves().get(1).toString(), poke.getMoves().get(2).toString(),
                                  poke.getMoves().get(3).toString()};

        //creates the adapter for putting the list array into the list view
        ArrayAdapter<String> adapter = new ArrayAdapter<>(myView.getContext(), R.layout.text_for_stats_listview, listPokeStats);
        pokeInfo = myView.findViewById(R.id.statsListView);
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
                                makeBoxWithOk(s + " Ability", "This ability lowers the Attack of the opponent by" +
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
                            //more cases later //TODO
                        }
                        break;
                    case 10: case 11: case 12: case 13: //all the move slots act the same
                        Move move = new StatusMove(text);
                        if(!move.getName().equals("")) {
                            makeBoxWithOk(text, getMoveDescription(move));
                            break;
                        }
                        move = new PhysicalMove(text);
                        if(!move.getName().equals("")) {
                            makeBoxWithOk(text, getMoveDescription(move));
                            break;
                        }
                         move = new SpecialMove(text);
                        if(!move.getName().equals("")) {
                            makeBoxWithOk(text, getMoveDescription(move));
                            break;
                        }
                    break;
                }
        });
        //sets the back button to change screens correctly.
        backButton.setOnClickListener((v) -> {
                if (poke.getName().equals("Bulbasaur"))
                    ma.displayView(2, "");
                else if (poke.getName().equals("Charmander"))
                    ma.displayView(1, "Bulbasaur");
                else if (poke.getName().equals("Squirtle") && !atEndOfTeam)
                    ma.displayView(1, "Charmander");
                else if (poke.getName().equals("Squirtle") && atEndOfTeam)
                    ma.displayView(2, "");
                else if(poke.getName().equals("Chikorita"))
                    ma.displayView(2, "");
                else if(poke.getName().equals("Cyndaquil"))
                    ma.displayView(1,"Chikorita");
                else if (poke.getName().equals("Totodile") && !atEndOfTeam)
                    ma.displayView(1, "Cyndaquil");
                else if (poke.getName().equals("Totodile") && atEndOfTeam)
                    ma.displayView(2, "");
        });
        //sets the forward button to change screens correctly
        forwardButton.setOnClickListener((v) -> {
                if (poke.getName().equals("Bulbasaur"))
                    ma.displayView(1, "Charmander");
                else if (poke.getName().equals("Charmander"))
                    ma.displayView(1, "Squirtle");
                else if (poke.getName().equals("Squirtle") && !atEndOfTeam)
                    prepareForBattleStage();
                else if (poke.getName().equals("Squirtle") && atEndOfTeam)
                    ma.displayView(3, "Bulbasaur");
                else if(poke.getName().equals("Chikorita"))
                    ma.displayView(1, "Cyndaquil");
                else if(poke.getName().equals("Cyndaquil"))
                    ma.displayView(1, "Totodile");
                else if (poke.getName().equals("Totodile") && !atEndOfTeam)
                    prepareForBattleStage();
                else if (poke.getName().equals("Totodile") && atEndOfTeam)
                    ma.displayView(3, "Chikorita");
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
        makeBoxWithOk("Last Pok" + '\u00E9' + "mon!", "This is the last Pok" + '\u00E9' + "mon on the team. Choose this team " +
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
            //more cases later, using the same format //TODO
            default:
                return "";
        }
    }

    /**
     * A helper method that generates a description of each move. It describes all of the basic features
     * of each move, and its exact implementation depends on the type of move.
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

