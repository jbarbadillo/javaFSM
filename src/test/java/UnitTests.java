import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class StateMachineTest {
	private StateMachine fsm;
    private final static String S_INITIALIZED = "INITIALIZED";
    private final static String S_STARTED = "STARTED";
    private final static String S_STOPPED = "STOPPED";
    private final static String nextEvent = "next";
	// Runnable declarations for later binding
	public Runnable stateInit = new Runnable(){
        @Override
        public void run() {
            System.out.println("DEBUG: " + TAG + ": " + S_INITIALIZED);
        }

    };
    public Runnable stateStart = new Runnable(){
        @Override
        public void run() {
            System.out.println("DEBUG: " + TAG + ": " + S_STARTED);
        }

    };
    public Runnable stateStop = new Runnable(){
        @Override
        public void run() {
            System.out.println("DEBUG: " + TAG + ": " + S_STOPPED);
        }
    };
	/**
	 * Binds functions to states
	 */
	public void bindFunctions(){
        fsm.getStateByName(S_INITIALIZED).actions = stateInit;
        fsm.getStateByName(S_STARTED).actions = stateStart;
        fsm.getStateByName(S_STOPPED).actions = stateStop;
    }
	@Before
    public void prepareFSM(){
        List<String> states = new LinkedList<>(Arrays.asList(S_INITIALIZED,S_STARTED,S_STOPPED));
        List<String> events = new LinkedList<>(Arrays.asList(nextEvent));
        List<List<String>> transitions = new LinkedList<>();
        transitions.add(new LinkedList<String>(Arrays.asList(S_INITIALIZED,nextEvent,S_STARTED)));
        transitions.add(new LinkedList<String>(Arrays.asList(S_STARTED,nextEvent,S_STOPPED)));
        transitions.add(new LinkedList<String>(Arrays.asList(S_STOPPED,nextEvent,S_INITIALIZED)));

        fsm = new StateMachine(states, S_INITIALIZED,events,transitions);
    }
	// Unit tests
	@Test
    public void create_statemachine_with_no_functions_returns_false_on_start(){
        boolean started = fsm.start();
        assertFalse(started);
    }
    @Test
    public void create_statemachine_with_functions_returns_true_on_start(){
        bindFunctions();

        boolean started = fsm.start();
        assertTrue(started);
    }
}