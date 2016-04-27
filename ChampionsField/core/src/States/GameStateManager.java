package States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by Evenilink on 27/04/2016.
 */
public class GameStateManager {
    private Stack<State> states;

    public GameStateManager() {
        this.states = new Stack<State>();
    }

    /*
    * Pushed the new state
    * */
    public void push(State state) {
        states.push(state);
    }

    /*
    * Pop the state at the top of the stack
    * */
    public void pop() {
        states.pop();
    }

    /*
    * Pops the at the top of the stack, and add the new 'state'
    * */
    public void set(State state) {
        states.pop().dispose();
        states.push(state);
    }

    /*
    * Updates the state at the top of the stack
    * */
    public void update(float dt) {
        states.peek().update(dt);
    }

    /*
    * Renders the state at the top of the stack
    * */
    public void render(SpriteBatch sb) {
        states.peek().render(sb);
    }
}
