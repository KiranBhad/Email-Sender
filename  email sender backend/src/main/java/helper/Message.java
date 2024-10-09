package helper;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private String from;

    private String content;

    private List<String> files;

    private String subjects;
}
