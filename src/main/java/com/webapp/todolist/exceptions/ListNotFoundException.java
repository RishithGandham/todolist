package com.webapp.todolist.exceptions;


// idk
public class ListNotFoundException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -8200390010167911343L;

    public ListNotFoundException() {
        // TODO Auto-generated constructor stub
    }

    public ListNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public ListNotFoundException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public ListNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public ListNotFoundException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
