package client;

import controller.GuiEngine;
import model.GameEngine;
import model.GameEngineImpl;
import view.ConsoleLoggerCallback;
import view.GuiCallBack;

/**
 * The type Gui card game.
 */
public class GuiCardGame {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)  {
        GameEngine gameEngine = new GameEngineImpl();
        GuiEngine guiEngine = new GuiEngine(gameEngine);
        gameEngine.registerCallback(new ConsoleLoggerCallback(gameEngine));
        gameEngine.registerCallback(new GuiCallBack(guiEngine));
        guiEngine.createMainWindow();
    }
}
