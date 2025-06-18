// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package azure.resourcemanager.operationtemplates;

import azure.resourcemanager.operationtemplates.fluent.models.WidgetInner;
import azure.resourcemanager.operationtemplates.models.ActionRequest;
import azure.resourcemanager.operationtemplates.models.ActionResult;
import azure.resourcemanager.operationtemplates.models.ChangeAllowanceRequest;
import azure.resourcemanager.operationtemplates.models.ChangeAllowanceResult;
import azure.resourcemanager.operationtemplates.models.Widget;
import azure.resourcemanager.operationtemplates.models.WidgetProperties;
import com.azure.core.http.HttpPipeline;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.util.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.utils.ArmUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.time.Duration;

public class OperationTests {
    private final OperationTemplatesManager manager = buildManager();

    @Test
    public void testOptionalBody() {
        String rgName = "test-rg";
        String resourceName = "widget1";
        Widget widget = manager.optionalBodies().getByResourceGroup(rgName, resourceName);
        Assertions.assertEquals("A test widget", widget.properties().description());
        widget = manager.optionalBodies().patch(rgName, resourceName);
        Assertions.assertEquals("A test widget", widget.properties().description());
        WidgetInner inner = widget.innerModel();
        inner.withProperties(new WidgetProperties().withName("updated-widget").withDescription("Updated description"));
        widget = manager.optionalBodies().patchWithResponse(rgName, resourceName, inner, Context.NONE).getValue();
        Assertions.assertEquals("updated-widget", widget.properties().description());

        ActionResult actionResult = manager.optionalBodies().post(rgName, resourceName);
        Assertions.assertEquals("Action completed successfully", actionResult.result());

        actionResult = manager.optionalBodies().postWithResponse(rgName, resourceName, new ActionRequest().withActionType("perform").withParameters("test-parameters"), Context.NONE).getValue();
        Assertions.assertEquals("Action completed successfully with parameters", actionResult.result());

        ChangeAllowanceResult result = manager.optionalBodies().providerPost();
        Assertions.assertEquals(50, result.totalAllowed());
        result = manager.optionalBodies().providerPostWithResponse(new ChangeAllowanceRequest().withReason("Increased demand").withTotalAllowed(100), Context.NONE).getValue();
        Assertions.assertEquals(100, result.totalAllowed());
    }

    // for LRO operations, we need to override default poll interval
    private static OperationTemplatesManager buildManager() {
        try {
            Constructor<OperationTemplatesManager> constructor = OperationTemplatesManager.class
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
