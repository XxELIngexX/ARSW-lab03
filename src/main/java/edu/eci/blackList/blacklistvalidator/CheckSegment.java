package edu.eci.blackList.blacklistvalidator;

import edu.eci.blackList.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CheckSegment class to check a segment of blacklist servers.
 */
public class CheckSegment extends Thread {

    private HostBlacklistsDataSourceFacade dataSource;
    private int startIndex;
    private int endIndex;
    private String ipaddress;
    private List<Integer> blacklists;
    private AtomicInteger globalOccurrences;
    private static final int ALARM_THRESHOLD = 5;


    public CheckSegment(HostBlacklistsDataSourceFacade dataSource, int startIndex, int endIndex, String ipaddress, AtomicInteger occurrences) {
        this.dataSource = dataSource;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.ipaddress = ipaddress;
        this.blacklists = new LinkedList<>();
        this.globalOccurrences = occurrences;
    }

    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            if (globalOccurrences.get() >= ALARM_THRESHOLD) {
                break;
            }

            if (dataSource.isInBlackListServer(i, ipaddress)) {
                blacklists.add(i);
                globalOccurrences.addAndGet(1);
            }

            if (globalOccurrences.get() >= ALARM_THRESHOLD) {
                break;
            }
        }
    }

    public List<Integer> getBlacklists() {
        return blacklists;
    }

    public AtomicInteger getOccurrences() {
        return globalOccurrences;
    }
}
