package firstTask;


import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Main {
    @Retention(RUNTIME)
    @Target({METHOD, TYPE})
    public @interface Selector {
        String xpath();
    }

    public interface MainPage {
        @Selector(xpath = ".//*[@test-attr='input_search']")
        String textInputSearch();

        @Selector(xpath = ".//*[@test-attr='button_search']")
        String buttonSearch();
    }

    public class MethodInterception {
        @Test
        public void annotationValue() {
            MainPage mainPage = createPage(MainPage.class);
            assertNotNull(mainPage);
            assertEquals(mainPage.buttonSearch(), ".//*[@test-attr='button_search']");
            assertEquals(mainPage.textInputSearch(), ".//*[@test-attr='input_search']");
        }

        private MainPage createPage(Class<MainPage> clazz) {
            return (MainPage) Proxy.newProxyInstance(MainPage.class.getClassLoader(), new Class[] {clazz}, new MainPageProxy());
        }
    }

    public class MainPageProxy implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.getAnnotation(Selector.class).xpath();
        }
    }
}