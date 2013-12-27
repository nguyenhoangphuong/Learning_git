package com.misfit.ta.gui.social;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.PrometheusHelper;

public class SocialProfileView {

	
	static public void tapEdit() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.EditButton);
	}
	
	static public void tapDone() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.DoneButton);
	}
	
	static public void tapBack() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.BackButton);
	}
	
	static public void tapCancel() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.CancelButton);
	}
	
	static public void tapSearchFriend() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.SearchFriendButtonTag);
	}
	
	static public void tapAccept() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.AcceptButton);
	}
	
	static public void tapIgnore() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.IgnoreButton);
	}
	
	static public void tapDeleteFriend(int index) {
		
		String cmd = "(Gui touchAView: (ViewUtils findViewWithViewName: @\"UIButton\" andIndex: 0 inView: (ViewUtils findViewWithViewName: @\"PTSocialFriendInfoCell\" andIndex: %index%)))";
		cmd = cmd.replace("%index%", index + "");
		NuRemoteClient.sendToServer(cmd);
	}
	
	static public void tapRemoveFriend() {
	
		Gui.touchAVIew("UILabel", DefaultStrings.RemoveFriendButton);
	}
	
	static public void tapEditAvatarButton() {
		
		Gui.touchAVIew("UIImageView", 0);
	}
	
	static public void tapChooseFromLibrary() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.ChooseFromLibrary);
	}
	
	static public void tapTakePhoto() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.TakePhotoButton);
	}
	
	static public void tapCancelChooseAvatar() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.CancelButton);
	}
	
	static public void tapCapturePhoto() {
		
		Gui.touchAVIew("UIButton", 1);
	}
	
	static public void tapCancelCapturePhoto() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.CancelButton.toUpperCase());
	}
	
	static public void tapUsePhoto() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.UsePhotoButton);
	}
	
	static public void tapRetakePhoto() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.RetakeButton);
		
	}
	
	static public void tapOKAlert() {
		
		Gui.touchPopupButton(0);
	}
	
	static public void tapAddPhotoAlert() {
		
		Gui.touchPopupButton(0);
	}
	
	
	
	static public boolean hasEmptyNameAlert() {
		
		return PrometheusHelper.hasAlert(DefaultStrings.EmptyNameMessage, null);
	}
	
	static public boolean hasEmptyHandleAlert() {
		
		return PrometheusHelper.hasAlert(DefaultStrings.EmptyHandleMessage, null);
	}
	
	static public boolean hasEmptyAvatarAlert() {
		
		return PrometheusHelper.hasAlert(DefaultStrings.EmptyAvatarMessage, null);
	}
	
	static public boolean hasInvalidHandleAlert() {
		
		return PrometheusHelper.hasAlert(DefaultStrings.InvalidHandleMessage, null);
	}
	
	static public boolean hasDuplicatedHandleAlert() {
		
		return PrometheusHelper.hasAlert(DefaultStrings.DuplicatedHandleMessage, null);
	}

	
	
	static public void inputName(String name) {
		
		Gui.touchAVIew("UITextField", 1);
		Gui.setText("UITextField", 1, name);
	}
	
	static public void inputHandle(String handle) {
		
		Gui.touchAVIew("UITextField", 0);
		Gui.setText("UITextField", 0, handle);
	}

	
	
	static public boolean isProfileReviewView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.ProfilePreviewViewTitle);
	}
	
	static public boolean isSocialProfileView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.SocialProfileViewTitle);
	}
	
	static public boolean isSocialProfileViewNoFriend() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.SocialProfileNoFriendMessage);
	}
	
	static public boolean isInEditMode() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.DoneButton);
	}
	
}
