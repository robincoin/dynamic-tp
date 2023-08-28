/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.dynamictp.core.support.task.runnable;

import lombok.extern.slf4j.Slf4j;
import org.dromara.dynamictp.core.aware.AwareManager;

import java.util.concurrent.Executor;

/**
 * Enhanced for some thread pools that do not have beforeExecute and afterExecute methods
 *
 * @author kyao
 * @since 1.1.4
 */
@Slf4j
public class EnhancedRunnable implements Runnable {

    private final Runnable runnable;

    private final Executor executor;

    public EnhancedRunnable(Runnable runnable, Executor executor) {
        this.runnable = runnable;
        this.executor = executor;
    }

    public static EnhancedRunnable of(Runnable runnable, Executor executor) {
        return new EnhancedRunnable(runnable, executor);
    }

    @Override
    public void run() {
        AwareManager.beforeExecuteEnhance(executor, Thread.currentThread(), this);
        Throwable t = null;
        try {
            runnable.run();
        } catch (Exception e) {
            t = e;
            throw e;
        } finally {
            AwareManager.afterExecuteEnhance(executor, this, t);
        }
    }
}