import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class implements an event driven finite state machine. The state machine must be initialized with the list of states, events
 * accepted, an initial state and a list of valid transitions. Valid transitions mean transitions between registered states and events. 
 * Also for a given state events cannot be duplicated as transitions must be unique. It is possible to have several events that triger the same
 * transition, for example: START->press_stop->STOP and START->timeout->STOP.
 *
 * The constructor will check itself that transitions are valid. The start method checks that the FSM is complete: every state has at least
 * one transition and has a runnable binded.
 * 
 * If the FSM is created correctly, the start() method must be called in order to be able to propagate events. Actions will execute automatically.
 * 
 */
public class StateMachine {
    private  final static String TAG = "StateMachine";
	protected State mCurrentState = null;
	private List<State> mStates;
	private List<String> mEvents;
    boolean started = false;
    State mInitialState = null;

	public class State{
		String name;
		Map<String, State> transitionMap;
		Runnable actions = null;
		
		State(final String name){
			this.name = name;
			this.transitionMap = new HashMap<>();
		}
	}
	/**
	 * Constructor for state machine.
	 *
	 * @param states		A list with the names of the states
	 * @param initialState	The name of the initial state. Must be on the list.
	 * @param events		A list with the name of the events.
	 * @param transitions 	A list of valid transitions for all states. Every transition is a list itself.
	 */
	StateMachine(List<String> states, final String initialState, List<String> events, List<List<String>> transitions){
		mStates = new LinkedList<>();
        mEvents = new LinkedList<>();
		for(String state: states){
            mStates.add(new State(state));
		}
		mEvents = events;
		this.setInitialState(initialState);
		for(List<String> transition : transitions){
			if(!addTransition(transition.get(0), transition.get(1), transition.get(2))){
                System.out.println("DEBUG: " + TAG + ": " + "Cannot add transition: "+transition.get(0)+"+"+transition.get(1)+"->"+transition.get(2));
            }
		}
	}
	/**	 
	 * Propagates an event if FSM is started.
	 *
	 * @param event		The event that will propagate in the FSM
	 * @return  		True if trigered a transition.
	 */
	public boolean propagateEvent(final String event){
		if(!this.started){
            return false;
        }
        if(mCurrentState.transitionMap.containsKey(event)){
            mCurrentState = mCurrentState.transitionMap.get(event);
            mCurrentState.actions.run();
            return true;
        }
        return false;
	}
	/**	 
	 * Gets a State object by name.
	 *
	 * @param name		The name of the State
	 * @return  		The state if exists. Null if not.
	 */
    public State getStateByName(final String name){
		for(State state : mStates){
            if(state.name.matches(name)){
                return state;
            }
        }
        return null;
	}
	/**	 
	 * Gets the name of the current State.
	 *
	 * @return	The name of the current state if it is not null;
	 */
	public String getCurrentStateName(){
		if(mCurrentState != null){
            return mCurrentState.name;
        }else{
            return "";
        }		
	}
	/**	 
	 * Starts the FSM
	 *
	 * @return	True if was able to start.
	 */
	public boolean start(){
		if(mInitialState != null && checkValidStates() ){
            if(mCurrentState == null){
                mCurrentState = mInitialState;
            }
            mCurrentState.actions.run();
            started = true;
            return started;
        }
        return false;
	}
	/**	 
	 * Updates the FSM by running actions
	 *
	 * @return	True if was able to update.
	 */
	public boolean update(){
		if(mCurrentState != null && started){
            mCurrentState.actions.run();
            return true;
        }
        return false;
	}
	/**	 
	 * Stops the FSM it was started
	 *
	 * @return	True if was able to stop.
	 */
	public boolean stop(){
		if(started){
            started = false;
            return true;
        }
        return false;
	}	
	/**	 
	 * Returns the index of the state in the state list.
	 *
	 * @param 	name	The name of the state
	 * @return			The index of the state in the list. Returns -1 if the state is not in the FSM.
	 */
    protected int getStateIndex(final String name){
		for(int i = 0; i < mStates.size(); i++){
            if(mStates.get(i).name.matches(name)){
                return i;
            }
        }
        return -1;
	}
	/**	 
	 * Checks wether an event exists in the FSM
	 *
	 * @param 	name	The name of the event
	 * @return			True if the event already exists
	 */
    protected boolean existsEvent(final String name){
		for(String event : mEvents){
            if(event.matches(name)){
                return true;
            }
        }
        return false;
	}
	/**	 
	 * Checks that states are correct. All have at least a transition and an action.
	 *
	 * @return	True if states are valid for a FSM
	 */
    protected boolean checkValidStates(){
		for(State state : mStates){
            boolean invalid = (state.transitionMap.isEmpty() || state.actions==null);
            if(invalid){
                System.out.println("DEBUG: " + TAG + ": " + "Invalid state "+ state.name);
                return false;
            }
        }
        return true;
	} 
	/**	 
	 * Sets the initialState of the FSM
	 *
	 * @param 	name	The name of the initialState
	 * @return			True if the state exists and was correctly set
	 */
	private boolean setInitialState(final String name){
		if(getStateByName(name) != null){
            this.mInitialState = getStateByName(name);
            return true;
        }
        return false;
	}
	/**	 
	 * Adds a transition to the source state.
	 *
	 * @param 	source	The name of the source state
	 * @param 	event	The name of the event
	 * @param 	target	The name of the target state
	 * @return			True if transition added correctly
	 */
	private boolean addTransition(final String  source, final String event, final String target){
		State sourceState = getStateByName(source);
        State targetState = getStateByName(target);

        boolean existsSource = sourceState != null;
        boolean existsTarget = targetState != null;

        if(existsSource && existsTarget && existsEvent(event)){
            if(sourceState.transitionMap.containsKey(event)){
                return false;
            }else{
                sourceState.transitionMap.put(event,targetState);
                return true;
            }
        }else{
            return false;
        }
	}
	
}