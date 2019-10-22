package de.hska.iwi.vislab.lab1.example.ws;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface FibonacciService {
    FibonacciTO getFibonacci(@WebParam(name="fibonacci-zahl") Integer n);
}
