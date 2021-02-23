package examples.cash;

import java.math.BigInteger;

public class ExpensiveFunction implements Computable<String, BigInteger> {
    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        /* Здесь могут быть какие-либо тяжёлые запросы и вычисления */
        return new BigInteger(arg);
    }
}
