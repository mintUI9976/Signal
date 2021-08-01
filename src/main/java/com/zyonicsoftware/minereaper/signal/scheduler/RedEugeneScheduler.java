/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.scheduler;

import com.zyonicsoftware.minereaper.redeugene.RedEugene;
import com.zyonicsoftware.minereaper.scheduler.RedEugeneIntroduction;

/**
 * @author Niklas Griese
 * @implSpec https://github.com/mintUI9976/RedEugene
 * @see RedEugene
 */
public class RedEugeneScheduler {

    private static RedEugene redEugene;
    private static RedEugeneIntroduction redEugeneIntroduction;

    public static void setRedEugene(final RedEugene redEugene) {
        RedEugeneScheduler.redEugene = redEugene;
        RedEugeneScheduler.redEugeneIntroduction =
                new RedEugeneIntroduction(RedEugeneScheduler.redEugene);
    }

    public static RedEugene getRedEugene() {
        return RedEugeneScheduler.redEugene;
    }

    public static RedEugeneIntroduction getRedEugeneIntroduction() {
        return RedEugeneScheduler.redEugeneIntroduction;
    }
}
