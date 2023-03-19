package com.udemy.info;

import lombok.Getter;
public class ElementInfo {
    @Getter
    protected String keyword;
    @Getter
    protected String locatorValue;
    @Getter
    protected String locatorType;
    @Getter
    protected String deviceInfo;

    @Override
    public String toString() {
        return "Elements[" + "keyword=" + keyword + ",locatorType=" + locatorType + ",locatorValue=" + locatorValue + "deviceInfo=" + deviceInfo + "]";
    }
}
