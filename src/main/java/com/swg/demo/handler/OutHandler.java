package com.swg.demo.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 任务消息输出处理器
 * @author eguid
 * @since jdk1.7
 * @version 2017年10月13日
 */
public class OutHandler extends Thread {
	/**控制状态 */
	private volatile boolean desstatus = true;
	
	/**读取输出流*/
	private BufferedReader br = null;
	
	/**任务ID*/
	private String id = null;
	
	/**消息处理方法*/
	private OutHandlerMethod ohm;

	/**
	 * 创建输出线程（默认立即开启线程）
	 * @param is
	 * @param id
	 * @param ohm
	 * @return
	 */
	public static OutHandler create(InputStream is, String id,OutHandlerMethod ohm) {
		return create(is, id, ohm,true);
	}
	
	/**
	 * 创建输出线程
	 * @param is
	 * @param id
	 * @param ohm
	 * @param start-是否立即开启线程
	 * @return
	 */
	public static OutHandler create(InputStream is, String id,OutHandlerMethod ohm,boolean start) {
		OutHandler out= new OutHandler(is, id, ohm);
		if(start)
			out.start();
		return out;
	}
	
	public void setOhm(OutHandlerMethod ohm) {
		this.ohm = ohm;
	}
	
	public void setDesStatus(boolean desStatus) {
		this.desstatus = desStatus;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OutHandlerMethod getOhm() {
		return ohm;
	}

	public OutHandler(InputStream is, String id,OutHandlerMethod ohm) {
		br = new BufferedReader(new InputStreamReader(is));
		this.id = id;
		this.ohm=ohm;
	}
	
	/**
	 * 重写线程销毁方法，安全的关闭线程
	 */
	@Override
	public void destroy() {
		setDesStatus(false);
	}

	/**
	 * 执行输出线程
	 */
	@Override
	public void run() {
		String msg = null;
		try {
			System.out.println(id + "开始转码切片！");
			while (desstatus && (msg = br.readLine()) != null) {
				ohm.parse(id,msg);
				if(ohm.isbroken()) {
					System.err.println("检测到到错误，本次任务终止，开始记录数据库本次操作状态为切片失败");
					//TODO
					// 1、如果发生异常中断，设置数据库状态为失败
					// 2、commandManager.stop(id);删除本次任务，停止线程和执行进程
				}
			}
			Thread.sleep(100);
			//走到这里，没有发生异常，没有任何输出日志了，这个时候说明已经完成了，这个时候，就认为是处理成功了
			System.out.println("切片成功！！！！！！！");
			//TODO
			// 1、更新数据库状态为切片成功，这样前端就可以知道哪些任务是切片中、切片成功、切片失败了
			// 2、程序定时任务去扫描状态为切片成功或切片失败，销毁进程和线程
		} catch (IOException e) {
			System.out.println("发生内部异常错误，自动关闭[" + this.getId() + "]线程");
			destroy();
		} catch (InterruptedException e) {
			e.printStackTrace();
			destroy();
		} finally {
			if (this.isAlive()) {
				destroy();
			}
		}
	}

}
