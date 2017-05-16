package com.being.base.http;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Gson对象管理类，尽量复用Gson对象<br>
 * Created by Zhp on 2014/5/29.
 */
public class GsonManager {

    private static Gson mGson = new GsonBuilder().create();

    /**
     * 获取默认配置的Gson对象，
     * 此对象为单例模式，减少重复创建的消耗
     *
     * @return 默认配置的Gson对象
     */
    public static Gson getGson() {
        return mGson;
    }



    public static Gson getPrettyGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * 获取包含剔除规则的Gson对象<br>
     * <p>The following example shows how to exclude fields marked with a specific "@Foo" annotation
     * and excludes top-level types (or declared field type) of class String.</p>
     *
     * <pre>public @interface Foo {
     * // Field tag only annotation
     * }
     *
     * public class SampleObjectForTest {
     *      //@Foo
     *      private final int annotatedField;
     *      private final String stringField;
     *      private final long longField;
     *      private final Class<?> clazzField;
     *      public SampleObjectForTest() {
     *          annotatedField = 5;
     *          stringField = "someDefaultValue";
     *          longField = 1234;
     *      }
     * }
     *
     * public class MyExclusionStrategy implements ExclusionStrategy {
     *      private final Class<?> typeToSkip;

     * private MyExclusionStrategy(Class<?> typeToSkip) {
     *      this.typeToSkip = typeToSkip;
     *      }

     * public boolean shouldSkipClass(Class<?> clazz) {
     *      return (clazz == typeToSkip);
     *      }

     * public boolean shouldSkipField(FieldAttributes f) {
     *      return f.getAnnotation(Foo.class) != null;
     *      }
     * }
     *
     * public static void main(String[] args) {
     *      Gson gson = new GsonBuilder()
     *      .setExclusionStrategies(new MyExclusionStrategy(String.class))
     *      .serializeNulls()
     *      .create();
     *      SampleObjectForTest src = new SampleObjectForTest();
     *      String json = gson.toJson(src);
     *      System.out.println(json);
     * }</pre>
     *
     * ======== OUTPUT ========<br>
     * {"longField":1234}
     *
     * @param exclusionStrategy 剔除规则
     * @return 包含剔除规则的Gson对象
     */
    public static Gson getGsonWithExclusionStrategies(ExclusionStrategy exclusionStrategy) {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(exclusionStrategy);
        return builder.create();
    }
}
