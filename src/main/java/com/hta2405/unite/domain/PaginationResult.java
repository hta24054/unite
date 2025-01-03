package com.hta2405.unite.domain;

import lombok.Getter;

@Getter
public class PaginationResult {
    private final int maxpage;
    private final int startpage;
    private final int endpage;

    public PaginationResult(int page, int limit, int listcount) {
        int maxpage = (listcount + limit - 1) / limit; //총 페이지 수
        //현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21 등..)
        int startpage = ((page - 1) / 10) * 10 + 1;
        //현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30 등..)
        int endpage = startpage + 10 - 1;
        if (endpage > maxpage) endpage = maxpage;

        this.maxpage = maxpage;
        this.endpage = endpage;
        this.startpage = startpage;
    }
}
