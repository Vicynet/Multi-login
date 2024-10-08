package org.project.multilogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogModel {
    private String className;
    private String methodName;
    @JsonIgnore
    private Object[] args;
    @JsonIgnore
    private Object result;

    public LogModel(String className, String methodName, Object[] args) {
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }

    public LogModel(String className, String methodName, Object result) {
        this.className = className;
        this.methodName = methodName;
        this.result = result;
    }

}
