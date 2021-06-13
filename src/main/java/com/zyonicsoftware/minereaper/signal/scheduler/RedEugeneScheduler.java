/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.scheduler;

import com.zyonicsoftware.minereaper.enums.EugeneFactoryPriority;
import com.zyonicsoftware.minereaper.redeugene.RedEugene;
import com.zyonicsoftware.minereaper.scheduler.RedEugeneIntroduction;

public class RedEugeneScheduler {

    private final static RedEugene redEugene = new RedEugene("SignalPool", 3, false, EugeneFactoryPriority.NORM);
    private final static RedEugeneIntroduction redEugeneIntroduction = new RedEugeneIntroduction(RedEugeneScheduler.redEugene);

    public static RedEugene getRedEugene() {
        return RedEugeneScheduler.redEugene;
    }

    public static RedEugeneIntroduction getRedEugeneIntroduction() {
        return RedEugeneScheduler.redEugeneIntroduction;
    }
}
