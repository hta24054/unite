package com.hta2405.unite.factory;

import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.strategy.doc.DocWriter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DocWriterFactory {
    private final Map<DocType, DocWriter> writers = new HashMap<>();

    public DocWriterFactory(List<DocWriter> writerList) {
        //모든 saver를 map에 담음(key = 각 saver에서 구현한 타입명 / value = Saver 객체)
        for (DocWriter writer : writerList) {
            writers.put(writer.getType(), writer);
        }
    }

    //컨트롤러에서 type에 맞는 saver를 Map에서 가져옴
    public DocWriter getWriter(DocType type) {
        return writers.getOrDefault(type, null);
    }
}
