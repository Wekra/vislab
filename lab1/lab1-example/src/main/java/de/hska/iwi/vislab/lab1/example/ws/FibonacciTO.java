package de.hska.iwi.vislab.lab1.example.ws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FibonacciTO {
    @XmlElement(nillable = true)
    private Integer resultOfOperation;
    private String message;
    private Integer returnCode;

    public Integer getResult() {
        return resultOfOperation;
    }

    public void setResult(Integer result) {
        this.resultOfOperation = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }
}
