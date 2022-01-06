package com.digiwin.plm.athena.bean.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.digiwin.plm.athena.bean.annotation.Alias;
import org.apache.commons.beanutils.BeanUtils;


import dyna.common.util.StringUtils;

public class JsonConvertUtil
{
	public static <T> T convertMapToObject(Map<String, Object> valueMap, Class<T> clazz)
	{
		try
		{
			T result = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			
			Map<String,Object> middleMap = new HashMap<>();
			middleMap.putAll(valueMap);

			if (fields != null && fields.length > 0)
			{
				for (Field field : fields)
				{
					Alias alias = field.getAnnotation(Alias.class);
					if (alias != null)
					{
						String plm_field = alias.value();
						if(!StringUtils.isNullString(plm_field))
						{
							middleMap.put(field.getName(), middleMap.remove(plm_field));
						}
					}
				}
			}

			BeanUtils.populate(result, middleMap);

			return result;
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
