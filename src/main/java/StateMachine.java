public class StateMachine {
	public class State{
		String name;
		Map<String, State> transitionMap;
		Runnable function = null;
		
		State(final String name){
			this.name = name;
			transitionMap = new HashMap<>();
		}
	};
	StateMachine();
}