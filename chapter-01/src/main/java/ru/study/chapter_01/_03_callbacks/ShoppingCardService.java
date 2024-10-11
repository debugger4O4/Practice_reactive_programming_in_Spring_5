package ru.study.chapter_01._03_callbacks;


import ru.study.chapter_01._02_commons.Input;
import ru.study.chapter_01._02_commons.Output;

import java.util.function.Consumer;

/*
  Императивный подход

  public class OrdersService {
     private final ShoppingCardService scService;
     void process() {
       Input input = ...;
       Output output = scService.calculate(input);
  }

   В данном случае службы тесно связанны. OrdersService сильно зависит от ShoppingCardService.
   При таком подходе нет возможности осуществлять другие дейсвтия, пока ShoppingCardService занят
   обработкой запроса. scService.calculate(input) блокирует потом Thred, в котором выполняется логика
   OrdersService. Чтобы организовать независимую обработку OrdersService, нужно запустить дополнительный поток
   Thread. С точки зрения реактивной системы такое поведение неприемлимо
 */

/**
 * Решение. Использование техники обратных вызовов для организации между компонентами
 */
public interface ShoppingCardService {
    // Ничего не возвращает, т.е. вызывающий компонент ничего не ждет
    void calculate(Input value, Consumer<Output> c);
}
