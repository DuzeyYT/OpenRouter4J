import com.github.duzeyyt.or4j.OpenRouter;
import com.github.duzeyyt.or4j.OpenRouter.TooManyRequestsException;
import com.github.duzeyyt.or4j.model.Model;
import com.github.duzeyyt.or4j.result.PromptResult;

public class SimpleTest {

  public static void main(String[] args) {
    // This is a simple test to check if Lombok is working
    OpenRouter openRouter = OpenRouter.builder()
        .apiKey("...")
        .build();

    Model model = Model.modelFromId("deepseek/deepseek-r1:free");
    System.out.println(model);
    if (model == null) return;

    while (true) {
      try {
        PromptResult result = openRouter.sendPrompt(model,
            "Can you please give me a simple hello world code for java?");
        if (!result.isSuccessful()) {
          System.out.println("Failed to get response for: " + model.getId());
          System.out.println("Response: " + result.getResponse());
          continue;
        }
        System.out.println(result.getResponseMessage());
      } catch (TooManyRequestsException tooManyRequestsException) {
        System.out.println("Too many requests");
      } catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }
  }
}