package ru.study.chapter_01._04_completion_stage;

import ru.study.chapter_01._02_commons.Input;

public class OrdersService {
    private final ShoppingCardService shoppingCardService;

    public OrdersService(ShoppingCardService shoppingCardService) {
        this.shoppingCardService = shoppingCardService;
    }

    void process() {
        Input input = new Input();

        shoppingCardService
                .calculate(input)
                // Асинхронно вызывается ShoppingCardService и немедленно получается экземпляр CompletionStageShoppingCardService
                .thenAccept(v -> System.out.println(shoppingCardService.getClass().getSimpleName() + " execution completed"));

        System.out.println(shoppingCardService.getClass().getSimpleName() + " calculate called");
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        OrdersService ordersService1 = new OrdersService(new CompletionStageShoppingCardService());

        ordersService1.process();
        ordersService1.process();

        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));

        Thread.sleep(1000);
    }
}
