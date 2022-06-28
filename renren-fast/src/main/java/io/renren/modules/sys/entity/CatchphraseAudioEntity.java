package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2022-02-23 21:34:08
 */
@Data
@TableName("gwy_catchphrase_audio")
public class CatchphraseAudioEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 对应鸡汤的主键id
	 */
	@TableId
	private Integer catId;
	/**
	 * 鸡汤音频的地址
	 */
	private String audioUri;

}
