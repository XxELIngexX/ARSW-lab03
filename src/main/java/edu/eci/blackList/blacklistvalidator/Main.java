package edu.eci.blackList.blacklistvalidator;

import java.util.List;

public class Main {

    public static void main(String a[]) throws InterruptedException {
        HostBlackListsValidator hblv = new HostBlackListsValidator();

        List<Integer> blackListOcurrences = hblv.checkHost("202.24.34.55", 20);
        System.out.println("The host was found in the following blacklists:" + blackListOcurrences);
    }
}
