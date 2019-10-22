package de.hska.iwi.vislab.lab1.example.ws;

import javax.jws.WebService;

@WebService(endpointInterface = "de.hska.iwi.vislab.lab1.example.ws.FibonacciService")
public class FibonacciServiceImpl implements FibonacciService {
    public static final Integer RET_CODE_OK = 0;
    public static final Integer RET_CODE_ERROR = 1;

    @Override
    public FibonacciTO getFibonacci(Integer n) {
        FibonacciTO fibonacciTO = new FibonacciTO();
        if(n >= 1){
            fibonacciTO.setResult(calculateFibonacci(n));
            fibonacciTO.setMessage("Fibonacci berechnet.");
            fibonacciTO.setReturnCode(RET_CODE_OK);
        } else {
            fibonacciTO.setMessage("Parameter n muss >= 1 sein.");
            fibonacciTO.setReturnCode(RET_CODE_ERROR);
        }
        return fibonacciTO;
    }

    private static int calculateFibonacci(int n) {
        switch (n) {
            case 1:
            case 2:
                return 1;
            default:
                return (calculateFibonacci(n - 1) + calculateFibonacci(n - 2));
        }
    }
}
