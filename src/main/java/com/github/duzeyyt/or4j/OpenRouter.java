package com.github.duzeyyt.or4j;

import com.github.duzeyyt.or4j.model.Model;
import com.github.duzeyyt.or4j.result.PromptResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class OpenRouter {
  private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
  private String apiKey;

  public PromptResult sendPrompt(Model model, String prompt) throws TooManyRequestsException, InterruptedException, IOException {
    JsonObject jsonRequest = new JsonObject();

    JsonArray messagesObject = new JsonArray();
    JsonObject messageObject = new JsonObject();
    messageObject.addProperty("role", "user");
    messageObject.addProperty("content", prompt);
    messagesObject.add(messageObject);

    jsonRequest.add("messages", messagesObject);
    jsonRequest.addProperty("model", model.getId());

    BodyPublisher body = BodyPublishers.ofString(jsonRequest.toString());

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(API_URL))
        .header("Authorization", "Bearer " + getApiKey())
        .header("Content-Type", "application/json")
        .POST(body).build();

    HttpResponse<String> response = HTTP_CLIENT.send(request,
        HttpResponse.BodyHandlers.ofString());

    PromptResult result = PromptResult.fromJson(response.body());
    if (result.getResponse().has("error")) {
      int code = result.getResponse().get("error").getAsJsonObject().get("code").getAsInt();
      if (code == 429) throw new TooManyRequestsException("Too many requests");
    }

    return result;
  }

  // create too many request exception
  public static class TooManyRequestsException extends Exception {
    public TooManyRequestsException(String message) {
      super(message);
    }
  }
}
