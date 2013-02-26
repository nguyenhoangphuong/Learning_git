package com.misfit.ta.android;

import com.misfit.ta.android.gui.Gui;

public aspect Screenshot {
  
  before():  execution(* com.misfit.ta.*..*.v_*(..)) {
    String name = thisJoinPoint.getSignature().getName();
    Gui.makeToast(name);
  }

  before(): execution(* com.misfit.ta.*..*.e_*(..)) {
    String name = thisJoinPoint.getSignature().getName();
    Gui.makeToast(name);
  }
  
  after() returning: androidVertexScreenshot() {
    Gui.captureScreen( thisJoinPoint.getSignature().getName());
  }
  
  after() returning: androidEdgeScreenshot() {
    Gui.captureScreen( thisJoinPoint.getSignature().getName());
  }

  after() throwing: androidVertexScreenshot() {
    Gui.captureScreen( thisJoinPoint.getSignature().getName());
  }

  after() throwing: androidEdgeScreenshot() {
    Gui.captureScreen( thisJoinPoint.getSignature().getName());
  }
  
  pointcut androidVertexScreenshot():
    execution(* com.misfit.ta.*..*.v_*(..));

  pointcut androidEdgeScreenshot():
    execution(* com.misfit.ta.*..*.e_*(..));
  
  pointcut androidVertexUINotification():
    execution(* com.misfit.ta.*..*.v_*(..));
  
  pointcut androidEdgeUINotification():
    execution(* com.misfit.ta.*..*.e_*(..));
    
}
