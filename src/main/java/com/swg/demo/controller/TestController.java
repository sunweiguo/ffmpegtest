package com.swg.demo.controller;


import com.swg.demo.ffmpeg.CommandManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private CommandManager commandManager;

    @RequestMapping("execute")
    public String execute() throws InterruptedException {
        System.out.println("开始转码切片");
        String id = commandManager.start("ID任务00000001", "E:\\ffmpeg-4.3.1-2021-01-26-full_build\\bin\\ffmpeg -i C:\\Users\\fossi\\Desktop\\切片测试\\happyedu.ts -c:v libx264 -c:a aac -strict -2 -f hls -hls_list_size 0 -hls_time 10 C:\\Users\\fossi\\Desktop\\切片测试\\output\\output.m3u8");
        Thread.sleep(30000);
        System.out.println("转码切片结束"+id);
        //停止任务
        commandManager.stop(id);
        return "success";
    }

}
