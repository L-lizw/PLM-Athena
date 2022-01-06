/**
 * author duansy
 * date 2017/3/28 15:27
 * Copyright(c) Dajiashequ Technology Co.,Ltd.
 */
package com.digiwin.plm.athena.bean.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.digiwin.plm.athena.bean.exception.WebException;
import com.digiwin.plm.athena.net.PLMServiceProvider;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


import dyna.common.conf.ConfigurableMSRImpl;
import dyna.common.conf.loader.ConfigLoaderFactory;
import dyna.common.systemenum.LanguageEnum;
import dyna.common.util.FileUtils;
import dyna.common.util.StringUtils;
import dyna.net.service.das.MSRM;

public class I18nResourceUtil {
     
	private static final Map<String, Map<String, String>>	msrCache	= new HashMap<String, Map<String, String>>();
    /**
     * 查找相应国际化资源文件
     * @param baseName
     * @param key
     * @return
     */
    public static String getResource(String localStr,String key,String defaultmes )
    {
        String resource = getResource(localStr, key, defaultmes,null);
		return resource;
    }
    /**
     * 查找相应国际化资源文件
     * @param baseName
     * @param key
     * @return
     */
    public static String getResource(String localStr,String key,String defaultmes ,Object ... param)
    {
    	String i18nResource = "";
    	Map<String, String> msrMap =msrCache.get(getLanguage(localStr).getId());
		if (msrMap != null && key != null)
    	{
    		i18nResource=msrMap.get(key);
    	}
    	if (StringUtils.isNullString(i18nResource))
    	{
			if (key != null)
			{
				i18nResource = key + "," + defaultmes;
			}
			else
			{
				i18nResource = defaultmes;
			}
    	}
        String res="";
        if (param != null) 
        {
            res = MessageFormat.format(i18nResource,param);
        }
        else
        {
            res = i18nResource;
        }
        return res;
    }


	public static void loadNLSResource() throws WebException
	{
        String path = I18nResourceUtil.class.getResource("/").getPath();
        path = FileUtils.decodeURLString(path,"UTF-8");
		ConfigurableMSRImpl config = ConfigLoaderFactory.getLoader4MSR().load(path+"i18n");
		if (config.getConfig()!=null)
		{
			msrCache.putAll(config.getConfig());
		}
		
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		PLMServiceProvider sp = wac.getBean(PLMServiceProvider.class);
		MSRM msr = sp.getServiceInstance(MSRM.class);
		for (LanguageEnum language : LanguageEnum.values())
		{
			Map<String, String> map =msr.getMSRMap(language.getId());
			if (map!=null)
			{
				if (msrCache.get(language.getId())!=null)
				{
					map.putAll(msrCache.get(language.getId()));
				}
				msrCache.put(language.getId(),map);
			}
		}
	}
	
    public static LanguageEnum getLanguage(String locale) 
    {
    	if (StringUtils.isNullString(locale))
    	{
    		return LanguageEnum.ZH_CN;
    	}
    	
    	if (locale.startsWith(LanguageEnum.EN.getId()))
    	{
    		return LanguageEnum.EN;
    	}
    	
		return LanguageEnum.getById(locale);
	}
    

}
