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

/**
 * 
 * @author: wangyi
 *
 */
public class Transformer implements ClassFileTransformer{
	
	private final String AGENT_PACKAGE = "com/ymy/xxb/migrat";
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
			if (className.startsWith(AGENT_PACKAGE)) {
				ClassPool classPool = ClassPool.getDefault();
				CtClass ctClass = null;
				try {	
						ctClass = classPool.get(className.replaceAll("/", "."));
						CtMethod[] methods = ctClass.getDeclaredMethods();
						for (CtMethod method : methods) {
							if (!method.isEmpty()) {
								method.insertBefore("System.out.println(\"before start execute method ... \");");
							}
						}
						ctClass.detach(); // 移除一些不必要的CtClass类
						return ctClass.toBytecode();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			return null;
	}

}
