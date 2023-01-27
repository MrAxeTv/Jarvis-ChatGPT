import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import java.util.concurrent.Future;

public class Main {


    private static String speechKey = System.getenv("f0bfd4873d2848249f7e88a4cb2c17ce");
    private static String speechRegion = System.getenv("eastus");

    static ConversationManager conversationManager;

    static {
        try {
            conversationManager = new ConversationManager();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Future<SpeechRecognitionResult> task;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

          speech();
    }

    public static void speech() throws ExecutionException, InterruptedException {


            String subscriptionKey = "f0bfd4873d2848249f7e88a4cb2c17ce";


            String serviceRegion = "eastus";

            var config = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);


            var recognizer = new SpeechRecognizer(config);

            while(true) {
                SpeechRecognitionResult result = recognizer.recognizeOnceAsync().get();

                if (result.getReason() == ResultReason.RecognizedSpeech) {
                    conversationManager.addMessage(result.getText());
                    conversationManager.getResponse(result.getText());
                } else {
                    //System.out.println("Speech not recognized.");
                }
            }
    }




    public static void speak(String args) throws ExecutionException, InterruptedException {


        SpeechConfig speechConfig = SpeechConfig.fromSubscription("f0bfd4873d2848249f7e88a4cb2c17ce", "eastus");

        speechConfig.setSpeechSynthesisVoiceName("en-GB-AlfieNeural");

        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(speechConfig);

        String text = args;
        if (text.isEmpty())
        {
            return;
        }

        SpeechSynthesisResult speechSynthesisResult = speechSynthesizer.SpeakTextAsync(text).get();

        if (speechSynthesisResult.getReason() == ResultReason.SynthesizingAudioCompleted) {
            //System.out.println("Speech synthesized to speaker for text [" + text + "]");
        }
        else if (speechSynthesisResult.getReason() == ResultReason.Canceled) {
            SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(speechSynthesisResult);
            System.out.println("CANCELED: Reason=" + cancellation.getReason());

            if (cancellation.getReason() == CancellationReason.Error) {
                System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                System.out.println("CANCELED: Did you set the speech resource key and region values?");
            }
        }

        //System.exit(0);

    }


}



