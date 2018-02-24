import java.util.List;

public class IntegrationTestWithExtends {
    private final static String TAG = "IntegrationTestWithExtends";
    private StateMachine fsm;
    private final static String S_MENU1 = "MENU1";
    private final static String S_MENU2 = "MENU2";
    private final static String S_MENU3 = "MENU3";
    private final static String S_MENU4 = "MENU4";

    private final static String click1 = "click1";
    private final static String click2 = "click2";
    private final static String click3 = "click3";
    private final static String click4 = "click4";

    public class MenuStateMachine extends StateMachine{

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
    }
}
