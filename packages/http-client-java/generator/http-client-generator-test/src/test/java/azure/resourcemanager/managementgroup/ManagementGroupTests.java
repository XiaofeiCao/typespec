// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package azure.resourcemanager.managementgroup;

import azure.resourcemanager.managementgroup.fluent.models.ManagementGroupChildResourceInner;
import azure.resourcemanager.managementgroup.models.ManagementGroupChildResource;
import azure.resourcemanager.managementgroup.models.ManagementGroupChildResourceProperties;
import azure.resourcemanager.managementgroup.models.ProvisioningState;
import com.azure.core.http.rest.PagedIterable;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.utils.ArmUtils;

public class ManagementGroupTests {
    private static final String MANAGEMENT_GROUP_ID = "test-mg";
    private static final String RESOURCE_NAME = "resource";
    private static final String RESOURCE_ID
        = "/providers/Microsoft.Management/managementGroups/test-mg/providers/Microsoft.ManagementGroupChild/managementGroupChildResources/resource";
    private static final String RESOURCE_TYPE = "Microsoft.ManagementGroupChild/managementGroupChildResources";
    private static final String DESCRIPTION_VALID = "valid";
    private static final String DESCRIPTION_VALID2 = "valid2";

    private final ManagementGroupManager manager
        = ManagementGroupManager.authenticate(ArmUtils.createTestHttpPipeline(), ArmUtils.getAzureProfile());

    @Test
    public void testGet() {
        ManagementGroupChildResource resource
            = manager.managementGroupChildResources().get(MANAGEMENT_GROUP_ID, RESOURCE_NAME);
        Assertions.assertEquals(RESOURCE_ID, resource.id());
        Assertions.assertEquals(RESOURCE_NAME, resource.name());
        Assertions.assertEquals(RESOURCE_TYPE, resource.type());
        Assertions.assertNotNull(resource.properties());
        Assertions.assertEquals(DESCRIPTION_VALID, resource.properties().description());
        Assertions.assertEquals(ProvisioningState.SUCCEEDED, resource.properties().provisioningState());
        Assertions.assertNotNull(resource.systemData());
    }

    @Test
    public void testCreateOrUpdate() {
        ManagementGroupChildResourceInner body = new ManagementGroupChildResourceInner()
            .withProperties(new ManagementGroupChildResourceProperties().withDescription(DESCRIPTION_VALID));
        ManagementGroupChildResource resource
            = manager.managementGroupChildResources().createOrUpdate(MANAGEMENT_GROUP_ID, RESOURCE_NAME, body);
        Assertions.assertEquals(RESOURCE_ID, resource.id());
        Assertions.assertEquals(RESOURCE_NAME, resource.name());
        Assertions.assertEquals(RESOURCE_TYPE, resource.type());
        Assertions.assertNotNull(resource.properties());
        Assertions.assertEquals(DESCRIPTION_VALID, resource.properties().description());
        Assertions.assertEquals(ProvisioningState.SUCCEEDED, resource.properties().provisioningState());
        Assertions.assertNotNull(resource.systemData());
    }

    @Test
    public void testUpdate() {
        ManagementGroupChildResourceInner body = new ManagementGroupChildResourceInner()
            .withProperties(new ManagementGroupChildResourceProperties().withDescription(DESCRIPTION_VALID2));
        ManagementGroupChildResource resource
            = manager.managementGroupChildResources().update(MANAGEMENT_GROUP_ID, RESOURCE_NAME, body);
        Assertions.assertEquals(RESOURCE_ID, resource.id());
        Assertions.assertEquals(RESOURCE_NAME, resource.name());
        Assertions.assertEquals(RESOURCE_TYPE, resource.type());
        Assertions.assertNotNull(resource.properties());
        Assertions.assertEquals(DESCRIPTION_VALID2, resource.properties().description());
        Assertions.assertEquals(ProvisioningState.SUCCEEDED, resource.properties().provisioningState());
        Assertions.assertNotNull(resource.systemData());
    }

    @Test
    public void testDelete() {
        manager.managementGroupChildResources().deleteByResourceGroup(MANAGEMENT_GROUP_ID, RESOURCE_NAME);
    }

    @Test
    public void testListByManagementGroup() {
        PagedIterable<ManagementGroupChildResource> resources
            = manager.managementGroupChildResources().listByManagementGroup(MANAGEMENT_GROUP_ID);
        List<ManagementGroupChildResource> resourceList = resources.stream().collect(Collectors.toList());
        Assertions.assertEquals(1, resourceList.size());
        ManagementGroupChildResource resource = resourceList.get(0);
        Assertions.assertEquals(RESOURCE_ID, resource.id());
        Assertions.assertEquals(RESOURCE_NAME, resource.name());
        Assertions.assertEquals(RESOURCE_TYPE, resource.type());
        Assertions.assertNotNull(resource.properties());
        Assertions.assertEquals(DESCRIPTION_VALID, resource.properties().description());
        Assertions.assertEquals(ProvisioningState.SUCCEEDED, resource.properties().provisioningState());
        Assertions.assertNotNull(resource.systemData());
    }
}
