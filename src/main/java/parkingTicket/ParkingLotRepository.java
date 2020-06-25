package parkingTicket;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface ParkingLotRepository extends PagingAndSortingRepository<ParkingLot, Long>{
    ParkingLot findByTicketReservationId(Long searchReservationId);
    List<ParkingLot> findByReservationDateAndStatus(Date searchDate, String searchStatus);
}