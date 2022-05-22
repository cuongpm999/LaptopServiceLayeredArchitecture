package vn.ptit.apis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ptit.dtos.ShipmentDTO;
import vn.ptit.services.StatisticService;
import vn.ptit.utils.ChartReport;

import java.util.List;

@RestController
@RequestMapping(path = "/api/statistic")
@Slf4j
public class StatisticApi {
    @Autowired private StatisticService statisticService;

    @GetMapping(path = "/income-last-5-month", produces = "application/json")
    public ResponseEntity<ChartReport> statisticIncomeLast6Month() {
        ChartReport res = statisticService.statisticIncomeLast5Month();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/item-best-seller", produces = "application/json")
    public ResponseEntity<ChartReport> statisticItemBestSeller() {
        ChartReport res = statisticService.itemBestSeller();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/user-total-money", produces = "application/json")
    public ResponseEntity<ChartReport> statisticUserTotalMoney() {
        ChartReport res = statisticService.statisticUserTotalMoney();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/total-order", produces = "application/json")
    public ResponseEntity<Long> statisticTotalOrder() {
        return new ResponseEntity<>(statisticService.totalOrder(), HttpStatus.OK);
    }

    @GetMapping(path = "/total-user", produces = "application/json")
    public ResponseEntity<Long> statisticTotalUser() {
        return new ResponseEntity<>(statisticService.totalUser(), HttpStatus.OK);
    }

    @GetMapping(path = "/total-money", produces = "application/json")
    public ResponseEntity<Double> statisticTotalMoney() {
        return new ResponseEntity<>(statisticService.totalMoney(), HttpStatus.OK);
    }
}
