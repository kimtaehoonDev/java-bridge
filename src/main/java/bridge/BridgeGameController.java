package bridge;

import bridge.domain.ActionAfterGameStatus;
import bridge.domain.Bridge;
import bridge.domain.BridgeGame;
import bridge.domain.FootrestLocation;
import bridge.domain.GameResultCode;
import bridge.utils.BridgeMaker;
import bridge.view.InputView;
import bridge.view.OutputView;

public class BridgeGameController {
    private BridgeGame game;
    private final BridgeMaker bridgeMaker;
    private final InputView inputView;
    private final OutputView outputView;

    private BridgeGameController() {
        this.game = new BridgeGame();
        this.bridgeMaker = new BridgeMaker(new BridgeRandomNumberGenerator());
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    public static BridgeGameController init() {
        return new BridgeGameController();
    }

    public void run() {
        inputView.showStartGameMessage();
        Bridge bridge = this.makeBridge();
        game.saveBridge(bridge);

        play();
        this.outputView.printResult(game.getResult());
    }

    private void play() {
        GameResultCode movingResult = moveUser();
        if (movingResult == GameResultCode.FAIL) { // 게임에 실패한 경우
            determineWhatToDo();
        }
    }

    private void replay() {
        game.retry();
        play();
    }

    private void determineWhatToDo() {
        try {
            ActionAfterGameStatus code = ActionAfterGameStatus.findByUserInput(this.inputView.readGameCommand());
            if (code == ActionAfterGameStatus.RESTART) {
                replay();
            }
        } catch (IllegalArgumentException e) {
            outputView.showErrorMessage(e);
            determineWhatToDo();
        }
    }

    private GameResultCode moveUser() { // TODO 코드 분리하기...
        try {
            String command = inputView.readMoving();
            FootrestLocation footrestLocation = FootrestLocation.findByUserInput(command);
            GameResultCode gameResult = game.move(footrestLocation);

            outputView.printMap(game.getFootPrint());
            if (gameResult == GameResultCode.MOVE_SUCCESS) {
                return moveUser();
            }
            return gameResult;
        } catch (IllegalArgumentException e) {
            outputView.showErrorMessage(e);
            return moveUser();
        }
    }

    private Bridge makeBridge() {
        try {
            Integer bridgeSize = inputView.readBridgeSize();
            return new Bridge(bridgeMaker.makeBridge(bridgeSize));
        } catch (IllegalArgumentException e) {
            outputView.showErrorMessage(e);
            return makeBridge();
        }
    }
}
