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
package com.abubusoft.kripton.processor.kripton56.internal;

import com.abubusoft.kripton.annotation.BindType;

@BindType
public enum MessageType {
    SYSTEM_CHANNEL_CREATED,
    SYSTEM_USER_ADDED,
    SYSTEM_USER_EXITS,
    SYSTEM_USER_BANNED,
    SYSTEM_CHANNEL_DESTROYED,
    TEXT,
    ACTION;

}