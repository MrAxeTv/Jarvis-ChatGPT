import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConversationManager {

    private final String CONVO_STARTER;

    private StringBuilder conversation = new StringBuilder();




    public ConversationManager() throws ExecutionException, InterruptedException {
        this.CONVO_STARTER = "The following is a conversation with an AI who represents a Jarvis AI from IronMan movie " +
                "The AI should call me with my name Chris or Mr.Chris  and try not to stray even if asked about something else. " +
                "Generally it should behave as iron man IA Jarvis." +
                "If you are trying to use any where Tony Start or Mr Start you should replace it with Mr Chris or Chris" +
                "So there is no Tony Stark you replace him with me Mr Chris or Sir Chris, I am now Mr Stark and you need to call me Mr Chris"+
                "Play this Jarvis role the best you can.\n\nHuman: Hey!\n\nAI:";
        startConversation();
    }



    private void startConversation() throws ExecutionException, InterruptedException {

        this.conversation = new StringBuilder(this.CONVO_STARTER);
        getResponse( "Hey JARVIS");
    }



    public void addMessage(String message){
        this.conversation.append("\n\nHuman:").append(message).append("\n\nAI:");
    }

    public void getResponse(String playerMessage) throws ExecutionException, InterruptedException {

        //Use OpenAI to get a response from GPT-3
        OpenAiService service = new OpenAiService("Yout Open-AI key", 0);
        CompletionRequest request = CompletionRequest.builder()
                .prompt(this.conversation.toString())
                .model("text-davinci-003") //Use the latest davinci model
                .temperature(0.50) //How creative the AI should be
                .maxTokens(150) //How many tokens the AI should generate. Tokens are words, punctuation, etc.
                .topP(1.0) //How much diversity the AI should have. 1.0 is the most diverse
                .frequencyPenalty(0.0) //How much the AI should avoid repeating itself
                .presencePenalty(0.6) //How much the AI should avoid repeating the same words
                .stop(List.of("Human:", "AI:")) //Stop the AI from generating more text when it sees these words
                .build();
        var choices = service.createCompletion(request).getChoices();
        var response = choices.get(0).getText(); //what the AI responds with
        this.conversation.append(response.stripLeading());

        System.out.println("YOU: " + playerMessage);
        String respons = response.stripLeading();
        System.out.println("JARVIS: " + respons);
        Main.speak(response);

    }

}
