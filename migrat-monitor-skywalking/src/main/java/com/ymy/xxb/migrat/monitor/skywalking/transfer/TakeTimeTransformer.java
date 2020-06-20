/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.ymy.xxb.migrat.monitor.skywalking.transfer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * java探针实现方法耗时
 * 
 * @author: wangyi
 *
 */
public class TakeTimeTransformer implements ClassFileTransformer{
    final String AGENT_PACKAGE = "com/ymy/xxb/migrat";
	final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
	final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		// java自带方法不进行统计
		if (className.startsWith("java") || className.startsWith("sun")) {
			return null;
		}
		if (className.startsWith(AGENT_PACKAGE)) {
			className = className.replace("/", ".");
	        CtClass ctclass = null;
	        try {
	            ctclass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
	            for(CtMethod ctMethod : ctclass.getDeclaredMethods()){
	                String methodName = ctMethod.getName();
	                String newMethodName = methodName + "$old";// 新定义一个方法叫做比如sayHello$old
	                ctMethod.setName(newMethodName);// 将原来的方法名字修改

	                // 创建新的方法，复制原来的方法，名字为原来的名字
	                CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctclass, null);

	                // 构建新的方法体
	                StringBuilder bodyStr = new StringBuilder();
	                bodyStr.append("{");
	                bodyStr.append("System.out.println(\"==============Enter Method: " + className + "." + methodName + " ==============\");");
	                bodyStr.append(prefix);
	                bodyStr.append(newMethodName + "($$);\n");// 调用原有代码，类似于method();($$)表示所有的参数
	                bodyStr.append(postfix);
	                bodyStr.append("System.out.println(\"==============Exit Method: " + className + "." + methodName + " Cost:\" +(endTime - startTime) +\"ms " + "===\");");
	                bodyStr.append("}");

	                newMethod.setBody(bodyStr.toString());// 替换新方法
	                ctclass.addMethod(newMethod);// 增加新方法
	            }
	            ctclass.detach();
	            return ctclass.toBytecode();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
        return null;
	}
	
}