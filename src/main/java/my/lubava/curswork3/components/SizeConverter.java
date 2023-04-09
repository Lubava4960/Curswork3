package my.lubava.curswork3.components;

import my.lubava.curswork3.model.Size;
import org.springframework.core.convert.converter.Converter;

public class SizeConverter implements Converter<String, Size> {
    @Override
    public Size convert(String sourse){
        return Size.convertSize(Integer.parseInt(sourse));
    }


}
