package com.ymy.xxb.migrat.monitor.skywalking;

import java.lang.instrument.Instrumentation;
import com.ymy.xxb.migrat.monitor.skywalking.transfer.TakeTimeTransformer;
import com.ymy.xxb.migrat.monitor.skywalking.transfer.Transformer;
/**
 * Agent代理入口类
 *
 * @author: wangyi
 *
 */
public class MigratMonitorSkyWalkingAgent {
	/**
	 * 该方法在执行main方法之前执行，与main方法一起运行与JVM中
	 * 
	 * @param agentArgs premain函数参数，程序参数
	 * @param inst 由JVM传入
	 */
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("=================premain方法1执行=====================");
		
		// inst.addTransformer(new Transformer());
		inst.addTransformer(new TakeTimeTransformer());
	}
	
	/**
	 * 如果不存在premain(String agentArgs, Instrumentation inst)
	 * 
	 * @param agentArgs
	 */
	public static void premain(String agentArgs) {
		System.out.println("===================premain方法2执行====================");
		
	}
	
}
