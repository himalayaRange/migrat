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
package com.ymy.xxb.migrat.gateway.exception;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * SpringGateway Global Excetion Handler
 * 
 * SpringGateway Finchley 版本底层使用WebFlux而不是Servlet容器，默认异常返回HTML，需单独处理异常.
 * 
 * @author: wangyi
 *
 */
@Slf4j
public class JsonExceptionHandler implements ErrorWebExceptionHandler{
	/**
	 * MessageReader
	 */
	private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
	
	/**
	 * MessageWriter
	 */
	private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
	
	/**
	 * ViewResolvers
	 */
	private List<ViewResolver> viewResolvers = Collections.emptyList();

	/**
	 * 存储异常信息
	 */
	private ThreadLocal<Map<String, Object>> exceptionHandlerResult = new ThreadLocal<Map<String, Object>>();
	
	/**
     * 参考AbstractErrorWebExceptionHandler
     * @param messageReaders
     */
    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     * @param viewResolvers
     */
    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     * @param messageWriters
     */
    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		/**
		 * 按照异常类型进行处理
		 */
		HttpStatus httpStatus = null;
		String body;
		if (ex instanceof NotFoundException) {
			httpStatus = HttpStatus.NOT_FOUND;
			body = "Service Not Found";
		} else if (ex instanceof ResponseStatusException) {
			ResponseStatusException responseStatusException = (ResponseStatusException) ex;
			httpStatus = responseStatusException.getStatus();
			body = responseStatusException.getMessage();
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            body ="Internal Server Error，{" + ex.getMessage() + "}";
		}
		
		/**
		 * 封装响应结果
		 */
		Map<String, Object> errInfo = Maps.newHashMap();
		errInfo.put("success", false);
		errInfo.put("code", httpStatus.value());
		errInfo.put("message", body);
		Map<String, Object> errResponse = Maps.newHashMap();
		errResponse.put("httpStatus", httpStatus);
		errResponse.put("body", errInfo);
		
		/**
		 * 错误日志
		 */
		ServerHttpRequest request = exchange.getRequest();
		log.error("[全局异常处理]异常请求路径:{},记录异常信息:{}", request.getPath(), ex.getMessage());
		
		if (exchange.getResponse().isCommitted()) {
			return Mono.error(ex);
		}
		exceptionHandlerResult.set(errResponse);
		
		/**
		 * ServerWebExchange 请求转发是通过创建一个新的请求，然后设置请求信息，再转发
		 */
		ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
					.switchIfEmpty(Mono.error(ex))
					.flatMap((handler) -> handler.handle(newRequest))
					.flatMap((response) -> write(exchange, response));
		}
		 /**
	     * 参考DefaultErrorWebExceptionHandler
	     * @param request
	     * @return
	     */
	    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
	        Map<String,Object> result = exceptionHandlerResult.get();
	        return ServerResponse
	        		.status((HttpStatus) result.get("httpStatus"))
	                .contentType(MediaType.APPLICATION_JSON_UTF8)
	                .body(BodyInserters.fromObject(result.get("body")));
	    }

	    /**
	     * 参考AbstractErrorWebExceptionHandler
	     * @param exchange
	     * @param response
	     * @return
	     */
	    private Mono<? extends Void> write(ServerWebExchange exchange,
	                                       ServerResponse response) {
	        exchange.getResponse().getHeaders()
	                .setContentType(response.headers().getContentType());
	        return response.writeTo(exchange, new ResponseContext());
	    }

	    /**
	     * 参考AbstractErrorWebExceptionHandler
	     */
	    private class ResponseContext implements ServerResponse.Context {

	        @Override
	        public List<HttpMessageWriter<?>> messageWriters() {
	            return JsonExceptionHandler.this.messageWriters;
	        }

	        @Override
	        public List<ViewResolver> viewResolvers() {
	            return JsonExceptionHandler.this.viewResolvers;
	        }
	}
	
}
