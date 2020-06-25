package parkingTicket;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Occupied extends AbstractEvent {

    private Long id;
    private Long ticketReservationId;
    private Date reservationDate;
    private String status;

    public Occupied(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getTicketReservationId() {
        return ticketReservationId;
    }

    public void setTicketReservationId(Long ticketReservationId) {
        this.ticketReservationId = ticketReservationId;
    }
    public Date getReservationDate() { return reservationDate; }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
