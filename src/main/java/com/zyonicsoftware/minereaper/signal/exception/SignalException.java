/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.exception;

/**
 * @author Niklas Griese
 * @see java.lang.Throwable
 * @see java.lang.RuntimeException
 * @see java.lang.Exception
 * @see java.io.Serializable
 */
public class SignalException extends RuntimeException {

    private static final long serialVersionUID = -5951107884665155782L;

    public SignalException() {
    }

    public SignalException(final String message) {
        super(message);
    }

    public SignalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SignalException(final Throwable cause) {
        super(cause);
    }

    public SignalException(
            final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
