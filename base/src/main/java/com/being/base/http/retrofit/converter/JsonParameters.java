package com.being.base.http.retrofit.converter;

import com.being.base.http.retrofit.converter.ValueDefaultParameters.Default;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public final class JsonParameters {
    @Retention(RUNTIME)
    @interface Json {
    }

    static class JsonStringConverterFactory extends Converter.Factory {
        private final Converter.Factory delegateFactory;

        JsonStringConverterFactory(Converter.Factory delegateFactory) {
            this.delegateFactory = delegateFactory;
        }

        @Override public Converter<?, String> stringConverter(Type type, Annotation[] annotations,
                                                              Retrofit retrofit) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Json) {
                    // NOTE: If you also have a JSON converter factory installed in addition to this factory,
                    // you can call retrofit.requestBodyConverter(type, annotations) instead of having a
                    // reference to it explicitly as a field.
                    Converter<?, RequestBody> delegate =
                            delegateFactory.requestBodyConverter(type, annotations, new Annotation[0], retrofit);
                    return new DelegateToStringConverter<>(delegate);
                }
            }
            return null;
        }

        static class DelegateToStringConverter<T> implements Converter<T, String> {
            private final Converter<T, RequestBody> delegate;

            DelegateToStringConverter(Converter<T, RequestBody> delegate) {
                this.delegate = delegate;
            }

            @Override public String convert(T value) throws IOException {
                Buffer buffer = new Buffer();
                delegate.convert(value).writeTo(buffer);
                return buffer.readUtf8();
            }
        }
    }

    static class Filter {
        final String userId;

        Filter(String userId) {
            this.userId = userId;
        }
    }

    interface Service {
        @FormUrlEncoded
        @POST("/filter")
        Call<ResponseBody> example(@Default("default") @Field("de") String de);
    }

    public static void main(String... args) throws IOException, InterruptedException {
        MockWebServer server = new MockWebServer();
        server.start();
        server.enqueue(new MockResponse());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(new ValueDefaultParameters.ValueDefaultConverterFactory())
                .build();
        Service service = retrofit.create(Service.class);

        Call<ResponseBody> call = service.example("sd");
        Response<ResponseBody> response = call.execute();

        // Print the request path that the server saw to show the JSON query param:
        RecordedRequest recordedRequest = server.takeRequest();
        System.out.println(recordedRequest.getPath());

        server.shutdown();
    }
}
