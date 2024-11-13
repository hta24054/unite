package com.hta2405.unite.dto;

import com.hta2405.unite.enums.DocType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class DocTrip extends Doc {
    private Long docTripId;
    private LocalDate tripStart;
    private LocalDate tripEnd;
    private String tripLoc;
    private String tripPhone;
    private String tripInfo;
    private LocalDate cardStart;
    private LocalDate cardEnd;
    private LocalDate cardReturn;

    public DocTrip(Long docId,
                   String docWriter,
                   DocType docType,
                   String docTitle,
                   String docContent,
                   LocalDateTime docCreateDate,
                   boolean signFinish,
                   Long docTripId,
                   LocalDate tripStart,
                   LocalDate tripEnd,
                   String tripLoc,
                   String tripPhone,
                   String tripInfo,
                   LocalDate cardStart,
                   LocalDate cardEnd,
                   LocalDate cardReturn) {
        super(docId, docWriter, docType, docTitle,
                docContent, docCreateDate, signFinish);
        this.docTripId = docTripId;
        this.tripStart = tripStart;
        this.tripEnd = tripEnd;
        this.tripLoc = tripLoc;
        this.tripPhone = tripPhone;
        this.tripInfo = tripInfo;
        this.cardStart = cardStart;
        this.cardEnd = cardEnd;
        this.cardReturn = cardReturn;
    }
}
