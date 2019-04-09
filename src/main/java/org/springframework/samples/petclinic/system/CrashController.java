/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import org.apache.logging.log4j.LogManager;
import org.springframework.samples.petclinic.FeatureToggles.timeAnalytics;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Michael Isvy
 * <p/>
 * Also see how a view that resolves to "error" has been added ("error.html").
 */
@Controller
class CrashController {
    private static org.apache.logging.log4j.Logger timeLogAnalytics = LogManager.getLogger("Time spent on welcome ");
    @GetMapping("/oups")
    public String triggerException() {
        timeAnalytics.endTime = System.nanoTime();
        timeLogAnalytics.info("Elapsed Time (ms) : " + timeAnalytics.elapsedTimeMS());
        timeAnalytics.resetTimeAnalystics();
        throw new RuntimeException("Expected: controller used to showcase what "
                + "happens when an exception is thrown");
    }

}
