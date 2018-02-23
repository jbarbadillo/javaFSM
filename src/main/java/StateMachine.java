import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StateMachine {
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
	};
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
		for(State state: states){
			states.add(new State(state));
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
	public String getCurrentStateName(){
		if(mCurrentState != null){
            return mCurrentState.name;
        }else{
            return "";
        }		
	}
	public boolean startFSM(){
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
	public boolean updateFSM(){
		if(mCurrentState != null && started){
            mCurrentState.actions.run();
            return true;
        }
        return false;
	}
	public boolean stopFSM(){
		if(started){
            started = false;
            return true;
        }
        return false;
	}	
	
    protected int getStateIndex(final String name){
		for(int i = 0; i < mStates.size(); i++){
            if(mStates.get(i).name.matches(name)){
                return i;
            }
        }
        return -1;
	}
    protected boolean existsEvent(final String event){
		for(String event : mEvents){
            if(event.matches(name)){
                return true;
            }
        }
        return false;
	}
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
	
	private boolean setInitialState(const std::string& name){
		if(getStateByName(name) != null){
            this.mInitialState = getStateByName(name);
            return true;
        }
        return false;
	}
	private boolean addTransition(const std::string&  source, const std::string& event, const std::string& target){
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