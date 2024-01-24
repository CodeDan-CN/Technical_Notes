package cn.codedan.security.camelmergersecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.sql.Time;
import java.util.concurrent.*;

/**
 * @ClassName: TestController
 * @Description: TODO
 * @Author: codedan
 * @Date: 2022/12/12 21:51
 * @Version: 1.0.0
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public Callable<String> async2() {
        System.out.println(" 当前线程 外部 " + Thread.currentThread().getName());
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(" 当前线程 内部 " + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(5);
                return "success";
            }
        };
        return callable;
    }

    static ExecutorService workerPool = Executors.newFixedThreadPool(1000);

    @RequestMapping("async")
    public DeferredResult<String> async() {
        DeferredResult<String> defer = new DeferredResult<>((long) 120000);
        workerPool.submit(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            defer.setResult("hello async");
        });
        return defer;
    }


    @RequestMapping("/sync")
    public String sync() throws ExecutionException, InterruptedException {
        Future<String> future = workerPool.submit(() ->
        {
            Thread.sleep(500);
            return "hello sync";
        });
        return future.get();
    }


}
