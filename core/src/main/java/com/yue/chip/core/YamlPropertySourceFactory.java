package com.yue.chip.core;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;

/**
 * 通过@PropertySource读取yml配置文件
 * 
 * @author mrliu
 *
 */
public class YamlPropertySourceFactory extends DefaultPropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		if (null == resource) {
			super.createPropertySource(name, resource);
		}
		  
		PropertySource<?> propertySource = new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource()).get(0);
		return propertySource;
	}
}
