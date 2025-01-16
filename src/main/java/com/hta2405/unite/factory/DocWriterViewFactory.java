package com.hta2405.unite.factory;

import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.strategy.doc.DocWriteViewer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DocWriterViewFactory {
    private final Map<DocType, DocWriteViewer> writers = new HashMap<>();

    public DocWriterViewFactory(List<DocWriteViewer> writerList) {
        //모든 writer를 map에 담음(key = 각 writer에서 구현한 타입명 / value = Writer 객체)
        for (DocWriteViewer writeViewer : writerList) {
            writers.put(writeViewer.getType(), writeViewer);
        }
    }

    //컨트롤러에서 type에 맞는 writer를 Map에서 가져옴
    public DocWriteViewer getWriteViewer(DocType type) {
        return writers.getOrDefault(type, null);
    }
}

