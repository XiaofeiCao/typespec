// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package azure.resourcemanager.largeheader;

import azure.resourcemanager.largeheader.models.CancelResult;
import com.azure.core.http.HttpPipeline;
import com.azure.core.management.profile.AzureProfile;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.utils.ArmUtils;

public class LargeHeaderTests {
    private final LargeHeaderManager manager = buildManager();

    @Test
    public void testLargeHeader() {
        String name = "header1";
        String resourceGroup = "test-rg";
        CancelResult result = manager.largeHeaders().two6k(resourceGroup, name);

        Assertions.assertTrue(result.succeeded());
    }

    // for LRO operations, we need to override default poll interval
    private static LargeHeaderManager buildManager() {
        try {
            Constructor<LargeHeaderManager> constructor = LargeHeaderManager.class
                .getDeclaredConstructor(HttpPipeline.class, AzureProfile.class, Duration.class);
            setAccessible(constructor);
            return constructor.newInstance(ArmUtils.createTestHttpPipeline(), ArmUtils.getAzureProfile(),
                Duration.ofMillis(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setAccessible(final AccessibleObject accessibleObject) {
        // avoid bug in Java8
        Runnable runnable = () -> accessibleObject.setAccessible(true);
        runnable.run();
    }
}
