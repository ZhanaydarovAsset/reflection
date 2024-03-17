package org.example.tests;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Comparator;

public class TestRunner {

    public static void run(Class<?> testClass) {

        final Object testObj = initTestObj(testClass);
        Method[] methods = testClass.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeAll.class)) {
                invokeMethod(testObj, method);
            }
        }

//        for (Method testMethod : testClass.getDeclaredMethods()) {
//            if (testMethod.accessFlags().contains(AccessFlag.PRIVATE)) {
//                continue;
//            }
//
//            if (testMethod.getAnnotation(Test.class) != null) {
//                invokeMethod(testObj, testMethod);
//            }
//        }

        Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Test.class))
                .filter(method -> !method.accessFlags().contains(AccessFlag.PRIVATE))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(Test.class).order()))
                .forEach(method -> {
                    for (Method method1 : methods){
                        if (method1.isAnnotationPresent(BeforeEach.class)) {
                            invokeMethod(testObj, method1);
                        }
                    }
                    invokeMethod(testObj, method);
                    for (Method method2 : methods){
                        if (method2.isAnnotationPresent(AfterEach.class)) {
                            invokeMethod(testObj, method2);
                        }
                    }
                });

        for (Method method : methods) {
            if (method.isAnnotationPresent(AfterAll.class)) {
                invokeMethod(testObj, method);
            }
        }
    }

    private static void invokeMethod(Object obj, Method method) {
        try {
            method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object initTestObj(Class<?> testClass) {
        try {
            Constructor<?> noArgsConstructor = testClass.getConstructor();
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Нет конструктора по умолчанию");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект тест класса");
        }
    }
}
