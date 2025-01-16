package com.hta2405.unite.factory;

import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.strategy.doc.DocEditor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DocEditorFactory {
    private final Map<DocType, DocEditor> editors = new HashMap<>();

    public DocEditorFactory(List<DocEditor> editorList) {
        //모든 editor를 map에 담음(key = 각 editor 구현한 타입명 / value = Editor 객체)
        for (DocEditor editor : editorList) {
            editors.put(editor.getType(), editor);
        }
    }

    //컨트롤러에서 type에 맞는 editor를 Map에서 가져옴
    public DocEditor getEditor(DocType type) {
        return editors.getOrDefault(type, null);
    }
}
