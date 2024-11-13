package com.hta2405.unite.dto;

import com.hta2405.unite.enums.DocType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class DocBuy extends Doc {
    private Long docBuyId;
    private List<DocBuyItem> buyList;

    public DocBuy(Long docId,
                  String docWriter,
                  DocType docType,
                  String docTitle,
                  String docContent,
                  LocalDateTime docCreateDate,
                  boolean signFinish,
                  Long docBuyId,
                  List<DocBuyItem> buyList) {
        super(docId, docWriter, docType, docTitle, docContent, docCreateDate, signFinish);
        this.docBuyId = docBuyId;
        this.buyList = buyList;
    }
}
