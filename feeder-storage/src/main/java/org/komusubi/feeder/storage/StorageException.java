package org.komusubi.feeder.storage;

import org.komusubi.feeder.FeederException;

public class StorageException extends FeederException {
    private static final long serialVersionUID = 1L;

    public StorageException() {
        super();
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }

}
