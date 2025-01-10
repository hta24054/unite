package com.hta2405.unite.factory;

import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.strategy.DocWriter;
import com.hta2405.unite.strategy.GeneralDocWriter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DocWriterFactory {
    private final Map<DocType, DocWriter> writers = new HashMap<>();
    private final DocWriter defaultWriter;

    public DocWriterFactory(List<DocWriter> writerList, GeneralDocWriter generalDocWriter) {
        //기본 writer 설정
        this.defaultWriter = generalDocWriter;

        //모든 writer를 map에 담음(key = 각 writer에서 구현한 타입명 / value = Writer 객체)
        for (DocWriter writer : writerList) {
            writers.put(writer.getType(), writer);
        }
    }

    //컨트롤러에서 type에 맞는 writer를 Map에서 가져옴
    public DocWriter getWriter(DocType type) {
        return writers.getOrDefault(type, defaultWriter);
    }
}

