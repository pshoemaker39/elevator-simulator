package config;

public class BadDataException extends Exception{
    public BadDataException() {
        super("Input data does not match requirements. Please check ensure all keys are present and all values are Integers.");
    }
}
