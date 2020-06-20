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

package com.ymy.xxb.migrat.common.result;

import org.springframework.http.HttpStatus;

import com.ymy.xxb.migrat.common.exception.CommonErrorCode;

import lombok.Data;

import java.io.Serializable;

/**
 * AjaxResult .
 *
 * @author wangyi
 */
@Data
public class SoulResult implements Serializable {

    private static final long serialVersionUID = -2792556188993845048L;
    
    private boolean success;
    
    private Integer code;

    private String message;

    private Object data;

    /**
     * Instantiates a new Soul result.
     */
    public SoulResult() {

    }

    /**
     * Instantiates a new Soul result.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     */
    public SoulResult(final Integer code, final String message, final Object data) {
    	this.success = code == 200 ? true : false;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * return success.
     *
     * @return {@linkplain SoulResult}
     */
    public static SoulResult success() {
        return success("");
    }

    /**
     * return success.
     *
     * @param msg msg
     * @return {@linkplain SoulResult}
     */
    public static SoulResult success(final String msg) {
        return success(msg, null);
    }

    /**
     * return success.
     *
     * @param data this is result data.
     * @return {@linkplain SoulResult}
     */
    public static SoulResult success(final Object data) {
        return success(null, data);
    }

    /**
     * return success.
     *
     * @param msg  this ext msg.
     * @param data this is result data.
     * @return {@linkplain SoulResult}
     */
    public static SoulResult success(final String msg, final Object data) {
        return get(CommonErrorCode.SUCCESSFUL, msg, data);
    }

    /**
     * return error .
     *
     * @param msg error msg
     * @return {@linkplain SoulResult}
     */
    public static SoulResult error(final String msg) {
        return error(CommonErrorCode.ERROR, msg);
    }
    
    public static SoulResult warn(final String msg) {
    	return warn(CommonErrorCode.WARN, msg);
    }
    
    public static SoulResult unauthenticated(final String msg) {
    	return get(CommonErrorCode.UNAUTHENTICATED, msg, null);
    }
    
    public static SoulResult unauthorise(final String msg) {
    	return get(CommonErrorCode.UNAUTHORISE, msg, null);
    }

    /**
     * return error .
     *
     * @param code error code
     * @param msg  error msg
     * @return {@linkplain SoulResult}
     */
    public static SoulResult error(final int code, final String msg) {
        return get(code, msg, null);
    }

    
    public static SoulResult warn(final int code, final String msg) {
        return get(code, msg, null);
    }
    
    /**
     * return timeout .
     *
     * @param msg error msg
     * @return {@linkplain SoulResult}
     */
    public static SoulResult timeout(final String msg) {
        return error(HttpStatus.REQUEST_TIMEOUT.value(), msg);
    }

    private static SoulResult get(final int code, final String msg, final Object data) {
        return new SoulResult(code, msg, data);
    }

}
