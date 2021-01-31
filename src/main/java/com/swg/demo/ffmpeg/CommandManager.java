package com.swg.demo.ffmpeg;

/**
 * FFmpeg命令操作管理器，可执行FFmpeg命令/停止/查询任务信息
 * 
 * @author eguid
 * @since jdk1.7
 * @version 2016年10月29日
 */
public interface CommandManager {

	public String start(String id, String command);

	public boolean stop(String id);

	public int stopAll(String id);
	
}
