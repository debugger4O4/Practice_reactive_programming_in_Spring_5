package ru.study.chapter_01._05_futures;

import ru.study.chapter_01._02_commons.Input;
import ru.study.chapter_01._02_commons.Output;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class OrdersService {
    private final ShoppingCardService shoppingCardService;

    public OrdersService(ShoppingCardService shoppingCardService) {
        this.shoppingCardService = shoppingCardService;
    }

    void process() {
        Input input = new Input();
        /*
        Получаем экземпляр Future. Можно выполнять другие операции, пока не завершится асинхронная
        обработка запроса. Спустя время проверим звершение работы и получим резльтат. В данном случае
        попытка может окончиться блокировкой || вернет результат немедленно
         */
        Future<Output> result = shoppingCardService.calculate(input);

        System.out.println(shoppingCardService.getClass().getSimpleName() + " execution completed");

        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        OrdersService ordersService1 = new OrdersService(new FutureShoppingCardService());

        ordersService1.process();
        ordersService1.process();

        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));
    }
}
