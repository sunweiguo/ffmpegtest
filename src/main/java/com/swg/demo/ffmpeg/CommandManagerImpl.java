package com.swg.demo.ffmpeg;

import com.swg.demo.handler.TaskHandler;
import com.swg.demo.task.CommandTasker;
import com.swg.demo.task.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;


/**
 * FFmpeg命令操作管理器
 * 
 * @author eguid
 * @since jdk1.7
 * @version 2017年10月13日
 */
@Component
public class CommandManagerImpl implements CommandManager {
	@Autowired
	private TaskHandler taskHandler;
	@Autowired
	private TaskDao taskDao;

	@Override
	public String start(String id, String path) {
		if (id != null && path != null) {
			CommandTasker tasker = taskHandler.process(id, path);
			System.out.println(tasker.toString());
			if (tasker != null) {
				//返回true，说明任务添加到map成功，且不存在重复任务正在执行
				boolean ret = taskDao.add(tasker);
				if (ret) {
					return tasker.getId();
				} else {
					// 持久化信息失败，停止处理
					taskHandler.stop(tasker.getProcess(), tasker.getThread());
					System.err.println("持久化失败，停止任务！");
				}
			}
		}
		return null;
	}

	@Override
	public boolean stop(String id) {
		if (id != null && taskDao.isHave(id)) {
			System.out.println("正在停止任务：" + id);
			CommandTasker tasker = taskDao.get(id);
			if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
				taskDao.remove(id);
				System.out.println("停止任务成功~id="+id);
				return true;
			}
		}
		System.err.println("停止任务失败！id=" + id);
		return false;
	}

	@Override
	public int stopAll(String id) {
		Collection<CommandTasker> list = taskDao.getAll();
		Iterator<CommandTasker> iter = list.iterator();
		CommandTasker tasker = null;
		int index = 0;
		while (iter.hasNext()) {
			tasker = iter.next();
			if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
				taskDao.remove(tasker.getId());
				index++;
			}
		}
		System.out.println("停止了" + index + "个任务！");
		return index;
	}


}

