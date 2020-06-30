package parkingTicket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.beans.BeanUtils;

 @RestController
 public class ParkingLotController {
     // 서킷브레이커 테스트를 위한 동기 호출
     @RequestMapping(method= RequestMethod.GET, path="/parkingLots/syncCall")
     public void getSyncCall (){
         System.out.println("##### ParkingLotController getSyncCall: ");
         Application.applicationContext.getBean(parkingTicket.external.DeliveryCancelationService.class).syncCall();
     }
 }
