package com.github.alexeylapin.sbapsd.config;

import lombok.Data;

import java.util.Map;

@Data
public class FilterDef {

    private String type;
    private Map<String, String> params;

}
