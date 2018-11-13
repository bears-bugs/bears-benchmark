package client.presenter.controller.util;

import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.ViewMessageType;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.LoginResponseMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.MenuMessageResponse;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.MovePieceResponse;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.RegisterResponseMessage;
import client.presenter.controller.messages.UnregisterMessage;
import client.presenter.controller.messages.UnregisterResponseMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import client.presenter.controller.messages.ViewValidMovesResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ViewMessageFactory {

  private static ViewMessageFactory ourInstance = new ViewMessageFactory();

  public static ViewMessageFactory getInstance() {
    return ourInstance;
  }

  private ViewMessageFactory() {
  }

  /**
   * View Message creator. The info[] must be in the same order as the constructor for the message
   * The exception to this is Menu messages. The first index should be the string value of the
   * MenuMessageType enum
   *
   * @param type the type of message to create
   * @param info the info to construct the message
   * @return the constructed message
   * @throws NoSuchAlgorithmException could not find SHA1 Hash
   */
  public ViewMessage createViewMessage(ViewMessageType type, String[] info)
      throws NoSuchAlgorithmException {
    switch (type) {
      case REGISTER:
        return new RegisterMessage(info[0], info[1], info[2]);
      case LOGIN:
        return new LoginMessage(info[0], info[1]);
      case UNREGISTER:
        return new UnregisterMessage(info[0], info[1], info[2]);
      case SHOW_VALID_MOVES:
        return new ViewValidMoves(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
      case MENU:
        return new MenuMessage(MenuMessageTypes.valueOf(info[0]),
            Arrays.copyOfRange(info, 1, info.length));
      case MOVE_PIECE:
        return new MovePieceMessage(Integer.parseInt(info[0]), Integer.parseInt(info[1]),
            Integer.parseInt(info[2]), Integer.parseInt(info[3]));
      case MOVE_PIECE_RESPONSE:
        boolean success = true;
        if (info[0].equals("false"))
          success = false;
        return new MovePieceResponse(success, info[1]);
      default:
        throw new IllegalArgumentException("The messageType of " + type.name() + " is not valid");
    }
  }

  /**
   * View Message Response creator. The info[] must be in the same order as the constructor for the
   * message. The exception to this is Menu Response messages. The first index should be the string
   * value of the MenuMessageType enum
   *
   *
   * @param type the type of response message to create
   * @param info the info to construct the message
   * @return the constructed message
   */
  public ViewMessage createViewMessageFromServer(ViewMessageType type, String[] info) {
    switch(type) {
      case REGISTER_RESPONSE:
        return new RegisterResponseMessage(Boolean.parseBoolean(info[0]),
            Arrays.copyOfRange(info, 1, info.length));
      case LOGIN_RESPONSE:
        return new LoginResponseMessage(Boolean.parseBoolean(info[0]), info[1]);
      case UNREGISTER_RESPONSE:
        return new UnregisterResponseMessage(Boolean.parseBoolean(info[0]),
            Arrays.copyOfRange(info, 1, info.length));
      case SHOW_VALID_MOVES_RESPONSE:
        return new ViewValidMovesResponse(info);
      case MENU_RESPONSE:
        return new MenuMessageResponse(MenuMessageTypes.valueOf(info[0]),
            Arrays.copyOfRange(info, 1, info.length));
      default:
        throw new IllegalArgumentException("The messageType of " + type.name() + " is not valid");
    }
  }
}
