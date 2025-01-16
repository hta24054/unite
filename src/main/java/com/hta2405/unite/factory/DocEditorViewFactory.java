package com.hta2405.unite.factory;

import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.strategy.doc.DocEditViewer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DocEditorViewFactory {
    private final Map<DocType, DocEditViewer> editViewers = new HashMap<>();

    public DocEditorViewFactory(List<DocEditViewer> editViewerList) {
        //모든 editor를 map에 담음[key = 각 editor에서 구현한 타입명(enum) / value = editor 객체]
        for (DocEditViewer editViewer : editViewerList) {
            this.editViewers.put(editViewer.getType(), editViewer);
        }
    }

    //컨트롤러에서 type에 맞는 editor를 Map에서 가져옴
    public DocEditViewer getEditViewer(DocType type) {
        return editViewers.getOrDefault(type, null);
    }
}
