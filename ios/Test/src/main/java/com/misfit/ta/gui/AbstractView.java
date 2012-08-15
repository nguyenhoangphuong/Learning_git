package com.misfit.ta.gui;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;

/**
 * Base class for all the views.
 */
public abstract class AbstractView {

  private static Logger logger = Util.setupLogger(AbstractView.class);

  /**
   * Go to the top of the page.
   */
  public static void gotoTop() {
    gotoTop(5);
  }

  /**
   * Go to the bottom of the page.
   */
  public static void gotoBottom() {
    gotoBottom(8);
  }

  /**
   * Verifies if a certain view with expected text is currently visible.
   * 
   * @param view name of the view.
   * @param expectedText expected text of the view.
   * @return <code>true</code> if such view is visible.
   */
  protected static boolean isContentVisible(String view, String expectedText) {
    Vector<ViewNode> views = Gui.getCurrentViews();

    // no text found -> view is not visible
    ViewNode text = ViewUtils.findView(views, "TextView", "mText", expectedText, 0);
    if (text == null) {
      return false;
    }

    // if found text, if there are 4 NoSaveStateFrameLayout components, make sure the text is after
    // the 3rd component. This is to support the navigation bar.
    ViewNode fourthNoSaveStateFrame = ViewUtils.findView(views, "NoSaveStateFrameLayout", 3);
    if (fourthNoSaveStateFrame != null && views.indexOf(text) < ViewUtils.findViewIndex(views, "NoSaveStateFrameLayout", 2)) {
      return false;
    }

    int p[] = Gui.getCoordinators(views, "ImageButton", "mID", "id/actionbar_nav");

    if (p[0] <= 0) {
      return false;
    } else {
      if (p[0] <= Gui.getWidth() / 2) {
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * Gets title of the view.
   * 
   * @return view title.
   */
  public static String getViewTitle() {
    ViewNode action = ViewUtils.findView("ImageButton", "mID", "id/actionbar_nav", 0);
    return action.parent.children.get(2).text;
  }

  /**
   * Verifies if a title is visible on a specific view.
   * 
   * @param view what view.
   * @param expectedText expected text of the view.
   * @return
   */
  public static boolean isTitleVisible(String view, String expectedText) {
    Vector<ViewNode> views = Gui.getCurrentViews();

    // no text found -> view is not visible
    // because of the navigation bar: the first item is in navigation bar, the second is the real
    // title
    ViewNode text = ViewUtils.findView(views, "TextView", "mText", expectedText, 1);
    if (text == null) {
      return false;
    }

    int p[] = Gui.getCoordinators(views, "ImageButton", "mID", "id/actionbar_nav");

    if (p[0] <= 0) {
      return false;
    } else {
      if (p[0] <= Gui.getWidth() / 2) {
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * Go to top within certain step.
   * 
   * @param steps how many swipes.
   */
  protected static void gotoTop(int steps) {
    Gui.swipeUp(steps, 400);
  }

  /**
   * Go to bottom within certain step.
   * 
   * @param steps how many swipes.
   */
  protected static void gotoBottom(int steps) {
    Gui.swipeDown(steps, 400);
  }

  /**
   * Verifies if a certain view is visible.
   * 
   * @param view expected view.
   * @return <code>true</code> if the view is visible.
   */
  protected static boolean isViewVisible(String view) {
    Vector<ViewNode> views = Gui.getCurrentViews();
    int[] playButton = Gui.getCoordinators(views, "ImageButton", "mID", "id/playPause");
    int[] p = Gui.getCoordinators(views, view, 0);
    return (p[1] > 0 && p[1] < playButton[1] - 10);
  }

  /**
   * Scrolls to a certain view with certain pair of property.
   * 
   * @param view what view.
   * @param property what property.
   * @param value expected value of the property.
   * @return found view.
   */
  protected static ViewNode scrollTo(String view, String property, String value) {
    final int times = 30;
    int count = 0;
    ViewNode v = ViewUtils.findView(view, property, value, 0);

    while (v == null) {
      Gui.swipeDown(1, 200);
      count++;
      v = ViewUtils.findView(view, property, value, 0);
      if (count > times) {
        break;
      }
    }

    return v;
  }


  /**
   * Press on an album in a horizontal list. We need to make sure that we click on the right list as
   * there can be multiple lists in a view by checking the section text right before the list.
   * 
   * @param sectionTextView view containing text like NEW RELEASES or RECOMMENDED ALBUMS
   * @param index index.
   * @return AlbumCoverCell view where we play.
   */
  protected static ViewNode pressOnAListItem(ViewNode sectionTextView, String listItemName, int index) {
    Vector<ViewNode> views = Gui.getCurrentViews();
    ViewNode cell = ViewUtils.findView(views, sectionTextView, listItemName, index);
    Gui.touch(Gui.getCoordinators(cell));
    return cell;
  }

  /**
   * Go to a certain session with certain text.
   * 
   * @param text name of session, for eg. RECOMMENDED ALBUMS.
   * @return view node of the session.
   */
  public static ViewNode goToSection(String text) {
    gotoTop();
    ViewNode node = scrollTo("TextView", "mText", text);
    ShortcutsTyper.delayTime(500);
    return node;
  }

  /**
   * Subclasses need to implement this method in order to handle different context menus for
   * different views.
   * 
   * @param item name of item to press.
   */
  public static void pressContextMenuItem(String item) {
    // to be overridden
  }

}
