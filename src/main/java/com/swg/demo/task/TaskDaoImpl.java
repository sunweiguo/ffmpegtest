package com.swg.demo.task;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 任务信息持久层实现
 * 
 * @author eguid
 * @since jdk1.7
 * @version 2016年10月29日
 */
@Component
public class TaskDaoImpl implements TaskDao {
	// 存放任务信息
	private ConcurrentMap<String, CommandTasker> map = new ConcurrentHashMap<>(10);

	@Override
	public CommandTasker get(String id) {
		return map.get(id);
	}

	@Override
	public Collection<CommandTasker> getAll() {
		return map.values();
	}

	@Override
	public boolean add(CommandTasker CommandTasker) {
		String id = CommandTasker.getId();
		if (id != null && !map.containsKey(id)) {
			map.put(CommandTasker.getId(), CommandTasker);
			if(map.get(id)!=null)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public int remove(String id) {
		if(map.remove(id) != null){
			return 1;
		};
		return 0;
	}

	@Override
	public int removeAll() {
		int size = map.size();
		try {
			map.clear();
		} catch (Exception e) {
			return 0;
		}
		return size;
	}

	@Override
	public boolean isHave(String id) {
		return map.containsKey(id);
	}

}
