package com.misfit.ta.android.aut.shinesample;


import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.gui.Gui;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;

public class ConnectionTest {
    
    static Logger logger = Util.setupLogger(Gui.class);

    public void testReconnect() {
        
        
//        Gui.printView();
//        System.exit(0);
       
        int times = 100;

        int disconnected = 0;
        int connected = 0;
        int others = 0;

        for (int i = 0; i < times; i++) {
            Gui.touchAView("Button", 1, false);

            String text = null;

            while (text == null || text.equalsIgnoreCase("CONNECTING")) {
                ShortcutsTyper.delayOne();
                Gui.setInvalidView();
                // the order of this text field is different
                ViewNode view = ViewUtils.findView("TextView", "mID", "id/textView", 0);
                if (view == null) {
                    break;
                } else {
                    text = view.namedProperties.get("mText").value;
                }
                System.out.println("LOG [Gui.main]: text= " + text);
            }

            if (text == null) {
                System.out.println("LOG [Gui.main]: VIEW WITH NO TEXT");
            } else if (text.equalsIgnoreCase("CLOSED")) {
                disconnected++;
            } else if (text.contains("CONNECTED -")) {
                connected++;
                Gui.touchAView("Button", 1, false);
                ShortcutsTyper.delayOne();
            }

            logger.info("LOG [Gui.main]: ====== round=" + i + " - connected= " + connected + " - disconnected= "
                    + disconnected + " - others= " + others);

        }

    }
    
    /**
     * Test the btle connection using the SimpleBLEConnector
     */
    public void btleConnectorTest() {

     
      int times = 100;

      int disconnected = 0;
      int connected = 0;
      int timeout = 0;
      int others = 0;

      for (int i = 0; i < times; i++) {
          Gui.touchAView("Button", 1, false);

          String text = null;
          long start = System.currentTimeMillis();

          while (text == null || text.equalsIgnoreCase("CONNECTING")) {
              ShortcutsTyper.delayOne();
              Gui.setInvalidView();
              // the order of this text field is different
              ViewNode view = ViewUtils.findView("TextView", "mID", "id/textView", 0);
              if (view == null) {
                  break;
              } else {
                  text = view.namedProperties.get("mText").value;
              }
              System.out.println("LOG [Gui.main]: text= " + text);
              if (System.currentTimeMillis() - start  >= 30000) {
                  timeout++;
                  break;
              }
          }

          if (text == null) {
              System.out.println("LOG [Gui.main]: VIEW WITH NO TEXT");
          } else if (text.equalsIgnoreCase("DISCONNECTED")) {
              disconnected++;
          } else if (text.equalsIgnoreCase("CONNECTED")) {
              connected++;
              Gui.touchAView("Button", 1, false);
              ShortcutsTyper.delayOne();
          } else if (text.equalsIgnoreCase("DISCONNECTING")) {
              Gui.touchAView("Button", 2, false);
              ShortcutsTyper.delayOne();
          }

          logger.info("LOG [Gui.main]: == round=" + i + " - connected= " + connected + " - disconnected= "
                  + disconnected + " - timeout= " + timeout + " - others= " + others);

      }

        
        
    }

    public static void main(String[] args) {
        
        Gui.init();

        ConnectionTest test = new ConnectionTest();
        test.testReconnect();
//        test.btleConnectorTest();
        Gui.shutdown();
    }
}
