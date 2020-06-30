
package parkingTicket.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="TicketDelivery", url="http://TicketDelivery:8080")
//@FeignClient(name="TicketDelivery", url="http://localhost:8083")
public interface DeliveryCancelationService {

    @RequestMapping(method= RequestMethod.POST, path="/deliveryCancelations")
    public void cancel(@RequestBody TicketDelivery ticketDelivery);

    @RequestMapping(method= RequestMethod.GET, path="/deliveryCancelations")
    public void syncCall();
}
