package org.mockito.release.notes.format;

import org.mockito.release.notes.model.Improvement;

import java.util.Map;

/**
 * Gives access to all release and version notes formatters
 */
public class ReleaseNotesFormatters {

    /**
     * Returns the default formatter for version notes data
     *
     * @param labelMapping Mapping of label to descriptive label summary.
     * Labels of the improvements (see {@link Improvement#getLabels()} are often short and not very descriptive.
     * In final release notes we want descriptive sections of improvements.
     * Mappings also allow controlling priority in presenting improvements -
     *  the formatter can use the order of label mappings. For example, 'noteworthy' labelled improvements on top.
     */
    public static SingleReleaseNotesFormatter defaultFormatter(Map<String, String> labelMapping) {
        return new DefaultFormatter(labelMapping);
    }

    /**
     * Returns the concise formatter intended to use for notable releases
     *
     * @param introductionText text to be placed on the top of the release notes content
     */
    public static MultiReleaseNotesFormatter conciseFormatter(String introductionText) {
        return new ConciseFormatter(introductionText);
    }
}