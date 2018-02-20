public class StateMachine {
	protected State mCurrentState = null;
	
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
}