package client.presenter.controller.util;

import static client.presenter.SharedTestAttributes.*;
import static org.junit.jupiter.api.Assertions.*;

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
import javax.print.DocFlavor.STRING;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ViewMessageFactoryTest {

  private final ViewMessageFactory factory = ViewMessageFactory.getInstance();

  @Test
  void createViewMessageRegister() throws NoSuchAlgorithmException {

    RegisterMessage expected =
        new RegisterMessage(TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD, TEST_NICKNAME);

    String[] info = {TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD, TEST_NICKNAME};
    testMessageEquals(expected, info, ViewMessageType.REGISTER);

  }

  @Test
  void createRegisterResponse() {
    String[] messages = {"Successfully Registered User"};
    RegisterResponseMessage expected = new RegisterResponseMessage(TEST_SUCCESS, messages);

    String[] info = {Boolean.toString(TEST_SUCCESS), messages[0]};
    testResponseEquals(expected, info, ViewMessageType.REGISTER_RESPONSE);

  }

  @Test
  void createViewMessageUnregister() throws NoSuchAlgorithmException {

    UnregisterMessage expected =
        new UnregisterMessage(TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD, TEST_NICKNAME);

    String[] info = {TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD, TEST_NICKNAME};
    testMessageEquals(expected, info, ViewMessageType.UNREGISTER);
  }

  @Test
  void createUnregisterResponse() {
    String[] messages = {"Successfully Unregistered User"};
    UnregisterResponseMessage expected = new UnregisterResponseMessage(TEST_SUCCESS, messages);

    String[] info = {Boolean.toString(TEST_SUCCESS), messages[0]};
    testResponseEquals(expected, info, ViewMessageType.UNREGISTER_RESPONSE);

  }

  @Test
  void createViewMessageLogin() throws NoSuchAlgorithmException {

    LoginMessage expected =
        new LoginMessage(TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD);

    String[] info = {TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD};
    testMessageEquals(expected, info, ViewMessageType.LOGIN);
  }

  @Test
  void createLoginResponse() {

    LoginResponseMessage expected = new LoginResponseMessage(TEST_SUCCESS, TEST_NICKNAME);

    String[] info = {Boolean.toString(TEST_SUCCESS), TEST_NICKNAME};
    testResponseEquals(expected, info, ViewMessageType.LOGIN_RESPONSE);
  }

  @Test
  void createViewMessageViewMoves() throws NoSuchAlgorithmException {

    ViewValidMoves expected = new ViewValidMoves(FROM_COL, FROM_ROW);

    String[] info = {Integer.toString(FROM_COL), Integer.toString(FROM_ROW)};
    testMessageEquals(expected, info, ViewMessageType.SHOW_VALID_MOVES);
  }

  @Test
  void createViewValidMovesResponse() {
    String[] validMoves = {"AACD"};
    ViewValidMovesResponse expected = new ViewValidMovesResponse(validMoves);

    testResponseEquals(expected, validMoves, ViewMessageType.SHOW_VALID_MOVES_RESPONSE);

  }

  @Test
  void createViewMessageMovePiece() throws NoSuchAlgorithmException {

    MovePieceMessage expected = new MovePieceMessage(FROM_COL, FROM_ROW, TO_COL, TO_COL);

    String[] info = {Integer.toString(FROM_COL), Integer.toString(FROM_ROW),
        Integer.toString(TO_COL), Integer.toString(TO_COL)};
    testMessageEquals(expected, info, ViewMessageType.MOVE_PIECE);
  }

  @DisplayName("testMenuMessages")
  @ParameterizedTest(name = "Menu type ({0}) should be {0}")
  @EnumSource(
      value = MenuMessageTypes.class,
      names = {"LOGOUT", "PLAYER_STATS", "ACTIVE_GAMES", "INVITES", "SELECT_GAME", "SEND_INVITE"})
  public void testMenuMessageTypes(MenuMessageTypes menuMessageTypes)
      throws NoSuchAlgorithmException {
    String[] expectInfo = {"TestInfo"};
    MenuMessage expected = new MenuMessage(menuMessageTypes, expectInfo);

    String[] info = {menuMessageTypes.name(), "TestInfo"};

    testMessageEquals(expected, info, ViewMessageType.MENU);
  }

  @DisplayName("testMenuMessageResponse")
  @ParameterizedTest(name = "Menu type ({0}) should be {0}")
  @EnumSource(
      value = MenuMessageTypes.class,
      names = {"LOGOUT", "PLAYER_STATS", "ACTIVE_GAMES", "INVITES", "SELECT_GAME", "SEND_INVITE"})
  public void testMenuMessageResponse(MenuMessageTypes menuMessageTypes) {
    String[] expectInfo = {"TestInfo"};
    MenuMessageResponse expected = new MenuMessageResponse(menuMessageTypes, expectInfo);

    String[] info = {menuMessageTypes.name(), "TestInfo"};

    testResponseEquals(expected, info, ViewMessageType.MENU_RESPONSE);
  }

  @Test
  void createMovePieceResponse() throws NoSuchAlgorithmException {

    MovePieceResponse expected =
        new MovePieceResponse(true, TEST_GAME_BOARD);

    String[] info = {String.valueOf(true), TEST_GAME_BOARD};
    testMessageEquals(expected, info, ViewMessageType.MOVE_PIECE_RESPONSE);
  }

  /**
   * Tests different types of messages to see if they are created correctly
   *
   * @param expected the expected message
   * @param info the info to build the message
   * @param type the type of message
   * @throws NoSuchAlgorithmException the SHA1 hash could not be found
   */
  private void testMessageEquals(ViewMessage expected, String[] info, ViewMessageType type)
      throws NoSuchAlgorithmException {
    ViewMessage result = factory.createViewMessage(type, info);
    assertEquals(expected, result);
  }

  /**
   * Tests different types of responses to see if they are created correctly
   *
   * @param expected the expected message
   * @param info the info to build the message
   * @param type the type of message
   */
  private void testResponseEquals(ViewMessage expected, String[] info, ViewMessageType type) {
    ViewMessage result = factory.createViewMessageFromServer(type, info);
    assertEquals(expected, result);
  }

}