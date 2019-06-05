package config;

public class BadDataException extends Exception{
    public BadDataException() {
        super("Bad input data");
    }
}
