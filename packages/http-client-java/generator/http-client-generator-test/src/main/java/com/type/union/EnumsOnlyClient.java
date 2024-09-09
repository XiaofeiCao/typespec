// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) TypeSpec Code Generator.

package com.type.union;

import com.azure.core.annotation.Generated;
import com.azure.core.annotation.ReturnType;
import com.azure.core.annotation.ServiceClient;
import com.azure.core.annotation.ServiceMethod;
import com.azure.core.exception.ClientAuthenticationException;
import com.azure.core.exception.HttpResponseException;
import com.azure.core.exception.ResourceModifiedException;
import com.azure.core.exception.ResourceNotFoundException;
import com.azure.core.http.rest.RequestOptions;
import com.azure.core.http.rest.Response;
import com.azure.core.util.BinaryData;
import com.type.union.implementation.EnumsOnliesImpl;
import com.type.union.implementation.models.SendRequest3;
import com.type.union.models.EnumsOnlyCases;
import com.type.union.models.GetResponse3;

/**
 * Initializes a new instance of the synchronous UnionClient type.
 */
@ServiceClient(builder = UnionClientBuilder.class)
public final class EnumsOnlyClient {
    @Generated
    private final EnumsOnliesImpl serviceClient;

    /**
     * Initializes an instance of EnumsOnlyClient class.
     * 
     * @param serviceClient the service client implementation.
     */
    @Generated
    EnumsOnlyClient(EnumsOnliesImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * The get operation.
     * <p><strong>Response Body Schema</strong></p>
     * 
     * <pre>{@code
     * {
     *     prop (Required): {
     *         lr: String(left/right/up/down) (Required)
     *         ud: String(up/down) (Required)
     *     }
     * }
     * }</pre>
     * 
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Response<BinaryData> getWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getWithResponse(requestOptions);
    }

    /**
     * The send operation.
     * <p><strong>Request Body Schema</strong></p>
     * 
     * <pre>{@code
     * {
     *     prop (Required): {
     *         lr: String(left/right/up/down) (Required)
     *         ud: String(up/down) (Required)
     *     }
     * }
     * }</pre>
     * 
     * @param sendRequest3 The sendRequest3 parameter.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Response<Void> sendWithResponse(BinaryData sendRequest3, RequestOptions requestOptions) {
        return this.serviceClient.sendWithResponse(sendRequest3, requestOptions);
    }

    /**
     * The get operation.
     * 
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public GetResponse3 get() {
        // Generated convenience method for getWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getWithResponse(requestOptions).getValue().toObject(GetResponse3.class);
    }

    /**
     * The send operation.
     * 
     * @param prop The prop parameter.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public void send(EnumsOnlyCases prop) {
        // Generated convenience method for sendWithResponse
        RequestOptions requestOptions = new RequestOptions();
        SendRequest3 sendRequest3Obj = new SendRequest3(prop);
        BinaryData sendRequest3 = BinaryData.fromObject(sendRequest3Obj);
        sendWithResponse(sendRequest3, requestOptions).getValue();
    }
}
