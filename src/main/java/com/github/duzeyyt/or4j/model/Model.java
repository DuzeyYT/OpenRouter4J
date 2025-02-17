package com.github.duzeyyt.or4j.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class Model {
  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
  private static final Gson GSON = new GsonBuilder().create();
  private static final String MODEL_API = "https://openrouter.ai/api/v1/models";

  private String id, name;
  private boolean free;

  public static List<Model> getModels() {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(MODEL_API))
        .GET()
        .build();

    try {
      HttpResponse<String> response = HTTP_CLIENT.send(request,
          HttpResponse.BodyHandlers.ofString());

      JsonObject jsonObject = GSON.fromJson(response.body(), JsonObject.class);
      JsonArray data = jsonObject.getAsJsonArray("data");

      List<Model> models = new ArrayList<>();
      for (int i = 0; i < data.size(); i++) {
        JsonObject model = data.get(i).getAsJsonObject();

        String id = model.get("id").getAsString();
        String name = model.get("name").getAsString();

        JsonObject pricing = model.get("pricing").getAsJsonObject();

        boolean free = pricing.get("prompt").getAsDouble() == 0;

        models.add(new Model(id, name, free));
      }

      return models;
    } catch (Exception ignored) {
      return new ArrayList<>();
    }
  }

  public static Model modelFromId(String id) {
    List<Model> models = getModels();
    for (Model model : models) {
      if (model.getId().equals(id)) {
        return model;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "Model{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", free=" + free +
        '}';
  }
}
