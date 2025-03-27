package com.ecommerce.app.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AnthropicService {
    @POST("v1/messages")
    Call<AnthropicResponse> sendMessage(
        @Header("x-api-key") String apiKey,
        @Header("anthropic-version") String anthropicVersion,
        @Body AnthropicMessage message
    );
}
