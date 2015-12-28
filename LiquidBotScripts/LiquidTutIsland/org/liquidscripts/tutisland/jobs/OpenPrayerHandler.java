package org.liquidscripts.tutisland.jobs;

import org.liquidscripts.tutisland.jobs.Job;
import org.liquidbot.osrs.api.enums.Tab;
import org.liquidbot.osrs.api.util.Time;
import org.liquidscripts.tutisland.Methods;
import org.liquidscripts.tutisland.State;

/**
 * Created with IntelliJ IDEA.
 * User: Magorium
 * Date: 1/5/14
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class OpenPrayerHandler implements Job {
    @Override
    public void run() {
        if(Tab.PRAYER.isOpen()){
            Tab.PRAYER.open();
        }
        Tab.PRAYER.open();
        Time.sleep(200,250);
    }

    @Override
    public boolean isActive() {
        return Methods.getState().equals(State.OPEN_PRAYER_MENU);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
