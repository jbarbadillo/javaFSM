import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class StateMachineIntegrationTest {
    private final static String TAG = "IntegrationTest";
	private StateMachine fsm;
    private final static String S_OUT_OF_ZONE_REMOVED = "OUT_OF_ZONE_REMOVED";
    private final static String S_APPENDING = "APPENDING";
    private final static String S_REMOVING = "REMOVING";
    private final static String S_IN_ZONE_APPENDED_BLOCK = "IN_ZONE_APPENDED_BLOCK";
    private final static String S_IN_ZONE_APPENDED = "IN_ZONE_APPENDED";
    private final static String S_OUT_OFF_ZONE_APPENDED_BLOCK = "OUT_OFF_ZONE_APPENDED_BLOCK";
    private final static String timeoutEvent = "timeout";
    private final static String deviceInZoneEvent = "device_in_zone";
    private final static String appendEvent = "append";
    private final static String removeEvent = "remove";
    private final static String noBeaconsEvent = "no_beacons";
    private final static String allDevicesOutOfZoneEvent = "all_devices_out_of_zone";
	
	// Runnable declarations
	public Runnable outOfZoneRemoved = new Runnable(){
        @Override
        public void run() {
            System.out.println("DEBUG: " + TAG + ": " + S_OUT_OF_ZONE_REMOVED);
        }
    };
    public Runnable appending = new Runnable(){
        @Override
        public void run() {
            System.out.println("DEBUG: " + TAG + ": " + S_APPENDING);
            fsm.propagateEvent(appendEvent);
        }
    };
    public Runnable removing = new Runnable(){
        @Override
        public void run() {
            System.out.println("DEBUG: " + TAG + ": " + S_REMOVING);
            fsm.propagateEvent(removeEvent);
        }
    };
    public Runnable inZoneAppendedBlock = new Runnable(){
        @Override
        public void run() {
            System.out.println("DEBUG: " + TAG + ": " + S_IN_ZONE_APPENDED_BLOCK);
            System.out.println("DEBUG: " + TAG + ": " + "Restart timer");
        }

    };
    public Runnable inZoneAppended = new Runnable(){
        @Override
        public void run() {
            System.out.println("DEBUG: " + TAG + ": " + S_IN_ZONE_APPENDED);
        }
    };
    public Runnable outOfZoneAppendedBlock = new Runnable(){
        @Override
        public void run() {
            System.out.println("DEBUG: " + TAG + ": " + S_OUT_OFF_ZONE_APPENDED_BLOCK);
        }
    };
	
	// Create FSM before running test.
    @Before
    public void prepareFSM(){
        List<String> states = new LinkedList<>(Arrays.asList(
                S_OUT_OF_ZONE_REMOVED,
                S_OUT_OFF_ZONE_APPENDED_BLOCK,
                S_APPENDING,
                S_REMOVING,
                S_IN_ZONE_APPENDED_BLOCK,
                S_IN_ZONE_APPENDED));
        List<String> events = new LinkedList<>(Arrays.asList(
                timeoutEvent,
                deviceInZoneEvent,
                appendEvent,
                removeEvent,
                noBeaconsEvent,
                allDevicesOutOfZoneEvent));
        List<List<String>> transitions = new LinkedList<>();
        transitions.add(new LinkedList<String>(Arrays.asList(S_OUT_OF_ZONE_REMOVED,deviceInZoneEvent,S_APPENDING)));

        transitions.add(new LinkedList<String>(Arrays.asList(S_APPENDING,appendEvent,S_IN_ZONE_APPENDED_BLOCK)));

        transitions.add(new LinkedList<String>(Arrays.asList(S_IN_ZONE_APPENDED_BLOCK,deviceInZoneEvent,S_IN_ZONE_APPENDED_BLOCK)));
        transitions.add(new LinkedList<String>(Arrays.asList(S_IN_ZONE_APPENDED_BLOCK,timeoutEvent,S_IN_ZONE_APPENDED)));
        transitions.add(new LinkedList<String>(Arrays.asList(S_IN_ZONE_APPENDED_BLOCK,noBeaconsEvent,S_OUT_OFF_ZONE_APPENDED_BLOCK)));
        transitions.add(new LinkedList<String>(Arrays.asList(S_IN_ZONE_APPENDED_BLOCK,allDevicesOutOfZoneEvent,S_OUT_OFF_ZONE_APPENDED_BLOCK)));

        transitions.add(new LinkedList<String>(Arrays.asList(S_IN_ZONE_APPENDED,noBeaconsEvent,S_REMOVING)));
        transitions.add(new LinkedList<String>(Arrays.asList(S_IN_ZONE_APPENDED,allDevicesOutOfZoneEvent,S_REMOVING)));
        transitions.add(new LinkedList<String>(Arrays.asList(S_IN_ZONE_APPENDED,deviceInZoneEvent,S_IN_ZONE_APPENDED_BLOCK)));

        transitions.add(new LinkedList<String>(Arrays.asList(S_OUT_OFF_ZONE_APPENDED_BLOCK,timeoutEvent,S_REMOVING)));
        transitions.add(new LinkedList<String>(Arrays.asList(S_OUT_OFF_ZONE_APPENDED_BLOCK,deviceInZoneEvent,S_IN_ZONE_APPENDED_BLOCK)));

        transitions.add(new LinkedList<String>(Arrays.asList(S_REMOVING,removeEvent,S_OUT_OF_ZONE_REMOVED)));

        fsm = new StateMachine(states, S_OUT_OF_ZONE_REMOVED,events,transitions);
    }

    public void bindFunctions(){
        fsm.getStateByName(S_OUT_OF_ZONE_REMOVED).actions = outOfZoneRemoved;
        fsm.getStateByName(S_OUT_OFF_ZONE_APPENDED_BLOCK).actions = outOfZoneAppendedBlock;
        fsm.getStateByName(S_APPENDING).actions = appending;
        fsm.getStateByName(S_REMOVING).actions = removing;
        fsm.getStateByName(S_IN_ZONE_APPENDED_BLOCK).actions = inZoneAppendedBlock;
        fsm.getStateByName(S_IN_ZONE_APPENDED).actions = inZoneAppended;
    }

    @Test
    public void integration_test_progpagates_as_expected(){
        bindFunctions();

        assertTrue(fsm.start());
        assertEquals(S_OUT_OF_ZONE_REMOVED, fsm.getCurrentStateName());

        assertTrue(fsm.propagateEvent(deviceInZoneEvent));
        assertEquals(S_IN_ZONE_APPENDED_BLOCK, fsm.getCurrentStateName());

        assertTrue(fsm.propagateEvent(deviceInZoneEvent));
        assertEquals(S_IN_ZONE_APPENDED_BLOCK, fsm.getCurrentStateName());

        assertTrue(fsm.propagateEvent(noBeaconsEvent));
        assertEquals(S_OUT_OFF_ZONE_APPENDED_BLOCK, fsm.getCurrentStateName());

        assertTrue(fsm.propagateEvent(deviceInZoneEvent));
        assertEquals(S_IN_ZONE_APPENDED_BLOCK, fsm.getCurrentStateName());

        assertTrue(fsm.propagateEvent(allDevicesOutOfZoneEvent));
        assertEquals(S_OUT_OFF_ZONE_APPENDED_BLOCK, fsm.getCurrentStateName());

        assertTrue(fsm.propagateEvent(timeoutEvent));
        assertEquals(S_OUT_OF_ZONE_REMOVED, fsm.getCurrentStateName());

        assertTrue(fsm.propagateEvent(deviceInZoneEvent));
        assertEquals(S_IN_ZONE_APPENDED_BLOCK, fsm.getCurrentStateName());

        assertTrue(fsm.propagateEvent(deviceInZoneEvent));
        assertEquals(S_IN_ZONE_APPENDED_BLOCK, fsm.getCurrentStateName());

        assertTrue(fsm.propagateEvent(timeoutEvent));
        assertEquals(S_IN_ZONE_APPENDED, fsm.getCurrentStateName());

        assertTrue(fsm.propagateEvent(deviceInZoneEvent));
        assertEquals(S_IN_ZONE_APPENDED_BLOCK, fsm.getCurrentStateName());
    }
}
