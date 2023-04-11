package my.lubava.curswork3.model;

import java.time.LocalDateTime;

public class Operation {
    private final Type type;

    private final LocalDateTime localDateTime;

    private final int quantity;

    private Sock sock;

    public Operation(Type type, LocalDateTime localDateTime, int quantity, Sock sock) {
        this.type = type;
        this.localDateTime = localDateTime;
        this.quantity = quantity;
        this.sock = sock;
    }



    public Type getType() {
        return type;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public Sock getSock() {
        return sock;
    }

    public void setSock(Sock sock) {
        this.sock = sock;
    }
}
