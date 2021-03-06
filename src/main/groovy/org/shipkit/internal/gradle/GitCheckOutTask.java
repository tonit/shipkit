package org.shipkit.internal.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.shipkit.internal.exec.DefaultProcessRunner;

/**
 * This task will checkout a certain revision.
 */
public class GitCheckOutTask extends DefaultTask {

    @Input
    private String rev;

    /**
     * See {@link #getRev()}
     */
    public void setRev(String rev) {
        this.rev = rev;
    }

    /**
     * Revision to check out
     */
    public String getRev() {
        return rev;
    }

    @TaskAction
    public void checkOut() {
        new DefaultProcessRunner(getProject().getProjectDir()).run("git", "checkout", rev);
    }
}
