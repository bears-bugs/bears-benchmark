package client.presenter.controller.messages;

import static client.presenter.SharedTestAttributes.*;
import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.ViewMessageType;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class MenuMessageTest {


  private static MenuMessage testMessage;

  @BeforeEach
  public void setup() throws NoSuchAlgorithmException {
    testMessage = new MenuMessage(MenuMessageTypes.PLAYER_STATS, TEST_MENU_OPTIONS_INT_STRING);
  }

  @DisplayName("testMenuTypes")
  @ParameterizedTest(name = "({0}) should be {0}")
  @EnumSource(
      value = MenuMessageTypes.class,
      names = {"LOGOUT", "PLAYER_STATS", "ACTIVE_GAMES", "INVITES", "SELECT_GAME", "SEND_INVITE"})

  public void testMenuTypes(MenuMessageTypes menuMessageTypes) {
    assertEquals(menuMessageTypes, new MenuMessage(menuMessageTypes, new String[0]).menuType);
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.MENU,
        new MenuMessage(MenuMessageTypes.LOGOUT, new String[0]).messageType);
  }

  @Test
  public void testNotEqualNull(){
    assertNotEquals( testMessage, null);
  }

  @Test
  public void testHashCode() {
    MenuMessage testHash = new MenuMessage(MenuMessageTypes.PLAYER_STATS, TEST_MENU_OPTIONS_INT_STRING);
    assertEquals(testMessage.hashCode(), testHash.hashCode());
  }
}