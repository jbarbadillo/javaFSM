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
```