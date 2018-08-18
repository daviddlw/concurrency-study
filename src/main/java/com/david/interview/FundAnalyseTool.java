package com.david.interview;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.lang3.time.DateUtils;

/**
 * 基金分析工具類
 * 
 * @author Administrator
 *
 */
public class FundAnalyseTool {

	private static final BigDecimal DEFAULT_VALUE = new BigDecimal("0");
	private static final String YYYY_MM_DD_MM_SS = "yyyy-MM-dd";

	public static BigDecimal analyse(List<FundNetValue> netValues) {
		if (netValues == null || netValues.size() == 0) {
			return DEFAULT_VALUE;
		}

		sortFundNetValueList(netValues);
		/**
		 * 淨值比較器
		 */
		Comparator<BigDecimal> netComparator = new Comparator<BigDecimal>() {

			@Override
			public int compare(BigDecimal o1, BigDecimal o2) {
				if (o1.compareTo(o2) == 1) {
					return -1;
				} else if (o1.compareTo(o2) == -1) {
					return 1;
				} else {
					return 0;
				}
			}
		};

		/**
		 * 獲得一段時間內最大淨值的最小對堆，用優先隊列實現
		 */
		PriorityQueue<BigDecimal> maxWithDrawQueue = new PriorityQueue<>(netValues.size(), netComparator);
		FundNetValue current = null;
		for (int i = 0; i < netValues.size(); i++) {
			current = netValues.get(i);
			NetValueDTO netValueDTO = new NetValueDTO(current.getNetValueDate(), i + 1, new ArrayList<BigDecimal>());
			PriorityQueue<BigDecimal> queue = new PriorityQueue<BigDecimal>(netValues.size(), netComparator);
			for (int j = 1; j < netValues.size(); j++) {
				BigDecimal netValue = current.getNetValue().subtract(netValues.get(j).getNetValue());
				if (current.getNetValue().compareTo(new BigDecimal("0")) == 0) {
					return DEFAULT_VALUE;
				}
				netValue = netValue.divide(current.getNetValue(), BigDecimal.ROUND_HALF_UP);
				netValueDTO.getValues().add(netValue);
			}
			queue.addAll(netValueDTO.getValues());
			System.out.println("queue=" + queue + ", netValue=" + netValueDTO);
			maxWithDrawQueue.add(queue.peek());
		}
		System.out.println("maxWithDrawQueue=" + maxWithDrawQueue);
		return maxWithDrawQueue.peek();
	}

	private static void sortFundNetValueList(List<FundNetValue> netValues) {
		Collections.sort(netValues, new Comparator<FundNetValue>() {

			@Override
			public int compare(FundNetValue o1, FundNetValue o2) {
				return o1.getNetValueDate().compareTo(o2.getNetValueDate());
			}
		});
	}

	public static void main(String[] args) throws ParseException {
		FundNetValue f1 = new FundNetValue(DateUtils.parseDate("2018-05-03", YYYY_MM_DD_MM_SS), new BigDecimal("1.00"));
		FundNetValue f2 = new FundNetValue(DateUtils.parseDate("2018-05-02", YYYY_MM_DD_MM_SS), new BigDecimal("2.12"));
		FundNetValue f3 = new FundNetValue(DateUtils.parseDate("2018-05-01", YYYY_MM_DD_MM_SS), new BigDecimal("3.67"));
		FundNetValue f4 = new FundNetValue(DateUtils.parseDate("2018-05-08", YYYY_MM_DD_MM_SS), new BigDecimal("6.88"));
		FundNetValue f5 = new FundNetValue(DateUtils.parseDate("2018-05-09", YYYY_MM_DD_MM_SS), new BigDecimal("5.12"));
		FundNetValue f6 = new FundNetValue(DateUtils.parseDate("2018-05-10", YYYY_MM_DD_MM_SS), new BigDecimal("5.12"));
		FundNetValue f7 = new FundNetValue(DateUtils.parseDate("2018-05-16", YYYY_MM_DD_MM_SS), new BigDecimal("9.12"));
		List<FundNetValue> list = Arrays.asList(new FundNetValue[]{f1, f2, f3, f4, f5, f6, f7});
		BigDecimal result = FundAnalyseTool.analyse(list);
		System.out.println(result);

	}
}
