package main;

public abstract class Log
{
    // Códigos ANSI para cores
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public static void info(String message)
    {
        System.out.println(BLUE + "Info: " + message + RESET);
    }

    public static void warn(String message)
    {
        System.out.println(YELLOW + "Aviso: " + message + RESET);
    }

    public static void error(String message)
    {
        System.out.println(RED + "Erro: " + message + RESET);
    }
}
