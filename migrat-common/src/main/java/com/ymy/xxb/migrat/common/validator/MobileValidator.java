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

package com.ymy.xxb.migrat.common.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.ymy.xxb.migrat.common.annotation.IsMobile;

/**
 * @author wangyi
 *
 */
@Component
public class MobileValidator implements ConstraintValidator<IsMobile, String> {

	@Override
	public void initialize(IsMobile isMobile) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			if (StringUtils.isBlank(value)) {
				return false;
			} else {
				String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(value);
				boolean matches = matcher.matches();
				return matches;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
