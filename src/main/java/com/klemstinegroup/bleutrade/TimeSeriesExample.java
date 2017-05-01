package com.klemstinegroup.bleutrade;

import java.io.*;

import java.util.List;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import org.nd4j.linalg.api.ndarray.INDArray;
import weka.core.Instances;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.timeseries.WekaForecaster;

import javax.swing.*;

/**
 * Example of using the time series forecasting API. To compile and
 * run the CLASSPATH will need to contain:
 * <p>
 * weka.jar (from your weka distribution)
 * pdm-timeseriesforecasting-ce-TRUNK-SNAPSHOT.jar (from the time series package)
 * jcommon-1.0.14.jar (from the time series package lib directory)
 * jfreechart-1.0.13.jar (from the time series package lib directory)
 */
public class TimeSeriesExample {

    private static int stepsToPredict=100;
    private static int cnter;
    private final int howManyRealToShow=stepsToPredict*2;

    public TimeSeriesExample() throws Exception {
        // path to the Australian wine data included with the time series forecasting
        // package
//      String data="% Sales of Australian wine (thousands of litres)\n" +
//              "% from Jan 1980 - 1980-07-01 1995. Data is sorted in\n" +
//              "% time\n" +
//              "@relation wine2\n" +
//              "\n" +
//              "@attribute Fortified numeric\n" +
//              "@attribute Dry-white numeric\n" +
//              "@attribute Sweet-white numeric\n" +
//              "@attribute Red numeric\n" +
//              "@attribute Rose numeric\n" +
//              "@attribute Sparkling numeric\n" +
//              "@attribute Date date 'yyyy-MM-dd'\n" +
//              "\n" +
//              "@data\n" +
//              "2585,1954,85,464,112,1686,1980-01-01\n" +
//              "3368,2302,89,675,118,1591,1980-02-01\n" +
//              "3210,3054,109,703,129,2304,1980-03-01\n" +
//              "3111,2414,95,887,99,1712,1980-04-01\n" +
//              "3756,2226,91,1139,116,1471,1980-05-01\n" +
//              "4216,2725,95,1077,168,1377,1980-06-01\n" +
//              "5225,2589,96,1318,118,1966,1980-07-01\n" +
//              "4426,3470,128,1260,129,2453,1980-08-01\n" +
//              "3932,2400,124,1120,205,1984,1980-09-01\n" +
//              "3816,3180,111,963,147,2596,1980-10-01\n" +
//              "3661,4009,178,996,150,4087,1980-11-01\n" +
//              "3795,3924,140,960,267,5179,1980-12-01\n" +
//              "2285,2072,150,530,126,1530,1981-01-01\n" +
//              "2934,2434,132,883,129,1523,1981-02-01\n" +
//              "2985,2956,155,894,124,1633,1981-03-01\n" +
//              "3646,2828,132,1045,97,1976,1981-04-01\n" +
//              "4198,2687,91,1199,102,1170,1981-05-01\n" +
//              "4935,2629,94,1287,127,1480,1981-06-01\n" +
//              "5618,3150,109,1565,222,1781,1981-07-01\n" +
//              "5454,4119,155,1577,214,2472,1981-08-01\n" +
//              "3624,3030,123,1076,118,1981,1981-09-01\n" +
//              "2898,3055,130,918,141,2273,1981-10-01\n" +
//              "3802,3821,150,1008,154,3857,1981-11-01\n" +
//              "2369,4001,163,1063,226,4551,1981-12-01\n" +
//              "2369,2529,101,544,89,1510,1982-01-01\n" +
//              "2511,2472,123,635,77,1329,1982-02-01\n" +
//              "3079,3134,127,804,82,1518,1982-03-01\n" +
//              "3728,2789,112,980,97,1790,1982-04-01\n" +
//              "4151,2758,108,1018,127,1537,1982-05-01\n" +
//              "4326,2993,116,1064,121,1449,1982-06-01\n" +
//              "5054,3282,153,1404,117,1954,1982-07-01\n" +
//              "5138,3437,163,1286,117,1897,1982-08-01\n" +
//              "3310,2804,128,1104,106,1706,1982-09-01\n" +
//              "3508,3076,142,999,112,2514,1982-10-01\n" +
//              "3790,3782,170,996,134,3593,1982-11-01\n" +
//              "3446,3889,214,1015,169,4524,1982-12-01\n" +
//              "2127,2271,134,615,75,1609,1983-01-01\n" +
//              "2523,2452,122,722,108,1638,1983-02-01\n" +
//              "3017,3084,142,832,115,2030,1983-03-01\n" +
//              "3265,2522,156,977,85,1375,1983-04-01\n" +
//              "3822,2769,145,1270,101,1320,1983-05-01\n" +
//              "4027,3438,169,1437,108,1245,1983-06-01\n" +
//              "4420,2839,134,1520,109,1600,1983-07-01\n" +
//              "5255,3746,165,1708,124,2298,1983-08-01\n" +
//              "4009,2632,156,1151,105,2191,1983-09-01\n" +
//              "3074,2851,111,934,95,2511,1983-10-01\n" +
//              "3465,3871,165,1159,135,3440,1983-11-01\n" +
//              "3718,3618,197,1209,164,4923,1983-12-01\n" +
//              "1954,2389,124,699,88,1609,1984-01-01\n" +
//              "2604,2344,124,830,85,1435,1984-02-01\n" +
//              "3626,2678,139,996,112,2061,1984-03-01\n" +
//              "2836,2492,137,1124,87,1789,1984-04-01\n" +
//              "4042,2858,127,1458,91,1567,1984-05-01\n" +
//              "3584,2246,134,1270,87,1404,1984-06-01\n" +
//              "4225,2800,136,1753,87,1597,1984-07-01\n" +
//              "4523,3869,171,2258,142,3159,1984-08-01\n" +
//              "2892,3007,112,1208,95,1759,1984-09-01\n" +
//              "2876,3023,110,1241,108,2504,1984-10-01\n" +
//              "3420,3907,147,1265,139,4273,1984-11-01\n" +
//              "3159,4209,196,1828,159,5274,1984-12-01\n" +
//              "2101,2353,112,809,61,1771,1985-01-01\n" +
//              "2181,2570,118,997,82,1682,1985-02-01\n" +
//              "2724,2903,125,1164,124,1846,1985-03-01\n" +
//              "2954,2910,122,1205,93,1589,1985-04-01\n" +
//              "4092,3782,120,1538,108,1896,1985-05-01\n" +
//              "3470,2759,118,1513,75,1379,1985-06-01\n" +
//              "3990,2931,281,1378,87,1645,1985-07-01\n" +
//              "4239,3641,344,2083,103,2512,1985-08-01\n" +
//              "2855,2794,366,1357,90,1771,1985-09-01\n" +
//              "2897,3070,362,1536,108,3727,1985-10-01\n" +
//              "3433,3576,580,1526,123,4388,1985-11-01\n" +
//              "3307,4106,523,1376,129,5434,1985-12-01\n" +
//              "1914,2452,348,779,57,1606,1986-01-01\n" +
//              "2214,2206,246,1005,65,1523,1986-02-01\n" +
//              "2320,2488,197,1193,67,1577,1986-03-01\n" +
//              "2714,2416,306,1522,71,1605,1986-04-01\n" +
//              "3633,2534,279,1539,76,1765,1986-05-01\n" +
//              "3295,2521,280,1546,67,1403,1986-06-01\n" +
//              "4377,3093,358,2116,110,2584,1986-07-01\n" +
//              "4442,3903,431,2326,118,3318,1986-08-01\n" +
//              "2774,2907,448,1596,99,1562,1986-09-01\n" +
//              "2840,3025,433,1356,85,2349,1986-10-01\n" +
//              "2828,3812,504,1553,107,3987,1986-11-01\n" +
//              "3758,4209,579,1613,141,5891,1986-12-01\n" +
//              "1610,2138,384,814,58,1389,1987-01-01\n" +
//              "1968,2419,335,1150,65,1442,1987-02-01\n" +
//              "2248,2622,320,1225,70,1548,1987-03-01\n" +
//              "3262,2912,496,1691,86,1935,1987-04-01\n" +
//              "3164,2708,448,1759,93,1518,1987-05-01\n" +
//              "2972,2798,377,1754,74,1250,1987-06-01\n" +
//              "4041,3254,523,2100,87,1847,1987-07-01\n" +
//              "3402,2895,468,2062,73,1930,1987-08-01\n" +
//              "2898,3263,428,2012,101,2638,1987-09-01\n" +
//              "2555,3736,520,1897,100,3114,1987-10-01\n" +
//              "3056,4077,493,1964,96,4405,1987-11-01\n" +
//              "3717,4097,662,2186,157,7242,1987-12-01\n" +
//              "1755,2175,304,966,63,1853,1988-01-01\n" +
//              "2193,3138,308,1549,115,1779,1988-02-01\n" +
//              "2198,2823,313,1538,70,2108,1988-03-01\n" +
//              "2777,2498,328,1612,66,2336,1988-04-01\n" +
//              "3076,2822,354,2078,67,1728,1988-05-01\n" +
//              "3389,2738,338,2137,83,1661,1988-06-01\n" +
//              "4231,4137,483,2907,79,2230,1988-07-01\n" +
//              "3118,3515,355,2249,77,1645,1988-08-01\n" +
//              "2524,3785,439,1883,102,2421,1988-09-01\n" +
//              "2280,3632,290,1739,116,3740,1988-10-01\n" +
//              "2862,4504,352,1828,100,4988,1988-11-01\n" +
//              "3502,4451,454,1868,135,6757,1988-12-01\n" +
//              "1558,2550,306,1138,71,1757,1989-01-01\n" +
//              "1940,2867,303,1430,60,1394,1989-02-01\n" +
//              "2226,3458,344,1809,89,1982,1989-03-01\n" +
//              "2676,2961,254,1763,74,1650,1989-04-01\n" +
//              "3145,3163,309,2200,73,1654,1989-05-01\n" +
//              "3224,2880,310,2067,91,1406,1989-06-01\n" +
//              "4117,3331,379,2503,86,1971,1989-07-01\n" +
//              "3446,3062,294,2141,74,1968,1989-08-01\n" +
//              "2482,3534,356,2103,87,2608,1989-09-01\n" +
//              "2349,3622,318,1972,87,3845,1989-10-01\n" +
//              "2986,4464,405,2181,109,4514,1989-11-01\n" +
//              "3163,5411,545,2344,137,6694,1989-12-01\n" +
//              "1651,2564,268,970,43,1720,1990-01-01\n" +
//              "1725,2820,243,1199,69,1321,1990-02-01\n" +
//              "2622,3508,273,1718,73,1859,1990-03-01\n" +
//              "2316,3088,273,1683,77,1628,1990-04-01\n" +
//              "2976,3299,236,2025,69,1615,1990-05-01\n" +
//              "3263,2939,222,2051,76,1457,1990-06-01\n" +
//              "3951,3320,302,2439,78,1899,1990-07-01\n" +
//              "2917,3418,285,2353,70,1605,1990-08-01\n" +
//              "2380,3604,309,2230,83,2424,1990-09-01\n" +
//              "2458,3495,322,1852,65,3116,1990-10-01\n" +
//              "2883,4163,362,2147,110,4286,1990-11-01\n" +
//              "2579,4882,471,2286,132,6047,1990-12-01\n" +
//              "1330,2211,198,1007,54,1902,1991-01-01\n" +
//              "1686,3260,253,1665,55,2049,1991-02-01\n" +
//              "2457,2992,173,1642,66,1874,1991-03-01\n" +
//              "2514,2425,186,1518,65,1279,1991-04-01\n" +
//              "2834,2707,185,1831,60,1432,1991-05-01\n" +
//              "2757,3244,105,2207,65,1540,1991-06-01\n" +
//              "3425,3965,228,2822,96,2214,1991-07-01\n" +
//              "3006,3315,214,2393,55,1857,1991-08-01\n" +
//              "2369,3333,189,2306,71,2408,1991-09-01\n" +
//              "2017,3583,270,1785,63,3252,1991-10-01\n" +
//              "2507,4021,277,2047,74,3627,1991-11-01\n" +
//              "3168,4904,378,2171,106,6153,1991-12-01\n" +
//              "1545,2252,185,1212,34,1577,1992-01-01\n" +
//              "1643,2952,182,1335,47,1667,1992-02-01\n" +
//              "2112,3573,258,2011,56,1993,1992-03-01\n" +
//              "2415,3048,179,1860,53,1997,1992-04-01\n" +
//              "2862,3059,197,1954,53,1783,1992-05-01\n" +
//              "2822,2731,168,2152,55,1625,1992-06-01\n" +
//              "3260,3563,250,2835,67,2076,1992-07-01\n" +
//              "2606,3092,211,2224,52,1773,1992-08-01\n" +
//              "2264,3478,260,2182,46,2377,1992-09-01\n" +
//              "2250,3478,234,1992,51,3088,1992-10-01\n" +
//              "2545,4308,305,2389,58,4096,1992-11-01\n" +
//              "2856,5029,347,2724,91,6119,1992-12-01\n" +
//              "1208,2075,203,891,33,1494,1993-01-01\n" +
//              "1412,3264,217,1247,40,1564,1993-02-01\n" +
//              "1964,3308,227,2017,46,1898,1993-03-01\n" +
//              "2018,3688,242,2257,45,2121,1993-04-01\n" +
//              "2329,3136,185,2255,41,1831,1993-05-01\n" +
//              "2660,2824,175,2255,55,1515,1993-06-01\n" +
//              "2923,3644,252,3057,57,2048,1993-07-01\n" +
//              "2626,4694,319,3330,54,2795,1993-08-01\n" +
//              "2132,2914,202,1896,46,1749,1993-09-01\n" +
//              "1772,3686,254,2096,52,3339,1993-10-01\n" +
//              "2526,4358,336,2374,48,4227,1993-11-01\n" +
//              "2755,5587,431,2535,77,6410,1993-12-01\n" +
//              "1154,2265,150,1041,30,1197,1994-01-01\n" +
//              "1568,3685,280,1728,35,1968,1994-02-01\n" +
//              "1965,3754,187,2201,42,1720,1994-03-01\n" +
//              "2659,3708,279,2455,48,1725,1994-04-01\n" +
//              "2354,3210,193,2204,44,1674,1994-05-01\n" +
//              "2592,3517,227,2660,45,1693,1994-06-01\n" +
//              "2714,3905,225,3670,?,2031,1994-07-01\n" +
//              "2294,3670,205,2665,?,1495,1994-08-01\n" +
//              "2416,4221,259,2639,46,2968,1994-09-01\n" +
//              "2016,4404,254,2226,51,3385,1994-10-01\n" +
//              "2799,5086,275,2586,63,3729,1994-11-01\n" +
//              "2467,5725,394,2684,84,5999,1994-12-01\n" +
//              "1153,2367,159,1185,30,1070,1995-01-01\n" +
//              "1482,3819,230,1749,39,1402,1995-02-01\n" +
//              "1818,4067,188,2459,45,1897,1995-03-01\n" +
//              "2262,4022,195,2618,52,1862,1995-04-01\n" +
//              "2612,3937,189,2585,28,1670,1995-05-01\n" +
//              "2967,4365,220,3310,40,1688,1995-06-01\n" +
//              "3179,4290,274,3923,62,2031,1995-07-01";
        String data = "@relation stock\n" +
                "@attribute time numeric\n" +
                "@attribute bid numeric\n" +
                "@attribute ask numeric\n" +
                "@data\n";
        Scanner sc = new Scanner(new File("./rnnRegression/passengers_raw.csv"));
        XYSeries s1 = new XYSeries("bid");
        XYSeries s2 = new XYSeries("ask");
        long cnt = 0;
        while (sc.hasNext()) {
            String ggh = sc.nextLine();
            String[] sp = ggh.split(",");
            s1.add(Long.parseLong(sp[0])/900000L, Double.parseDouble(sp[1]));
            s2.add(Long.parseLong(sp[0])/900000L, Double.parseDouble(sp[2]));
            data += ggh + "\n";
            cnt=Math.max(cnt,Long.parseLong(sp[0])/900000L);
        }
        System.out.println(data);
        while(s1.getItemCount()>howManyRealToShow)s1.remove(0);
        while(s2.getItemCount()>howManyRealToShow)s2.remove(0);
        // load the wine data
        Instances wine = new Instances(new BufferedReader(new StringReader(data)));

        // new forecaster
        WekaForecaster forecaster = new WekaForecaster();

        // set the targets we want to forecast. This method calls
        // setFieldsToLag() on the lag maker object for us
        forecaster.setFieldsToForecast("bid,ask");

        // default underlying classifier is SMOreg (SVM) - we'll use
        // gaussian processes for regression instead
        forecaster.setBaseForecaster(new GaussianProcesses());

        forecaster.getTSLagMaker().setTimeStampField("time"); // date time stamp
        forecaster.getTSLagMaker().setMinLag(1);
        forecaster.getTSLagMaker().setMaxLag(stepsToPredict); // monthly data


        // build the model
        forecaster.buildForecaster(wine, System.out);

        // prime the forecaster with enough recent historical data
        // to cover up to the maximum lag. In our case, we could just supply
        // the 12 most recent historical instances, as this covers our maximum
        // lag period
        forecaster.primeForecaster(wine);

        // forecast for 12 units (months) beyond the end of the
        // training data
        List<List<NumericPrediction>> forecast = forecaster.forecast(stepsToPredict, System.out);

        // output the predictions. Outer list is over the steps; inner list is over
        // the targets
        XYSeries s3 = new XYSeries("predict bid");
        XYSeries s4 = new XYSeries("predict ask");
        for (int i = 0; i < stepsToPredict; i++) {
            List<NumericPrediction> predsAtStep = forecast.get(i);
            NumericPrediction bibb = predsAtStep.get(0);
            NumericPrediction askb = predsAtStep.get(1);
            s3.add(i+cnt, bibb.predicted());
            s4.add(i+cnt, askb.predicted());
            System.out.println(bibb.predicted() + "," + askb.predicted());
        }

        String title = "Regression example";
        String xAxisLabel = "Timestep";
        String yAxisLabel = "Coin Price";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean legend = true;
        boolean tooltips = false;
        boolean urls = false;
        XYSeriesCollection c = new XYSeriesCollection();
//        createSeries(c, fullArray, 0, "Train data");
//        createSeries(c, testArray, trainSize - 1, "Actual test data");
//        createSeries(c, predicted, trainSize - 1, "Predicted test data");
//        createSeries(c, predicted1, trainSize+testSize - 1, "Predicted future data");
//         createSeries(c, predicted1, trainSize+numberOfTimesteps - 1, "Predicted test data");

        c.addSeries(s1);
        c.addSeries(s2);
        c.addSeries(s3);
        c.addSeries(s4);

        JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, c, orientation, legend, tooltips, urls);

        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();

        // Auto zoom to fit time series in initial window
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRange(true);
        rangeAxis.setAutoRangeIncludesZero(false);

        final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(false);
        domainAxis.setAutoRange(true);

        try {
            ChartUtilities.saveChartAsPNG(new File("./images/image" + (cnter++) + ".png"), chart, 1400, 800);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        JPanel panel = new ChartPanel(chart);
//
//        JFrame f = new JFrame();
//        f.add(panel);
//        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        f.pack();
//        f.setTitle("Training Data");
//
//        RefineryUtilities.centerFrameOnScreen(f);
//        f.setVisible(true);

        // we can continue to use the trained forecaster for further forecasting
        // by priming with the most recent historical data (as it becomes available).
        // At some stage it becomes prudent to re-build the model using current
        // historical data.

    }

}