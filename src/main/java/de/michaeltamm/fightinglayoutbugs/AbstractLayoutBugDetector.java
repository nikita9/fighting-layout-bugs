/*
 * Copyright 2009 Michael Tamm
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
 */

package de.michaeltamm.fightinglayoutbugs;

import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Michael Tamm
 */
public abstract class AbstractLayoutBugDetector extends AbstractDetector implements LayoutBugDetector {

    /** The directory where screenshots of erroneous pages will be saved. */
    File _screenshotDir;

    public void setScreenshotDir(File screenshotDir) {
        _screenshotDir = screenshotDir;
    }

    protected LayoutBug createLayoutBug(String message, FirefoxDriver driver, boolean[][] buggyPixels) {
        File screenshot = null;
        if (_screenshotDir != null) {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String prefix = getClass().getSimpleName();
            if (prefix.startsWith("Detect")) {
                prefix = prefix.substring("Detect".length());
            }
            try {
                screenshot = File.createTempFile(prefix + "_" + df.format(new Date()) + ".", ".png", _screenshotDir);
                driver.saveScreenshot(screenshot);
            } catch (IOException e) {
                // TODO: LOG.warn("Could not save screenshot.", e);
            }
        }
        final LayoutBug layoutBug = new LayoutBug(message, screenshot);
        try {
            layoutBug.markBuggyPixels(buggyPixels);
        } catch (Exception e) {
            // TODO: LOG.warn("Could not mark buggy pixels in screenshot.", e);            
        }
        return layoutBug;
    }
}
