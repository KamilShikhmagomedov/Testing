package secondTask;

import org.junit.Test;

import org.testng.IAnnotationTransformer;
import org.testng.TestNG;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Listeners;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ServiceLoader;
import static org.junit.Assert.assertEquals;


public class SecondTask {

    private enum Priority {
        Blocker, Critical, Major, Minor
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD) //on class level
    public @interface TestMethodInfo {

        //Приоритет теста
        Priority priority() default Priority.Major;

        //Автор теста
        String author() default "Bill Gates";

        //Дата последних изменений в тесте
        String lastModified() default "01.01.2019";
    }


    @Test
    @TestMethodInfo(priority = Priority.Critical, author = "Test user", lastModified = "20.08.2019")
    public void annotation() {
        assertEquals(1, 1);
    }

    public static class AnnotationInterceptor implements IAnnotationTransformer {
        public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
            TestMethodInfo methodAnnotation = testMethod.getAnnotation(TestMethodInfo.class);
            System.out.println(methodAnnotation.priority() + " " + methodAnnotation.author() + " " + methodAnnotation.lastModified());
        }
    }
}