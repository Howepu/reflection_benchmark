package backend.academy.benchmark;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:UncommentedMain", "checkstyle:MultipleStringLiterals"})
@State(Scope.Thread)
public class ReflectionBenchmark {

    private Student student;
    private Method method;
    private MethodHandle methodHandle;
    private NameGetter lambdaGetter;

    @Setup
    public void setup() throws Throwable {
        student = new Student("Alexander", "Biryukov");
        // Получение метода name() через рефлексию
        method = Student.class.getMethod("name");
        // MethodHandles
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        methodHandle = lookup.findVirtual(Student.class, "name", MethodType.methodType(String.class));
        // LambdaMetafactory
        CallSite callSite = LambdaMetafactory.metafactory(
            lookup,
            "getName",
            MethodType.methodType(NameGetter.class),
            MethodType.methodType(String.class, Student.class),
            methodHandle,
            methodHandle.type()
        );
        lambdaGetter = (NameGetter) callSite.getTarget().invokeExact();
    }

    // Прямой доступ
    @Benchmark
    public void directAccess(Blackhole bh) {
        String name = student.name();
        bh.consume(name);
    }

    // java.lang.reflect.Method
    @Benchmark
    public void reflectionAccess(Blackhole bh) throws InvocationTargetException, IllegalAccessException {
        String name = (String) method.invoke(student);
        bh.consume(name);
    }

    // java.lang.invoke.MethodHandles
    @Benchmark
    public void methodHandlesAccess(Blackhole bh) throws Throwable {
        String name = (String) methodHandle.invoke(student);
        bh.consume(name);
    }

    // LambdaMetafactory
    @Benchmark
    public void lambdaMetafactoryAccess(Blackhole bh) {
        String name = lambdaGetter.getName(student);
        bh.consume(name);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(ReflectionBenchmark.class.getSimpleName())
            .shouldFailOnError(true)
            .shouldDoGC(true)
            .mode(Mode.AverageTime)
            .timeUnit(TimeUnit.NANOSECONDS)
            .forks(1)
            .warmupForks(1)
            .warmupIterations(1)
            .warmupTime(TimeValue.seconds(5))
            .measurementIterations(1)
            .measurementTime(TimeValue.seconds(5))
            .build();

        new Runner(options).run();
    }

    // Вложенные типы должны быть в конце
    @FunctionalInterface
    interface NameGetter {
        String getName(Student student);
    }

    record Student(String name, String surname) {
    }
}
