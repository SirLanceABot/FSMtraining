package Turnstile;

import org.jeasy.states.api.TransitionHandler;

class CoinReturn implements TransitionHandler {

    public void handleTransition() throws Exception {
        System.out.println("Returning Coin");
    }

}
