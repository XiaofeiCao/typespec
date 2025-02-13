// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package type.enums.extensible;

import io.clientcore.core.http.client.HttpClient;
import io.clientcore.core.http.models.HttpHeaders;
import io.clientcore.core.http.models.HttpRequest;
import io.clientcore.core.http.models.HttpResponse;
import io.clientcore.core.http.models.Response;
import io.clientcore.core.http.pipeline.HttpPipelineBuilder;
import io.clientcore.core.http.pipeline.HttpPipelineNextPolicy;
import io.clientcore.core.http.pipeline.HttpPipelinePolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.BiPredicate;


public class ExtensibleClientTest {

    private final ExtensibleClient client = new ExtensibleClientBuilder().buildExtensibleClient();

    @Test
    @Disabled("java.lang.ClassCastException: class java.lang.String cannot be cast to class type.enums.extensible.DaysOfWeekExtensibleEnum")
    public void getKnownValue() {
        DaysOfWeekExtensibleEnum daysOfWeekExtensibleEnum = client.getKnownValue();
        Assertions.assertEquals(DaysOfWeekExtensibleEnum.MONDAY, daysOfWeekExtensibleEnum);
    }

    @Test
    public void getKnownValueWithQuery() {
      HttpClient httpClient = new HttpClient() {
        @Override
        public Response<?> send(HttpRequest httpRequest) throws IOException {
          Assertions.assertTrue(httpRequest.getUri().getQuery() != null && httpRequest.getUri().getQuery().contains("enum=MONDAY"));
          return new HttpResponse<>(httpRequest, 200, new HttpHeaders(), DaysOfWeekExtensibleEnum.MONDAY.getValue());
        }
      };
      ExtensibleClient client = new ExtensibleClientBuilder().httpPipeline(new HttpPipelineBuilder().httpClient(httpClient).build()).httpClient(httpClient).buildExtensibleClient();
      client.getKnownValue();
    }

    @Test
    @Disabled("java.lang.ClassCastException: class java.lang.String cannot be cast to class type.enums.extensible.DaysOfWeekExtensibleEnum")
    public void getUnknownValue() {
        DaysOfWeekExtensibleEnum daysOfWeekExtensibleEnum = client.getUnknownValue();
        Assertions.assertEquals("Weekend", daysOfWeekExtensibleEnum.toString());
    }

    @Test
    public void putKnownValue() {
        DaysOfWeekExtensibleEnum daysOfWeekExtensibleEnum = DaysOfWeekExtensibleEnum.MONDAY;
        client.putKnownValue(daysOfWeekExtensibleEnum);
    }

    @Test
    public void putUnknownValue() {
        DaysOfWeekExtensibleEnum daysOfWeekExtensibleEnum = DaysOfWeekExtensibleEnum.fromValue("Weekend");
        client.putUnknownValue(daysOfWeekExtensibleEnum);
    }

}
