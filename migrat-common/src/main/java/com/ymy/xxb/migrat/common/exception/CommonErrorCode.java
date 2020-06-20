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

package com.ymy.xxb.migrat.common.exception;

/**
 * CommonErrorCode.
 *
 * @author wangyi
 */
public class CommonErrorCode {
	
	  /**
     * The constant WARN.
     */
    public static final int WARN = 400;
	
    /**
     * The constant ERROR.
     */
    public static final int ERROR = 500;

    /**
     * The constant SUCCESSFUL.
     */
    public static final int SUCCESSFUL = 200;
    
    /**
     * The constant UNAUTHENTICATED
     */
    public static final int UNAUTHENTICATED = 401;
    
    /**
     * The constant UNAUTHORISE
     */
    public static final int UNAUTHORISE = 403;
    
    /**
     * The constant KICKOUT
     */
    public static final int KICKOUT = 409;
    
    /**
     *  The constant ERROR_MSG
     */
    public static final String ERROR_MSG = "migrat have some exception!";

}
