package com.mercadolibre.orbit.app.job.exception;

public class JobStillRunningRuntimeException extends RuntimeException {

    public JobStillRunningRuntimeException(String msg) {
        super(msg);
    }

}
