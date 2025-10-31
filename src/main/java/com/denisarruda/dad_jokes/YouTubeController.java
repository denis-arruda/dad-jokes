package com.denisarruda.dad_jokes;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/youtube")
public class YouTubeController {

    private final ChatClient chatClient;

    @Value("classpath:prompts/music.st")
    private Resource musicPrompt;

    public YouTubeController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    
    @GetMapping("popular")   
    public String findPopularYoutubersByGenre(@RequestParam(value = "genre", defaultValue = "tech") String genre) {
        String message = """
                List 10 of most popular YouTubers in {genre} along with their subscriber counts. If you don't know the 
                answer, just say 'I don't know'.
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre));
        return chatClient.prompt(prompt).call().content();
    }

    @GetMapping("music")   
    public List<String> findPopularMusicsByGenre(@RequestParam(value = "genre", defaultValue = "rock") String genre) {
        ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());
        PromptTemplate promptTemplate = new PromptTemplate(musicPrompt);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre, "format", listOutputConverter.getFormat()));
        return listOutputConverter.convert(chatClient.prompt(prompt).call().content());
    }
}