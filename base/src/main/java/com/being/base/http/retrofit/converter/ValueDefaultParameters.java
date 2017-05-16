package com.being.base.http.retrofit.converter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 带有默认值的请求参数
 * Created by zhangpeng on 17/3/22.
 */

public final class ValueDefaultParameters {

    @Documented
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @interface Default {
        /**
         * 默认值
         * @return
         */
        String value();
    }

    static class ValueDefaultConverterFactory extends Converter.Factory {
        public ValueDefaultConverterFactory() {
        }

        @Override
        public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Default) {
                    return new DefaultConverter((Default) annotation);
                }
            }
            return null;
        }
    }

    static class DefaultConverter implements Converter<Object, String> {

        private Default mDefault;

        public DefaultConverter(Default queryDefault) {
            mDefault = queryDefault;
        }

        @Override
        public String convert(Object value) throws IOException {
            if (value == null) {
                return mDefault.value();
            } else {
                return value.toString();
            }
        }
    }

}
