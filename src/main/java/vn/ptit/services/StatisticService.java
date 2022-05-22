package vn.ptit.services;

import vn.ptit.entities.mysql.Order;
import vn.ptit.utils.ChartReport;

public interface StatisticService {
    public ChartReport statisticIncomeLast5Month();
    public ChartReport itemBestSeller();
    public ChartReport statisticUserTotalMoney();
    public long totalOrder();
    public long totalUser();
    public double totalMoney();
}
