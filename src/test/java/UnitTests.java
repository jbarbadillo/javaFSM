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
	@Test
    public void propagate_wrong_event_returns_false(){
        bindFunctions();

        fsm.start();
        assertEquals(S_INITED,fsm.getCurrentStateName());
        boolean changedState = fsm.propagateEvent("back");
        assertEquals(S_INITED,fsm.getCurrentStateName());
        assertFalse(changedState);
    }
    @Test
    public void propagate_right_event_returns_true(){
        bindFunctions();

        fsm.start();
        assertEquals(S_INITED,fsm.getCurrentStateName());
        boolean changedState = fsm.propagateEvent(nextEvent);
        assertEquals(S_STARTED,fsm.getCurrentStateName());
        assertTrue(changedState);
    }
	@Test
    public void update_statemachine_before_start_returns_false(){
        bindFunctions();
        assertFalse(fsm.update());
    }
    @Test
    public void update_statemachine_after_start_returns_true(){
        bindFunctions();
        fsm.start();
        assertTrue(fsm.update());
    }
    @Test
    public void stop_statemachine_before_start_returns_false(){
        bindFunctions();
        assertFalse(fsm.stop());
    }
    @Test
    public void stop_statemachine_after_start_returns_true(){
        bindFunctions();
        fsm.start();
        assertTrue(fsm.update());
    }
	@Test
    public void get_non_existing_state_returns_null(){
        assertNull(fsm.getStateByName("NULL"));

    }
    @Test
    public void get_state_returns_object_of_type_state(){
        assertTrue(fsm.getStateByName(S_STARTED) instanceof StateMachine.State);
    }
    @Test
    public void get_state_index_returns_right_order(){
        assertEquals(0, fsm.getStateIndex(S_INITED));
        assertEquals(1, fsm.getStateIndex(S_STARTED));
        assertEquals(2, fsm.getStateIndex(S_STOPPED));
        assertEquals(-1, fsm.getStateIndex("NULL"));
    }
}