package com.digiwin.plm.athena.bean.util;

import com.digiwin.plm.athena.net.PLMServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import dyna.common.util.FileUtils;

public class ConfigFileUtils
{
	private static final Logger	log	= LoggerFactory.getLogger(ConfigFileUtils.class);
	public static String getBasePath()
	{
		// 璇ュ嚱鏁板湪涓嶅悓鐜涓嬭幏寰楃殑璺緞鏄笉鍚岀殑
		// 缂栬瘧鐜涓嬪緱鍒扮殑璺緞鏄� .../target/classes/
		// 鎵撳寘鎴� jar 鏂囦欢鍚庯紝寰楀埌鐨勮矾寰勬槸 jar 鏂囦欢鐨勪綅缃�
		// 姝ゅ鑾峰緱鐨勮矾寰勶紝鍗充娇鍦� windows 涓嬶紝浣跨敤鐨勪篃鏄� linux 涓嬬殑鏂囦欢鍒嗛殧绗�
		String basePath = PLMServiceProviderImpl.class.getProtectionDomain().getCodeSource().getLocation().getPath();

		// 濡傛灉鍖呭惈涓枃璺緞锛屽垯瀵瑰叾杩涜 decode 澶勭悊

		basePath = FileUtils.decodeURLString(basePath, "UTF-8");

		// 灏嗚矾寰勪腑鐨勬枃浠跺垎鍓茬鏇存崲涓哄綋鍓嶈繍琛岀幆澧冪殑鏂囦欢鍒嗛殧绗�
		basePath = basePath.replace('/', System.getProperty("file.separator").charAt(0));

		// 鍦ㄦ墦鍖呯幆澧冧笅锛屽彇寰� jar 鏂囦欢鎵�鍦ㄧ殑鏂囦欢澶硅矾寰勶紝鑰屼笉鏄� jar 鏂囦欢璺緞
		int firstIndex = basePath.indexOf(System.getProperty("file.separator")) + 1;
		int lastIndex = basePath.lastIndexOf(System.getProperty("file.separator")) + 1;
		basePath = basePath.substring(firstIndex, lastIndex);

		// 璁惧畾閰嶇疆鏂囦欢鐩綍锛岀粨灏惧甫鏂囦欢鍒嗛殧绗�
		basePath = basePath + "conf" + System.getProperty("file.separator");
		return basePath;
	}
}
