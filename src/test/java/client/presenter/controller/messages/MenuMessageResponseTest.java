package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuMessageResponseTest {

  private static final MenuMessageTypes menuType = MenuMessageTypes.PLAYER_STATS;
  private static final String[] information = {"Test"};
  private static MenuMessageResponse testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new MenuMessageResponse(menuType, information);
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.MENU_RESPONSE, testMessage.messageType);
  }

  @Test
  public void testMenuType() {
    assertEquals(menuType, testMessage.menuType);
  }

  @Test
  public void testInformation() {
    assertEquals(information, testMessage.information);
  }

}