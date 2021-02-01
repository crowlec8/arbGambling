import java.math.BigDecimal;
import java.math.RoundingMode;

public class Odds {
    public static String myKey = "ca1ebcca6225cd2a2b3d2b6dcb3d6c5f";
    public static double firstOdd = (7.0/5.0);                    //home team
    public static double secondOdd = (2.0/1.0);      //away
    public static double thirdOdd = (29.0/10.0);       //draw

    public static void main(String[] args) {
        //firstOdd = changeToFraction(firstOdd);
        //secondOdd = changeToFraction(secondOdd);
        boolean winnerFound = false;
        winnerFound = findWinner3(winnerFound);
        // if(winnerFound == false){
        //     findBest();
        // }
    }

    public static boolean findWinner2(boolean ding){
        double currentLowestProfit = 0.0;
        double lowestProfit = 0.0;
        int currentX = 0;
        int currentY = 0;
        double profitHigh = 0;
        double profitLow = 0;
        for(int x = 5; x <= 100; x += 5){
            for(int y = 5; y<=100; y += 5){
                double winningsHigh = x * firstOdd;
                double winningsLow = y * secondOdd;
                if((winningsHigh > y) && (winningsLow>x)){
                    double profit1 = winningsHigh - y;
                    double profit2 = winningsLow - x;
                    if(profit1 < profit2){
                        lowestProfit = profit1;
                    }
                    else {lowestProfit = profit2;}
                    if(lowestProfit > currentLowestProfit){
                        currentLowestProfit = lowestProfit;
                        currentX = x;
                        currentY = y;
                        profitHigh = profit1;
                        profitLow = profit2;
                    }
                    ding = true;
                }
            } 
        }
        if(ding == true){
            System.out.print("Spend " + currentX + "Euro on the home odd bet, and make " + roundDouble(profitHigh, 2) + "Euro \nSpend " + currentY + "Euro on the away odd bet, and make " + roundDouble(profitLow, 2) + "Euro \n");
        }
        else{System.out.print("No matches\n");}
        return ding;
    }

    public static boolean findWinner3(boolean ding){
        double currentLowestProfit = 0.0;
        double lowestProfit = 0.0;
        int currentX = 0;
        int currentY = 0;
        int currentZ = 0;
        double profitWin = 0;
        double profitLoss = 0;
        double profitDraw = 0;
        for(int x = 5; x <= 100; x += 5){
            for(int y = 5; y<=100; y += 5){
                for(int z = 5; z <=100; z += 5){
                    double winningsWin = x * firstOdd;
                    double winningsLoss = y * secondOdd;
                    double winningsDraw = z * thirdOdd;
                    if((winningsWin > y + z) && (winningsLoss > x + z) && (winningsDraw > x + y)){
                        double profit1 = winningsWin - (y + z);
                        double profit2 = winningsLoss - (x + z);
                        double profit3 = winningsDraw - (x + y);
                        if(profit1 < profit2 && profit1 < profit3){
                            lowestProfit = profit1;
                        }
                        else if(profit2 < profit3){
                            lowestProfit = profit2;
                        }
                        else {lowestProfit = profit3;}
                        if(lowestProfit > currentLowestProfit){
                            currentLowestProfit = lowestProfit;
                            currentX = x;
                            currentY = y;
                            currentZ = z;
                            profitWin = profit1;
                            profitLoss = profit2;
                            profitDraw = profit3;
                        }
                        ding = true;
                    }
                }
            } 
        }
        if(ding == true){
            System.out.print("Spend " + currentX + "Euro on the home odd bet, and make " + roundDouble(profitWin, 2) + "Euro \nSpend " + currentY + "Euro on the away odd bet, and make " + roundDouble(profitLoss, 2) + "Euro \nSpend " + currentZ + "Euro on the draw odd bet, and make " + roundDouble(profitDraw, 2) + "Euro \n");
        }
        else{System.out.print("No matches\n");}
        return ding;
    }

    // public static void win(){
    //     int x;
    //     int y;
    //     double winningsHigh = x * firstOdd;
    //     double winningsLow = x * secondOdd;
    // }

    public static void findBest(){
        int closestX = 0;
        int closestY = 0;
        double closestNetHigh = -100.0;
        double closestNetLow = -100.0;
        for(int x = 100;x <= 1000; x+=1){
            for(int y = 100;y<=1000; y+=1){
                int spent = x+y;
                double winningsHigh = (x * firstOdd);
                double winningsLow = (y * secondOdd);
                double highBetWinsNet = (winningsHigh - y)/spent;
                double lowBetWinsNet = (winningsLow - x)/spent;
                //System.out.print("x=" + x + "; y=" + y + "; highbet wins net :" + roundDouble(highBetWinsNet, 2)+ "; lowbet wins net :" + roundDouble(lowBetWinsNet, 2) + "\n");
                if((highBetWinsNet >= 0 && lowBetWinsNet > closestNetLow) || (lowBetWinsNet >= 0 && highBetWinsNet > closestNetHigh)){
                    closestX = x;
                    closestY = y;
                    closestNetHigh = highBetWinsNet;
                    closestNetLow = lowBetWinsNet;
                }
            } 
        }
        System.out.print("The best option would be \nx=" + closestX + "; y=" + closestY + "; highbet wins net :" + roundDouble(closestNetHigh, 2)+ "; lowbet wins net :" + roundDouble(closestNetLow, 2) + "\n");
    }

    public static double roundDouble(double d, int places) {
 
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
    
    public static double changeToFraction(double odd){
        double newOdd;
        if(odd > 0){
            newOdd = odd / 100;
        }
        else{
            newOdd = -100 / odd;
        }
        return newOdd;
    }

}