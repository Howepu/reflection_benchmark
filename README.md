# Reflection Benchmark

Этот проект предназначен для сравнения различных способов вызова методов в Java с помощью JMH (Java Microbenchmarking Harness). Он включает несколько подходов, таких как прямой доступ, рефлексия, MethodHandles и использование `LambdaMetafactory`.

## Цель

Цель проекта — измерить производительность различных способов доступа к методам в Java и оценить их подходящие сценарии использования в зависимости от производительности.

Проект включает следующие методы:

1. **Прямой доступ**: прямой вызов метода объекта.
2. **Рефлексия**: использование Java Reflection для вызова метода.
3. **MethodHandles**: использование `MethodHandle` для вызова метода.
4. **LambdaMetafactory**: создание лямбда-выражений с помощью `LambdaMetafactory` для вызова метода.

## Структура проекта

Проект состоит из одного класса `ReflectionBenchmark`, который выполняет бенчмарки для разных способов доступа к методам.

### Класс `ReflectionBenchmark`

- **@State(Scope.Thread)**: аннотация JMH, которая устанавливает локальное состояние для каждого потока.
- **@Setup**: метод, который выполняется перед началом каждого бенчмарка и настраивает объекты для тестирования.
- **Методы-бенчмарки**:
    - `directAccess()`: прямой вызов метода объекта.
    - `reflection()`: вызов метода через рефлексию.
    - `methodHandles()`: вызов метода через `MethodHandle`.
    - `lambdaMetafactory()`: вызов метода через `LambdaMetafactory`.

| Benchmark                                   | Score | Units  |
|---------------------------------------------|-------|--------|
| ReflectionBenchmark.directAccess            | 0.447 | ns/op  |
| ReflectionBenchmark.lambdaMetafactoryAccess | 0.632 | ns/op  |
| ReflectionBenchmark.methodHandlesAccess     | 3.003 | ns/op  |
| ReflectionBenchmark.reflectionAccess        | 5.369 | ns/op  |
