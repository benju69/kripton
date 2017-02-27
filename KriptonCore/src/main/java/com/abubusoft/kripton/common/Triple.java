/*******************************************************************************
 * Copyright 2015, 2016 Francesco Benincasa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/**
 * 
 */
package com.abubusoft.kripton.common;

/**
 * @author Francesco Benincasa (abubusoft@gmail.com)
 *
 * @param <V0>
 * @param <V1>
 * @param <V2>
 */
public class Triple<V0, V1, V2> extends Pair<V0, V1> {

	public Triple() {

	}

	public Triple(V0 v1, V1 v2, V2 v3) {
		super(v1, v2);
		this.value3 = v3;
	}

	public V2 value3;
}