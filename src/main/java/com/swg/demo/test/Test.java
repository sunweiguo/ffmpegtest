package com.swg.demo.test;


import com.swg.demo.ffmpeg.CommandManager;
import com.swg.demo.ffmpeg.CommandManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 测试入口
 * @author eguid
 * @since jdk1.7
 * @version 2017年10月13日
 */
@Component
public class Test {

	@Autowired
	private CommandManager commandManager;

	/**
	 * 转为HLS流
	 * @throws InterruptedException
	 */
	public void test(){
		System.out.println("开始转码切片");
		String id = commandManager.start("ID任务00000001", "E:\\ffmpeg-4.3.1-2021-01-26-full_build\\bin\\ffmpeg -i C:\\Users\\fossi\\Desktop\\切片测试\\happyedu.ts -c:v libx264 -c:a aac -strict -2 -f hls -hls_list_size 0 -hls_time 10 C:\\Users\\fossi\\Desktop\\切片测试\\output\\output.m3u8");
		System.out.println("转码切片结束"+id);
		// 停止全部任务
//		manager.stopAll();
	}
}