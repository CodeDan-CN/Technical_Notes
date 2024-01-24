### Spring boot单元测试

> 不管是单体项目还是微服务项目，在进行业务逻辑处理的时候，难免会有一些地方需要我们在不启动项目的情况下进行业务逻辑测试，那么此时单元测试就很有必要了，其实在某个业务模块编写完成时，也需要一定的单元测试，那么在Spring boot中如何进行保证启动IOC容器下的单元测试呢？

一般我们在创建项目之后，在maven构建项目之后，src目录下会存在两个目录，其中`main`目录是存放项目程序包的，而`test`目录则是存放测试类的，一般来说test在项目创建之初，目录结构和main相同。

那么怎么使用test目录呢？一般test目录下会提供一个已经编写好的测试类，那么我们可以在其相同路径下创建一个新的测试类，也可以直接使用其。

这里一般来说在项目需要进行单元测试的模块比较多的情况，还是建议新建测试类比较规范。新建的类模版如下：

```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)    //这里是为了给测试类指定启动类
public class TestServiceImpIBooy {

    @Resource
    private TestServiceImpI testServiceImpI;

    @Test
    public void test(){
        testServiceImpI.sout();
    }

}
```

&nbsp;

### Junit断言介绍和使用

如果在项目中导入Junit包，那么我们就可以在test单元测试类中使用断言机制，来进行一些数据check。

***断言主要用于项目中对于业务逻辑测试时，返回或者提供数据的检查，将实际返回值/参数与预料数据进行对比，从而返回一些异常消息。***

下述就是Junit提供的断言一部分方式：

```java
public class Assert {
    protected Assert() {
    }

    public static void assertTrue(String message, boolean condition) {
        if (!condition) {
            fail(message);
        }

    }

    public static void assertTrue(boolean condition) {
        assertTrue((String)null, condition);
    }

    public static void assertFalse(String message, boolean condition) {
        assertTrue(message, !condition);
    }

    public static void assertFalse(boolean condition) {
        assertFalse((String)null, condition);
    }

    public static void fail(String message) {
        if (message == null) {
            throw new AssertionError();
        } else {
            throw new AssertionError(message);
        }
    }

    public static void fail() {
        fail((String)null);
    }

    public static void assertEquals(String message, Object expected, Object actual) {
        if (!equalsRegardingNull(expected, actual)) {
            if (expected instanceof String && actual instanceof String) {
                String cleanMessage = message == null ? "" : message;
                throw new ComparisonFailure(cleanMessage, (String)expected, (String)actual);
            } else {
                failNotEquals(message, expected, actual);
            }
        }
    }

    private static boolean equalsRegardingNull(Object expected, Object actual) {
        if (expected == null) {
            return actual == null;
        } else {
            return isEquals(expected, actual);
        }
    }

    private static boolean isEquals(Object expected, Object actual) {
        return expected.equals(actual);
    }

    public static void assertEquals(Object expected, Object actual) {
        assertEquals((String)null, (Object)expected, (Object)actual);
    }

    public static void assertNotEquals(String message, Object unexpected, Object actual) {
        if (equalsRegardingNull(unexpected, actual)) {
            failEquals(message, actual);
        }

    }

    public static void assertNotEquals(Object unexpected, Object actual) {
        assertNotEquals((String)null, unexpected, actual);
    }

    private static void failEquals(String message, Object actual) {
        String formatted = "Values should be different. ";
        if (message != null) {
            formatted = message + ". ";
        }

        formatted = formatted + "Actual: " + actual;
        fail(formatted);
    }

    public static void assertNotEquals(String message, long unexpected, long actual) {
        if (unexpected == actual) {
            failEquals(message, actual);
        }

    }

    public static void assertNotEquals(long unexpected, long actual) {
        assertNotEquals((String)null, unexpected, actual);
    }

    public static void assertNotEquals(String message, double unexpected, double actual, double delta) {
        if (!doubleIsDifferent(unexpected, actual, delta)) {
            failEquals(message, actual);
        }

    }

    public static void assertNotEquals(double unexpected, double actual, double delta) {
        assertNotEquals((String)null, unexpected, actual, delta);
    }

    public static void assertNotEquals(float unexpected, float actual, float delta) {
        assertNotEquals((String)null, unexpected, actual, delta);
    }

    public static void assertArrayEquals(String message, Object[] expecteds, Object[] actuals) throws ArrayComparisonFailure {
        internalArrayEquals(message, expecteds, actuals);
    }
}
```



10分                10分      11分     10分        10分            10分

addbc             5d          5b                                              3c

cbdbc                                                          2d                            1a

adcab

dcadc                                         5d
