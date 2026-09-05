package com.octopusmall.common.aichat.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Web MVC 配置
 * 保留此配置类以便后续扩展（例如拦截器、CORS、静态资源等）。
 *
 * 重要说明：Spring 6.x 中并不存在 ServerSentEventHttpMessageConverter，
 * Spring MVC 处理 SSE 流式响应的正确方式是直接使用 SseEmitter，
 * 它是 Spring MVC 原生支持的类，无需额外的 HttpMessageConverter。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 当前没有需要自定义的转换器，保留为空实现以便后续扩展
    // 如需自定义转换器，可以重写以下方法：
    // @Override
    // public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    //     // 在此处添加自定义 HttpMessageConverter
    // }
}
