import org.junit.Before;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class IntegrationTestWithExtends {
    private final static String TAG = "IntegrationTestWithExtends";
    private MenuStateMachine menu;
    private final static String S_MENU1 = "MENU1";
    private final static String S_MENU2 = "MENU2";
    private final static String S_MENU3 = "MENU3";
    private final static String S_MENU4 = "MENU4";

    private final static String click1 = "click1";
    private final static String click2 = "click2";
    private final static String click3 = "click3";
    private final static String click4 = "click4";

    public class MenuStateMachine extends StateMachine{
        MenuStateMachine(){


        }
        /**
         * Constructor for state machine.
         *
         * @param states       A list with the names of the states
         * @param initialState The name of the initial state. Must be on the list.
         * @param events       A list with the name of the events.
         * @param transitions  A list of valid transitions for all states. Every transition is a list itself.
         */
        MenuStateMachine(List<String> states, String initialState, List<String> events, List<List<String>> transitions) {
            super(states, initialState, events, transitions);
        }
        public MenuStateMachine init(){
            List<String> states = new LinkedList<>(Arrays.asList(
                    S_MENU1, S_MENU2, S_MENU3, S_MENU4));
            List<String> events = new LinkedList<>(Arrays.asList(
                    click1, click2, click3, click4
            ));
            List<List<String>> transitions = new LinkedList<>();
            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU1,click2,S_MENU2)));
            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU1,click3,S_MENU3)));
            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU1,click4,S_MENU4)));

            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU2,click1,S_MENU1)));
            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU2,click3,S_MENU3)));
            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU2,click4,S_MENU4)));

            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU3,click1,S_MENU1)));
            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU3,click2,S_MENU2)));
            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU3,click4,S_MENU4)));

            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU4,click2,S_MENU2)));
            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU4,click3,S_MENU3)));
            transitions.add(new LinkedList<String>(Arrays.asList(S_MENU4,click1,S_MENU1)));

            return new MenuStateMachine(states, S_MENU1, events, transitions);
        }
    }
    @Before
    public void instantiateMenuStateMachine(){
        menu = new MenuStateMachine().init();
    }
    
}
