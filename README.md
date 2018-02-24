# javaFSM

A simple event driven FSM for Java applications.

# Presentation

This project is a Java version of a simple FSM class. You may implement this class to take 
advantage of a state machine that is able to automatically propagate events and run fucntions
in every state.

# Description

This repository implements an event driven finite state machine. The state machine must be initialized with the list of states, events
accepted, an initial state and a list of valid transitions. Valid transitions mean transitions between registered states and events. 
Also for a given state events cannot be duplicated as transitions must be unique. It is possible to have several events that triger the same
transition, for example: START->press_stop->STOP and START->timeout->STOP.

The constructor will check itself that transitions are valid. The start method checks that the FSM is complete: every state has at least
one transition and has a runnable binded.

If the FSM is created correctly, the start() method must be called in order to be able to propagate events. Actions will execute automatically.

# Use case

> Example 1: Create an FSM and propagate events.

You can create a FSM, bind functions and propagate events to see how functions are executed.
```java
public class MyApp {
	private StateMachine fsm;
	private final static String S_ONE = "ONE";
	private final static String S_TWO = "TWO";
	private final static String change = "change";
	
	// Runnable declarations
	public Runnable one = new Runnable(){
		@Override
		public void run() {
			System.out.println("DEBUG: " + TAG + ": " + S_ONE);
		}
	};
	public Runnable two = new Runnable(){
		@Override
		public void run() {
			System.out.println("DEBUG: " + TAG + ": " + S_TWO);
		}
	};
	private startFSM(){
		List<String> states = new LinkedList<>(Arrays.asList(
				S_ONE,
				S_TWO));
		List<String> events = new LinkedList<>(Arrays.asList(
				change));
		List<List<String>> transitions = new LinkedList<>();
		transitions.add(new LinkedList<String>(Arrays.asList(S_ONE,change,S_TWO)));
		transitions.add(new LinkedList<String>(Arrays.asList(S_TWO,change,S_ONE)));
		
		fsm = new StateMachine(states, S_ONE,events,transitions);
		
		fsm.getStateByName(S_ONE).actions = one;
		fsm.getStateByName(S_TWO).actions = two;
		
		// Firs start your state machine
		fsm.start();
		
		// Propagate events as you want.
		fsm.propagateEvent(change);
		fsm.propagateEvent(change);
		fsm.propagateEvent(change);
		fsm.propagateEvent(change);
		fsm.propagateEvent(change);
	}
}
```

> Example 2: Create a class that extends StateMachine.

You can create a class that extends from StateMachine

```java
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
```