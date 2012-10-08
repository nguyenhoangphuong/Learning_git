function testManualTracking()
{
	mt = new ManualTracking();

	mt.isVisible();
	mt.isInInputMode();
	mt.getFieldsInfo();
	
	mt.tapField("Duration");
	mt.isVisible();
	mt.getFieldRanges();
	mt.setField([1, 20, 3]);
	mt.tapDone();
	
	mt.tapField("Start time");
	mt.isVisible();
	mt.getFieldRanges();
	mt.setField([9, 31, "PM"]);
	mt.tapDone();
	
	mt.tapField("Distance");
	mt.isVisible();
	mt.getFieldRanges();
	mt.setField([7]);
	mt.tapDone();
	
	mt.done();
	// mt.cancel();
}
