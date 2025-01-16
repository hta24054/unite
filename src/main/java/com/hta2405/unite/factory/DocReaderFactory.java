package com.hta2405.unite.factory;

import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.strategy.doc.DocReader;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DocReaderFactory {
    private final Map<DocType, DocReader> readers = new HashMap<>();

    public DocReaderFactory(List<DocReader> readerList) {
        //모든 reader를 map에 담음[key = 각 reader에서 구현한 타입명(enum) / value = reader 객체]
        for (DocReader reader : readerList) {
            readers.put(reader.getType(), reader);
        }
    }

    //컨트롤러에서 type에 맞는 reader를 Map에서 가져옴
    public DocReader getReader(DocType type) {
        return readers.getOrDefault(type, null);
    }
}

