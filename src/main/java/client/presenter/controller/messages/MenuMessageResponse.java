package client.presenter.controller.messages;

import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.ViewMessageType;
import java.util.Arrays;

public class MenuMessageResponse extends ViewMessage {

  /**
   * The type of menu action
   */
  public final MenuMessageTypes menuType;
  /**
   * The information on the menu which can vary in size with the different menu types
   */
  public final String[] information;

  /**
   * Sets the menu type and the information for the message response
   * @param menuType the menu type
   * @param information the menu information
   */
  public MenuMessageResponse(MenuMessageTypes menuType, String[] information) {
    super(ViewMessageType.MENU_RESPONSE);

    this.menuType = menuType;
    this.information = information;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof MenuMessageResponse)) {
      return false;
    }
    MenuMessageResponse other = (MenuMessageResponse) o;
    return menuType.equals(other.menuType) && Arrays.equals(information, other.information);
  }
}
