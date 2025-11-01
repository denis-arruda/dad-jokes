package com.denisarruda.dad_jokes;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/olympics")
public class OlympicController {

    private final ChatClient chatClient;

    @Value("classpath:prompts/olympic-sports.st")
    private Resource olympicPrompt;

    @Value("classpath:docs/olympic-sports.txt")
    private Resource olympicSports;

    public OlympicController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/2024")
    public String get2024OlympicSports(@RequestParam(value = "message", defaultValue = "What sports are being included in the 2024 Summer Olympics?") String message,
                                       @RequestParam(value = "stuffit", defaultValue = "false") boolean stuffit) {
        PromptTemplate promptTemplate = new PromptTemplate(olympicPrompt);
        Map<String, Object> variables = new HashMap<>();
        variables.put("question", message);
        if (stuffit) {
            variables.put("context", olympicSports);
        } else {
            variables.put("context", "");            
        }
        Prompt prompt = promptTemplate.create(variables);
        return chatClient.prompt(prompt).call().content();
    }
    
}
