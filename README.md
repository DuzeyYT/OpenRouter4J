# OpenRouter4J

This is a Java library that implements the most basic features from<br>
[OpenRouter](https://openrouter.ai)

## Basic usage
```java
// Create a new OpenRouter instance with your API key
OpenRouter openRouter = OpenRouter.builder()
        .apiKey("...")
        .build();

// Get a model by its ID, you can find them on the OpenRouter website
// https://openrouter.ai/models
Model model = Model.modelFromId("deepseek/deepseek-r1:free");

// Send a request to the OpenRouter API
try {
  // Send a prompt to the API with the given Model
  PromptResult result = openRouter.sendPrompt(model,
      "Can you please give me a simple hello world code for java?");
  
  // Check if the request was successful, if not print the response
  if (!result.isSuccessful()) {
    System.out.println("Failed to get response for: " + model.getId());
    System.out.println("Response: " + result.getResponse());
    return;
  }
  
  // Print the successful response
  System.out.println(result.getResponseMessage());
} catch (TooManyRequestsException tooManyRequestsException) { // Handle rate limiting
  System.out.println("Too many requests");
} catch (Exception e) { // Handle any other exception
  e.printStackTrace(System.err);
}
```

## Adding to Gradle
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.duzeyyt:openrouter4j:1.0.0'
}
```