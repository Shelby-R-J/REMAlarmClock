import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

public class Clock {

    private static String grabCurrent;
    private static int limit;
    private static int elapseTime = elapseTime();
    private static int fallAsleep = 0;

    public static boolean setFallAsleep(String l) {
        if (sleepCheck(l) == true) {
            fallAsleep = Integer.parseInt(l);
            return true;
        }
        return false;
    }

    public static int getFallAsleep() {
        return fallAsleep;
    }

    public static String increaseLength(String userInput) {
        String input = " ";
        if (userInput.length() == 4) {
            input = "0" + userInput;
        } else {
            input = userInput;
        }
//        System.out.println(input);
        return input;
    }

    public static boolean checkValid(String userInput) {
        String digits = "0123456789";
        if (userInput.length() == 5) {
            if (digits.contains(userInput.substring(0, 1)) && digits.contains(userInput.substring(1, 2))) {
                if (userInput.indexOf(":") == 2) {
                    if (digits.contains(userInput.substring(3, 4)) && digits.contains(userInput.substring(4, 5))) {
                        if (Integer.parseInt(userInput.substring(0, 2)) < 24
                                && Integer.parseInt(userInput.substring(3, 5)) < 60) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //    public static boolean setLimit(String l) {
//        l = increaseLength(l);
//        if (checkValid(l) && sleepCheck(l)) {
//            LocalTime l1 = LocalTime.parse(getCurrentTime());
//            LocalTime l2 = LocalTime.parse(l);
//            long result = 0;
//            if (l1.until(l2, ChronoUnit.MINUTES) > 0.00001) {
//                result = l1.until(l2, ChronoUnit.MINUTES)+1;
//            }
//            else
//            {
//                result = l1.until(l2, ChronoUnit.MINUTES);
//            }
//            if (result < 0) {
//                result += 24 * 60;
//            }
//            limit = (int) result;
////            System.out.println("Minutes: " + result);
//            return true;
//        } else
//            return false;
//    }
    public static boolean setLimit(String l) {
        l = increaseLength(l);
        if (checkValid(l) && sleepCheck(l)) {
            LocalTime l1 = LocalTime.parse(getCurrentTime());
            LocalTime l2 = LocalTime.parse(l);
            long result = l1.until(l2, ChronoUnit.MINUTES);
            if (result < 0) {
                result += 24 * 60;
            }
            limit = (int) result;
            return true;
        } else
            return false;
    }

    public static int getLimit() {
        return limit;
    }

    public static String getCurrentTime() {
        String stringHour = "";
        String stringMin = "";
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date temp = new Date(System.currentTimeMillis());

        String[] integerInString = formatter.format(temp).split(":");
        int[] integer = new int[3];

        for (int i = 0; i < integerInString.length; i++) {
            integer[i] = Integer.parseInt(integerInString[i]);
        }

        integer[1] = integer[1];
        if (integer[1] >= 60) {
            integer[1] %= 60;
            integer[0] += 1;
        }
        if (integer[1] < 10)
            stringMin = "0" + Integer.toString(integer[1]);
        else
            stringMin = Integer.toString(integer[1]);

        if (integer[0] >= 24) {
            integer[0] %= 24;
        }
        if (integer[0] < 10)
            stringHour = "0" + Integer.toString(integer[0]);
        else
            stringHour = Integer.toString(integer[0]);

        return stringHour + ":" + stringMin + ":" + "00";
    }

    public static int elapseTime() {
        int elapseTime1 = 0;
        if (limit < 110) {
            elapseTime1 = limit;
        } else {
            for (int cycle = 0; (elapseTime1 + (100 + (10 * (cycle + 1)))) <= limit; cycle++) {
                elapseTime1 += 85 + (15 * cycle);
            }
            if (elapseTime1 + 85 <= limit) {
                elapseTime1 += 85;
            }
        }
        elapseTime = elapseTime1;
        return elapseTime;
    }

    public static String addThem() {
        int ElaMins = elapseTime % 60;
        int ElaHour = (elapseTime - ElaMins) / 60;
        grabCurrent = getCurrentTime();
        int GrabMins = Integer.parseInt(grabCurrent.substring(3, 5));
        int GrabHour = Integer.parseInt(grabCurrent.substring(0, 2));
        int calcHour = ElaHour + GrabHour;
        int calcMin = ElaMins + GrabMins;

        String calcHours = "";
        String calcMins = "";

        if (calcMin >= 60) {
            calcMin %= 60;
            calcHour += 1;
        }

        if (calcMin < 10) {
            calcMins = "0" + Integer.toString(calcMin);
        } else {
            calcMins = Integer.toString(calcMin);
        }

        if (calcHour >= 24) {
            calcHour %= 24;
        }

        if (calcHour < 10) {
            calcHours = "0" + Integer.toString(calcHour);
        } else {
            calcHours = Integer.toString(calcHour);
        }

        return calcHours + ":" + calcMins + ":00";
    }

    public static boolean sleepCheck(String tempFallAsleep) {
        String digits = "0123456789:";
        for (char x : tempFallAsleep.toCharArray()) {
            if (digits.contains(String.valueOf(x)) == false)
                return false;
        }
        return true;
    }

    public static String grabRealCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date temp = new Date(System.currentTimeMillis());
        return formatter.format(temp);
    }

    public static String randomString() {
        Random random = new Random();
        String codeString = "";
        int num = 0;
        for (int i = 0; i < 8; i++) {
            int randomNum = random.nextInt(2);
            if (randomNum == 2) {
                num = random.nextInt(25) + 66;
            } else if (randomNum == 1) {
                num = random.nextInt(22) + 99;
            } else {
                num = random.nextInt(9) + 49;
            }

            char character = (char) num;

            codeString = codeString + character;
        }
        return codeString;
    }

    public static boolean checkString(String stringInput, String codeString) {
        if (stringInput.equals(codeString)) {
            return true;
        } else {
            return false;
        }
    }

    public static void printStatistics() {
        if (elapseTime() < 420) {
            System.out.println("You are one of the 35.3% of Americans who got less than 7 hours of sleep each day.");
        } else {
            System.out.println("You are one of the 64.7% of Americans who got more than 7 hours of sleep each day.");
        }
    }

    public static void main(String[] args) {
        setLimit("11:00");
        elapseTime();
        addThem();
        System.out.println(randomString());
        printStatistics();
    }
}
