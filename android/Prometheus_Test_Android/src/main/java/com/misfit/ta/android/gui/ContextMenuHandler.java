package com.misfit.ta.android.gui;

import java.util.List;
import java.util.Vector;
import com.misfit.ta.Settings;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

/**
 * Class to handle context menus.
 */
public class ContextMenuHandler {


  public static final String ALBUM = "Album";
  public static final String ARTIST = "Artist";
  public static final String ADD_TO = "Add to";
  public static final String STAR = "Star";
  public static final String UNSTAR = "Unstar";
  public static final String QUEUE = "Queue";
  public static final String RADIO = "Radio";
  public static final String SHARE = "Share";
  public static final String DEL_TRACK = "Delete track";
  public static final String DEL_PLAYLIST = "Delete playlist";
  public static final String PUBLISH = "Publish";
  public static final String UNPUBLISH = "Unpublish";
  public static final String COLLABORATIVE = "Collaborative";
  public static final String DOWNLOAD = "Download";
  public static final String RENAME = "Rename";
  public static final String CANCEL = "Cancel download";
  public static final String UNDOWNLOAD = "Undownload";

  public static final String MORE = "More";

  public static final String CONTEXT_MENU_MIDDLE = "CONTEXT_MENU_MIDDLE";
  public static final String CONTEXT_MENU_HEIGHT = "CONTEXT_MENU_HEIGHT";


  private static Logger logger = Util.setupLogger(ContextMenuHandler.class);


  /**
   * Since heirarchy viewer cannot access context menu, we need to know the middle point and height
   * of context menu of each device so that we can calculate the coordinators accurately.
   */
  private static final int MIDDLE_X = getContextMenuSettings(CONTEXT_MENU_MIDDLE);
  private static final int MENU_ITEM_HEIGHT = getContextMenuSettings(CONTEXT_MENU_HEIGHT);

  /**
   * Containing all context menu options.
   */
  private final List<String> contextMenu = new Vector<String>();

  /**
   * Views that use context menu handler need to explicitly add those options in the right order to
   * the context menu holder.
   * 
   * @param item context menu item.
   */
  public void addToContextMenu(String item) {
    contextMenu.add(item);
  }

  /**
   * Gets the index of certain item in the view's list of items.
   * 
   * @param item context menu item.
   * @return index.
   */
  public int getIndex(String item) {
    return contextMenu.indexOf(item);
  }

  /**
   * Gets a list of context menu items.
   * 
   * @return
   */
  public List<String> getContextMenu() {
    return contextMenu;
  }

  /**
   * Calculates the coordinators of a context menu item. Calculation is done differently from other
   * views as we cannot query the view of context menu.
   * 
   * @param item which item.
   * @return coordinators.
   */
  public int[] getCoordinators(String item) {
    logger.debug("Finding context menu item = " + item);
    Gui.captureScreen("ContextMenu");
    int index = getIndex(item);

    if (index < 0) {
      return new int[] {-1, -1};
    }

    int half = (int) (Math.floor(contextMenu.size() / 2));

    int y = MIDDLE_X + (index - half) * MENU_ITEM_HEIGHT + 10;

    return new int[] {200, y};
  }

  /**
   * Loads the settings for context menu for each device. The properties include coordinator of
   * middle point and height of context menu and
   * 
   * @param property property key.
   * @return properfy value.
   */
  private static final int getContextMenuSettings(String property) {
    String device = System.getProperty("deviceId");
    String fullProperty = property;
    if (device != null && !device.isEmpty()) {
      fullProperty = device + "_" + property;
    }
    try {
    	return Integer.valueOf(Settings.getValue(fullProperty));
    } catch (Exception e) {
    	logger.info("No dimension for " + property);
    	return 0;
    }
  }


}
