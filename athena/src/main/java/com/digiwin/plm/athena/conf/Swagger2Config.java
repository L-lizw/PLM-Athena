package com.digiwin.plm.athena.conf;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class Swagger2Config
{

	@Bean
	public Docket docket()
	{
		Docket docket =  new Docket(DocumentationType.SWAGGER_2);
		docket.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.digiwin.plm.controller"))
				.build();
		return docket;
	}

	@Bean
	public ApiInfo apiInfo()
	{
		//PLM to Athena API Documentation
		return new ApiInfo("PLM to Athena API Documentation", "PLM对接Athena平台接口说明", "1.0.43.1", null, new Contact("", "", ""), "", "", new ArrayList()) ;
	}

}
