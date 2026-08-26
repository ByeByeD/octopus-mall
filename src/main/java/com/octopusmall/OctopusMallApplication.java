package com.octopusmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.octopusmall.**.mapper")  // 扫描项目中的mapper文件，不用再单独给mapper添加@mapper注解
public class OctopusMallApplication {

	public static void main(String[] args) {
		SpringApplication.run(OctopusMallApplication.class, args);
	}

}
