import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

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
        public Runnable menu1 = new Runnable(){
            @Override
            public void run() {
                System.out.println("DEBUG: " + TAG + ": Render menu 1");
            }
        };
        public Runnable menu2 = new Runnable(){
            @Override
            public void run() {
                System.out.println("DEBUG: " + TAG + ": Render menu 2");
            }
        };
        public Runnable menu3 = new Runnable(){
            @Override
            public void run() {
                System.out.println("DEBUG: " + TAG + ": Render menu 3");
            }
        };
        public Runnable menu4 = new Runnable(){
            @Override
            public void run() {
                System.out.println("DEBUG: " + TAG + ": Render menu 4");
            }
        };
        public void bindMethods(){
            this.getStateByName(S_MENU1).actions = menu1;
            this.getStateByName(S_MENU2).actions = menu2;
            this.getStateByName(S_MENU3).actions = menu3;
            this.getStateByName(S_MENU4).actions = menu4;
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

            MenuStateMachine menu = new MenuStateMachine(states, S_MENU1, events, transitions);
            menu.bindMethods();
            return menu;
        }
    }
    @Before
    public void instantiateMenuStateMachine(){
        menu = new MenuStateMachine().init();
    }
    @Test
    public void test_class_estends_state_machine(){
        assertTrue(menu.start());

        assertTrue(menu.propagateEvent(click4));
        assertEquals(S_MENU4, menu.getCurrentStateName());

        assertTrue(menu.propagateEvent(click2));
        assertEquals(S_MENU2, menu.getCurrentStateName());

        assertTrue(menu.propagateEvent(click3));
        assertEquals(S_MENU3, menu.getCurrentStateName());

        assertFalse(menu.propagateEvent(click3));
        assertEquals(S_MENU3, menu.getCurrentStateName());

        assertTrue(menu.update());

        assertTrue(menu.stop());

        assertFalse(menu.propagateEvent(click3));

        assertTrue(menu.start());

        assertTrue(menu.propagateEvent(click1));
        assertEquals(S_MENU1, menu.getCurrentStateName());
    }

}
