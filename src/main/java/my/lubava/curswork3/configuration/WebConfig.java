package my.lubava.curswork3.configuration;

import my.lubava.curswork3.components.SizeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Formatter;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addFormatters (FormatterRegistry registry){
        registry.addConverter(new SizeConverter());
    }

}
