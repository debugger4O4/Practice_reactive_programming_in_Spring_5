package ru.study.chapter_01.callbacks;

import ru.study.chapter_01.commons.Input;

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
public class OrdersService {
    private final ShoppingCardService shoppingCardService;

    public OrdersService(ShoppingCardService shoppingCardService) {
        this.shoppingCardService = shoppingCardService;
    }

    void process() {
        Input input = new Input();
        /*
         Осуществляется асинхронный вызов ShoppingCardService, который тут же возвращает управление, и выполнение
         OrdersService продолжается
         */
        shoppingCardService.calculate(input, output -> {
            System.out.println(shoppingCardService.getClass().getSimpleName() + " execution completed");
        });
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        OrdersService ordersServiceAsync = new OrdersService(new AsyncShoppingCardService());
        OrdersService ordersServiceSync = new OrdersService(new SyncShoppingCardService());

        ordersServiceAsync.process();
        ordersServiceAsync.process();
        ordersServiceSync.process();

        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));

        Thread.sleep(1000);
    }
}