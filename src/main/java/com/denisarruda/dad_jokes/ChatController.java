package com.denisarruda.dad_jokes;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ChatController {
    
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/dad-jokes")
    public String generate(@RequestParam(value = "message", defaultValue = "Tell me a dad joke") String message) {
        return this.chatClient.prompt()
            .user(message)
            .call()
            .content();
    }

    @GetMapping("/jokes")
    public String jokes(@RequestParam(value = "message", defaultValue = "Tell me a dad joke") String message) {
        var system = new SystemMessage("Your primary function is to tell dad jokes. If someone asks you for any other type of joke tell them you only know dad jokes.");
        var user = new UserMessage(message);
        Prompt prompt = new Prompt(system, user);
        return this.chatClient.prompt(prompt)
            .call()
            .content();
    }
}
