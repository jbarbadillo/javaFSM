public class StateMachine {
	protected State mCurrentState = null;
	private List<State> mStates;
	private List<String> mEvents;
    boolean started = false;
    State mInitialState = null;
	
	public class State{
		String name;
		Map<String, State> transitionMap;
		Runnable function = null;
		
		State(final String name){
			this.name = name;
			transitionMap = new HashMap<>();
		}
	};
	StateMachine(List<String> states, final String initialState, List<String> events, List<List<String>> transitions){
		for(State state: states){
			states.put(new State(state));
		}
		mEvents = events;
		setInitialState(initialState);
		for(List<String> transition : transitions){
			addTransition(transition.get(0),transition.get(1),transition.get(2));
		}
	}
	public boolean propagateEvent(final String event){
		
	}
    public State getStateByName(final String name){
		
	}
	public String getCurrentState(){
		
	}
	boolean startFSM(){}
	boolean updateFSM(){}
	boolean stopFSM(){}
	
	
    protected int getStateIndex(final String name){}
    protected boolean existsEvent(final String event){}
    protected boolean checkValidStates(){} 
	
	bool setInitialState(const std::string& name);
	bool addTransition(const std::string&  source, const std::string& event, const std::string& target);
	
}