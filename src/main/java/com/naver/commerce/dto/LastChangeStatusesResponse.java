package com.naver.commerce.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class LastChangeStatusesResponse {

    private List<LastChangeStatus> lastChangeStatuses;
    private int count;

    public List<LastChangeStatus> getLastChangeStatuses() {
        return lastChangeStatuses;
    }

    public void setLastChangeStatuses(List<LastChangeStatus> lastChangeStatuses) {
        this.lastChangeStatuses = lastChangeStatuses;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
