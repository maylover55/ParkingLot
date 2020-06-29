package parkingTicket;

import org.springframework.core.env.Environment;
import parkingTicket.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.lang.System.getProperty;

@Service
public class PolicyHandler{
    @Autowired
    ParkingLotRepository parkingLotRepo;

    @Autowired
    Environment env;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverTicketReserved_Occupy(@Payload TicketReserved ticketReserved){

        if(ticketReserved.isMe()){
            System.out.println("##### listener Occupy : " + ticketReserved.toJson());

            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setTicketReservationId(ticketReserved.getId());
            parkingLot.setReservationDate(ticketReserved.getReservationDate());

            // 일일 주차권 신규 요청의 예약일자 기준, 이미 예약된 주차 공간 건수를 조회
            Date searchDate = ticketReserved.getReservationDate();
            //String searchStatus = "ParkingLot Occupied";
            String searchStatus = StatusType.Occupied;
            List<ParkingLot> searchParkingLots = parkingLotRepo.findByReservationDateAndStatus(searchDate, searchStatus);

            // 일일 주차권 발급 최대 가능 건수를 환경변수에서 추출
            String max =  env.getProperty("maxp");
            System.out.println("##### max parking : " + max);
            System.out.println("##### searchParkingLots size : " + searchParkingLots.size());

            // 일일 주차권 발급 최대 가능 건수를 초과한 경우
            if(searchParkingLots.size() >= Long.valueOf(max)) {
                //parkingLot.setStatus("ParkingLot fully Occupied");
                parkingLot.setStatus(StatusType.FullyOccupied);
            } else { // 일일 주차권 발급 가능한 경우
                //parkingLot.setStatus("ParkingLot Occupied");
                parkingLot.setStatus(StatusType.Occupied);
            }

            parkingLotRepo.save(parkingLot);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverTicketReservationCanceled_Vacate(@Payload TicketReservationCanceled ticketReservationCanceled){

        if(ticketReservationCanceled.isMe()){
            System.out.println("##### listener Vacate : " + ticketReservationCanceled.toJson());

            ParkingLot searchParkingLot = parkingLotRepo.findByTicketReservationId(ticketReservationCanceled.getId());
            //searchParkingLot.setStatus("ParkingLot Vacated");
            searchParkingLot.setStatus(StatusType.Vacated);
            parkingLotRepo.save(searchParkingLot);
        }
    }
}
