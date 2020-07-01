package parkingTicket;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="ParkingLot_table")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long ticketReservationId;
    private Date reservationDate;
    private String status;

    @PostPersist
    public void onPostPersist(){
        System.out.println("##### ParkingLot onPostPersist start : ");

        // 일일 주차권 발급 최대 가능 공간 수를 초과한 경우
        //if(this.getStatus().equals("ParkingLot fully Occupied")) {
        if(this.getStatus().equals(StatusType.FullyOccupied)) {
            FullyOccupied fullyOccupied = new FullyOccupied();
            BeanUtils.copyProperties(this, fullyOccupied);
            fullyOccupied.publishAfterCommit();

            System.out.println("##### fullyOccupied published : " + fullyOccupied.toJson());
        //} else if(this.getStatus().equals("ParkingLot Occupied")){ // 일일 주차권 발급 가능한 경우
        } else if(this.getStatus().equals(StatusType.Occupied)){ // 일일 주차권 발급 가능한 경우
            Occupied occupied = new Occupied();
            BeanUtils.copyProperties(this, occupied);
            occupied.publishAfterCommit();

            System.out.println("##### occupied published : " + occupied.toJson());
        }
    }

    @PostUpdate
    public void onPostUpdate(){
        System.out.println("##### ParkingLot onPostUpdate start : ");
        // ticketReservationCanceled 이벤트를 수신 받아, ParkingLot 상태를 "ParkingLot Vacated"로 update 한 이후 처리
        //if(this.getStatus().equals("ParkingLot Vacated")){
        if(this.getStatus().equals(StatusType.Vacated)){
            System.out.println("##### ParkingLot Vacated process start : ");

            Vacated vacated = new Vacated();
            BeanUtils.copyProperties(this, vacated);
            vacated.publishAfterCommit();

            System.out.println("##### vacated published : " + vacated.toJson());

            //Following code causes dependency to external APIs
            // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

            parkingTicket.external.TicketDelivery ticketDelivery = new parkingTicket.external.TicketDelivery();
            // mappings goes here
            ticketDelivery.setParkingLotId(this.getId());
            ticketDelivery.setTicketReservationId(this.getTicketReservationId());
            ticketDelivery.setReservationDate(this.getReservationDate());
            ticketDelivery.setStatus(StatusType.Vacated);

            Application.applicationContext.getBean(parkingTicket.external.DeliveryCancelationService.class)
                    .cancel(ticketDelivery);
        }
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
    public Date getReservationDate() {
        return reservationDate;
    }

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
