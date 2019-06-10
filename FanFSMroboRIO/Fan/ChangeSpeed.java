package Fan;

import org.jeasy.states.api.TransitionHandler;

class ChangeSpeed implements TransitionHandler {

    public void handleTransition() throws Exception {
        System.out.println("Changing Speed");
    }

}
