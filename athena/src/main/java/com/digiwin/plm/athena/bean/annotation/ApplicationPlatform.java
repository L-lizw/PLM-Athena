package com.digiwin.plm.athena.bean.annotation;

import com.digiwin.plm.athena.bean.enums.PlatformEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApplicationPlatform
{
	PlatformEnum platform() default PlatformEnum.Client ;
}
