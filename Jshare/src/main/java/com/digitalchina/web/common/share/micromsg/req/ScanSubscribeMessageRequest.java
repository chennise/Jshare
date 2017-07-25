package com.digitalchina.web.common.share.micromsg.req;
import com.digitalchina.web.common.share.micromsg.MicroMessage;


/**
 * 扫描关注
 * @author ZRD
 *
 */
public class ScanSubscribeMessageRequest extends MicroMessage{

	private String scene_id;
	private Boolean isScene;
	
	@Override
	public String getXml() {
		return null;
	}

	public String getScene_id() {
		return scene_id;
	}

	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}

	public Boolean getIsScene() {
		return isScene;
	}

	public void setIsScene(Boolean isScene) {
		this.isScene = isScene;
	}

}
