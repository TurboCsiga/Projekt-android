package com.example.projekt;

import java.util.List;

public class DataResponse {
    List<Item> data;

    public DataResponse(List<Item> data) {
        this.data = data;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }
}
