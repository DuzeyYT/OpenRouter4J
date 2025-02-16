package com.github.duzeyyt.or4j.result;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class PromptResult {
  private static final Gson GSON = new GsonBuilder().create();

  private String id, provider, model;
  private String responseMessage;
  private boolean successful;
  private JsonObject response;

  public static PromptResult fromJson(String json) {
    JsonObject responseObj = GSON.fromJson(json, JsonObject.class);

    try {
      String id = responseObj.get("id").getAsString();
      String provider = responseObj.get("provider").getAsString();
      String model = responseObj.get("model").getAsString();

      JsonArray choices = responseObj.getAsJsonArray("choices");

      String content = null;
      for (int i = 0; i < choices.size(); i++) {
        JsonElement element = choices.get(i);

        if (element instanceof JsonObject jsonObject) {
          if (!jsonObject.has("message"))
            continue;

          JsonObject message = jsonObject.getAsJsonObject("message");
          content = message.get("content").getAsString();
        }
      }

      if (content == null || content.isEmpty())
        throw new PromptResultException("No content found");

      return new PromptResult(id, provider, model, content, true, responseObj);
    } catch (Exception e) {
      return new PromptResult(null, null, null, null, false, responseObj);
    }
  }

  public static class PromptResultException extends Exception {
    public PromptResultException(String message) {
      super(message);
    }
  }
}